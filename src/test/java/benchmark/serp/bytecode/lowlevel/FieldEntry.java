package benchmark.serp.bytecode.lowlevel;

import benchmark.serp.bytecode.visitor.*;

/**
 * A reference to a class field.
 *
 * @author Abe White
 */
public class FieldEntry extends ComplexEntry {
    /**
     * Default constructor.
     */
    public FieldEntry() {
    }

    /**
     * Constructor.
     *
     * @see ComplexEntry#ComplexEntry(int,int)
     */
    public FieldEntry(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    public int getType() {
        return Entry.FIELD;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterFieldEntry(this);
        visit.exitFieldEntry(this);
    }
}
