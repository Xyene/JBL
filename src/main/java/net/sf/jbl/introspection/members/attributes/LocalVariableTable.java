package net.sf.jbl.introspection.members.attributes;

import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.util.ByteStream;
import net.sf.jbl.util.Bytes;

import java.util.Iterator;
import java.util.LinkedList;

import static net.sf.jbl.introspection.Opcode.TAG_UTF_STRING;

public class LocalVariableTable extends Attribute implements Iterable<LocalVariableTable.Entry> {

    private final LinkedList<Entry> variables = new LinkedList<Entry>();

    /**
     * Constructs a LocalVariableTable attribute.
     * @param stream stream containing encoded data.
     * @param name the name, "LocalVariableTable".
     * @param pool the associated constant pool.
     */
    public LocalVariableTable(ByteStream stream, Constant name, Pool<Constant> pool) {
        super(name, stream.readInt());
        short size = stream.readShort();
        for (int i = 0; i != size; i++) {
            variables.add(new Entry(stream, pool));
        }
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public LocalVariableTable() {}

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write((short) variables.size());
        for (Entry e : variables) {
            out.write((short) e.startPC);
            out.write((short) e.length);
            out.write((short) e.name.getIndex());
            out.write((short) e.descriptor.getIndex());
            out.write((short) e.index);
        }
        byte[] bytes = out.toByteArray();
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entry> iterator() {
        return variables.iterator();
    }

    /**
     * An entry into a LocalVariableTable.
     */
    public class Entry {
        private int startPC;
        private int length;
        private Constant name;
        private Constant descriptor;
        private int index;

        public Entry(ByteStream stream, Pool<Constant> owning) {
            startPC = stream.readShort();
            length = stream.readShort();
            name = owning.get(stream.readShort());
            descriptor = owning.get(stream.readShort());
            index = stream.readShort();
        }

        public int getStartPC() {
            return startPC;
        }

        public void setStartPC(int startPC) {
            this.startPC = startPC;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getName() {
            return name.stringValue();
        }

        public void setName(String newName) {
            name.getOwner().set(name.getIndex(), (name = new Constant(name.getIndex(), TAG_UTF_STRING, newName.getBytes())));
        }

        public String getDescriptor() {
            return descriptor.stringValue();
        }

        public void setDescriptor(String newDescriptor) {
            descriptor.getOwner().set(descriptor.getIndex(), (descriptor = new Constant(descriptor.getIndex(),TAG_UTF_STRING, newDescriptor.getBytes())));
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
