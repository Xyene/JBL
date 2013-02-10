import benchmark.asm.ClassReader;
import benchmark.bcel.classfile.ClassParser;
import benchmark.bcel.classfile.JavaClass;
import benchmark.serp.bytecode.Project;
import org.junit.Test;
import tk.jblib.bytecode.introspection.ClassFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BenchmarkTest {

    @Test
    public void testSpeed() throws IOException {
        File clazz = new File(System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");

        RandomAccessFile f = new RandomAccessFile(clazz, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.read(bytes);

        //JBL benchmark
        long average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 500; i++) {
            long start = System.currentTimeMillis();
            ClassFile cf = new ClassFile(bytes);
            average += System.currentTimeMillis() - start;
        }
        average /= 500;
        System.out.println("JBL Time: " + average + "ms");

        //ASM benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 500; i++) {
            long start = System.currentTimeMillis();
            ClassReader cr = new ClassReader(bytes);
            average += System.currentTimeMillis() - start;
        }
        average /= 500;
        System.out.println("ASM Time: " + average + "ms");


        //BCEL benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 500; i++) {
            long start = System.currentTimeMillis();
            JavaClass jc = new ClassParser(new FileInputStream(clazz), "PhantomTest").parse();
            average += System.currentTimeMillis() - start;
        }
        average /= 500;
        System.out.println("BCEL Time: " + average + "ms");

        //SERP benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 500; i++) {
            long start = System.currentTimeMillis();
            new Project("Test").loadClass(clazz);
            average += System.currentTimeMillis() - start;
        }
        average /= 500;
        System.out.println("SERP Time: " + average + "ms");
    }
}
