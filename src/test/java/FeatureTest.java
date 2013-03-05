import benchmark.asm.ClassReader;
import benchmark.asm.util.CheckClassAdapter;
import benchmark.asm.util.TraceClassVisitor;
import net.sf.jbl.commons.generation.CodeBuilder;
import net.sf.jbl.core.ClassFile;
import net.sf.jbl.core.Method;

import static net.sf.jbl.core.Opcode.*;

import net.sf.jbl.commons.generation.CodeParser;
import org.junit.Test;

import java.io.*;


/**
 * Performs various feature tests.
 */
public class FeatureTest {

    /**
     * Generation test - attempts to generate the following basic class:
     * public final class JBLGen implements Cloneable {
     * public final JBLGen() {
     * super();
     * Object object = new Object();
     * Object dup = object;
     * System.out.println(object);
     * do
     * System.out.println((int)5.0D * 5.0D);
     * while (null != null);
     * throw new RuntimeException("       This is an exception!         ".trim());
     * }
     * }
     * <p/>
     * This test tests the CodeBuilder and related classes. It
     * contains invocations, local variable storing, branches,
     * throwing, and math operations.
     * <p/>
     * Interesting note: the logic expressed in this generator
     * confuses all decompilers this has been tested against.
     * Not one generated valid source code.
     */
    @Test
    public void generationTest() {
        ClassFile test = new ClassFile(ACC_PUBLIC | ACC_FINAL, "JBLGen", JDK_6);
        //test.setSource("JBL.mem");


        CodeBuilder cb = new CodeBuilder(test.getConstants());

        //Generate constructor
        Method ctor = new Method(ACC_PUBLIC, "<init>", "()V");
        cb.op(ALOAD_0);
        cb.invokeSpecial("java/lang/Object", "<init>", "()V");
        cb.invokeStatic("JBLGen", "generated", "()V");

        cb.getStatic("java/lang/System", "out", "Ljava/io/PrintStream;");
        cb.push("Class generation complete!");
        cb.invokeVirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V");

        cb.exit();

        ctor.setCode(cb.generateCode());

        CodeParser ctorp = new CodeParser(ctor, test.getConstants());
        ctor.setCode(ctorp.generate());
        cb.reset();
        test.getMethods().add(ctor);


        Method mem = new Method(ACC_PUBLIC, "generated", "()V");
        cb.getStatic("java/lang/System", "out", "Ljava/io/PrintStream;");
        cb.push(5D);
        cb.op(D2I);
        cb.push(5L);
        cb.op(L2I);

        cb.op(ISHR);
        cb.invokeVirtual("java/io/PrintStream", "println", "(I)V");
        cb.exit();

        mem.setCode(cb.generateCode());

        CodeParser parser = new CodeParser(mem, test.getConstants());
        mem.setCode(parser.generate());

        cb.reset();


        test.getMethods().add(mem);

        test.implement("java/lang/Cloneable");

        try {
            byte[] raw = test.toByteArray();
            new FileOutputStream(new File(System.getenv("USERPROFILE") + "/Desktop/JBLGen.class")).write(raw);
            System.out.println(test.getConstants());

            System.out.println("\n\nVerifying...\n");
            new ClassReader(raw).accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);
        } catch (Exception e) {
            System.err.println("Ehm.");
            e.printStackTrace();
        }
    }
}
