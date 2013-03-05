/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.core.attributes;

import net.sf.jbl.core.ConstantPool;
import net.sf.jbl.core.Attribute;
import net.sf.jbl.core.ByteStream;

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
