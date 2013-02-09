package core.disassembler;

import tk.jblib.bytecode.generation.Branch;
import tk.jblib.bytecode.generation.CodeGenerator;
import tk.jblib.bytecode.generation.Instruction;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;
import tk.jblib.bytecode.introspection.members.attributes.Code;
import tk.jblib.bytecode.introspection.members.attributes.ConstantValue;
import tk.jblib.bytecode.util.Bytes;
import tk.jblib.bytecode.visitor.ClassVisitor;

public class DisassemblerVisitor extends ClassVisitor {

    private final ScopedWriter scope;

    public DisassemblerVisitor() {
        scope = new ScopedWriter();
    }

    public String get() {
        return scope.getText();
    }

    protected void visitAttribute(Attribute a) {

    }

    protected void visitConstant(Constant c) {
        scope.write("#" + c.getIndex() + " (" + Pretty.constantName(c.getType()) + ") " + c.stringValue() + "\n");
    }

    protected void visitMethodPool(Pool<Member> methods) {
        scope.writeln("Method pool (" + methods.size() + " item(s)): \n");
    }

    protected int visitAccessFlags(int mask) {
        scope.writeln("\n" + Pretty.modifiers(mask));
        return mask;
    }

    protected String visitName(String name) {
        scope.write(" class " + name + " ");
        return name;
    }

    protected String visitSuperClass(String superClass) {
        scope.write(" extends " + superClass + " {");
        scope.increasePad(3);
        return superClass;
    }

    protected void visitConstantPool(Pool<Constant> constantPool) {
        scope.writeln("Constant pool (" + constantPool.size() + " item(s)): \n");
    }

    protected void visitMember(Member m) {
        scope.writeln("");
        scope.write(m.isDeprecated() ? "@Deprecated\n" : "");
        scope.write(Pretty.memberHeader(m));
    }

    protected void visitMethod(Member method) {
        scope.writeNoPad(" {\n");
        scope.increasePad(3);

        CodeGenerator cgi = new CodeGenerator((Code) method.getMetadata("Code"));
        for (Instruction i : cgi.instructions) {
            scope.write(i.getAddress() + ": " + NamedOpcode.getByValue((byte) i.getOpcode()));
            if (i instanceof Branch) {
                int jump = ((Branch) i).getTarget();
                scope.writeNoPad("[" + (jump > 0 ? "+" + jump : jump + "") + " -> " + (i.getAddress() + jump) + "]");
            } else {
                scope.writeNoPad(Bytes.bytesToString(i.getArguments())); //Its not an opcode we have more info on, so just dump the bytes
            }
            scope.writeNoPad("\n"); //End instruction line
        }

        scope.decreasePad(3);
        scope.write("}\n");
    }

    protected void visitField(Member field) {
        scope.write((field.isStatic() && field.hasMetadata("ConstantValue") ? " = " +
                ((ConstantValue) field.getMetadata("ConstantValue")).getConstantIndex() : "") + ";");
    }

    protected void visitAttributePool(Pool<Attribute> attributePool) {
        scope.writeln("Attribute pool (" + attributePool.size() + " item(s)): \n");
    }

    protected void visitInterfacePool(Pool<Interface> interfacePool) {
    }

    protected int visitMinorVersion(int minor) {
        scope.write("\nMajor version: " + minor);
        return minor;
    }

    protected int visitMajorVersion(int major) {
        scope.write("\nMajor version: " + major);
        scope.write("\nCompiler version: " + Pretty.compilerVersion(major));
        return major;
    }
}
