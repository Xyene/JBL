package benchmark.serp.bytecode;

/**
 * Any array load or store instruction. This class has
 * no functionality beyond the {@link TypedInstruction} but is provided
 * so that users can easily identify array instructions in code if need be.
 *
 * @author Abe White
 */
public abstract class ArrayInstruction extends TypedInstruction {
    ArrayInstruction(Code owner) {
        super(owner);
    }

    ArrayInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }
}
