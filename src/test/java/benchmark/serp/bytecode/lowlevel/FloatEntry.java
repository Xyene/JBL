package benchmark.serp.bytecode.lowlevel;

import java.io.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * A constant float value in the constant pool.
 *
 * @author Abe White
 */
public class FloatEntry extends Entry implements ConstantEntry {
    private float _value = 0.0F;

    /**
     * Default constructor.
     */
    public FloatEntry() {
    }

    /**
     * Constructor.
     *
     * @param value the constant float value of this entry
     */
    public FloatEntry(float value) {
        _value = value;
    }

    public int getType() {
        return Entry.FLOAT;
    }

    /**
     * Return the value of this constant.
     */
    public float getValue() {
        return _value;
    }

    /**
     * Set the value of this constant.
     */
    public void setValue(float value) {
        Object key = beforeModify();
        _value = value;
        afterModify(key);
    }

    public Object getConstant() {
        return new Float(getValue());
    }

    public void setConstant(Object value) {
        setValue(((Number) value).floatValue());
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterFloatEntry(this);
        visit.exitFloatEntry(this);
    }

    void readData(DataInput in) throws IOException {
        _value = in.readFloat();
    }

    void writeData(DataOutput out) throws IOException {
        out.writeFloat(_value);
    }
}
