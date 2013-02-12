package net.sf.jbl.visitor;

import net.sf.jbl.introspection.ClassFile;
import net.sf.jbl.introspection.Member;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.members.Interface;

import java.util.Collection;
import java.util.LinkedList;

public class ClassAdapter {
    protected LinkedList<ClassVisitor> visitors = new LinkedList<ClassVisitor>();

    public ClassAdapter(ClassVisitor... visit) {
        addVisitors(visit);
    }

    public ClassAdapter(Collection<ClassVisitor> visits) {
        addVisitors(visits);
    }

    public void addVisitors(Collection<ClassVisitor> visits) {
        visitors.addAll(visits);
    }

    public void addVisitors(ClassVisitor... visits) {
        for (ClassVisitor cv : visits)
            visitors.add(cv);
    }

    public LinkedList<ClassVisitor> visitors() {
        return visitors;
    }

    public void adapt(ClassFile clazz) {
        for (ClassVisitor cv : visitors) {
            cv.visitClass(clazz);

            clazz.setMinorVersion(cv.visitMinorVersion(clazz.getMinorVersion()));
            clazz.setMajorVersion(cv.visitMajorVersion(clazz.getMajorVersion()));

            clazz.setFlag(cv.visitAccessFlags(clazz.getFlag()));
            clazz.setName(cv.visitName(clazz.getName()));
            clazz.setSuperClass(cv.visitSuperClass(clazz.getSuperClass()));

            cv.visitInterfacePool(clazz.getInterfaces());

            Pool<Interface> interfaces = clazz.getInterfaces();
            cv.visitInterfacePool(interfaces);
            for (int i = 0; i != interfaces.size(); i++) {
                Interface in = interfaces.get(i);
                cv.visitInterface(in);
                interfaces.set(i, in);
            }

            Pool<Constant> constants = clazz.getConstants();
            cv.visitConstantPool(constants);
            for (int i = 1; i != constants.size(); i++) {
                Constant c = constants.get(i);
                cv.visitConstant(c);
                constants.set(i, c);
            }

            Pool<Member> fields = clazz.getFields();
            cv.visitFieldPool(fields);
            for (int i = 0; i != fields.size(); i++) {
                Member f = fields.get(i);
                cv.visitMember(f);
                cv.visitField(f);
                fields.set(i, f);
            }

            Pool<Member> methods = clazz.getMethods();
            cv.visitMethodPool(methods);
            for (int i = 0; i != methods.size(); i++) {
                Member m = methods.get(i);
                cv.visitMember(m);
                cv.visitMethod(m);
                methods.set(i, m);
            }

            Pool<Attribute> attrs = clazz.getAttributes();
            cv.visitAttributePool(attrs);
            for (int i = 0; i != attrs.size(); i++) {
                Attribute a = attrs.get(i);
                cv.visitAttribute(a);
                attrs.set(i, a);
            }

            cv.visitEnd();
        }
    }
}
