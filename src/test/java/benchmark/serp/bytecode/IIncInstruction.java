package benchmark.serp.bytecode;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * The <code>iinc</code> instruction.
 *
 * @author Abe White
 */
public class IIncInstruction extends LocalVariableInstruction {
    private int _inc = 0;

    IIncInstruction(Code owner) {
        super(owner, Constants.IINC);
    }

    int getLength() {
        return super.getLength() + 2;
    }

    /**
     * Return the increment for this IINC instruction.
     */
    public int getIncrement() {
        return _inc;
    }

    /**
     * Set the increment on this IINC instruction.
     *
     * @return this Instruction, for method chaining
     */
    public IIncInstruction setIncrement(int val) {
        _inc = val;
        return this;
    }

    public boolean equalsInstruction(Instruction other) {
        if (this == other)
            return true;
        if (!(other instanceof IIncInstruction))
            return false;
        if (!super.equalsInstruction(other))
            return false;

        IIncInstruction ins = (IIncInstruction) other;
        return getIncrement() == 0 || ins.getIncrement() == 0 
            || getIncrement() == ins.getIncrement();
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterIIncInstruction(this);
        visit.exitIIncInstruction(this);
    }

    void read(Instruction other) {
        super.read(other);
        _inc = ((IIncInstruction) other).getIncrement();
    }

    void read(DataInput in) throws IOException {
        super.read(in);
        setLocal(in.readUnsignedByte());
        _inc = in.readByte();
    }

    void write(DataOutput out) throws IOException {
        super.write(out);
        out.writeByte(getLocal());
        out.writeByte(_inc);
    }
}
