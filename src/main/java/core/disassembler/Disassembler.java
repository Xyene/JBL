package core.disassembler;

import net.sf.jbl.introspection.ClassFile;

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
           // DisassemblerVisitor dis = new DisassemblerVisitor();
            //dis.visit(cs);

            //System.out.println(dis.get());

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
