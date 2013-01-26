package tk.jblib.bytecode.introspection.pools;

import tk.jblib.bytecode.introspection.members.Interface;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.LinkedList;

/**
 * A pool to hold all Interface structures.
 */
public class InterfacePool extends LinkedList<Interface> {

    /**
     * Constructs an interface pool.
     *
     * @param stream The stream of bytes containing the pool data.
     * @param pool   An associated constant pool.
     */
    public InterfacePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new Interface(pool.get(Bytes.toShort(stream.read(2), 0))));
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public InterfacePool() {
    };

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (Interface cpi : this)
            raw = Bytes.concat(raw, cpi.getBytes());
        return raw;
    }

    /**
     * Checks if this pool contains an interface with the fully qualified path of s.
     *
     * @param s the fully qualified path.
     * @return True if it does, false otherwise.
     */
    public boolean contains(String s) {
        for (Interface c : this)
            if (c.getClassReference().equals(s))
                return true;
        return false;
    }
}
