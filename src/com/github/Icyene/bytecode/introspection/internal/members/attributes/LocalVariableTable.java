package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.Iterator;
import java.util.LinkedList;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

public class LocalVariableTable extends Attribute implements Iterable<LocalVariableTable.Entry> {

    private final LinkedList<Entry> variables = new LinkedList<Entry>();

    public LocalVariableTable(ByteStream stream, Constant name, ConstantPool pool) {
        super(name, stream.readInt());
        short size = stream.readShort();
        for (int i = 0; i != size; i++) {
            variables.add(new Entry(stream, pool));
        }
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream(super.getBytes());
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

    @Override
    public Iterator<Entry> iterator() {
        return variables.iterator();
    }

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
            name.getOwner().set(name.getIndex(), (name = new Constant(TAG_UTF_STRING, newName.getBytes())));
        }

        public Constant getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(Constant descriptor) {
            this.descriptor = descriptor;
        }

        public void setDescriptor(String newDescriptor) {
            descriptor.getOwner().set(descriptor.getIndex(), (name = new Constant(TAG_UTF_STRING, newDescriptor.getBytes())));
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
