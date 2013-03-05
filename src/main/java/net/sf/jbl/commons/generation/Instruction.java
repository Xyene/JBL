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

import java.util.Arrays;

public class Instruction {
    int address, opcode, trueLen;
    byte[] args;
    boolean wide = false;

    public Instruction(int opcode, boolean wide, int address, byte[] args) {
        this.opcode = opcode;
        this.wide = wide;
        this.address = address;
        this.args = args;
        trueLen = args.length;
    }

    public Instruction(int opcode, int address, byte[] args) {
        this(opcode, false, address, args);
    }

    public Instruction() {
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public int getAddress() {
        return address;
    }

    public boolean isWide() {
        return wide;
    }

    public int getLength() {
        return trueLen + 1;
    }

    public String toString() {
        return String.format("[Opcode @ %s of type %s with args %s]", address, opcode, Arrays.toString(args));
    }
}