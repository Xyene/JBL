package tk.jblib.bytecode.introspection.members.attributes;

import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.introspection.Opcode.TAG_UTF_STRING;

public class Signature extends Attribute {
    private Constant signature;

    public Signature(ByteStream stream, Constant name, Pool<Constant> pool) {
        super(name, stream.readInt());
        signature = pool.get(stream.readShort());
    }

    public Signature(){}

    public byte[] getBytes() {
        byte[] bytes = Bytes.toByteArray((short) signature.getIndex());
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    public String getSignature() {
        return signature.stringValue();
    }

    public void setSignature(String newSignature) {
        signature.getOwner().set(signature.getIndex(), (signature = new Constant(signature.getIndex(), TAG_UTF_STRING, newSignature.getBytes())));
    }
}
