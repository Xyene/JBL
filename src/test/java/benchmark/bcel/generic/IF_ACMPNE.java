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
 * IF_ACMPNE - Branch if reference comparison doesn't succeed
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id: IF_ACMPNE.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class IF_ACMPNE extends IfInstruction {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    IF_ACMPNE() {
    }


    public IF_ACMPNE(InstructionHandle target) {
        super(benchmark.bcel.Constants.IF_ACMPNE, target);
    }


    /**
     * @return negation of instruction
     */
    public IfInstruction negate() {
        return new IF_ACMPEQ(target);
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitIfInstruction(this);
        v.visitIF_ACMPNE(this);
    }
}
