package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Code blocks compiled from source have local variable type tables mapping
 * generics-using locals used in opcodes to their names and signatures.
 *
 * @author Abe White
 */
public class LocalVariableTypeTable extends LocalTable {
    LocalVariableTypeTable(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    /**
     * Return all the locals of this method.
     */
    public LocalVariableType[] getLocalVariableTypes() {
        return (LocalVariableType[]) getLocals();
    }

    /**
     * Return the local with the given locals index, or null if none.
     */
    public LocalVariableType getLocalVariableType(int local) {
        return (LocalVariableType) getLocal(local);
    }

    /**
     * Return the local with the given name, or null if none. If multiple
     * locals have the given name, which is returned is undefined.
     */
    public LocalVariableType getLocalVariableType(String name) {
        return (LocalVariableType) getLocal(name);
    }

    /**
     * Return all locals with the given name, or empty array if none.
     */
    public LocalVariableType[] getLocalVariableTypes(String name) {
        return (LocalVariableType[]) getLocals(name);
    }

    /**
     * Import a local from another method/class. Note that
     * the program counter and length from the given local is copied
     * directly, and thus will be incorrect unless this method is the same
     * as the one the local is copied from, or the pc and length are reset.
     */
    public LocalVariableType addLocalVariableType(LocalVariableType local) {
        return (LocalVariableType) addLocal(local);
    }

    /**
     * Add a local to this table.
     */
    public LocalVariableType addLocalVariableType() {
        return (LocalVariableType) addLocal();
    }

    /**
     * Add a local to this table.
     */
    public LocalVariableType addLocalVariableType(String name, String type) {
        return (LocalVariableType) addLocal(name, type);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLocalVariableTypeTable(this);
        LocalVariableType[] locals = (LocalVariableType[]) getLocals();
        for (int i = 0; i < locals.length; i++)
            locals[i].acceptVisit(visit);
        visit.exitLocalVariableTypeTable(this);
    }

    protected Local newLocal() {
        return new LocalVariableType(this);
    }

    protected Local[] newLocalArray(int size) {
        return new LocalVariableType[size];
    }
}
