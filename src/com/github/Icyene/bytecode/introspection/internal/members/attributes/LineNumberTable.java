package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.Iterator;
import java.util.LinkedList;

public class LineNumberTable extends Attribute implements Iterable<LineNumberTable.Entry> {

    private final LinkedList<Entry> lines = new LinkedList<Entry>();

    public LineNumberTable(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        short size = stream.readShort();
        for(int i = 0; i != size; i++) {
            lines.add(new Entry(stream.readShort(), stream.readShort()));
        }
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream(super.getBytes());
        out.write((short)lines.size());
        for(Entry e: lines) {
            out.write(Bytes.concat(Bytes.toByteArray((short)e.startPC), Bytes.toByteArray((short)e.lineNumber)));
        }
        return out.toByteArray();
    }

    @Override
    public Iterator<Entry> iterator() {
        return lines.iterator();
    }

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
