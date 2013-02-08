package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;

import java.util.Collection;
import java.util.LinkedList;

public class MemberAdapter {
    protected LinkedList<MemberVisitor> visitors = new LinkedList<MemberVisitor>();

    public MemberAdapter(MemberVisitor... visit) {
        addVisitors(visit);
    }

    public MemberAdapter(Collection<MemberVisitor> visits) {
        addVisitors(visits);
    }

    public void addVisitors(Collection<MemberVisitor> visits) {
        visitors.addAll(visits);
    }

    public void addVisitors(MemberVisitor... visits) {
        for (MemberVisitor mv : visits)
            visitors.add(mv);
    }

    public void adapt(Member member) {
        for (MemberVisitor mv : visitors) {
            mv.visitMember(member);

            member.setFlag(mv.visitAccessFlags(member.getFlag()));
            member.setName(mv.visitName(member.getName()));
            member.setDescriptor(mv.visitDescriptor(member.getDescriptor()));

            Pool<Attribute> attrs = member.getAttributePool();
            mv.visitAttributePool(attrs);
            for (int i = 0; i != attrs.size(); i++) {
                Attribute a = attrs.get(i);
                mv.visitAttribute(a);
                attrs.set(i, a);
            }
        }
    }

    public LinkedList<MemberVisitor> visitors() {
        return visitors;
    }
}
