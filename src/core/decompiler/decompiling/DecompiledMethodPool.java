package core.decompiler.decompiling;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.members.constants.MemberRef;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.internal.pools.MemberPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import core.decompiler.ImportList;
import core.decompiler.InstructionStack;
import core.decompiler.Scope;
import com.github.Icyene.bytecode.introspection.internal.pools.instructions.Operand;
import com.github.Icyene.bytecode.introspection.internal.pools.instructions.Operator;

import java.util.HashMap;
import java.util.LinkedList;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class DecompiledMethodPool<E> extends MemberPool<DecompiledMethodPool.DecompiledMethod> {


    public DecompiledMethodPool(ByteStream stream, ConstantPool pool, ClassFile owner) {
        super(stream, pool, owner);
    }

    @Override
    public DecompiledMethod make(ByteStream stream, ConstantPool pool) {
        return new DecompiledMethod((DecompiledClassFile)owner, stream, pool);
    }

    public class DecompiledMethod extends Member {
        private String source = "";

        public DecompiledMethod(DecompiledClassFile owner, ByteStream stream, ConstantPool pool) {
            super(stream, pool);


            HashMap<String, Object> meta = owner.getMetadata();
            ImportList importList = (ImportList) meta.get("imports");
            Scope c = (Scope) meta.get("scope");
            if(c == null) {
                System.out.println("SCOPE BE NULL!");
            }
            String mName = (String) meta.get("name");

            SignatureReader sign = new SignatureReader(this);

            source += "\n\n" + c.indent;

            InstructionStack stack = new InstructionStack();

            String flags = /*getAccessFlags().getStringValue() TODO: Redo flags*/ "public";

            if (mName.equals("<clinit>")) {
                source += "static";
            } else {
                source += (isDeprecated() ? c.indent + "@Deprecated\n   " : "") + flags + " ";
                if (mName.equals("<init>")) {
                    source += owner.getThisClass().getStringValue();
                } else {
                    source += importList.getWImport(sign.getType()) + " " + mName;
                }
                source += "(";
                LinkedList<String> params = sign.getAugmentingTypes();
                int size = params.size();
                for (int p = 0; p != size; p++) {
                    String argname = "arg" + p;
                    source += importList.getWImport(params.get(p)) + " " + argname + (p != size - 1 ? ", " : "");
                    stack.add(argname);
                }
                source += ")";
            }

            source += " {\n";
            c.indent();
           /*
            ByteStream code = new ByteStream(((Code) attributePool.getInstancesOf(Code.class).get(0)).getCodePool());
            while (code.remaining() != 0) {
                Object operand = null;
                Operator operator = null;
                switch ((int) code.readByte()) {
                    case ARETURN:
                    case FRETURN:
                    case DRETURN:
                    case IRETURN:
                    case LRETURN:
                        String ret = stack.lastElement();
                        source += c.indent + "return " + ret + (ret.lastIndexOf(";") != ret.length() - 1 ? ";" : "") + "\n";
                        break;
                    case NEW:
                        stack.add(importList.getWImport(operator.getOperand().get().toString()));
                        break;
                    case LDC:
                    case LDC2_W:
                    case LDC_W:
                    case SIPUSH:
                    case BIPUSH:
                        stack.add(operand instanceof Constant && ((Constant) operand).getType() == TAG_STRING ? "\"" + operand + "\"" : operand + "");
                        break;
                    case INVOKESTATIC:
                        MemberRef entry = ((MemberRef) operator.getOperand().get());
                        stack.add(importList.getWImport(entry.getOwningClass()) + "." + entry.getDescriptor().getName() + "(" + stack.subList(stack.size() - new SignatureReader(entry).getAugmentingTypes().size(), stack.size()) + ")");
                        break;
                    case F2D:
                    case I2D:
                    case L2D:
                        stack.setLast("(double)" + stack.lastElement());
                        break;
                    case D2F:
                    case I2F:
                    case L2F:
                        stack.setLast("(float)" + stack.lastElement());
                        break;
                    case L2I:
                    case F2I:
                    case D2I:
                        stack.setLast("(int)" + stack.lastElement());
                        break;
                    case I2L:
                    case F2L:
                    case D2L:
                        stack.setLast("(long)" + stack.lastElement());
                        break;
                    case I2S:
                        stack.setLast("(short)" + stack.lastElement());
                        break;
                    case I2B:
                        stack.setLast("(byte)" + stack.lastElement());
                        break;
                    case I2C:
                        stack.setLast("(char)" + stack.lastElement());
                        break;
                    case ATHROW:
                        source += c.indent + "throw " + stack.lastElement() + ";" + "\n";
                        break;
                    case PUTSTATIC:
                        Operand field = operator.getOperand();
                        source += c.indent + field + " = " + stack.lastElement() + ";\n";
                        break;
                    case LADD:
                    case DADD:
                    case FADD:
                    case IADD:
                        stack.setLast(stack.getSecondLast() + "+" + stack.lastElement());
                        break;
                    case LSUB:
                    case DSUB:
                    case FSUB:
                    case ISUB:
                        stack.setLast(stack.getSecondLast() + "-" + stack.lastElement());
                        break;
                    case LMUL:
                    case DMUL:
                    case FMUL:
                    case IMUL:
                        stack.setLast(stack.getSecondLast() + "*" + stack.lastElement());
                        break;
                    case LDIV:
                    case DDIV:
                    case FDIV:
                    case IDIV:
                        stack.setLast(stack.getSecondLast() + "/" + stack.lastElement());
                        break;
                    case IOR:
                    case LOR:
                        stack.setLast(stack.getSecondLast() + "|" + stack.lastElement());
                        break;
                    case LAND:
                    case IAND:
                        stack.setLast(stack.getSecondLast() + "&" + stack.lastElement());
                        break;
                    case ISHR:
                    case LSHR:
                    case IUSHR:
                    case LUSHR:
                        stack.setLast(stack.getSecondLast() + ">>" + stack.lastElement());
                        break;
                    case ISHL:
                    case LSHL:
                        stack.setLast(stack.getSecondLast() + "<<" + stack.lastElement());
                        break;
                    case INEG:
                    case LNEG:
                        stack.setLast("~" + stack.lastElement());
                        break;
                    case IXOR:
                    case LXOR:
                        stack.setLast(stack.getSecondLast() + "^" + stack.lastElement());
                        break;
                    case IREM:
                    case LREM:
                        stack.setLast(stack.getSecondLast() + "%" + stack.lastElement());
                        break;

             case POP2:
                 stack.pop();
             case POP:                   //TODO: Pop bottom
                 stack.pop();
                 break;

                    case ACONST_NULL:
                        stack.add("null");
                        break;
                    case FCONST_0:
                        stack.add("0.0F");
                        break;
                    case FCONST_1:
                        stack.add("1.0F");
                        break;
                    case FCONST_2:
                        stack.add("2.0F");
                        break;
                    case DCONST_0:
                        stack.add("0.0D");
                        break;
                    case DCONST_1:
                        stack.add("1.0D");
                        break;
                    case ICONST_M1:
                        stack.add("-1");
                        break;
                    case ICONST_0:
                        stack.add("0");
                        break;
                    case ICONST_1:
                        stack.add("1");
                        break;
                    case ICONST_2:
                        stack.add("2");
                        break;
                    case ICONST_3:
                        stack.add("3");
                        break;
                    case ICONST_4:
                        stack.add("4");
                        break;
                    case ICONST_5:
                        stack.add("5");
                        break;
                    case LCONST_0:
                        stack.add("0L");
                        break;
                    case LCONST_1:
                        stack.add("1L");
                        break;
                    case NEWARRAY:
                        String ar = "";
                        switch ((Byte) operand) {
                            case T_BOOLEAN:
                                ar = "new boolean[%s]";
                                break;
                            case T_CHAR:
                                ar = "new char[%s]";
                                break;
                            case T_FLOAT:
                                ar = "new float[%s]";
                                break;
                            case T_DOUBLE:
                                ar = "new double[%s]";
                                break;
                            case T_BYTE:
                                ar = "new byte[%s]";
                                break;
                            case T_SHORT:
                                ar = "new short[%s]";
                                break;
                            case T_INT:
                                ar = "new int[%s]";
                                break;
                            case T_LONG:
                                ar = "new long[%s]";
                                break;
                        }
                        stack.add(String.format(ar, stack.lastElement()) + ";");
                        break;
                    case MONITORENTER:
                        source += c.indent + "synchronized(" + stack.lastElement() + ") {\n";
                        c.indent();
                        break;
                    case MONITOREXIT:
                        c.deindent();
                        source += c.indent + "}\n";
                        break;

                    case NOP:
                        break;

                    case RETURN:
                        source += c.indent + "return;\n";
                        break;
                    default:
                        source += operator + "\n";
                }
            }
            c.deindent();

            source += c.indent + "}";       */
        }

        public String getSource() {
            return source;
        }
    }
}
