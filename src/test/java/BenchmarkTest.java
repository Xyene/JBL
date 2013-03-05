import benchmark.asm.ClassReader;
import benchmark.asm.ClassWriter;
import benchmark.bcel.classfile.ClassParser;
import benchmark.serp.bytecode.Project;
import org.junit.Test;
import net.sf.jbl.core.ClassFile;

import java.io.*;

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
        for (int i = 0; i != 1024; i++) {
            double start = System.nanoTime();
            new ClassFile(bytes).toByteArray();
            //    ClassFile cf = new ClassFile();
            //  cf.implement("java/lang/Object");
            //   cf.implementationOf("java/lang/Serializable");
            average += System.nanoTime() - start;
        }
        average /= 1024;
        System.out.println("JBL Time: " + average / 1000000 + "ms");

        //ASM benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 1024; i++) {
            double start = System.nanoTime();
            new ClassWriter(new ClassReader(bytes), 0).toByteArray();
            average += System.nanoTime() - start;
        }
        average /= 1024;
        System.out.println("ASM Time: " + average / 1000000 + "ms");

        //BCEL benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 1024; i++) {
            double start = System.nanoTime();
            new ClassParser(new FileInputStream(clazz), "PhantomTest").parse().getBytes();
            // JavaClass jc = new ClassParser(new FileInputStream(clazz), "PhantomTest").parse();
            // jc.isSuper()
            average += System.nanoTime() - start;
        }
        average /= 1024;
        System.out.println("BCEL Time: " + average / 1000000 + "ms");

        //SERP benchmark
        average = 0;
        Runtime.getRuntime().gc();
        for (int i = 0; i != 1024; i++) {
            double start = System.nanoTime();
            new Project("Test").loadClass(clazz).toByteArray();
            average += System.nanoTime() - start;
        }
        average /= 1024;
        System.out.println("SERP Time: " + average / 1000000 + "ms");
    }
}
