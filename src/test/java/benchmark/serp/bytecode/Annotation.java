package benchmark.serp.bytecode;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import benchmark.serp.bytecode.lowlevel.*;
import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * A declared annotation.
 *
 * @author Abe White
 */
public class Annotation implements BCEntity, VisitAcceptor {
    private static Method ENUM_VALUEOF = null;
    private static Method ENUM_NAME = null;
    static {
        try {
            Class c = Class.forName("java.lang.Enum");
            ENUM_VALUEOF = c.getMethod("valueOf", new Class[] {
                Class.class, String.class });
            ENUM_NAME = c.getMethod("name", (Class[]) null);
        } catch (Throwable t) {
            // pre-1.5 JDK
        }
    }
 
    private BCEntity _owner = null;
    private int _typeIndex = 0;
    private List _properties = null;

    Annotation(BCEntity owner) {
        _owner = owner;
    }

    /**
     * Annotations are stored in an {@link Annotations} table or as
     * part of an {@link Annotation} property value.
     */
    public BCEntity getOwner() {
        return _owner;
    }

    void invalidate() {
        _owner = null;
    }

    /**
     * The index in the class {@link ConstantPool} of the
     * {@link UTF8Entry} holding the type of this annotation.
     */
    public int getTypeIndex() {
        return _typeIndex;
    }

    /**
     * The index in the class {@link ConstantPool} of the
     * {@link UTF8Entry} holding the type of this annotation.
     */
    public void setTypeIndex(int index) {
        _typeIndex = index;
    }

    /**
     * The name of this annotation's type.
     */
    public String getTypeName() {
        String desc = ((UTF8Entry) getPool().getEntry(_typeIndex)).getValue();
        return getProject().getNameCache().getExternalForm(desc, false);
    }

    /**
     * The {@link Class} object for this annotation's type.
     */
    public Class getType() {
        return Strings.toClass(getTypeName(), getClassLoader());
    }

    /**
     * The bytecode for the type of this annotation.
     */
    public BCClass getTypeBC() {
        return getProject().loadClass(getTypeName(), getClassLoader());
    }

    /**
     * This annotation's type.
     */
    public void setType(String type) {
        type = getProject().getNameCache().getInternalForm(type, true);
        _typeIndex = getPool().findUTF8Entry(type, true);
    }

    /**
     * This annotation's type.
     */
    public void setType(Class type) {
        setType(type.getName());
    }

    /**
     * This annotation's type.
     */
    public void setType(BCClass type) {
        setType(type.getName());
    }

    /**
     * All declared properties.
     */
    public Property[] getProperties() {
        if (_properties == null)
            return new Property[0];
        return (Property[]) _properties.toArray
            (new Property[_properties.size()]);
    }

    /**
     * Set the annotation properties.  This method is useful when
     * importing properties from another instance.
     */
    public void setProperties(Property[] props) {
        clearProperties();
        if (props != null)
            for (int i = 0; i < props.length; i++)
                addProperty(props[i]);
    }

    /**
     * Return the property with the given name, or null if none.
     */
    public Property getProperty(String name) {
        if (_properties == null)
            return null;
        Property prop;
        for (int i = 0; i < _properties.size(); i++) {
            prop = (Property) _properties.get(i);
            if (prop.getName().equals(name))
                return prop;
        }
        return null;
    }

    /**
     * Import a property from another instance.
     *
     * @return the newly added property
     */
    public Property addProperty(Property p) {
        Property prop = addProperty(p.getName());
        prop.setValue(p.getValue());
        return prop;
    }

    /**
     * Add a new property.
     */
    public Property addProperty(String name) {
        Property prop = new Property(this);
        prop.setName(name);
        if (_properties == null)
            _properties = new ArrayList();
        _properties.add(prop);
        return prop;
    }

    /**
     * Clear all annotation properties.
     */
    public void clearProperties() {
        if (_properties == null)
            return;
        for (int i = 0; i < _properties.size(); i++)
            ((Property) _properties.get(i)).invalidate();
        _properties.clear();
    }

    /**
     * Remove the given property.
     *
     * @return true if an property was removed, false otherwise
     */
    public boolean removeProperty(Property prop) {
        return prop != null && removeProperty(prop.getName());
    }

    /**
     * Remove the property with the given name.
     *
     * @return true if a property was removed, false otherwise
     */
    public boolean removeProperty(String name) {
        if (name == null || _properties == null)
            return false;
        Property prop;
        for (int i = 0; i < _properties.size(); i++) {
            prop = (Property) _properties.get(i);
            if (prop.getName().equals(name)) {
                prop.invalidate();
                _properties.remove(i);
                return true;
            }
        }
        return false;
    }

    public Project getProject() {
        return _owner.getProject();
    }

    public ConstantPool getPool() {
        return _owner.getPool();
    }

    public ClassLoader getClassLoader() {
        return _owner.getClassLoader();
    }

    public boolean isValid() {
        return _owner != null;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterAnnotation(this);
        if (_properties != null)
            for (int i = 0; i < _properties.size(); i++)
                ((Property) _properties.get(i)).acceptVisit(visit);
        visit.exitAnnotation(this);
    }

    int getLength() {
        int len = 4;
        if (_properties != null)
            for (int i = 0; i < _properties.size(); i++)
                len += ((Property) _properties.get(i)).getLength();
        return len;
    }

    void read(DataInput in) throws IOException {
        _typeIndex = in.readUnsignedShort();
        clearProperties();
        int props = in.readUnsignedShort();
        if (props > 0) {
            if (_properties == null)
                _properties = new ArrayList(props);
            Property prop;
            for (int i = 0; i < props; i++) {
                prop = new Property(this);
                prop.read(in);
                _properties.add(prop); 
            }
        }
    }

    void write(DataOutput out) throws IOException {
        out.writeShort(_typeIndex); 
        out.writeShort((_properties == null) ? 0 : _properties.size());
        if (_properties != null) {
            for (int i = 0; i < _properties.size(); i++)
                ((Property) _properties.get(i)).write(out);
        }
    }

    /**
     * An annotation property.
     */
    public static class Property implements BCEntity, VisitAcceptor {
        private Annotation _owner = null;
        private int _nameIndex = 0;
        private final Value _value = new Value();
        private Value[] _values = null;

        Property(Annotation owner) {
            _owner = owner;
        }

        /**
         * The owning annotation.
         */
        public Annotation getAnnotation() {
            return _owner;
        }

        void invalidate() {
            _owner = null;
        }

        /**
         * Return the index in the class {@link ConstantPool} of the
         * {@link UTF8Entry} holding the name of this property.
         */
        public int getNameIndex() {
            return _nameIndex;
        }

        /**
         * Set the index in the class {@link ConstantPool} of the
         * {@link UTF8Entry} holding the name of this property.
         */
        public void setNameIndex(int index) {
            _nameIndex = index;
        }

        /**
         * Return the name of this property.
         */
        public String getName() {
            return ((UTF8Entry) getPool().getEntry(_nameIndex)).getValue();
        }

        /**
         * Set the name of this property.
         */
        public void setName(String name) {
            _nameIndex = getPool().findUTF8Entry(name, true);
        }

        /**
         * Return the value of the property as its wrapper type.
         * Returns class values as the class name.
         */
        public Object getValue() {
            if (_values == null)
                return getValue(_value);
            Object[] vals = new Object[_values.length];
            for (int i = 0; i < vals.length; i++)
                vals[i] = getValue(_values[i]);
            return vals;
        }

        /**
         * Extract the Java value.
         */
        private Object getValue(Value val) {
            if (val.index == -1)
                return val.value;

            Object o = ((ConstantEntry) getPool().getEntry(val.index)).
                getConstant();
            if (val.index2 != -1) {
                // enum value
                String e = getProject().getNameCache().
                    getExternalForm((String) o, false);
                String name = ((UTF8Entry) getPool().getEntry(val.index2)).
                    getValue();
                try {
                    Class cls = Class.forName(e, true, getClassLoader());  
                    return ENUM_VALUEOF.invoke(null, new Object[] {cls, name});
                } catch (Throwable t) {
                    return e + "." + name;
                } 
            }
            if (val.type == null)
                return o;

            switch (val.type.getName().charAt(0)) {
            case 'b': 
                if (val.type == boolean.class)
                    return (((Number) o).intValue() != 0) ? Boolean.TRUE
                        : Boolean.FALSE;
                return new Byte(((Number) o).byteValue());
            case 'c':
                return new Character((char) ((Number) o).intValue());
            case 'j': // java.lang.Class
                return getProject().getNameCache().getExternalForm((String) o, 
                    false);
            case 's':
                return new Short(((Number) o).shortValue());
            default:
                return o;
            }
        }

        /**
         * Set value of this property. The value should be an instance of any
         * primitive wrapper type, String, Class, BCClass, an enum constant,
         * an annotation, or an array of any of these types.
         */
        public void setValue(Object value) {
            if (!value.getClass().isArray()) {
                _values = null;
                setValue(_value, value);
            } else {
                _value.value = null;
                _values = new Value[Array.getLength(value)];
                for (int i = 0; i < _values.length; i++) {
                    _values[i] = new Value();
                    setValue(_values[i], Array.get(value, i));
                } 
            }
        }

        /**
         * Set the given value.
         */
        private void setValue(Value val, Object o) {
            if (o instanceof String) 
                setValue(val, (String) o);
            else if (o instanceof Boolean)
                setValue(val, ((Boolean) o).booleanValue());
            else if (o instanceof Byte)
                setValue(val, ((Byte) o).byteValue());
            else if (o instanceof Character)
                setValue(val, ((Character) o).charValue());
            else if (o instanceof Double)
                setValue(val, ((Double) o).doubleValue());
            else if (o instanceof Float)
                setValue(val, ((Float) o).floatValue());
            else if (o instanceof Integer)
                setValue(val, ((Integer) o).intValue());
            else if (o instanceof Long)
                setValue(val, ((Long) o).longValue());
            else if (o instanceof Short)
                setValue(val, ((Short) o).shortValue());
            else if (o instanceof Class)
                setClassNameValue(val, ((Class) o).getName());
            else if (o instanceof BCClass)
                setClassNameValue(val, ((BCClass) o).getName());
            else if (o instanceof Annotation)
                setValue(val, (Annotation) o);
            else {
                String name = getEnumName(o);
                if (name != null) {
                    String type = getProject().getNameCache().
                        getInternalForm(o.getClass().getName(), true);
                    val.index = getPool().findUTF8Entry(type, true);
                    val.index2 = getPool().findUTF8Entry(name, true);
                    val.value = null;
                    val.type = null;
                } else {
                    val.index = -1;
                    val.index2 = -1;
                    val.value = o;
                    val.type = o.getClass();
                }
            }
        }

        /**
         * Return the name of this enum value, or null if not an enum.
         */
        private static String getEnumName(Object o) {
            for (Class c = o.getClass(); true; c = c.getSuperclass()) {
                if (c == Object.class || c == null)
                    return null;
                if ("java.lang.Enum".equals(c.getName()))
                    break;
            }
            try {
                return (String) ENUM_NAME.invoke(o, (Object[]) null);
            } catch (Throwable t) {
                return o.toString();
            }
        }

        /**
         * Return the string value of this property, or null if not set.
         */
        public String getStringValue() {
            return (String) getValue();
        }

        /**
         * Return the boolean value of this property, or false if not set.
         */
        public boolean getBooleanValue() {
            Object value = getValue();
            return (value == null) ? false : ((Boolean) value).booleanValue();
        }

        /**
         * Return the byte value of this property, or false if not set.
         */
        public byte getByteValue() {
            Object value = getValue();
            return (value == null) ? (byte) 0 : ((Number) value).byteValue();
        }

        /**
         * Return the int value of this property, or 0 if not set.
         */
        public int getIntValue() {
            Object value = getValue();
            return (value == null) ? 0 : ((Number) value).intValue();
        }

        /**
         * Return the long value of this property, or 0 if not set.
         */
        public long getLongValue() {
            Object value = getValue();
            return (value == null) ? 0L : ((Number) value).longValue();
        }

        /**
         * Return the float value of this property, or 0 if not set.
         */
        public float getFloatValue() {
            Object value = getValue();
            return (value == null) ? 0F : ((Number) value).floatValue();
        }

        /**
         * Return the double value of this property, or 0 if not set.
         */
        public double getDoubleValue() {
            Object value = getValue();
            return (value == null) ? 0D : ((Number) value).doubleValue();
        }

        /**
         * Return the short value of this property, or 0 if not set.
         */
        public short getShortValue() {
            Object value = getValue();
            return (value == null) ? (short) 0 : ((Number) value).shortValue();
        }

        /**
         * Return the class value of this property, or null if not set.
         */
        public String getClassNameValue() {
            return (String) getValue();
        }

        /**
         * Return the annotation value of this property, or null if not set.
         */
        public Annotation getAnnotationValue() {
            return (Annotation) getValue();
        }

        /**
         * Set the string value of this property.
         */
        public void setValue(String value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the string value of this property.
         */
        private void setValue(Value val, String o) {
            val.index = getPool().findUTF8Entry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = null;
        }

        /**
         * Set the boolean value of this property.
         */
        public void setValue(boolean value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the boolean value of this property.
         */
        private void setValue(Value val, boolean o) {
            setValue(val, (o) ? 1 : 0);
            val.type = boolean.class;
        }

        /**
         * Set the byte value of this property.
         */
        public void setValue(byte value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the byte value of this property.
         */
        private void setValue(Value val, byte o) {
            setValue(val, (int) o);
            val.type = byte.class;
        }

        /**
         * Set the int value of this property.
         */
        public void setValue(int value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the int value of this property.
         */
        private void setValue(Value val, int o) {
            val.index = getPool().findIntEntry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = null;
        }

        /**
         * Set the long value of this property.
         */
        public void setValue(long value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the long value of this property.
         */
        private void setValue(Value val, long o) {
            val.index = getPool().findLongEntry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = null;
        }

        /**
         * Set the float value of this property.
         */
        public void setValue(float value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the float value of this property.
         */
        private void setValue(Value val, float o) {
            val.index = getPool().findFloatEntry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = null;
        }

        /**
         * Set the double value of this property.
         */
        public void setValue(double value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the double value of this property.
         */
        private void setValue(Value val, double o) {
            val.index = getPool().findDoubleEntry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = null;
        }

        /**
         * Set the short value of this property.
         */
        public void setValue(short value) {
            _values = null;
            setValue(_value, value);
        }

        /**
         * Set the short value of this property.
         */
        private void setValue(Value val, short o) {
            setValue(val, (int) o);
            val.type = short.class;
        }

        /**
         * Set the class value of this property.
         */
        public void setValue(Class value) {
            setClassNameValue(value.getName());
        }

        /**
         * Set the class value of this property.
         */
        public void setValue(BCClass value) {
            setClassNameValue(value.getName());
        }

        /**
         * Set the class value of this property.
         */
        public void setClassNameValue(String value) {
            _values = null;
            setClassNameValue(_value, value);
        }

        /**
         * Set the class value of this property.
         */
        private void setClassNameValue(Value val, String o) {
            o = getProject().getNameCache().getInternalForm(o, true);
            val.index = getPool().findUTF8Entry(o, true);
            val.index2 = -1;
            val.value = null;
            val.type = Class.class;
        }

        /**
         * Set the annotation value of this property by importing the given
         * annotation from another instance.
         */
        public Annotation setValue(Annotation value) {
            _values = null;
            return setValue(_value, value);
        }

        /**
         * Set the annotation value of this property by importing the given
         * annotation from another instance.
         */
        private Annotation setValue(Value val, Annotation o) {
            Annotation anno = new Annotation(this);
            anno.setType(o.getTypeName());
            anno.setProperties(o.getProperties());
            val.index = -1;
            val.index2 = -1;
            val.value = anno;
            val.type = null;
            return anno;
        }

        /**
         * Set the annotation value of this property by importing the given
         * annotation from another instance.
         */
        public Annotation[] setValue(Annotation[] value) {
            _value.value = null;
            _values = new Value[value.length];
            Annotation[] ret = new Annotation[value.length];
            for (int i = 0; i < _values.length; i++) {
                _values[i] = new Value();
                ret[i] = setValue(_values[i], value[i]);
            }
            return ret;
        }

        /**
         * Set this property value to a new annotation of the given type, 
         * returning the annotation for manipulation.
         */
        public Annotation newAnnotationValue(Class type) {
            return newAnnotationValue(type.getName());
        }

        /**
         * Set this property value to a new annotation of the given type, 
         * returning the annotation for manipulation.
         */
        public Annotation newAnnotationValue(BCClass type) {
            return newAnnotationValue(type.getName());
        }

        /**
         * Set this property value to a new annotation of the given type, 
         * returning the annotation for manipulation.
         */
        public Annotation newAnnotationValue(String type) {
            Annotation anno = new Annotation(this);
            anno.setType(type);
            _values = null;
            _value.index = -1;
            _value.index2 = -1;
            _value.value = anno;
            _value.type = null;
            return anno;
        }

        /**
         * Set this property value to a new annotation array of the given type
         * and length, returning the annotations for manipulation.
         */
        public Annotation[] newAnnotationArrayValue(Class type, int length) {
            return newAnnotationArrayValue(type.getName(), length);
        }

        /**
         * Set this property value to a new annotation array of the given type
         * and length, returning the annotations for manipulation.
         */
        public Annotation[] newAnnotationArrayValue(BCClass type, int length) {
            return newAnnotationArrayValue(type.getName(), length);
        }

        /**
         * Set this property value to a new annotation array of the given type
         * and length, returning the annotations for manipulation.
         */
        public Annotation[] newAnnotationArrayValue(String type, int length) {
            _value.value = null;
            _values = new Value[length]; 
            Annotation[] ret = new Annotation[length];
            for (int i = 0; i < length; i++) {
                ret[i] = new Annotation(this);
                ret[i].setType(type);
                _values[i] = new Value();
                _values[i].index = -1;
                _values[i].index2 = -1;
                _values[i].value = ret[i];
                _values[i].type = null;
            }
            return ret;
        }

        public Project getProject() {
            return _owner.getProject();
        }

        public ConstantPool getPool() {
            return _owner.getPool();
        }

        public ClassLoader getClassLoader() {
            return _owner.getClassLoader();
        }

        public boolean isValid() {
            return _owner != null && (_values != null || _value.index != -1
                || _value.value != null);
        }

        public void acceptVisit(BCVisitor visit) {
            visit.enterAnnotationProperty(this);
            visit.exitAnnotationProperty(this);
        }

        int getLength() {
            if (!isValid())
                throw new IllegalStateException();

            int len = 2; // name
            if (_values == null)
                len += getLength(_value);
            else {
                len += 3; // arr length + tag
                for (int i = 0; i < _values.length; i++)
                    len += getLength(_values[i]);
            }
            return len;
        }

        /**
         * Return the length of the given value.
         */
        private int getLength(Value val) {
            if (val.index2 != -1)
                return 5; // tag + enum type + enum name
            if (val.index != -1)
                return 3; // tag + constant or class
            return 1 + ((Annotation) val.value).getLength(); // tag + anno
        }

        void read(DataInput in) throws IOException {
            _nameIndex = in.readUnsignedShort(); 
            int tag = in.readByte();
            if (tag == '[') {
                int len = in.readUnsignedShort();
                _values = new Value[len];
                for (int i = 0; i < len; i++) {
                    _values[i] = new Value();
                    read(_values[i], in.readByte(), in); 
                }
            } else
                read(_value, tag, in);
        }

        /**
         * Read data into the given value.
         */
        private void read(Value val, int tag, DataInput in) throws IOException {
            switch (tag) {
            case 'B':
                val.index = in.readUnsignedShort();
                val.index2 = -1;
                val.value = null;
                val.type = byte.class;
                break;
            case 'C':
                val.index = in.readUnsignedShort();
                val.index2 = -1;
                val.value = null;
                val.type = char.class;
                break;
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 's':
                val.index = in.readUnsignedShort();
                val.index2 = -1;
                val.value = null;
                val.type = null;
                break;
            case 'Z':
                val.index = in.readUnsignedShort();
                val.index2 = -1;
                val.value = null;
                val.type = boolean.class;
                break;
            case 'c':
                val.index = in.readUnsignedShort();
                val.index2 = -1;
                val.value = null;
                val.type = Class.class;
                break;
            case 'e':
                val.index = in.readUnsignedShort();
                val.index2 = in.readUnsignedShort();
                val.value = null;
                val.type = null;
                break;
            case '@':
                Annotation anno = new Annotation(this);
                anno.read(in);
                val.index = -1;
                val.index2 = -1;
                val.value = anno;
                val.type = null;
                break;
            default:
                throw new IllegalStateException(String.valueOf(tag));
            }
        }

        void write(DataOutput out) throws IOException {
            if (!isValid())
                throw new IllegalStateException();

            out.writeShort(_nameIndex);
            if (_values == null)
                write(_value, out);
            else {
                out.writeByte('[');
                out.writeShort(_values.length);
                for (int i = 0; i < _values.length; i++)
                    write(_values[i], out);
            }
        }

        /**
         * Write the data for the given value to the stream.
         */
        private void write(Value val, DataOutput out) throws IOException {
            if (val.index2 != -1) {
                out.writeByte('e');
                out.writeShort(val.index);
                out.writeShort(val.index2);
            } else if (val.index != -1) {
                if (val.type != null) {
                    switch (val.type.getName().charAt(0)) {
                    case 'b':
                        if (val.type == byte.class)
                            out.writeByte('B');
                        else 
                            out.writeByte('Z');
                        break;
                    case 'c':
                        out.writeByte('C');
                        break;
                    case 'j': // java.lang.Class
                        out.writeByte('c');
                        break;
                    case 's':
                        out.writeByte('S');
                        break;
                    default:
                        throw new IllegalStateException(val.type.getName());
                    }
                } else {
                    Entry entry = getPool().getEntry(val.index);
                    if (entry instanceof DoubleEntry) 
                        out.writeByte('D');
                    else if (entry instanceof FloatEntry)
                        out.writeByte('F');
                    else if (entry instanceof IntEntry)
                        out.writeByte('I');
                    else if (entry instanceof LongEntry)
                        out.writeByte('J');
                    else if (entry instanceof UTF8Entry)
                        out.writeByte('s');
                    else
                        throw new IllegalStateException(entry.getClass().
                            getName());
                }
                out.writeShort(val.index);
            } else {
                out.writeByte('@');
                ((Annotation) val.value).write(out);
            }
        }

        /**
         * Property value struct.
         */ 
        private static class Value {
            public int index = -1;
            public int index2 = -1;
            public Class type = null;
            public Object value = null;
        }
    }
}
