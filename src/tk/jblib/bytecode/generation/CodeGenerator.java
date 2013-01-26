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

    public CodeGenerator(Code code, Member member) {
        this(code);
        method = member;
    }

    public CodeGenerator(Code code) {
        this(code.getCodePool(), 0);
        exceptionPool = code.getExceptionPool();
    }

    public CodeGenerator(byte[] bytes, int pos) {
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
        System.out.println("::: " + instructions) ;
    }

    public byte[] synthesize() {
//        sort();
        ByteStream out = new ByteStream();
        for (Instruction i : instructions) {
            out.write(i.getBytes());
        }
        return out.toByteArray();
    }

    public int size() {
        //  synthesize().length;
        System.out.println("size() returned " + (synthesize().length));
        return synthesize().length;
    }

    public CodeGenerator inject(int pc, byte... bytes) {
        System.out.println("Injecting " + Bytes.bytesToString(bytes) + " at pc " + pc);
        if (pc == -1) {
            pc = size();
        }

        System.out.println(">>> " + new CodeGenerator(bytes, pc).instructions);

        for (Instruction i : instructions) {
            if (i.address >= pc) {
                i.address += bytes.length;
            }

            if (i instanceof Branch) {
                Branch j = (Branch) i;
                //Above injection
                if (j.address < pc && j.jump > pc)
                    j.jump += bytes.length;
                    //Below injection
                else if (j.address > pc && j.jump < 0 && j.address - Math.abs(j.jump) < pc)
                    j.jump += -bytes.length;

                if (j.address >= pc) {
                    j.address += bytes.length;
                }
            }
        }

        for (TryCatch e : exceptionPool) {
            if (e.getEndPC() >= pc) {
                e.setEndPC(e.getEndPC() + bytes.length);
            } else if (e.getStartPC() >= pc) {
                e.setStartPC(e.getStartPC() + bytes.length);
            }
            if (e.getHandlerPC() >= pc) {
                e.setHandlerPC(e.getHandlerPC() + bytes.length);
            }
        }

        instructions.addAll(new CodeGenerator(bytes, pc).instructions);
        return this;
    }

    public CodeGenerator inject(int pc, Instruction... instrs) {  //TODO: Has to recalibrate
        instructions.addAll(instructions);
        return this;
    }

    private void recalculateSize() {
        length = 0;
        for (int i = 0; i != instructions.size(); i++) {
            length += (instructions.get(i).trueLen + 1);
        }
    }

    public void sort() {
        Collections.sort(instructions, new Comparator<Instruction>() {
            public int compare(Instruction f, Instruction s) {
                if (f.address == s.address) {
                    throw new RuntimeException("Invalid same address for instructions " + f + ", " + s);
                }
                return f.address - s.address;
            }
        });
    }

    public int computeStackSize() {
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