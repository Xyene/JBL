package benchmark.serp.bytecode;

/**
 * A synchronization instruction.
 *
 * @author Abe White
 */
public abstract class MonitorInstruction extends Instruction {
    MonitorInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public int getStackChange() {
        return -1;
    }
}
