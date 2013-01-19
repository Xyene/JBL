package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.internal.code.ExceptionPool;
import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.metadata.Opcode;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.*;

import static com.github.Icyene.bytecode.generation.Groups.JUMPS;
import static com.github.Icyene.bytecode.generation.Groups.JUMPS_W;
import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class CodeGenerator {

    public byte[] raw;
    public List<Jump> jumpMap = new ArrayList<Jump>();
    public LinkedList<Instruction> instructions = new LinkedList<Instruction>();
    public ExceptionPool exceptionPool = new ExceptionPool();
    private Member method = null;

    public CodeGenerator(Code code, Member member) {
        this(code);
        method = member;
    }

    public CodeGenerator(Code code) {
        this(code.getCodePool(), 0);
        exceptionPool = code.getExceptionPool();
    }

    public CodeGenerator(byte[] bytes, int index) {
        raw = bytes;
        ByteStream in = new ByteStream(bytes);
        for (int i = 0; i != bytes.length; i++) {
            int opcode = in.readByte() & 0xFF;

            if (JUMPS.contains(opcode)) {
                byte[] to = in.read(2);
                instructions.add(new Instruction(opcode, i + index, to));
                jumpMap.add(new Jump(opcode, i + index, Bytes.toShort(to, 0), false));
                i = i + 2;
            } else if (JUMPS_W.contains(opcode)) {
                byte[] to = in.read(4);
                instructions.add(new Instruction(opcode, i + index, to));
                jumpMap.add(new Jump(opcode, i + index, Bytes.toInteger(to, 0), true));
                i = i + 4;
            } else switch (opcode) {
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
                    instructions.add(new Instruction(opcode, i + index, in.read(1)));
                    i++;
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
                    instructions.add(new Instruction(opcode, i + index, in.read(2)));
                    i = i + 2;
                    continue;
                default:
                    instructions.add(new Instruction(opcode, i + index, new byte[]{}));
            }
        }
    }

    public byte[] synthesize() {
        for (Jump j : jumpMap) {
            if (!j.wide) {
                byte[] jump = Bytes.toByteArray((short) j.jump);
                raw[j.address + 1] = jump[0];
                raw[j.address + 2] = jump[1];
            } else {
                byte[] jump = Bytes.toByteArray(j.jump);
                raw[j.address + 1] = jump[0];
                raw[j.address + 2] = jump[1];
                raw[j.address + 3] = jump[2];
                raw[j.address + 4] = jump[3];
            }

        }
        return raw;
    }

    public CodeGenerator inject(int pc, byte... bytes) {
        for (Jump j : jumpMap) {
            //Above injection
            if (j.address < pc && j.jump >= pc)
                j.jump += bytes.length;
                //Below injection
            else if (j.address > pc && j.jump < 0 && j.address - Math.abs(j.jump) < pc)
                j.jump += -bytes.length;

            if (j.address >= pc) {
                j.address += bytes.length;
            }
        }

        for (Instruction i : instructions) {
            if (i.address >= pc) {
                i.address += bytes.length;
            }
        }

        for (TryCatch e : exceptionPool) {
            if (e.getEndPC() >= pc) {
                e.setEndPC(e.getEndPC() + bytes.length);
            }
            if (e.getStartPC() >= pc) {
                e.setStartPC(e.getStartPC() + bytes.length);
            }
            if (e.getHandlerPC() >= pc) {
                e.setHandlerPC(e.getHandlerPC() + bytes.length);
            }
        }

        CodeGenerator cg = new CodeGenerator(bytes, pc);

        jumpMap.addAll(cg.jumpMap);
        instructions.addAll(cg.instructions);
        raw = Bytes.concat(Bytes.concat(Bytes.slice(raw, 0, pc), bytes), Bytes.slice(raw, pc, raw.length));
        return this;
    }

    private void sortJumps() {
        Collections.sort(jumpMap, new Comparator<Jump>() {
            public int compare(Jump f, Jump s) {
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
            currentStackSize += Opcode.STACK_GROW[instructions.get(i).opcode];
            if (currentStackSize > maxStackSize) {
                maxStackSize = currentStackSize;
            }
        }
        return maxStackSize;
    }

    public int computeMaxLocals() {
        throw new UnsupportedOperationException("Max local fetching not yet implemented!");
    }

    public class Jump {
        public transient int address, jump, opcode;
        public transient boolean wide;

        public Jump(int opcode, int address, int jump, boolean wide) {
            this.opcode = opcode;
            this.address = address;
            this.jump = jump;
            this.wide = wide;
        }

        public String toString() {
            return String.format("[Jump @ %s of type %s JUMPS to %s]", address, opcode, jump);
        }
    }

    public class Instruction {
        public transient int address, opcode;
        public transient byte[] args;

        public Instruction(int opcode, int address, byte[] args) {
            this.opcode = opcode;
            this.address = address;
            this.args = args;
        }

        public String toString() {
            return String.format("[Instruction @ %s of type %s with args %s]", address, opcode, Bytes.bytesToString(args));
        }
    }
}