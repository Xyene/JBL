package core.verifier;

import net.sf.jbl.introspection.ClassFile;
import net.sf.jbl.introspection.Member;
import net.sf.jbl.introspection.Opcode;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.members.Interface;
import net.sf.jbl.introspection.metadata.SignatureReader;
import net.sf.jbl.visitor.ClassVisitor;

import static net.sf.jbl.introspection.Opcode.*;

public class VerificationVisitor implements ClassVisitor {

    @Override
    public void visitClass(ClassFile clazz) {
    }

    @Override
    public int visitMinorVersion(int minorVersion) {
        return 0;
    }

    @Override
    public int visitMajorVersion(int majorVersion) {
        return 0;
    }

    @Override
    public void visitConstantPool(Pool<Constant> constantPool) {
    }

    @Override
    public void visitConstant(Constant c) {
    }

    @Override
    public int visitAccessFlags(int mask) {
        return 0;
    }

    @Override
    public String visitName(String name) {
        return null;
    }

    @Override
    public String visitSuperClass(String superClass) {
        return null;
    }

    @Override
    public void visitInterfacePool(Pool<Interface> interfacePool) {
    }

    @Override
    public void visitInterface(Interface iface) {
    }

    @Override
    public void visitFieldPool(Pool<Member> fields) {
    }

    @Override
    public void visitField(Member field) {
       /* String name = field.getName();
        if (jc.isClass()) {
            //TODO: neater?
            int maxone = 0;
            if (field.isPrivate()) {
                maxone++;
            }
            if (field.isProtected()) {
                maxone++;
            }
            if (field.isPublic()) {
                maxone++;
            }
            if (maxone > 1) {
                System.err.println("Field '" + name + "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
            }

            if (field.isFinal() && field.isVolatile()) {
                System.err.println("Field '" + name + "' must only have at most one of its ACC_FINAL, ACC_VOLATILE modifiers set.");
            }
        } else { // isInterface!
            if (!field.isPublic()) {
                System.err.println("Interface field '" + name + "' must have the ACC_PUBLIC modifier set but doesn't");
            }
            if (!field.isStatic()) {
                System.err.println("Interface field '" + name + "' must have the ACC_STATIC modifier set but doesn't");
            }
            if (!field.isFinal()) {
                System.err.println("Interface field '" + name + "' must have the ACC_FINAL modifier set but doesn't");
            }
        }

        if ((field.getFlag() & ~(ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_VOLATILE | ACC_TRANSIENT)) > 0) {
            System.err.println("Field '" + name + "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_VOLATILE, ACC_TRANSIENT set (ignored).");
        }

        if (!validFieldName(name)) {
            System.err.println("Field '" + name + "' has illegal name '" + name + "'.");
        }

        new SignatureReader(field); //Checks for malformed descriptor      */

        //TODO: Same sig check
        //TODO: Same name check
        //TODO: Attribute check
    }

    @Override
    public void visitMethodPool(Pool<Member> methods) {
    }

    @Override
    public void visitMethod(Member method) {
    }

    @Override
    public void visitMember(Member m) {
    }

    @Override
    public void visitAttributePool(Pool<Attribute> attributePool) {
    }

    @Override
    public void visitAttribute(Attribute a) {
    }

    @Override
    public void visitEnd() {
    }
}
