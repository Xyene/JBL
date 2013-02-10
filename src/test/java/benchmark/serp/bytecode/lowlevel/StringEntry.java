package benchmark.serp.bytecode.lowlevel;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * A String constant in the constant pool. String constants
 * hold a reference to a {@link UTF8Entry} that stores the actual value.
 *
 * @author Abe White
 */
public class StringEntry extends Entry implements ConstantEntry {
    private int _stringIndex = -1;

    /**
     * Default constructor.
     */
    public StringEntry() {
    }

    /**
     * Constructor.
     *
     * @param stringIndex the constant pool index of the {@link UTF8Entry}
     * containing the value of this string
     */
    public StringEntry(int stringIndex) {
        _stringIndex = stringIndex;
    }

    public int getType() {
        return Entry.STRING;
    }

    /**
     * Return the constant pool index of the {@link UTF8Entry}
     * storing the value of this string.
     */
    public int getStringIndex() {
        return _stringIndex;
    }

    /**
     * Set the constant pool index of the {@link UTF8Entry}
     * storing the value of this string.
     */
    public void setStringIndex(int stringIndex) {
        Object key = beforeModify();
        _stringIndex = stringIndex;
        afterModify(key);
    }

    /**
     * Return the referenced {@link UTF8Entry}. This method can only
     * be run for entries that have been added to a constant pool.
     */
    public UTF8Entry getStringEntry() {
        return (UTF8Entry) getPool().getEntry(_stringIndex);
    }

    public Object getConstant() {
        return getStringEntry().getValue();
    }

    public void setConstant(Object value) {
        getStringEntry().setConstant(value);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterStringEntry(this);
        visit.exitStringEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _stringIndex = in.readUnsignedShort();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeShort(_stringIndex);
    }
}
