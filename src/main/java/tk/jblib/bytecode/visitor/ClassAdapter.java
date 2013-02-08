package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;

import java.util.Collection;
import java.util.LinkedList;

public class ClassAdapter {
    protected LinkedList<ClassVisitor> visitors = new LinkedList<ClassVisitor>();

    public ClassAdapter(ClassVisitor... visit) {
        for (ClassVisitor cv : visit)
            visitors.add(cv);
    }

    public ClassAdapter(Collection<ClassVisitor> visits) {
        addVisitors(visits);
    }

    public void addVisitors(Collection<ClassVisitor> visits) {
        visitors.addAll(visits);
    }

    public void addVisitors(ClassVisitor... visits) {

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

            cv.visitInterfacePool(clazz.getInterfacePool());

            Pool<Constant> constants = clazz.getConstantPool();
            cv.visitConstantPool(constants);
            for (int i = 1; i != constants.size(); i++) {
                Constant c = constants.get(i);
                cv.visitConstant(c);
                constants.set(i, c);
            }

            Pool<Member> fields = clazz.getFieldPool();
            cv.visitFieldPool(fields);
            for (int i = 0; i != fields.size(); i++) {
                Member f = fields.get(i);
                cv.visitMember(f);
                cv.visitField(f);
                fields.set(i, f);
            }

            Pool<Member> methods = clazz.getMethodPool();
            cv.visitMethodPool(methods);
            for (int i = 0; i != methods.size(); i++) {
                Member m = methods.get(i);
                cv.visitMember(m);
                cv.visitMethod(m);
                methods.set(i, m);
            }

            Pool<Attribute> attrs = clazz.getAttributePool();
            cv.visitAttributePool(attrs);
            for (int i = 0; i != attrs.size(); i++) {
                Attribute a = attrs.get(i);
                cv.visitAttribute(a);
                attrs.set(i, a);
            }
        }
    }
}
