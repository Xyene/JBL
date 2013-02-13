package core.verifier;

import net.sf.jbl.introspection.ClassFile;

public class Verifier {
    public static void main(String[] args) {
        ClassFile verf;
        try {
            verf = new ClassFile();
        } catch (Exception e) {
            System.err.println("Class failed pass one of verification: " + e.getMessage());
            return;
        }

    }
}
