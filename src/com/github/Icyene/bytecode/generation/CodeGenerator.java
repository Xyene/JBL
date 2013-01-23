package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.internal.code.ExceptionPool;
import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.LineNumberTable;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.LocalVariableTable;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.*;

import static com.github.Icyene.bytecode.generation.Groups.JUMPS;
import static com.github.Icyene.bytecode.generation.Groups.JUMPS_W;
import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class CodeGenerator {

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
        ByteStream in = new ByteStream(bytes, index);
        boolean wide = false;
        for (int i = index; i != bytes.length; i++) {
            int opcode = in.readByte() & 0xFF;

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
                    instructions.add(new Instruction(opcode, wide, i, in.read(wide ? 2 : 1)));
                    i++;
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
                    instructions.add(new Instruction(opcode, i, in.read(2)));
                    i += 2;
                    continue;
                case LOOKUPSWITCH:
                    LookupSwitch look = new LookupSwitch(i, in);
                    instructions.add(look);
                    i += look.trueLen;
                    continue;
                case TABLESWITCH:
                    TableSwitch table = new TableSwitch(i, in);
                    instructions.add(table);
                    i += table.trueLen;
                    continue;
                default:
                    if (JUMPS.contains(opcode)) {
                        instructions.add(new Branch(opcode, false, i, Bytes.toShort(in.read(2), 0)));
                        i += 2;
                    } else if (JUMPS_W.contains(opcode)) {
                        instructions.add(new Branch(opcode, true, i, Bytes.toInteger(in.read(4), 0)));
                        i += 4;
                    } else
                        instructions.add(new Instruction(opcode, i, new byte[]{}));
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

    public CodeGenerator inject(int pc, byte... bytes) {  //TODO: Should also recalibrate LineNumberTable/LocalVariableTable
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

        if(method != null) {
            AttributePool attrs = method.getAttributePool();
            if(attrs.contains("LineNumberTable")) {
                LineNumberTable table = (LineNumberTable)attrs.getInstancesOf("LineNumberTable").get(0);
                for(LineNumberTable.Entry en: table) {
                    if (en.getStartPC() >= pc) {
                        en.setStartPC(en.getStartPC() + bytes.length);
                    }
                }
            }
            if(attrs.contains("LocalVariableTable")) {
                LocalVariableTable table = (LocalVariableTable)attrs.getInstancesOf("LocalVariableTable").get(0);
                for(LocalVariableTable.Entry en: table) {
                    if (en.getStartPC() >= pc) {
                        en.setStartPC(en.getStartPC() + bytes.length);
                    }
                }
            }
        }

        return inject(pc, new CodeGenerator(bytes, pc).instructions);
    }

    public CodeGenerator inject(int pc, Collection<Instruction> instr) {  //TODO: Has to recalibrate
        instructions.addAll(instructions);
        return this;
    }

    public CodeGenerator inject(int pc, Instruction... instrs) {  //TODO: Has to recalibrate
        instructions.addAll(instructions);
        return this;
    }

    public void sort() {
        Collections.sort(instructions, new Comparator<Instruction>() {
            public int compare(Instruction f, Instruction s) {
                if (f.address == s.address)
                    throw new RuntimeException("Invalid same address for instructions " + f + ", " + s);
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