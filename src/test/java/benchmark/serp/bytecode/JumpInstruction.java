package benchmark.serp.bytecode;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * An instruction that specifies a position in the code block to jump to.
 * Examples include <code>go2, jsr</code>, etc.
 *
 * @author Abe White
 */
public abstract class JumpInstruction extends Instruction 
    implements InstructionPtr {
    private InstructionPtrStrategy _target = new InstructionPtrStrategy(this);

    JumpInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    /**
     * Get the current target instruction to jump to, if it has been set.
     */
    public Instruction getTarget() {
        return _target.getTargetInstruction();
    }

    /**
     * Set the instruction to jump to; the instruction must already be
     * added to the code block.
     *
     * @return this instruction, for method chaining
     */
    public JumpInstruction setTarget(Instruction instruction) {
        _target.setTargetInstruction(instruction);
        return this;
    }

    /**
     * JumpInstructions are equal if they represent the same operation and
     * the instruction they jump to is the
     * same, or if the jump Instruction of either is unset.
     */
    public boolean equalsInstruction(Instruction other) {
        if (this == other)
            return true;
        if (!super.equalsInstruction(other))
            return false;

        Instruction target = ((JumpInstruction) other).getTarget();
        return target == null || getTarget() == null || target == getTarget();
    }

    public void updateTargets() {
        _target.updateTargets();
    }

    public void replaceTarget(Instruction oldTarget, Instruction newTarget) {
        _target.replaceTarget(oldTarget, newTarget);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterJumpInstruction(this);
        visit.exitJumpInstruction(this);
    }

    void read(Instruction orig) {
        super.read(orig);
        _target.setByteIndex(((JumpInstruction) orig)._target.getByteIndex());
    }

    void read(DataInput in) throws IOException {
        super.read(in);
        switch (getOpcode()) {
        case Constants.GOTOW:
        case Constants.JSRW:
            _target.setByteIndex(getByteIndex() + in.readInt());
            break;
        default:
            _target.setByteIndex(getByteIndex() + in.readShort());
        }
    }

    void write(DataOutput out) throws IOException {
        super.write(out);
        switch (getOpcode()) {
        case Constants.GOTOW:
        case Constants.JSRW:
            out.writeInt(_target.getByteIndex() - getByteIndex());
            break;
        default:
            out.writeShort(_target.getByteIndex() - getByteIndex());
        }
    }

    public void setOffset(int offset) {
        _target.setByteIndex(getByteIndex() + offset);
    }

    public int getOffset() {
        return _target.getByteIndex() - getByteIndex();
    }
}
