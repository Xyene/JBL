package benchmark.serp.bytecode;

/**
 * An entity that maintains ptrs to instructions in a code block.
 *
 * @author Abe White
 */
public interface InstructionPtr {
    /**
     * Use the byte indexes read from the class file to calculate and
     * set references to the target instruction(s) for this ptr.
     * This method will be called after the byte code
     * has been read in for the first time and before it is written after
     * modification.
     */
    public void updateTargets();

    /**
     * Replace the given old, likely invalid, target with a new target. The
     * new target Instruction is guaranteed to be in the same code
     * block as this InstructionPtr.
     */
    public void replaceTarget(Instruction oldTarget, Instruction newTarget);

    /**
     * Returns the Code block that owns the Instruction(s) this
     * InstructionPtr points to.
     */
    public Code getCode();
}
