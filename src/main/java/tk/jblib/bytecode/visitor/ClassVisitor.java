package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;

public abstract interface ClassVisitor {

    abstract void visitAttribute(Attribute a);

    abstract void visitConstant(Constant c);

    abstract void visitMethodPool(Pool<Member> methods);

    abstract void visitFieldPool(Pool<Member> fields);

    abstract void visitClass(ClassFile clazz);

    abstract int visitAccessFlags(int mask);

    abstract String visitName(String name);

    abstract void visitConstantPool(Pool<Constant> constantPool);

    abstract void visitMember(Member m);

    abstract void visitMethod(Member method);

    abstract void visitField(Member field);

    abstract void visitAttributePool(Pool<Attribute> attributePool);

    abstract void visitInterfacePool(Pool<Interface> interfacePool);

    abstract int visitMinorVersion(int minorVersion);

    abstract int visitMajorVersion(int majorVersion);

    abstract String visitSuperClass(String superClass);

    abstract void visitEnd();
}