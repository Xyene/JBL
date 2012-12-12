package disassembler;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.members.Member;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Disassembler {

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/ReflectionHelper$MethodContainer.class");
        try {
            DisassembledClassFile cc = new DisassembledClassFile(clazz);
              System.out.println(cc.getSource());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMethod(Member m, LinkedList<String> imports, String className) {
        String out = "";
        return out;
    }

}
