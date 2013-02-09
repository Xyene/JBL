package core.disassembler;

import tk.jblib.bytecode.generation.Branch;
import tk.jblib.bytecode.generation.CodeGenerator;
import tk.jblib.bytecode.generation.Instruction;
import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.attributes.*;
import tk.jblib.bytecode.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Disassembler {
    public static void main(String[] arg) {
        File clazz = new File(arg.length > 1 ? arg[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        List<String> args = new ArrayList<String>(Arrays.asList(arg));
        try {
            ClassFile cs = new ClassFile(clazz);
            DisassemblerVisitor dis = new DisassemblerVisitor();
            dis.visit(cs);
            System.out.println(dis.get());
         //   System.out.println(dis.get());
         /*   println("Compiler info: ");
            println("  Major version:    " + cs.getMajorVersion());
            println("  Minor version:    " + cs.getMinorVersion());
            println("  Compiler version: " + Pretty.compilerVersion(cs.getMajorVersion()));

            if (args.hasMetadata("-c")) {
                ConstantPool cp = cs.getConstantPool();
                print("\nConstant pool (" + cp.size() + " item(s)): ");

                for (Constant c : cp) {
                    print("\n   #" + c.getIndex() +
                            " (" + Pretty.constantName(c.getType()) + ") " +
                            c.stringValue());
                }
            }

            println("\n");


            if (cs.getAttributePool().hasMetadata("SourceFile"))
                println("Compiled from " + ((SourceFile) cs.getAttributePool().getInstancesOf("SourceFile").getMetadata(0)).getSourceFile());

            println(Pretty.modifiers(cs.getFlag()) + " class " + cs.getName() + " extends " + cs.getSuperClass() + " {");

            for (Member f : cs.getFieldPool()) {
                println("   " + (f.isDeprecated() ? "@Deprecated\n " : "") + Pretty.memberHeader(f) +
                        ((f.isStatic() && f.getAttributePool().hasMetadata("ConstantValue")) ? " = " +
                                ((ConstantValue) f.getAttributePool().getInstancesOf("ConstantValue").getMetadata(0)).getConstantIndex() : "") + ";");
            }

            for (Member m : cs.getMethodPool()) {
                print("\n" + (m.isDeprecated() ? "   @Deprecated\n " : "") +
                        "   " + Pretty.memberHeader(m) + " {");

                CodeGenerator cgi = new CodeGenerator((Code) m.getAttributePool().getInstancesOf("Code").getMetadata(0));

                for (Instruction i : cgi.instructions) {
                    print("\n      " + i.getAddress() + ": " + NamedOpcode.getByValue((byte) i.getOpcode()));
                    if (i instanceof Branch) {
                        int jump = ((Branch) i).getTarget();
                        print("[" + (jump > 0 ? "+" + jump : jump + "") + " -> " + (i.getAddress() + jump) + "]");
                    } else {
                        print(Bytes.bytesToString(i.getArguments()));
                    }
                }
           //     print(Pretty.dumpAttributes(m.getAttributePool(), "      "));
                print("\n   }\n");
            }

            //print(Pretty.dumpAttributes(cs.getAttributePool(), "   "));
            println("\n}");

*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(String text) {
        System.out.print(text);
    }

    public static void println(String text) {
        print(text + "\n");
    }
}
