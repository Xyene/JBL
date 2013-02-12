package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;

public interface ClassVisitor {

    void visitAttribute(Attribute a);

    void visitConstant(Constant c);

    void visitMethodPool(Pool<Member> methods);

    void visitFieldPool(Pool<Member> fields);

    void visitClass(ClassFile clazz);

    int visitAccessFlags(int mask);

    String visitName(String name);

    void visitConstantPool(Pool<Constant> constantPool);

    void visitMember(Member m);

    void visitMethod(Member method);

    void visitField(Member field);

    void visitAttributePool(Pool<Attribute> attributePool);

    void visitInterfacePool(Pool<Interface> interfacePool);

    int visitMinorVersion(int minorVersion);

    int visitMajorVersion(int majorVersion);

    String visitSuperClass(String superClass);

    void visitEnd();
}