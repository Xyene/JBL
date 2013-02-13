package core.disassembler;

import net.sf.jbl.generation.Branch;
import net.sf.jbl.generation.CodeGenerator;
import net.sf.jbl.generation.Instruction;
import net.sf.jbl.introspection.ClassFile;
import net.sf.jbl.introspection.Member;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.members.Interface;
import net.sf.jbl.introspection.members.attributes.Code;
import net.sf.jbl.visitor.ClassVisitor;

import java.util.Arrays;

public class DisassemblerVisitor implements ClassVisitor {

    private final ScopedWriter scope;

    public DisassemblerVisitor() {
        scope = new ScopedWriter();
    }

    public String get() {
        return scope.getText();
    }

    public void visitAttribute(Attribute a) {

    }

    public void visitConstant(Constant c) {
        scope.write("#" + c.getIndex() + " (" + Pretty.constantName(c.getType()) + ") " + c.stringValue() + "\n");
    }

    public void visitMethodPool(Pool<Member> methods) {
        scope.writeln("Method pool (" + methods.size() + " item(s)): \n");
    }

    @Override
    public void visitFieldPool(Pool<Member> fields) {
    }

    @Override
    public void visitClass(ClassFile clazz) {
    }

    public int visitAccessFlags(int mask) {
        scope.writeln("\n" + Pretty.modifiers(mask));
        return mask;
    }

    public String visitName(String name) {
        scope.write(" class " + name + " ");
        return name;
    }

    public String visitSuperClass(String superClass) {
        scope.write(" extends " + superClass + " {");
        scope.increasePad(3);
        return superClass;
    }


    public void visitEnd() {
        scope.write("\n}");
    }

    public void visitConstantPool(Pool<Constant> constantPool) {
        scope.writeln("Constant pool (" + constantPool.size() + " item(s)): \n");
    }

    public void visitMember(Member m) {
        scope.writeln("");
        scope.write(m.isDeprecated() ? "@Deprecated\n" : "");
        scope.write(Pretty.memberHeader(m));
    }

    public void visitMethod(Member method) {
        scope.writeNoPad(" {\n");
        scope.increasePad(3);

        CodeGenerator cgi = new CodeGenerator((Code) method.getMetadata("Code"));
        for (Instruction i : cgi.instructions) {
            scope.write(i.getAddress() + ": " + NamedOpcode.getByValue((byte) i.getOpcode()));
            if (i instanceof Branch) {
                int jump = ((Branch) i).getTarget();
                scope.writeNoPad("[" + (jump > 0 ? "+" + jump : jump + "") + " -> " + (i.getAddress() + jump) + "]");
            } else {

                scope.writeNoPad(Arrays.toString(i.getArguments())); //Its not an opcode we have more info on, so just dump the bytes
            }
            scope.writeNoPad("\n"); //End instruction line
        }

        scope.decreasePad(3);
        scope.write("}\n");
    }

    public void visitField(Member field) {
        scope.write((field.isStatic() && field.hasMetadata("ConstantValue") ? " = " + field.getMetadata("ConstantValue") : "") + ";");
    }

    public void visitAttributePool(Pool<Attribute> attributePool) {
        scope.writeln("Attribute pool (" + attributePool.size() + " item(s)): \n");
    }

    public void visitInterfacePool(Pool<Interface> interfacePool) {
    }

    public int visitMinorVersion(int minor) {
        scope.write("\nMajor version: " + minor);
        return minor;
    }

    public int visitMajorVersion(int major) {
        scope.write("\nMajor version: " + major);
        scope.write("\nCompiler version: " + Pretty.compilerVersion(major));
        return major;
    }
}
