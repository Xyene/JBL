import benchmark.asm.ClassReader;
import benchmark.bcel.classfile.ClassParser;
import benchmark.bcel.classfile.JavaClass;
import benchmark.serp.bytecode.Project;
import org.junit.Test;
import net.sf.jbl.introspection.ClassFile;

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
        double average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 512; i++) {
            double start = System.nanoTime();
            ClassFile cf = new ClassFile(bytes);
            average += System.nanoTime() - start;
        }
        average /= 512;
        System.out.println("JBL Time: " + average/1000000 + "ms");

        //ASM benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 512; i++) {
            double start = System.nanoTime();
            ClassReader cr = new ClassReader(bytes);
            average += System.nanoTime() - start;
        }
        average /= 512;
        System.out.println("ASM Time: " + average/1000000 + "ms");

        //BCEL benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 512; i++) {
            double start = System.nanoTime();
            JavaClass jc = new ClassParser(new FileInputStream(clazz), "PhantomTest").parse();
            average += System.nanoTime() - start;
        }
        average /= 512;
        System.out.println("BCEL Time: " + average/1000000 + "ms");

        //SERP benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 512; i++) {
            double start = System.nanoTime();
            new Project("Test").loadClass(clazz);
            average += System.nanoTime() - start;
        }
        average /= 512;
        System.out.println("SERP Time: " + average/1000000 + "ms");
    }
}
