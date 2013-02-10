/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package benchmark.bcel.generic;

/**
 * Super class for instructions dealing with array access such as IALOAD.
 *
 * @version $Id: ArrayInstruction.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public abstract class ArrayInstruction extends Instruction implements ExceptionThrower,
        TypedInstruction {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    ArrayInstruction() {
    }


    /**
     * @param opcode of instruction
     */
    protected ArrayInstruction(short opcode) {
        super(opcode, (short) 1);
    }


    public Class[] getExceptions() {
        return benchmark.bcel.ExceptionConstants.EXCS_ARRAY_EXCEPTION;
    }


    /** @return type associated with the instruction
     */
    public Type getType( ConstantPoolGen cp ) {
        switch (opcode) {
            case benchmark.bcel.Constants.IALOAD:
            case benchmark.bcel.Constants.IASTORE:
                return Type.INT;
            case benchmark.bcel.Constants.CALOAD:
            case benchmark.bcel.Constants.CASTORE:
                return Type.CHAR;
            case benchmark.bcel.Constants.BALOAD:
            case benchmark.bcel.Constants.BASTORE:
                return Type.BYTE;
            case benchmark.bcel.Constants.SALOAD:
            case benchmark.bcel.Constants.SASTORE:
                return Type.SHORT;
            case benchmark.bcel.Constants.LALOAD:
            case benchmark.bcel.Constants.LASTORE:
                return Type.LONG;
            case benchmark.bcel.Constants.DALOAD:
            case benchmark.bcel.Constants.DASTORE:
                return Type.DOUBLE;
            case benchmark.bcel.Constants.FALOAD:
            case benchmark.bcel.Constants.FASTORE:
                return Type.FLOAT;
            case benchmark.bcel.Constants.AALOAD:
            case benchmark.bcel.Constants.AASTORE:
                return Type.OBJECT;
            default:
                throw new ClassGenException("Oops: unknown case in switch" + opcode);
        }
    }
}
