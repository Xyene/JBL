package benchmark.serp.bytecode;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import benchmark.serp.bytecode.lowlevel.*;
import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * In bytecode attributes are used to represent anything that is not
 * part of the class structure. This includes the source file name, code of
 * methods, the line number table, etc. All attributes contain at a minimum
 * an immutable name that also determines the attribute's type.
 *
 * @author Abe White
 */
public abstract class Attribute extends Attributes implements VisitAcceptor {
    private int _nameIndex = 0;
    private Attributes _owner = null;

    Attribute(int nameIndex, Attributes owner) {
        _owner = owner;
        _nameIndex = nameIndex;
    }

    /**
     * Create an attribute of the appropriate type based on the
     * the attribute name.
     */
    static Attribute create(String name, Attributes owner) {
        // special case for annotations
        int nameIndex = owner.getPool().findUTF8Entry(name, true);
        if ("RuntimeVisibleAnnotations".equals(name)
            || "RuntimeInvisibleAnnotations".equals(name))
            return new Annotations(nameIndex, owner);

        try {
            Class type = Class.forName("serp.bytecode." + name);
            Constructor cons = type.getDeclaredConstructor(new Class[] {
                int.class, Attributes.class
            });
            return (Attribute) cons.newInstance(new Object[] {
                Numbers.valueOf(nameIndex), owner
            });
        } catch (Throwable t) {
            return new UnknownAttribute(nameIndex, owner);
        }
    }

    /**
     * Return the {@link Attributes} that owns this attribute. The entity
     * might be a {@link BCClass}, {@link BCField}, {@link BCMethod}, or other
     * attribute.
     */
    public Attributes getOwner() {
        return _owner;
    }

    /**
     * Return the index in the {@link ConstantPool} of the {@link UTF8Entry}
     * holding the name of this attribute.
     */
    public int getNameIndex() {
        return _nameIndex;
    }

    /**
     * Return the name of this attribute.
     */
    public String getName() {
        return ((UTF8Entry) getPool().getEntry(_nameIndex)).getValue();
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

    Collection getAttributesHolder() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Invalidate this attribute.
     */
    void invalidate() {
        _owner = null;
    }

    /**
     * Return the length of the bytecode representation of this attribute
     * in bytes, excluding the name index.
     */
    int getLength() {
        return 0;
    }

    /**
     * Copy the information from the given attribute to this one. Does
     * nothing by default.
     */
    void read(Attribute other) {
    }

    /**
     * Read the attribute bytecode from the given stream, up to length
     * bytes, excluding the name index. Does nothing by default.
     */
    void read(DataInput in, int length) throws IOException {
    }

    /**
     * Write the attribute bytecode to the given stream, up to length bytes,
     * excluding the name index. Does nothing by default.
     */
    void write(DataOutput out, int length) throws IOException {
    }
}
