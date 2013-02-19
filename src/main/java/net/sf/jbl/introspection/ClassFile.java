package net.sf.jbl.introspection;

import net.sf.jbl.introspection.attributes.Code;
import net.sf.jbl.introspection.metadata.Metadatable;
import net.sf.jbl.util.ByteStream;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A class file structure, used for introspection and modification of classes.
 */
public class ClassFile extends AccessibleMember implements Metadatable<Attribute>, Opcode, Cloneable {
    protected int majorVersion;
    protected int minorVersion;
    protected ConstantPool constants;
    protected String thisClass;
    protected String superClass;
    protected List<String> interfaces;
    protected List<Field> fields;
    protected List<Method> methods;
    protected Metadatable.Container metadata;

    public ClassFile(ByteStream in) {
        if (in.readInt() != 0xCAFEBABE)
            throw new ClassFormatError("file does not start with magic number 0xCAFEBABE");

        minorVersion = in.readShort();
        majorVersion = in.readShort();
        if (majorVersion > Opcode.JDK_8)
            throw new UnsupportedClassVersionError("unknown major version: " + majorVersion);

        constants = new ConstantPool(in);

        flag = in.readShort();
        thisClass = constants.getString(in.readShort());
        superClass = constants.getString(in.readShort());

        int ni = in.readShort();
        interfaces = new ArrayList<String>(ni); //Allocate the size, saves time for not having to enlarge later
        for (int i = 0; i != ni; i++)
            interfaces.add(constants.getString(in.readShort()));

        int nf = in.readShort();
        fields = new ArrayList<Field>(nf);
        for (int i = 0; i != nf; i++) {
            fields.add(new Field(in.readShort(), constants.getUTF(in.readShort()), constants.getUTF(in.readShort()), new Container(readAttributes(in))));
        }
/*
        int nm = in.readShort();
        methods = new ArrayList<Method>(nm);
        for (int i = 0; i != nm; i++)
            methods.add(new Method(in.readShort(), constants.getUTF(in.readShort()), constants.getUTF(in.readShort())));      */

       //  metadata = new Metadatable.Container(readAttributes(in));
    }

    /**
     * Constructs a class file object.
     *
     * @param stream The stream to construct this object from.
     */
    public ClassFile(InputStream stream) {
        this(ByteStream.readStream(stream));
    }

    public ClassFile(File file) {
        this(ByteStream.readStream(file));
    }

    public ClassFile(Class clazz) {
        this(clazz.getName());
    }

    public ClassFile(String name) {
        this(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
    }

    public ClassFile(byte[] bytes) {
        this(ByteStream.readStream(bytes));
    }

    public ClassFile() {
    }

    protected <E extends Member> void dumpMembers(ByteStream out, List<E> members) {
        int ms = members.size();
        out.writeShort(ms);
        for (int i = 0; i != ms; i++) {
            Member m = members.get(i);
            out.writeShort(flag).writeShort(constants.newUTF(m.name)).writeShort(constants.newUTF(m.descriptor));

            List<Attribute> meta = m.metadata.getAttributes();
            int size;
            out.writeShort(size = meta.size());
            for (int a = 0; a != size; a++) {
                meta.get(a).dump(out, constants);
            }
        }
    }

    protected List<Attribute> readAttributes(ByteStream in) {
        int size;
        List<Attribute> out = new ArrayList<Attribute>(size = in.readShort());
        for (int i = 0; i != size; i++) {
            String name = constants.getUTF(in.readShort());
            int len = in.readInt();
            if ("Code".equals(name)) out.add(new Code(in));
            else out.add(new Attribute(name, in.read(len)));
        }
        return out;
    }

    public byte[] toByteArray() {
        // We can guarantee that at least the magic number and the major/minor shorts
        // will be written, so enlarge now to save 3 System.arraycopy calls in ByteStream.enlarge
        ByteStream out = ByteStream.writeStream(8);
        ByteStream body = ByteStream.writeStream();
        body.writeShort(constants.newUTF(thisClass));
        body.writeShort(constants.newUTF(superClass));

        int is = interfaces.size();
        body.writeShort(is);
        for (int i = 0; i != is; i++)
            body.writeShort(constants.newString(interfaces.get(i)));


        dumpMembers(out, fields);
        // dumpMembers(out, methods);

        //dumpAttributes(out, metadata.getAttributes());

        out.writeInt(0xCAFEBABE);
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);
        out.writeBytes(constants.cache.getBuffer());
        out.writeShort(flag);

        out.writeBytes(body.getBuffer());
        return out.getBuffer();
    }

    /**
     * Defines a Class from this class file.
     *
     * @return a defined class.
     */

    public Class define(final String name) {
        return new ClassLoader() {
            // Here we have to define our own class loader due to the System one
            // refusing to load classes from a byte[]. We also require the name of the class
            // since the one parsed may be invalid.
            public Class defineClass(byte[] bytes) {
                return super.defineClass(name, bytes, 0, bytes.length);
            }
        }.defineClass(toByteArray());
    }

    /**
     * Fetches the major version of this class.
     *
     * @return the major version of this class.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the major version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMajorVersion(int version) {
        majorVersion = version;
    }

    /**
     * Fetches the minor version of this class.
     *
     * @return the minor version of this class.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the minor version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMinorVersion(int version) {
        minorVersion = version;
    }

    /**
     * Returns this class' constant pool.
     *
     * @return a constant pool.
     */
    public ConstantPool getConstants() {
        return constants;
    }

    /**
     * Sets this class' constant pool.
     *
     * @param constants the pool to set it to.
     */
    public void setConstants(ConstantPool constants) {
        this.constants = constants;
    }

    /**
     * Returns the fully qualified name of this class.
     *
     * @return the fully qualified name of this class.
     */
    public String getName() {
        return thisClass;
    }

    /**
     * Sets the name of this class.
     *
     * @param clazz The new name for this class.
     */
    public void setName(String clazz) {
        thisClass = clazz;
    }

    /**
     * Returns the fully qualified name of this class' superclass.
     *
     * @return the fully qualified name of this class' superclass.
     */
    public String getSuperClass() {
        return superClass;
    }

    /**
     * Sets the name of this class' superclass.
     *
     * @param clazz The new name for this class' superclass.
     */
    public void setSuperClass(String clazz) {
        superClass = clazz;
    }

    /**
     * Returns this class' interface pool.
     *
     * @return an interface pool.
     */
    public List<String> getInterfaces() {
        return interfaces;
    }

    /**
     * Sets this class' interface pool.
     *
     * @param interfaces the pool to set it to.
     */
    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * Returns this class' field pool.
     *
     * @return a field pool.
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Sets this class' field pool.
     *
     * @param fields the pool to set it to.
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * Sets this class' method pool.
     *
     * @param methods the pool to set it to.
     */
    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public List<Attribute> getAttributes() {
        return metadata.getAttributes();
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributePool the pool to set it to.
     */
    public void setAttributes(List<Attribute> attributePool) {
        metadata.setAttributes(attributePool);
    }

    @Override
    public void removeMetadata(String meta) {
        metadata.removeMetadata(meta);
    }

    @Override
    public Object getMetadata(String meta) {
        return metadata.getMetadata(meta);
    }

    @Override
    public boolean hasMetadata(String meta) {
        return metadata.hasMetadata(meta);
    }

    @Override
    public <V> void addMetadata(String meta, V value) {
        metadata.addMetadata(meta, value);
    }

    //General purpose class access flags
    public boolean isInterface() {
        return is(ACC_INTERFACE);
    }

    public void setInterface(boolean i) {
        flag = i ? flag | ACC_INTERFACE : flag & ~ACC_INTERFACE;
    }

    public boolean isEnum() {
        return is(ACC_ENUM);
    }

    public void setEnum(boolean i) {
        flag = i ? flag | ACC_ENUM : flag & ~ACC_ENUM;
    }

    public boolean isAnnotation() {
        return is(ACC_ANNOTATION);
    }

    public void setAnnotation(boolean i) {
        flag = i ? flag | ACC_ANNOTATION : flag & ~ACC_ANNOTATION;
    }

    public boolean isSuper() {
        return is(ACC_SUPER);
    }

    public void setSuper(boolean i) {
        flag = i ? flag | ACC_SUPER : flag & ~ACC_SUPER;
    }

    public boolean isAbstract() {
        return is(ACC_ABSTRACT);
    }

    public void setAbstract(boolean i) {
        flag = i ? flag | ACC_ABSTRACT : flag & ~ACC_ABSTRACT;
    }

    public boolean isClass() {
        return !isInterface() && !isAnnotation() && isEnum();
    }

    @Override
    public ClassFile clone() {
        ClassFile clone = new ClassFile();
        clone.minorVersion = minorVersion;
        clone.majorVersion = majorVersion;
        //TODO: clone constants
        clone.flag = flag;
        clone.thisClass = thisClass;
        clone.superClass = superClass;
        //TODO: clone interfaces, fields, methods, and attributes
        return clone;
    }
}
