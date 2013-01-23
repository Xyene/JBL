package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Line number metadata table, used for stacktraces and debugging.
 */
public class LineNumberTable extends Attribute implements Iterable<LineNumberTable.Entry> {

    private final LinkedList<Entry> lines = new LinkedList<Entry>();

    /**
     * Constructs a LineNumberTable attribute.
     * @param stream stream containing encoded data.
     * @param name the name, "LineNumberTable"
     */
    public LineNumberTable(ByteStream stream, Constant name) {
        super(name, stream.readInt());
        short size = stream.readShort();
        for (int i = 0; i != size; i++) {
            lines.add(new Entry(stream.readShort(), stream.readShort()));
        }
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public LineNumberTable() {

    }

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write((short) lines.size());
        for (Entry e : lines) {
            out.write(Bytes.toByteArray((short) e.startPC)).write(Bytes.toByteArray((short) e.lineNumber));
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
        return lines.iterator();
    }

    /**
     * An entry into a LineNumberTable.
     */
    public class Entry {
        private int startPC;
        private int lineNumber;

        public Entry(int start, int line) {
            startPC = start;
            lineNumber = line;
        }

        public int getStartPC() {
            return startPC;
        }

        public void setStartPC(int startPC) {
            this.startPC = startPC;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
