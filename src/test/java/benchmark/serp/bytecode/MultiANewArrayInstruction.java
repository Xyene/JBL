package benchmark.serp.bytecode;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * The <code>multianewarray</code> instruction, which creates a new
 * multi-dimensional array.
 *
 * @author Abe White
 */
public class MultiANewArrayInstruction extends ClassInstruction {
    private int _dims = -1;

    MultiANewArrayInstruction(Code owner) {
        super(owner, Constants.MULTIANEWARRAY);
    }

    int getLength() {
        return super.getLength() + 1;
    }

    public int getStackChange() {
        return -_dims + 1;
    }

    /**
     * Return the dimensions of the array, or -1 if not set.
     */
    public int getDimensions() {
        return _dims;
    }

    /**
     * Set the dimensions of the array.
     *
     * @return this instruction, for method chaining
     */
    public MultiANewArrayInstruction setDimensions(int dims) {
        _dims = dims;
        return this;
    }

    /**
     * Two MultiANewArray instructions are equal if they have the same
     * type and dimensions, or if the type and dimensions of either is unset.
     */
    public boolean equalsInstruction(Instruction other) {
        if (other == this)
            return true;
        if (!(other instanceof MultiANewArrayInstruction))
            return false;
        if (!super.equalsInstruction(other))
            return false;

        MultiANewArrayInstruction ins = (MultiANewArrayInstruction) other;
        int dims = getDimensions();
        int otherDims = ins.getDimensions();
        return dims == -1 || otherDims == -1 || dims == otherDims;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterMultiANewArrayInstruction(this);
        visit.exitMultiANewArrayInstruction(this);
    }

    void read(Instruction orig) {
        super.read(orig);
        _dims = ((MultiANewArrayInstruction) orig).getDimensions();
    }

    void read(DataInput in) throws IOException {
        super.read(in);
        _dims = in.readUnsignedByte();
    }

    void write(DataOutput out) throws IOException {
        super.write(out);
        out.writeByte(_dims);
    }
}
