package net.sf.jbl.introspection.attributes;

import net.sf.jbl.introspection.ConstantPool;
import net.sf.jbl.introspection.Attribute;
import net.sf.jbl.util.ByteStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalVariableTable extends Attribute implements Iterable<LocalVariableTable.Entry> {
    private final List<Entry> variables;

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public LocalVariableTable() {
        super("LocalVariableTable");
        variables = new ArrayList<Entry>();
    }

    public LocalVariableTable(List<Entry> locals) {
        super("LineNumberTable");
        variables = locals;
    }

    public LocalVariableTable(ByteStream in, ConstantPool constants) {
        super("LocalVariableTable");
        int size = in.readShort();
        variables = new ArrayList<Entry>(size);
        for (int i = 0; i != size; i++) {
            variables.add(new Entry(in.readShort(), in.readShort(), constants.getUTF(in.readShort()), constants.getUTF(in.readShort()), in.readShort()));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void dump(ByteStream out, ConstantPool constants) {
        int size = variables.size();
        int len = size * 10 + 2;
        out.enlarge(len + 4);
        out.writeShort(constants.newUTF(name)).writeInt(len);
        out.writeShort(size);
        for (int i = 0; i != size; i++) {
            Entry e = variables.get(i);
            out.writeShort(e.startPC);
            out.writeShort(e.length);
            out.writeShort(constants.newUTF(e.name));
            out.writeShort(constants.newUTF(e.descriptor));
            out.writeShort(e.index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entry> iterator() {
        return variables.iterator();
    }

    public List<Entry> getLocals() {
        return variables;
    }

    /**
     * An entry into a LocalVariableTable.
     */
    public class Entry {
        private int startPC;
        private int length;
        private String name;
        private String descriptor;
        private int index;

        public Entry(int startPC, int length, String name, String descriptor, int index) {
            this.startPC = startPC;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
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
            return name;
        }

        public void setName(String newName) {
            name = newName;
        }

        public String getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(String newDescriptor) {
            descriptor = newDescriptor;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
