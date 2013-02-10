package benchmark.serp.bytecode.lowlevel;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * A constant pool entry describing a class.
 * Class entries are used to refer to the current class, the superclass,
 * implemented interfaces, etc. Each class entry contains the constant pool
 * index of the {@link UTF8Entry} that stores the class name, which is
 * represented in internal form.
 *
 * @author Abe White
 */
public class ClassEntry extends Entry implements ConstantEntry {
    private int _nameIndex = 0;

    /**
     * Default constructor.
     */
    public ClassEntry() {
    }

    /**
     * Constructor.
     *
     * @param nameIndex the constant pool index of the {@link UTF8Entry}
     * containing the class name
     */
    public ClassEntry(int nameIndex) {
        _nameIndex = nameIndex;
    }

    /**
     * Return the constant pool index of the {@link UTF8Entry}
     * containing the class name. Defaults to 0.
     */
    public int getNameIndex() {
        return _nameIndex;
    }

    /**
     * Set the constant pool index of the {@link UTF8Entry}
     * containing the class name.
     */
    public void setNameIndex(int nameIndex) {
        Object key = beforeModify();
        _nameIndex = nameIndex;
        afterModify(key);
    }

    /**
     * Return the referenced {@link UTF8Entry}. This method can only
     * be run for entries that have been added to a constant pool.
     */
    public UTF8Entry getNameEntry() {
        return (UTF8Entry) getPool().getEntry(_nameIndex);
    }

    public int getType() {
        return Entry.CLASS;
    }

    public Object getConstant() {
        return getNameEntry().getValue();
    }

    public void setConstant(Object value) {
        getNameEntry().setConstant(value);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterClassEntry(this);
        visit.exitClassEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _nameIndex = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_nameIndex);
    }
}
