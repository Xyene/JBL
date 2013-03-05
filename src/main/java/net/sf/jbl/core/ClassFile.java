/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.core;

import net.sf.jbl.core.attributes.Code;
import net.sf.jbl.core.metadata.Metadatable;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    protected AttributePool metadata;

    public ClassFile(ByteStream in) {
        if (in.readInt() != 0xCAFEBABE)
            throw new ClassFormatError("file does not start with magic number 0xCAFEBABE");

        minorVersion = in.readShort();
        majorVersion = in.readShort();
        if (majorVersion > Opcode.JDK_7)
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
            fields.add(new Field(in.readShort(), constants.getUTF(in.readShort()), constants.getUTF(in.readShort()), AttributePool.Handler.readPool(constants, in)));
        }

        int nm = in.readShort();
        methods = new ArrayList<Method>(nm);
        for (int i = 0; i != nm; i++)
            methods.add(new Method(in.readShort(), constants.getUTF(in.readShort()), constants.getUTF(in.readShort()), AttributePool.Handler.readPool(constants, in)));

         metadata = AttributePool.Handler.readPool(constants, in);
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

    public ClassFile(int access, String name, int major) {
        flag = access;
        majorVersion = major;
        minorVersion = 0;
        //Space enough for this class and super class
        constants = new ConstantPool(2);
        thisClass = name;
        superClass = "java/lang/Object";
        interfaces = new ArrayList<String>();
        fields = new ArrayList<Field>();
        methods = new ArrayList<Method>();
        metadata = new AttributePool();
    }

    protected <E extends Member> void dumpMembers(ByteStream out, List<E> members) {
        int ms = members.size();
        out.writeShort(ms);
        for (int i = 0; i != ms; i++) {
            Member m = members.get(i);
            out.writeShort(m.flag).writeShort(constants.newUTF(m.name)).writeShort(constants.newUTF(m.descriptor));

            if(m instanceof Method) {
                m.metadata.add(((Method)m).code);
            }
           AttributePool.Handler.writePool(m.metadata, constants, out);
        }
    }

    public byte[] toByteArray() {
        ByteStream header = ByteStream.writeStream();
        ByteStream body = ByteStream.writeStream();

        body.writeShort(constants.newClass(thisClass));
        body.writeShort(constants.newClass(superClass));

        int is = interfaces.size();
        body.writeShort(is);
        for (int i = 0; i != is; i++)
            body.writeShort(constants.newClass(interfaces.get(i)));

        dumpMembers(body, fields);
        dumpMembers(body, methods);
        AttributePool.Handler.writePool(metadata, constants, body);

        header.writeInt(0xCAFEBABE);
        header.writeShort(minorVersion);
        header.writeShort(majorVersion);
        header.writeShort(constants.size());
        header.writeBytes(constants.cache.getBuffer());
        header.writeShort(flag);

        return header.writeBytes(body.getBuffer()).getBuffer();
    }


    /**
     * Defines a Class from this class file.
     *
     * @return a defined class.
     */

    public Class define() {
        return new ClassLoader() {
            // Here we have to define our own class loader due to the System one
            // refusing to load classes from a byte[]. We also require the name of the class
            // since the one parsed may be invalid.
            public Class defineClass(byte[] bytes) {
                return super.defineClass(thisClass, bytes, 0, bytes.length);
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

    public void unimplement(CharSequence inter) {
        interfaces.remove(interfaces);
    }

    public void implement(String inter) {
        interfaces.add(inter);
    }

    public boolean implementationOf(CharSequence inter) {
        return interfaces.contains(inter);
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

    public Field getField(String name) {
        int size = fields.size();
        for (int i = 0; i != size; i++) {
            Field f = fields.get(i);
            if (f.name.equals(name))
                return f;
        }
        return null;
    }

    public void addField(int access, String name, String descriptor) {

    }

    public void addField(int access, String name, Object value) {

    }


    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public List<Method> getMethods() {
        return methods;
    }

    public Method getMethod(String name, String descriptor) {
        int size = methods.size();
        for (int i = 0; i != size; i++) {
            Method m = methods.get(i);
            if (m.name.equals(name) && m.descriptor.equals(descriptor))
                return m;
        }
        return null;
    }

    public Method getMethod(String name, Class... args) {
        throw new UnsupportedOperationException("not yet...");
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
    public AttributePool getAttributes() {
        return metadata;
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributes the pool to set it to.
     */
    public void setAttributes(AttributePool attributes) {
        metadata = attributes;
    }

    public String getSource() {
        return (String) getMetadata("SourceFile");
    }

    public void setSource(String name) {
        addMetadata("SourceFile", name);
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
        ClassFile clone = new ClassFile(flag, thisClass, majorVersion);
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
