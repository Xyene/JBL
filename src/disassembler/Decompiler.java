package disassembler;

import com.github.Icyene.bytecode.introspection.internal.members.Member;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Decompiler {

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        try {
            DecompiledClassFile cc = new DecompiledClassFile(clazz);
            System.out.println("Constant pool: " + cc.getConstantPool());
            System.out.println();
            System.out.println();
            System.out.println(cc.getSource());
            System.out.println();
            System.out.println();
            System.out.println("Constant pool: " + cc.getConstantPool());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMethod(Member m, LinkedList<String> imports, String className) {
        String out = "";
        return out;
    }
}
