package core.decompiler.decompiling;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import core.decompiler.ImportList;
import core.decompiler.Scope;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DecompiledClassFile extends ClassFile {

    String source = "";

    public DecompiledClassFile(File file) throws IOException {
        super(file);

        ImportList importList = new ImportList();
        Scope scope = new Scope();

        String className = thisClass.getStringValue().replace("/", ".");
        String superName = superClass.getStringValue().replace("/", ".");
        String header = "";

        int pin = className.lastIndexOf(".");
        if (pin != -1) {
            header = "package " + className.substring(0, pin) + ";\n\n";
            className = className.substring(pin + 1, className.length());
        }

        HashMap<String, Object> aa = new HashMap<String, Object>();
                aa.put("shit", "bob");
        System.out.println(aa.get("shit"));

        meta.put("scope", scope);
        meta.put("name", className);
        meta.put("imports", importList);

        source += /*TODO: Redo flags*/ "public" + " class " + className + (!superName.equals("java.lang.Object") ? " extends " + superName : "") + " {";
        scope.indent();

        for (Member m : methodPool) {
            DecompiledMethodPool.DecompiledMethod dm = (DecompiledMethodPool.DecompiledMethod) m;
            source += dm.getSource();
        }
        /*TODO: Redo flags
        for (Member f : fieldPool) {
            AccessFlag acc = f.getAccessFlags();
            source += "\n" + scope.indent + (f.isDeprecated() ? "@Deprecated\n   " : "") + acc.getStringValue() + " " + f.getModifiers() + " " + f.getName().getStringValue();
            if (acc.isFinal()) {
                source += " = " + ((ConstantValue) f.getAttributePool().getInstancesOf(ConstantValue.class).get(0)).getConstantIndex().getStringValue();
            }
            source += ";";
        }   */

        source += "\n}";


        for (String i : importList) {
            header += "\nimport " + i + ";\n";
        }

        source = header + source;

    }

    @Override
    public void handleMethodPool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        methodPool = new DecompiledMethodPool(stream, constantPool, this);
        System.out.println("Method pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    public String getSource() {
        return source;
    }
}
