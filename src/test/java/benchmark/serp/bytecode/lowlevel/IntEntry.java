package benchmark.serp.bytecode.lowlevel;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * A constant int value in the constant pool.
 *
 * @author Abe White
 */
public class IntEntry extends Entry implements ConstantEntry {
    private int _value = -1;

    /**
     * Default constructor.
     */
    public IntEntry() {
    }

    /**
     * Constructor.
     *
     * @param value the constant int value of this entry
     */
    public IntEntry(int value) {
        _value = value;
    }

    public int getType() {
        return Entry.INT;
    }

    /**
     * Return the value of this constant.
     */
    public int getValue() {
        return _value;
    }

    /**
     * Set the value of this constant.
     */
    public void setValue(int value) {
        Object key = beforeModify();
        _value = value;
        afterModify(key);
    }

    public Object getConstant() {
        return Numbers.valueOf(getValue());
    }

    public void setConstant(Object value) {
        setValue(((Number) value).intValue());
    }

    protected void readData(DataInput in) throws IOException {
        _value = in.readInt();
    }

    protected void writeData(DataOutput out) throws IOException {
        out.writeInt(_value);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterIntEntry(this);
        visit.exitIntEntry(this);
    }
}
