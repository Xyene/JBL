package benchmark.serp.bytecode.lowlevel;

import benchmark.serp.bytecode.visitor.*;

/**
 * A reference to an interface method.
 *
 * @author Abe White
 */
public class InterfaceMethodEntry extends ComplexEntry {
    /**
     * Default constructor.
     */
    public InterfaceMethodEntry() {
    }

    /**
     * Constructor.
     *
     * @see ComplexEntry#ComplexEntry(int,int)
     */
    public InterfaceMethodEntry(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    public int getType() {
        return Entry.INTERFACEMETHOD;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterInterfaceMethodEntry(this);
        visit.exitInterfaceMethodEntry(this);
    }
}
