package net.sf.jbl.introspection.attributes;

import net.sf.jbl.introspection.ConstantPool;
import net.sf.jbl.introspection.Attribute;
import net.sf.jbl.util.ByteStream;

import java.util.*;

/**
 * Represents a line number metadata table, used for stacktraces and debugging.
 */
public class LineNumberTable extends Attribute implements Iterable<LineNumberTable.Entry> {
    private final List<Entry> lines;

    /**
     * Constructs a LineNumberTable attribute.
     *
     * @param stream stream containing encoded out.
     */
    public LineNumberTable(ByteStream stream) {
        super("LineNumberTable");
        int size = stream.readShort();
        lines = new ArrayList<Entry>(size);
        for (int i = 0; i != size; i++) {
            lines.add(new Entry(stream.readShort(), stream.readShort()));
        }
    }

    public LineNumberTable(List<Entry> entries) {
        super("LineNumberTable");
        lines = entries;
    }

    public LineNumberTable() {
        super("LineNumberTable");
        lines = new ArrayList<Entry>();
    }

    public void dump(ByteStream out, ConstantPool constants) {
        int size = lines.size();
        int len = (size << 2) + 2;
        out.enlarge(len);
        out.writeShort(constants.newUTF(name)).writeInt(len);
        out.writeShort(size);
        for (int i = 0; i != size; i++) {
            Entry e = lines.get(i);
            out.writeShort(e.startPC).writeShort(e.lineNumber);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entry> iterator() {
        return lines.iterator();
    }

    public List<Entry> getLines() {
        return lines;
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
