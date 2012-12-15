package disassembler;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.members.Member;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.CodeAttribute;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.ConstantValueAttribute;
import com.github.Icyene.bytecode.introspection.internal.metadata.AccessFlag;
import disassembler.instructions.Operator;
import disassembler.readers.SignatureReader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class DecompiledClassFile extends ClassFile {

    String source = "";

    public DecompiledClassFile(File file) throws IOException {
        super(file);

        ImportList importList = new ImportList();

        String className = thisClass.getStringValue().replace("/", ".");
        String superName = superClass.getStringValue().replace("/", ".");
        String header = "";

        int pin = className.lastIndexOf(".");
        if (pin != -1) {
            header = "package " + className.substring(0, pin) + ";\n\n";
            className = className.substring(pin + 1, className.length());
        }

        source += accessFlags.getStringValue(true) + " class " + className + (!superName.equals("java.lang.Object") ? " extends " + superName : "") + " {";

        for (int i = 0; i != methodPool.size(); i++) {
            Member m = methodPool.get(i);
            SignatureReader sign = new SignatureReader(m);

            String mName = m.getName().getStringValue();
            source += "\n\n   " + (m.isDeprecated() ? "@Deprecated\n   " : "") + m.getAccessFlags().getStringValue() + " " +
                    (!mName.equals("<init>") ? mName : importList.getWImport(sign.getType()) + " " + mName.replace("<init>", className))
                    + "(";

            LinkedList<String> params = sign.getAugmentingTypes();
            int size = params.size();
            for (int p = 0; p != size; p++) {
                source += importList.getWImport(params.get(p)) + " arg" + p + (p != size - 1 ? ", " : "");
            }

            source += ") {\n";
            for (Operator op : ((CodeAttribute) m.getAttributePool().getInstancesOf(CodeAttribute.class).get(0)).getCodePool()) {
                source += "      " + op.toString() + ";  " + "\n";
                //(op.getOperand() != null ? "//" + constantPool.get(Short.parseShort(op.getOperand().toString())) : "") +
            }
            source += "   }";

            /*if (attrs.hasAttribute(".*Annotation.*")) {
                System.out.println("Has annotation");
                for (Attribute attr : attrs.getInstancesOf(".*Annotation.*")) {
                    AnnotationReader reader = new AnnotationReader((UnknownAttribute) attr, constantPool);
                    System.out.println("@" + reader.getName());
                }
            }  else {
                System.out.println("HAS NO ANNOTATIONS!");
            }    */
        }

        for (Member f : fieldPool) {
            AccessFlag acc = f.getAccessFlags();
            source += "\n\n   " + (f.isDeprecated() ? "@Deprecated\n   " : "") + acc.getStringValue() + " " + f.getModifiers() + " " + f.getName().getStringValue() +
                    (acc.isStatic() ? " = " +
                            ((ConstantValueAttribute) f.getAttributePool().getInstancesOf(ConstantValueAttribute.class).get(0)).getConstantIndex().getStringValue() : "")
                    + ";";
        }

        source += "\n}";


        for (String i : importList) {
            header += "\nimport " + i + ";\n";
        }

        source = header + source;

    }

    public String getSource() {
        return source;
    }
}
