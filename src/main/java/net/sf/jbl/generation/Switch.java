package net.sf.jbl.generation;

import net.sf.jbl.util.ByteStream;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class Switch extends Instruction implements Iterable<Switch.Case> {
    int padding;
    int defaultJump, length;
    LinkedList<Case> cases = new LinkedList<Case>();

    public Switch(int addr, ByteStream stream) {
        address = addr;
        padding = (4 - (stream.position() % 4)) % 4;
        stream.read(padding);
        defaultJump = stream.readInt();
    }

    public Switch() {}

    public byte[] getArguments() {
        return new ByteStream().write(new byte[padding]).write(defaultJump).toByteArray();
    }

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
