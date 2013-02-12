package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;

public interface ClassVisitor {

    void visitClass(ClassFile clazz);

    int visitMinorVersion(int minorVersion);

    int visitMajorVersion(int majorVersion);

    void visitConstantPool(Pool<Constant> constantPool);

    void visitConstant(Constant c);

    int visitAccessFlags(int mask);

    String visitName(String name);

    String visitSuperClass(String superClass);

    void visitInterfacePool(Pool<Interface> interfacePool);

    void visitInterface(Interface iface);

    void visitFieldPool(Pool<Member> fields);

    void visitField(Member field);

    void visitMethodPool(Pool<Member> methods);

    void visitMethod(Member method);

    void visitMember(Member m);

    void visitAttributePool(Pool<Attribute> attributePool);

    void visitAttribute(Attribute a);

    void visitEnd();
}