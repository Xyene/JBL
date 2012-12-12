package disassembler;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.members.Member;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class DisassembledClassFile extends ClassFile {

    String source = "";

    public DisassembledClassFile(File file) throws IOException {
        super(file);

        LinkedList<String> imports = new LinkedList<String>();

        String className = thisClass.getStringValue().replace("/", ".");
        String superName = superClass.getStringValue().replace("/", ".");

        int pin = className.lastIndexOf(".");
        if (pin != -1) {
            source += "package " + className.substring(0, pin) + ";\n\n" + source;
            className = className.substring(pin + 1, className.length());
        }

        source += accessFlags.getStringValue(true) + " class " + className + " extends " + superName + " {\n";

        for(int i = 0; i != methodPool.size(); i++) {
            Member m = methodPool.get(i);
            SignatureReader sign = new SignatureReader(m.getDescriptor().getStringValue());
            source += "\n   " + m.getAccessFlags().getStringValue() + " " +
                    sign.getReturnType() + " " + m.getName().getStringValue() + "(" + sign.getParameterTypes().toString().substring(1, sign.getParameterTypes().toString().length() -1)
            + ") { /* Compiled code */ }";

        }

        for (Member f : fieldPool) {
            source += "\n   " + f.getAccessFlags().getStringValue() + " " + f.getModifiers() + " " + f.getName().getStringValue() + ";\n";
        }

        String imps = "";

        for (String i : imports) {
            imps += "import " + i + ";\n";
        }

        System.out.println(imps);

        source.replaceAll("aaa\n", imps);


        source += "}";

    }

    public String getSource() {
        return source;
    }
}
