package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;

public abstract interface MemberVisitor {

    public abstract void visitAttribute(Attribute a);

    public abstract void visitMember(Member member);

    public abstract int visitAccessFlags(int mask);

    public abstract String visitName(String name);

    public abstract String visitDescriptor(String descriptor);

    public abstract void visitAttributePool(Pool<Attribute> attributePool);
}
