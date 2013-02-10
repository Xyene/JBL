package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * A local variable type contains the name, signature, index and scope
 * of a generics-using local used in opcodes.
 *
 * @author Abe White
 */
public class LocalVariableType extends Local {
    LocalVariableType(LocalVariableTypeTable owner) {
        super(owner);
    }

    /**
     * The owning table.
     */
    public LocalVariableTypeTable getLocalVariableTypeTable() {
        return (LocalVariableTypeTable) getTable();
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLocalVariableType(this);
        visit.exitLocalVariableType(this);
    }
}
