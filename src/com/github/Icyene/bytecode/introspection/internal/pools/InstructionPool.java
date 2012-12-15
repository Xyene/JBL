package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.metadata.Opcode;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;
import disassembler.instructions.Operand;
import disassembler.instructions.Operator;

import java.util.LinkedList;

public class InstructionPool extends LinkedList<Operator> {

    public InstructionPool(ByteStream stream) {
        int size = stream.readInt();

        for (int i = 0; i != size; i++) {
            Operand operand = null;
            Opcode op = Opcode.getByValue(stream.readByte());
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
                case LDC:
                case LSTORE:
                case NEWARRAY:
                case RET:
                    operand = new Operand(stream.readByte());
                    i++;
                    break;
                case ANEWARRAY:
                case CHECKCAST:
                case GETFIELD:
                case GETSTATIC:
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
                case INSTANCEOF:
                case INVOKEDYNAMIC:
                case INVOKEINTERFACE:
                case INVOKESPECIAL:
                case INVOKESTATIC:
                case INVOKEVIRTUAL:
                case JSR:
                case LDC_W:
                case LDC2_W:
                case NEW:
                case PUTFIELD:
                case PUTSTATIC:
                case SIPUSH:
                    operand = new Operand(stream.readShort());
                    i = i + 2;
                    break;
                case GOTO_W:
                case JSR_W:
                    operand = new Operand(stream.readInt());
                    i = i + 4;
                    break;

            }
            add(new Operator(op, operand));
        }
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(size()));
        // for (Operator code : this)
        //   out.write(code.getByte());
        return out.toByteArray();
    }
}
