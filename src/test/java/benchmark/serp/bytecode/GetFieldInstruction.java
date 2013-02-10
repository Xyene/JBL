package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Loads a value from a field onto the stack.
 *
 * @author Abe White
 */
public class GetFieldInstruction extends FieldInstruction {
    GetFieldInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public int getLogicalStackChange() {
        if (getOpcode() == Constants.GETSTATIC)
            return 1;
        return 0;
    }

    public int getStackChange() {
        String type = getFieldTypeName();
        if (type == null)
            return 0;

        int stack = 0;
        if (long.class.getName().equals(type) 
            || double.class.getName().equals(type))
            stack++;
        if (getOpcode() == Constants.GETSTATIC)
            stack++;
        return stack;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterGetFieldInstruction(this);
        visit.exitGetFieldInstruction(this);
    }
}
