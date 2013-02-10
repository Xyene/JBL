package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Stores a value from the stack into a field.
 *
 * @author Abe White
 */
public class PutFieldInstruction extends FieldInstruction {
    PutFieldInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public int getLogicalStackChange() {
        if (getFieldTypeName() == null)
            return 0;
        if (getOpcode() == Constants.PUTSTATIC)
            return -1;
        return -2;
    }

    public int getStackChange() {
        String type = getFieldTypeName();
        if (type == null)
            return 0;

        int stack = -2;
        if (long.class.getName().equals(type) 
            || double.class.getName().equals(type))
            stack++;
        if (getOpcode() == Constants.PUTSTATIC)
            stack++;
        return stack;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterPutFieldInstruction(this);
        visit.exitPutFieldInstruction(this);
    }
}
