package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.pools.instructions.Operator;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class InstructionPool extends ArrayList<Operator> {

    public InstructionPool(byte[] bytes, ConstantPool pool) {
        this(new ByteStream(bytes), pool);
    }

    public InstructionPool(ByteStream stream, ConstantPool pool) {
        int size = stream.readInt();
        System.out.println("Code pool of size " + stream.toByteArray().length);
        for (int i = 0; i != size; i++) {
            byte[] operand = new byte[]{};
            int op = (int) stream.readByte();
            switch (op) {
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
                    operand = stream.read(1);
                    i++;
                    break;
                case ANEWARRAY:
                case GOTO:
                case IF_ACMPEQ:
                case IF_ACMPNE:
                case IF_ICMPEQ:
                case IF_ICMPGE:
                case IF_ICMPGT:
                case IF_ICMPLE:
                case IF_ICMPLT:
                case IF_ICMPNE:
                case IFEQ:
                case IFNE:
                case IFLT:
                case IFGE:
                case IFGT:
                case IFLE:
                case IFNONNULL:
                case IFNULL:
                case IINC:
                case JSR:
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
                    // case INVOKEDYNAMIC:       //TODO: Add to Opcode.java
                case INVOKEINTERFACE:
                    operand = stream.read(2);
                    i = i + 2;
                    break;
                case GOTO_W:
                case JSR_W:
                    operand = stream.read(4);
                    i = i + 4;
                    break;
            }
            add(new Operator(op, operand));
        }
    }

    public InstructionPool() {

    }

    @NotNull
    public InstructionPool subList(int from, int to) {
        InstructionPool out = new InstructionPool();
        for(int i = from; i != to; i++) {
            out.add(get(i));
        }
        return out;
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(size()));
        // for (Operator code : this)
        //   out.write(code.getByte());
        return out.toByteArray();
    }

    public int sizeInBytes() {
        int size = size();
        for(Operator op: this) {
            size += op.getOperand().length;
        }
        return size;
    }
}
