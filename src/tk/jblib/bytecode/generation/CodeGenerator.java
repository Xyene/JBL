package tk.jblib.bytecode.generation;

import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.code.ExceptionPool;
import tk.jblib.bytecode.introspection.members.TryCatch;
import tk.jblib.bytecode.introspection.members.attributes.Code;
import tk.jblib.bytecode.introspection.metadata.readers.SignatureReader;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.*;

import static tk.jblib.bytecode.generation.Groups.JUMPS;
import static tk.jblib.bytecode.generation.Groups.JUMPS_W;
import static tk.jblib.bytecode.introspection.metadata.Opcode.*;

public class CodeGenerator {

    public LinkedList<Instruction> instructions = new LinkedList<Instruction>();
    ExceptionPool exceptionPool = new ExceptionPool();
    Member method = null;
    private int length;
    private boolean sorted = false;

    public CodeGenerator(Code code, Member member) {
        this(code);
        method = member;
    }

    public CodeGenerator(Code code) {
        this(code.getCodePool(), 0);
        exceptionPool = code.getExceptionPool();
    }

    public CodeGenerator(byte[] bytes, int pos) {
        length = bytes.length;
        ByteStream in = new ByteStream(bytes);
        boolean wide = false;
        for (int i = 0; i != bytes.length; i++) {
            int opcode = in.readByte() & 0xFF;
            int address = i + pos;

            switch (opcode) {
                case WIDE:
                    wide = true;
                    continue;
                case ALOAD:
                case AASTORE:
                case BIPUSH:
                case DLOAD:
                case DSTORE:
                case FLOAD:
                case FSTORE:
                case ILOAD:
                case ISTORE:
                case LSTORE:
                case NEWARRAY:
                case RET:
                case LDC:
                    instructions.add(new Instruction(opcode, wide, address, in.read(wide ? 2 : 1)));
                    i += wide ? 2 : 1;
                    wide = false;
                    continue;
                case ANEWARRAY:
                case IINC:
                case SIPUSH:
                case GETFIELD:
                case GETSTATIC:
                case PUTFIELD:
                case PUTSTATIC:
                case INVOKESTATIC:
                case INVOKESPECIAL:
                case INVOKEVIRTUAL:
                case CHECKCAST:
                case LDC_W:
                case LDC2_W:
                case INSTANCEOF:
                case NEW:
                case INVOKEINTERFACE:
                    instructions.add(new Instruction(opcode, address, in.read(2)));
                    i += 2;
                    continue;
                case LOOKUPSWITCH:
                    LookupSwitch look = new LookupSwitch(address, in);
                    instructions.add(look);
                    i += look.trueLen;
                    continue;
                case TABLESWITCH:
                    TableSwitch table = new TableSwitch(address, in);
                    instructions.add(table);
                    i += table.trueLen;
                    continue;
                default:
                    if (JUMPS.contains(opcode)) {
                        instructions.add(new Branch(opcode, false, address, Bytes.toShort(in.read(2), 0)));
                        i += 2;
                    } else if (JUMPS_W.contains(opcode)) {
                        instructions.add(new Branch(opcode, true, address, Bytes.toInteger(in.read(4), 0)));
                        i += 4;
                    } else
                        instructions.add(new Instruction(opcode, address, new byte[]{}));
            }
        }
    }

    public byte[] synthesize() {
        sort();
        ByteStream out = new ByteStream();
        for (Instruction i : instructions) {
            out.write(i.getBytes());
        }
        return out.toByteArray();
    }

    public int size() {
        return length;
    }

    public CodeGenerator inject(int pc, byte... bytes) {
        length += bytes.length;
        sorted = false;
        if (pc == -1) {
            pc = size();
        }
//TODO: Paradox: switch recalibration needs these to already be in, but sorting cannot run if they are in
        instructions.addAll(new CodeGenerator(bytes, pc).instructions);
        expand(pc, bytes.length);


        return this;
    }


    protected int recalculateJump(int pc, int length, int address, int jump) {
        if (address < pc && jump > pc)
            jump += length;
        else if (address > pc && jump < 0 && address - Math.abs(jump) < pc)
            jump += -length;
        return jump;
    }



    protected void expand(int pc, int len) {
        for (Instruction i : instructions) {
            if (i.address >= pc) {
                i.address += len;
            }

            if (i instanceof Branch) {
                Branch j = (Branch) i;
                j.jump = recalculateJump(pc, len, j.address, j.jump);
            } else if (i instanceof Switch) {
                Switch s = (Switch) i;
                int off = s.padding;
                s.padding = (4 - ((s.address + 1) % 4)) % 4;
                off = s.padding - off;
                System.out.println("Padding grew/shrunk by " + off + " bytes.");
                length += off;
                s.defaultJump = recalculateJump(pc, len, s.address, s.defaultJump) + off;
                for (Switch.Case c : s) {
                    c.target = recalculateJump(pc, len, s.address, c.target) + off;
                }
                if(off > 0) expand(s.address-1, off);  //If padding changed, expand offsets by the number it changed  << TODO: Doesn't expand correctly at all
            }
        }

        for (TryCatch e : exceptionPool) {
            if (e.getEndPC() >= pc) {
                e.setEndPC(e.getEndPC() +len);
            } else if (e.getStartPC() >= pc) {
                e.setStartPC(e.getStartPC() + len);
            }
            if (e.getHandlerPC() >= pc) {
                e.setHandlerPC(e.getHandlerPC() +len);
            }
        }
    }

    public CodeGenerator inject(int pc, Instruction... instrs) {  //TODO: Has to recalibrate
        sorted = false;
        for (int i = 0; i != instrs.length; i++) {
            length += instrs[i].trueLen + 1;
            instructions.add(instrs[i]);
        }
        return this;
    }

    public void sort() {
        if (!sorted) {
            Collections.sort(instructions, new Comparator<Instruction>() {
                public int compare(Instruction f, Instruction s) {
                    return f.address - s.address;
                }
            });
            sorted = true;
        }
    }

    public int computeStackSize() {
        sort();
        if (method == null) {
            throw new RuntimeException("Cannot compute max stack size without method descriptor");
        }

        SignatureReader reader = new SignatureReader(method.getDescriptor());
        int maxStackSize = reader.getAugmentingTypes().size();
        int currentStackSize = 0;

        for (int i = 0; i != instructions.size(); i++) {
            currentStackSize += STACK_GROWTH[instructions.get(i).opcode];
            if (currentStackSize > maxStackSize) {
                maxStackSize = currentStackSize;
            }
        }
        return maxStackSize;
    }

    public int computeMaxLocals() {
        throw new UnsupportedOperationException("Max local fetching not yet implemented!");
    }
}