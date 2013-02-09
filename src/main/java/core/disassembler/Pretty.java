package core.disassembler;

import tk.jblib.bytecode.introspection.AccessibleMember;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.attributes.LineNumberTable;
import tk.jblib.bytecode.introspection.members.attributes.LocalVariableTable;
import tk.jblib.bytecode.introspection.members.attributes.UnknownAttribute;
import tk.jblib.bytecode.introspection.metadata.SignatureReader;
import tk.jblib.bytecode.util.Bytes;

import java.util.LinkedList;

import static tk.jblib.bytecode.introspection.Opcode.*;

public class Pretty {
    public static String constantName(int id) {
        switch (id) {
            case TAG_UTF_STRING:
                return "UTF-8 STRING";
            case TAG_INTEGER:
                return "INTEGER";
            case TAG_FLOAT:
                return "FLOAT";
            case TAG_LONG:
                return "LONG";
            case TAG_DOUBLE:
                return "DOUBLE";
            case TAG_CLASS:
                return "CLASS REF";
            case TAG_STRING:
                return "STRING REF";
            case TAG_FIELD:
                return "FIELD REF";
            case TAG_METHOD:
                return "METHOD REF";
            case TAG_INTERFACE_METHOD:
                return "IMETHOD REF";
            case TAG_DESCRIPTOR:
                return "DESCRIPTOR";
            case TAG_PHANTOM:
                return "PHANTOM";
            default:
                return "UNKNOWN";
        }
    }

    public static String modifiers(int mod) {
        AccessibleMember am = new AccessibleMember(mod);
        String s = "";
        if (am.isPrivate())
            s += "private";
        if (am.isPublic())
            s += "public";
        if (am.isProtected())
            s += "protected";
        if (am.isStatic())
            s += " static";
        if (am.isFinal())
            s += " final";
        if (am.isAbstract())
            s += " abstract";
        if (am instanceof Member && ((Member) am).isSynchronized())
            s += " synchronized";
        if (am instanceof Member && ((Member) am).isNative())
            s += " native";
        if (am.isStrict())
            s += " strictfp";
        return s.trim(); //For cases where a space is added to, say, <clinit>, where there it is package-local
    }

    private static String descriptor(SignatureReader m) {
        String out = "";
        LinkedList<String> args = m.getAugmentingTypes();
        for (int i = 0; i != args.size(); i++)
            out += baseType(args.get(i)) + (i != args.size() - 1 ? ", " : "");
        return out;
    }

    public static String baseType(String s) {
        String out = "";
        int arrayDepth = Strings.occurrenceOf('[', s);
        s = s.replaceAll("\\[", "");

        if (s.startsWith("L"))
            out = s.substring(1, s.length());
        else if (s.equals("B"))
            out = "byte";
        else if (s.equals("C"))
            out = "char";
        else if (s.equals("D"))
            out = "double";
        else if (s.equals("F"))
            out = "float";
        else if (s.equals("I"))
            out = "int";
        else if (s.equals("J"))
            out = "long";
        else if (s.equals("S"))
            out = "short";
        else if (s.equals("Z"))
            out = "boolean";
        else if (s.equals("V"))
            out = "void";
        else out = "unknown";

        return out.concat(Strings.repeat("[]", arrayDepth));
    }

    public static String compilerVersion(int major) {
        switch (major) {
            case 46:
                return "JDK 1.2";
            case 47:
                return "JDK 1.3";
            case 48:
                return "JDK 1.4";
            case 49:
                return "JDK 5";
            case 50:
                return "JDK 6";
            case 51:
                return "JDK 7";
            case 52:
                return "JDK 8";
            default:
                return "unknown";
        }
    }

    public static String memberHeader(Member m) {
        return Pretty.modifiers(m.getFlag()) + memberHeader(m.getDescriptor(), m.getName());
    }

    private static String memberHeader(String descriptor, String name) {
        SignatureReader s = new SignatureReader(descriptor);
        return baseType(s.getType()) + " " + name + (s.isMethodSignature() ? "(" + descriptor(s) + ")" : descriptor(s));
    }

    public static String dumpAttributes(Attribute a, ScopedWriter pad) {
        String out = "";
        out += "\n" + pad + "   " + a.getName();
        if (a instanceof LineNumberTable) {
            out += "\n" + pad + "      {";
            for (LineNumberTable.Entry e : (LineNumberTable) a) {
                out += "\n" + pad + "      line " + e.getLineNumber() + ": " + e.getStartPC();
            }
            out += "\n" + pad + "      }";
        } else if (a instanceof LocalVariableTable) {
            out += "\n" + pad + "      {";
            for (LocalVariableTable.Entry e : (LocalVariableTable) a) {
                out += "\n" + pad + "      " + memberHeader(e.getDescriptor(), e.getName()) + ": " + e.getStartPC();
            }
            out += "\n" + pad + "      }";
        } else {
            out += a instanceof UnknownAttribute ? Bytes.bytesToString(((UnknownAttribute) a).getValue()) : "";
        }

        return out;
    }

}
