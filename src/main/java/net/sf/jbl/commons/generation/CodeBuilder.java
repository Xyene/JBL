/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.commons.generation;

import net.sf.jbl.core.ByteStream;
import net.sf.jbl.core.ConstantPool;
import net.sf.jbl.core.Opcode;
import net.sf.jbl.core.attributes.Code;

import java.util.Arrays;

public class CodeBuilder implements Opcode {
    private ConstantPool constants;
    private ByteStream bytecode;

    public CodeBuilder(ConstantPool consts) {
        constants = consts;
        bytecode = ByteStream.writeStream();
    }

    public int invoke(int opcode, String clazz, String name, String signature) {
        bytecode.writeByte(opcode);
        int loc;
        bytecode.writeShort(loc = constants.newMethod(clazz, name, signature));
        //  if(opcode == bytecode.writeByte(constants.getMethod(loc).length); TODO
        //  bytecode.writeByte(0); // Used for JVM caching
        return bytecode.position() - 2;
    }

    public int invokeStatic(String clazz, String name, String signature) {
        return invoke(INVOKESTATIC, clazz, name, signature);
    }

    public int invokeSpecial(String clazz, String name, String signature) {
        return invoke(INVOKESPECIAL, clazz, name, signature);
    }

    public int invokeInterface(String clazz, String name, String signature) {
        return invoke(INVOKEINTERFACE, clazz, name, signature);
    }

    public int invokeVirtual(String clazz, String name, String signature) {
        return invoke(INVOKEVIRTUAL, clazz, name, signature);
    }

    public int fieldAccess(int opcode, String clazz, String name, String signature) {
        bytecode.writeByte(opcode);
        bytecode.writeShort(constants.newField(clazz, name, signature));
        return bytecode.position() - 2;
    }

    public int getStatic(String clazz, String name, String signature) {
        return fieldAccess(GETSTATIC, clazz, name, signature);
    }

    public int putStatic(String clazz, String name, String signature) {
        return fieldAccess(PUTSTATIC, clazz, name, signature);
    }

    public int getField(String clazz, String name, String signature) {
        return fieldAccess(GETFIELD, clazz, name, signature);
    }

    public int putField(String clazz, String name, String signature) {
        return fieldAccess(PUTFIELD, clazz, name, signature);
    }

    public int push(Object val) {
        if (val instanceof String) {
            int str = constants.newString((String) val);
            if (str <= 256) {
                bytecode.writeByte(LDC);
                bytecode.writeByte(str);
            } else {
                bytecode.writeByte(LDC_W); //Always use W to ensure space
                bytecode.writeShort(str);
            }
        } else if (val instanceof Integer) {
            int intg = (Integer) val;
            byte ldc;
            switch (intg) {
                case -1:
                    ldc = ICONST_M1;
                    break;
                case 0:
                    ldc = ICONST_0;
                    break;
                case 1:
                    ldc = ICONST_1;
                    break;
                case 2:
                    ldc = ICONST_2;
                    break;
                case 3:
                    ldc = ICONST_3;
                    break;
                case 4:
                    ldc = ICONST_4;
                    break;
                case 5:
                    ldc = ICONST_5;
                    break;
                default:
                    bytecode.writeByte(LDC);
                    bytecode.writeShort(constants.newInt(intg));
                    return bytecode.position() - 2;
            }
            bytecode.writeByte(ldc);
        } else if (val instanceof Float) {
            float flg = (Float) val;
            byte ldc;
            switch ((int) flg) {
                case 0:
                    ldc = FCONST_0;
                    break;
                case 1:
                    ldc = FCONST_1;
                    break;
                case 2:
                    ldc = FCONST_2;
                    break;
                default:
                    bytecode.writeByte(LDC);
                    bytecode.writeShort(constants.newInt((int) flg)); //TODO: verify
                    return bytecode.position() - 2;
            }
            bytecode.writeByte(ldc);
        } else if (val instanceof Long) {
            long longg = (Long) val;
            byte ldc;
            switch ((int) longg) {
                case 0:
                    ldc = LCONST_0;
                    break;
                case 1:
                    ldc = LCONST_1;
                    break;
                default:
                    bytecode.writeByte(LDC2_W);
                    bytecode.writeShort(constants.newLong(longg));
                    return bytecode.position() - 2;

            }
            bytecode.writeByte(ldc);
        } else if (val instanceof Double) {
            double dobg = (Double) val;
            byte ldc;
            switch ((int) dobg) {
                case 0:
                    ldc = DCONST_0;
                    break;
                case 1:
                    ldc = DCONST_1;
                    break;
                default:
                    bytecode.writeByte(LDC2_W);
                    bytecode.writeShort(constants.newDouble(dobg));
                    return bytecode.position() - 2;

            }
            bytecode.writeByte(ldc);
        } else if (val instanceof Short) {
            bytecode.writeByte(SIPUSH);
            bytecode.writeShort((Short) val);
        } else if (val instanceof Byte) {
            bytecode.writeByte(BIPUSH);
            bytecode.writeByte((Byte) val);
        } else if (!(val instanceof Object)) {
            // The only literal that is not an object is
            // null: meaning we can safely load the null constant
            bytecode.writeByte(ACONST_NULL);
            return bytecode.position();
        } else
            throw new UnsupportedOperationException("could not dispatch push");
        return bytecode.position() - 1;
    }

    public int[] pushGroup(Object... values) {
        if (values.length == 0)
            throw new IllegalArgumentException("must push at least one value");
        int[] ret = new int[values.length];
        for (int i = 0; i != values.length; i++) {
            ret[i] = push(values[i]);

        }
        return ret;
    }

    public int branch(int opcode, int to) {
        bytecode.writeByte(opcode);
        bytecode.writeShort(to);
        return bytecode.position() - 2;
    }

    public int newInstance(String clazz) {
        bytecode.writeByte(NEW);
        bytecode.writeShort(constants.newClass(clazz));
        return bytecode.position() - 2;
    }

    public int cast(int opcode, String check) {
        bytecode.writeByte(opcode);
        bytecode.writeShort(constants.newClass(check));
        return bytecode.position() - 2;
    }

    public int checkcast(String check) {
        return cast(CHECKCAST, check);
    }

    public int push(int opcode, String clazz) {
        bytecode.writeByte(opcode);
        bytecode.writeShort(constants.newClass(clazz));
        return bytecode.position() - 2;
    }

    public int op(int opcode) {
        bytecode.writeByte(opcode);
        return bytecode.position();
    }

    public int pop() {
        return op(POP);
    }

    public int dup() {
        return op(DUP);
    }

    public int swap() {
        return op(SWAP);
    }

    public int array(int type, int dim) {
        bytecode.writeByte(MULTIANEWARRAY);
        bytecode.writeShort(type);
        bytecode.writeByte(dim);
        return bytecode.position() - 3;
    }

    public int array(int type) {
        bytecode.writeByte(ANEWARRAY);
        bytecode.writeByte(type);
        return bytecode.position() - 1;
    }

    public int exit() {
        bytecode.writeByte(RETURN);
        return bytecode.position();
    }

    public int local(int opcode, int pos) {
        //TODO: optimize with FLOAD_0 etc
        bytecode.writeByte(opcode);
        bytecode.writeByte(pos);
        return bytecode.position() - 1;
    }

    public Code generateCode() {
        Code code = new Code();
        code.setBytecode(bytecode.getBuffer());
        return code;
    }

    public void reset() {
        bytecode = ByteStream.writeStream();
    }
}
