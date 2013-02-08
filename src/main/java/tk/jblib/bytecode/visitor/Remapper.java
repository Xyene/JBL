package tk.jblib.bytecode.visitor;

import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.metadata.SignatureReader;
import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.introspection.Opcode.*;

public abstract class Remapper implements ClassVisitor {

    @Override
    public final void visitConstant(Constant con) {
        Pool<Constant> cp = con.getOwner();
        switch (con.getType()) {
            case TAG_CLASS: {
                Constant pack = cp.get(Bytes.toShort(con.getRawValue(), 0));
                pack.setRawValue(remapReference(pack.stringValue()).getBytes());
                cp.set(pack.getIndex(), pack);
                break;
            }
            case TAG_FIELD: {
                Constant sig = cp.get(Bytes.toShort(cp.get(Bytes.toShort(con.getRawValue(), 2)).getRawValue(), 2));
                sig.setRawValue(remapSignature(sig.stringValue()).getBytes());
                cp.set(sig.getIndex(), sig);
                break;
            }
            case TAG_INTERFACE_METHOD:
            case TAG_METHOD: {
                Constant sig = cp.get(Bytes.toShort(cp.get(Bytes.toShort(con.getRawValue(), 2)).getRawValue(), 2));
                sig.setRawValue(remapSignature(sig.stringValue()).getBytes());
                cp.set(sig.getIndex(), sig);
                break;
            }
        }
    }

    @Override
    public final void visitMember(Member m) {
        m.setDescriptor(remapSignature(m.getDescriptor()));
    }

    protected String remapSignature(String sig) {
        SignatureReader reader = new SignatureReader(sig);
        String newSig = "(";
        for (String s : reader.getAugmentingTypes()) {
            if (s.startsWith("L"))
                s = remapReference(s);
            newSig += s;
        }
        newSig += ")" + remapReference(reader.getType());
        return newSig;
    }

    protected String remapReference(String type) {
        if (type.startsWith("L") && type.endsWith(";"))  //Its a reference
            return "L" + remap(type.substring(1, type.length() - 1)) + ";";  //Trim the reference (L & ;)and feed to remap, then readd
        return type;
    }

    protected String remap(String generic) {
        return generic;
    }
}
