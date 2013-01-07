package core.decompiler;

import core.decompiler.decompiling.DecompiledClassFile;
import core.obfuscator.Obfuscator;

import java.io.File;
import java.io.IOException;

public class Decompiler {

    public static void main(String[] args) {
        Obfuscator.main(args);
        /*File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        try {
            long start;
            start = System.currentTimeMillis();
            //new ClassFile(clazz);
            //DecompiledClassFile cc = new DecompiledClassFile(clazz);
            System.out.println(new DecompiledClassFile(clazz).getSource());
            // System.out.println("Constant pool: " + cc.getConstantPool());
            System.out.println(System.currentTimeMillis() - start + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }    */
    }
}
