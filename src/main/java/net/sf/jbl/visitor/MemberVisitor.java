package net.sf.jbl.visitor;

import net.sf.jbl.introspection.Member;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.Attribute;

public interface MemberVisitor {

    void visitMember(Member member);

    int visitAccessFlags(int mask);

    String visitName(String name);

    String visitDescriptor(String descriptor);

    void visitAttributePool(Pool<Attribute> attributePool);

    void visitAttribute(Attribute a);

    void visitEnd();
}
