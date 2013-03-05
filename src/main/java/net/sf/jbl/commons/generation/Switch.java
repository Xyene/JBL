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

import java.util.Iterator;
import java.util.LinkedList;

public class Switch extends Instruction implements Iterable<Switch.Case> {
    int padding;
    int defaultJump, length;
    LinkedList<Case> cases = new LinkedList<Case>();

    public Switch(int opcode, int address) {
        this.address = address;
        this.opcode = opcode;
    }

    public Switch() {}

    public void setDefaultJump(int jump) {
        defaultJump = jump;
    }

    public int getDefaultJump() {
        return defaultJump;
    }

    public int getNumberOfCases() {
        return length;
    }

    public Case getCase(int ca) {
        return cases.get(ca);
    }

    public void addCase(int mat, int go) {
        cases.add(new Case(mat, go));
    }

    public void addCase(Case caze) {
        cases.add(caze);
    }

    public void setCases(LinkedList<Case> cases) {
        this.cases = cases;
    }

    public LinkedList<Case> getCases() {
        return cases;
    }

    @Override
    public Iterator<Case> iterator() {
        return cases.iterator();
    }

    public String toString() {
        return "[Switch @ " + address + " of type " + opcode + ": " + cases + ", default -> " + defaultJump + "]";
    }

    public class Case {
        int match;
        int target;

        public Case(int mat, int indice) {
            match = mat;
            target = indice;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getMatch() {
            return match;
        }

        public void setMatch(int match) {
            this.match = match;
        }

        public String toString() {
            return "[Case{" + match + " -> " + target + "}]";
        }
    }
}
