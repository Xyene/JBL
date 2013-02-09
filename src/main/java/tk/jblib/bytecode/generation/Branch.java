package tk.jblib.bytecode.generation;

import tk.jblib.bytecode.util.Bytes;

public class Branch extends Instruction {
    int jump;

    public Branch(int opcode, boolean wide, int address, int jump) {
        this.opcode = opcode;
        this.address = address;
        this.jump = jump;
        this.wide = wide;
        trueLen = 2 + (wide ? 4 : 2);  //TODO: Should implement this for all wide instructions
    }

    public byte[] getArguments() {
        return wide ? Bytes.toByteArray(jump) : Bytes.toByteArray((short) jump);
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