package benchmark.serp.bytecode.lowlevel;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * A long constant in the constant pool.
 *
 * @author Abe White
 */
public class LongEntry extends Entry implements ConstantEntry {
    private long _value = 0L;

    /**
     * Default constructor.
     */
    public LongEntry() {
    }

    /**
     * Constructor.
     *
     * @param value the constant long value of this entry
     */
    public LongEntry(long value) {
        _value = value;
    }

    public boolean isWide() {
        return true;
    }

    public int getType() {
        return Entry.LONG;
    }

    /**
     * Return the value of the constant.
     */
    public long getValue() {
        return _value;
    }

    /**
     * Set the value of the constant.
     */
    public void setValue(long value) {
        Object key = beforeModify();
        _value = value;
        afterModify(key);
    }

    public Object getConstant() {
        return Numbers.valueOf(getValue());
    }

    public void setConstant(Object value) {
        setValue(((Number) value).longValue());
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLongEntry(this);
        visit.exitLongEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _value = in.readLong();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeLong(_value);
    }
}
