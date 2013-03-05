import net.sf.jbl.core.ClassFile;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * To be used with the VisualVM profiler for performance tracking. Loops ad infinatum to provide large windows where VisualVM can profile.
 */
public class VisualVMProfilerTest {
    @Test
    public void profile() {
        File clazz = new File(System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");

        byte[] bytes;
        try {
            RandomAccessFile f = new RandomAccessFile(clazz, "r");
            bytes = new byte[(int) f.length()];
            f.read(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().gc();
        for (int i = 1; i != Integer.MAX_VALUE; i++) {
            new ClassFile(bytes).toByteArray();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("I'm sorry Dave, but I can't let you do that", e);
            }
        }
    }
}
