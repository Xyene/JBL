package benchmark.serp.bytecode;

/**
 * InstructionPtrStrategy handles the different strategies for finding the
 * Instructions that InstructionPtrs point to. These strategies include,
 * from least desirable to most desirable, using byte indexes,
 * and storing a reference to the target Instruction proper.
 *
 * @author Eric Lindauer
 * @date 2002.7.26
 */
class InstructionPtrStrategy implements InstructionPtr {
    // the Instruction doing the targetting
    private InstructionPtr _pointer;

    // two different ways to find the target from the pointer.
    // _target is used first, then _byteIndex
    private Instruction _target = null;
    private int _byteIndex = -1;

    public InstructionPtrStrategy(InstructionPtr pointer) {
        _pointer = pointer;
    }

    public InstructionPtrStrategy(InstructionPtr pointer, Instruction target) {
        this(pointer);
        setTargetInstruction(target);
    }

    /**
     * Sets the byteIndex where the target Instruction can be found.
     * This target will now be using byte indices as its target finding
     * strategy, which is the least robust option. Changing the Code block
     * or importing it into another Method may result in an invalid target.
     */
    public void setByteIndex(int index) {
        if (index < -1)
            throw new IllegalArgumentException(String.valueOf(index));
        _byteIndex = index;
        _target = null;
    }

    /**
     * Changes the target Instruction. The target is in the best state
     * possible and should maintain this information even in the face
     * of Code imports and Code changes.
     */
    public void setTargetInstruction(Instruction ins) {
        if (ins.getCode() != getCode())
            throw new IllegalArgumentException("Instruction pointers and " 
                + "targets must be part of the same code block.");
        _target = ins;
        _byteIndex = -1;
    }

    /**
     * Returns the Instruction this Target is targetting. This request
     * does not change the targetting strategy for this Target.
     */
    public Instruction getTargetInstruction() {
        if (_target != null)
            return _target;
        return getCode().getInstruction(_byteIndex);
    }

    /**
     * Returns the byteIndex at which the target instruction can be found.
     * This call does not change the Target strategy.
     */
    public int getByteIndex() {
        if (_target == null)
            return _byteIndex;
        return _target.getByteIndex();
    }

    /**
     * Same as getInstruction, but this method alters the Target strategy
     * to use the returned Instruction. This method alters the Target
     * strategy (and Instruction) iff it was previously using byte indexes.
     */
    public void updateTargets() {
        if (_target == null)
            _target = getCode().getInstruction(_byteIndex);
    }

    public void replaceTarget(Instruction oldTarget, Instruction newTarget) {
        if (getTargetInstruction() == oldTarget)
            setTargetInstruction(newTarget);
    }

    public Code getCode() {
        return _pointer.getCode();
    }
}
