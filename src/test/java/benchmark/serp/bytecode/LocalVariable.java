package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * A local variable contains the name, description, index and scope
 * of a local used in opcodes.
 *
 * @author Abe White
 */
public class LocalVariable extends Local {
    LocalVariable(LocalVariableTable owner) {
        super(owner);
    }

    /**
     * The owning table.
     */
    public LocalVariableTable getLocalVariableTable() {
        return (LocalVariableTable) getTable();
    }

    /**
     * Return the type of this local.
     * If the type has not been set, this method will return null.
     */
    public Class getType() {
        String type = getTypeName();
        if (type == null)
            return null;
        return Strings.toClass(type, getClassLoader());
    }

    /**
     * Return the type of this local.
     * If the type has not been set, this method will return null.
     */
    public BCClass getTypeBC() {
        String type = getTypeName();
        if (type == null)
            return null;
        return getProject().loadClass(type, getClassLoader());
    }

    /**
     * Set the type of this local.
     */
    public void setType(Class type) {
        if (type == null)
            setType((String) null);
        else
            setType(type.getName());
    }

    /**
     * Set the type of this local.
     */
    public void setType(BCClass type) {
        if (type == null)
            setType((String) null);
        else
            setType(type.getName());
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLocalVariable(this);
        visit.exitLocalVariable(this);
    }
}
