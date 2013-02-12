package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;

public interface MemberVisitor {

    void visitMember(Member member);

    int visitAccessFlags(int mask);

    String visitName(String name);

    String visitDescriptor(String descriptor);

    void visitAttributePool(Pool<Attribute> attributePool);

    void visitAttribute(Attribute a);

    void visitEnd();
}
