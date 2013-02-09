package tk.jblib.bytecode.introspection.pools;

import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.LinkedList;

/**
 * A pool to hold all Member structures.
 */
public class MemberPool extends LinkedList<Member> {

    /**
     * Constructs a member pool.
     *
     * @param stream The stream of bytes containing the pool data.
     * @param pool   An associated constant pool.
     */
    public MemberPool(ByteStream stream, ConstantPool pool, ClassFile owner) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new Member(stream, pool));
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public MemberPool() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) size()));
        for (Member p : this)
            out.write(p.getBytes());
        return out.toByteArray();
    }
}
