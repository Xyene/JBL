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

public class Branch extends Instruction {
    int jump;

    public Branch(int opcode, int address, int jump) {
        this.opcode = opcode;
        this.address = address;
        this.jump = jump;
        trueLen = 2 + (wide ? 4 : 2);  //TODO: Should implement this for all wide instructions
    }

    public int getTarget() {
        return jump;
    }

    public void setTarget(int target) {
        jump = target;
    }

    public String toString() {
        return String.format("[Branch @ %s of type %s JUMPS to %s]", address, opcode, jump);
    }
}