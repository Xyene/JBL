package tk.jblib.bytecode.introspection.members.attributes;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.pools.ConstantPool;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.Iterator;
import java.util.LinkedList;

import static tk.jblib.bytecode.introspection.metadata.Opcode.TAG_UTF_STRING;

public class LocalVariableTable extends Attribute implements Iterable<LocalVariableTable.Entry> {

    private final LinkedList<Entry> variables = new LinkedList<Entry>();

    /**
     * Constructs a LocalVariableTable attribute.
     * @param stream stream containing encoded data.
     * @param name the name, "LocalVariableTable".
     * @param pool the associated constant pool.
     */
    public LocalVariableTable(ByteStream stream, Constant name, ConstantPool pool) {
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

        public Entry(ByteStream stream, ConstantPool owning) {
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
            return name.getStringValue();
        }

        public void setName(String newName) {
            name.getOwner().set(name.getIndex(), (name = new Constant(name.getIndex(), TAG_UTF_STRING, newName.getBytes())));
        }

        public Constant getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(Constant descriptor) {
            this.descriptor = descriptor;
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
