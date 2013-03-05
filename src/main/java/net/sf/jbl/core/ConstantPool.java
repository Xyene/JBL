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

package net.sf.jbl.core;

import java.util.Arrays;

// Based off of ASM's ClassReader/Writer constant pool modification,
// but with most parsing changed (i.e. introduction of lazy-load constants).
public class ConstantPool implements Opcode {
    // In this implementation of a constant pool, we assume that the latter is can be read, but only appended
    // to. This allows us to optimize it to an array rather than its slower ArrayList counterpart. Constants
    // are loaded lazily from this array.
    Entry[] items;
    private short index = 0;
    final ByteStream cache;
    Entry last;

    public ConstantPool() {
        this(0);
    }

    public ConstantPool(int size) {
        items = new Entry[size];
        cache = ByteStream.writeStream();
    }

    ConstantPool(ByteStream in) {
        short len = in.readShort();
        items = new Entry[len + 1];
        // Store the current index of the positioned stream to allow forking of the stream after all constants
        // Are parsed and loaded into the constant array.
        int start = in.position();
        for (short i = 1; i != len; ++i) {
            byte tag = in.readByte();
            Entry item = new Entry(i, this);
            item.type = tag;
            switch (tag) {
                case TAG_FIELD:
                case TAG_METHOD:
                case TAG_INTERFACE_METHOD:
                    item.set(tag, new Short[]{in.readShort(), in.readShort()});
                    item.resolved = false;
                    break;
                case TAG_INTEGER:
                    item.set(tag, in.readInt());
                    break;
                case TAG_FLOAT:
                    item.set(tag, in.readFloat());
                    break;
                case TAG_DESCRIPTOR:
                    item.set(tag, new Short[]{in.readShort(), in.readShort()});
                    item.resolved = false;
                    break;
                // Long and double values are considered wide by the JVM, meaning that they take up two slots in
                // the constant pool. We simply increment the index. Any program accessing the phantom index in
                // such a constant will be met with an appropriate null pointer.
                case TAG_LONG:
                    item.set(tag, in.readLong());
                    i++;
                    break;
                case TAG_DOUBLE:
                    item.set(tag, in.readDouble());
                    i++;
                    break;
                case TAG_UTF_STRING:
                    // Load this constant lazily: UTF-8 decoding is a very expensive operation. This optimization
                    // works on the assumption that most strings will never be accessed during execution.
                    // Most of these constants are wrapped by a TAG_STRING constant. The latter correspond to
                    // all strings in source. It is safe to assume that there will be few cases where
                    // absolutely all strings have to be loaded. This saves lots of time. We also set the constant's
                    // resolved flag to false, so that it will be loaded when it is accessed externally. Until then,
                    // we just set its complex object to a byte[] containing its undecoded form.
                    item.set(tag, in.read(in.readShort()));
                    item.resolved = false;
                    break;
                case TAG_STRING:
                case TAG_CLASS:
                    item.set(tag, in.readShort());
                    item.resolved = false;
                    break;
                case 2:
                default:
                    // Adding this here allows the JVM to make this a tableswitch, since the values now range from
                    // 1-13. Otherwise, 2 would not be included and this optimization could not be possible.
                    // Since this is part of the default, invalid case,
                    throw new ClassFormatError("unknown constant @ " + i + " with tag " + tag);
            }

            items[item.index] = item;
            if(last != null)items[last.index].next = item;
            last = item;
        }
        index = len;
        // By forking the stream to a writable form, we save time on dumping, because the contents of pre-existing
        // constants are already stored in it. This means we no longer have to start from scratch every time the
        // pool is dumped.
        // All internal constant generators write to this stream: it is always up to date.
        int size = in.position() - start;
        cache = ByteStream.writeStream(size);
        System.arraycopy(in.buffer, start, cache.buffer, 0, size);
    }

    public int size() {
        return index + 1;
    }

    public int newConst(Object cst) {
        //Do not use Java 6's auto-boxing, rather, allow for compilation on JDK 1.5
        if (cst instanceof Integer) {
            return newInt(((Integer) cst).intValue());
        } else if (cst instanceof Byte) {
            return newInt(((Byte) cst).intValue());
        } else if (cst instanceof Character) {
            return newInt(((Character) cst).charValue());
        } else if (cst instanceof Short) {
            return newInt(((Short) cst).intValue());
        } else if (cst instanceof Boolean) {
            return newInt(((Boolean) cst).booleanValue() ? 1 : 0);
        } else if (cst instanceof Float) {
            return newFloat(((Float) cst).floatValue());
        } else if (cst instanceof Long) {
            return newLong(((Long) cst).longValue());
        } else if (cst instanceof Double) {
            return newDouble(((Double) cst).doubleValue());
        } else if (cst instanceof String) {
            return newString((String) cst);
        }
        throw new UnsupportedOperationException("could not dispatch constant creation");
    }

    public int newUTF(String value) {
        Entry result = get(TAG_UTF_STRING, value);
        if (result == null) {
            cache.writeByte(TAG_UTF_STRING).writeUTF(value);
            (result = new Entry(index++, this)).set(TAG_UTF_STRING, value);
            put(result);
        }
        return result.index;
    }

    public int newString(String value) {
        Entry result = get(TAG_STRING, value);
        if (result == null) {
            int utf = newUTF(value);
            cache.writeByte(TAG_STRING).writeShort(utf);
            (result = new Entry(index++, this)).set(TAG_STRING, value);
            put(result);
        }
        return result.index;
    }

    public int newClass(String value) {
        Entry result = get(TAG_CLASS, value);
        if (result == null) {
            int utf = newUTF(value);
            cache.writeByte(TAG_CLASS).writeShort(utf);
            (result = new Entry(index++, this)).set(TAG_CLASS, value);
            put(result);
        }
        return result.index;
    }

    public int newField(String owner, String name, String desc) {
        String[] proc = new String[]{owner, name, desc};
        Entry result = get(TAG_FIELD, proc);
        if (result == null) {
            int cp = newClass(owner);
            int ntp = newNameType(name, desc);
            cache.writeByte(TAG_FIELD).writeShort(cp).writeShort(ntp);
            (result = new Entry(index++, this)).set(TAG_FIELD, proc);
            put(result);
        }
        return result.index;
    }

    public int newMethod(String owner, String name, String desc) {
        String[] proc = new String[]{owner, name, desc};
        Entry result = get(TAG_METHOD, proc);
        if (result == null) {
            int cp = newClass(owner);
            int ntp = newNameType(name, desc);
            cache.writeByte(TAG_METHOD).writeShort(cp).writeShort(ntp);
            (result = new Entry(index++, this)).set(TAG_METHOD, proc);
            put(result);
        }
        return result.index;
    }

    public int newInt(int value) {
        Entry result = get(TAG_INTEGER, value);
        if (result == null) {
            cache.writeByte(TAG_INTEGER).writeInt(value);
            (result = new Entry(index++, this)).set(TAG_INTEGER, value);
            put(result);
        }
        return result.index;
    }

    public int newFloat(float value) {
        Entry result = get(TAG_FLOAT, value);
        if (result == null) {
            cache.writeByte(TAG_FLOAT).writeInt((int) value);
            (result = new Entry(index++, this)).set(TAG_FLOAT, value);
            put(result);
        }
        return result.index;
    }

    public int newLong(long value) {
        Entry result = get(TAG_LONG, value);
        if (result == null) {
            cache.writeByte(TAG_LONG).writeDouble(Double.longBitsToDouble(value));
            (result = new Entry(index, this)).set(TAG_LONG, value);
            index += 2;
            put(result);
        }
        return result.index;
    }

    public int newDouble(double value) {
        Entry result = get(TAG_DOUBLE, value);
        if (result == null) {
            cache.writeByte(TAG_DOUBLE).writeDouble(value);
            (result = new Entry(index, this)).set(TAG_DOUBLE, value);
            index += 2;
            put(result);
        }
        return result.index;
    }

    public int newNameType(String name, String desc) {
        String[] proc = new String[]{name, desc};
        Entry result = get(TAG_DESCRIPTOR, proc);
        if (result == null) {
            int np = newUTF(name);
            int dp = newUTF(desc);
            cache.writeByte(TAG_DESCRIPTOR).writeShort(np).writeShort(dp);
            (result = new Entry(index++, this)).set(TAG_DESCRIPTOR, proc);
            put(result);
        }
        return result.index;
    }

    public Object get(int i) {
        Entry c = items[i];
        switch (c.type) {
            case TAG_DESCRIPTOR:
            case TAG_FIELD:
            case TAG_METHOD:
            case TAG_INTERFACE_METHOD:
                return getDescriptor(i);
            case TAG_CLASS:
            case TAG_STRING:
                return getString(i);
            case TAG_UTF_STRING:
                return getUTF(i);
            default:
                return c.complex;
        }
    }

    public String getUTF(int index) {
        Entry dec = items[index];
        if (dec != null)
            if (dec.resolved) {
                return (String) dec.complex;
            } else {
                byte[] bc;
                int size = (bc = (byte[]) dec.complex).length;
                int pos = 0;
                char[] buf = new char[size];
                int strLen = 0;

                while (pos < size) {
                    int c;
                    if ((c = bc[pos++] & 0xFF) < 0x80) {
                        buf[strLen++] = (char) c;
                    } else {
                        char cc;
                        if (c < 0xE0 && c > 0xBF) {
                            cc = (char) (c & 0x1F);
                        } else {
                            cc = (char) ((c & 0xF) << 0x6 | c & 0x3F);
                        }
                        buf[strLen++] = (char) (cc << 0x6 | c & 0x3F);
                    }
                }

                String str = new String(buf, 0, strLen);
                items[index].complex = str;
                items[index].resolved = true;
                return str;
            }
        return null;
    }

    public String getString(int index) {
        Entry con = items[index];
        if (con.resolved)
            return (String) con.complex;
        items[index].resolved = true;
        return (String) (items[index].complex = getUTF((Short) con.complex));
    }

    public String[] getMember(int index) {
        Entry con = items[index];
        if (con.resolved)
            return (String[]) con.complex;
        Short[] comp = (Short[]) con.complex;
        items[index].resolved = true;
        String[] desc = getDescriptor(comp[1]);
        return (String[]) (items[index].complex = new String[]{getString(comp[0]), desc[0], desc[1]});
    }

    public String[] getDescriptor(int index) {
        Entry con = items[index];
        if (con.resolved)
            return (String[]) con.complex;
        Short[] comp = (Short[]) con.complex;
        items[index].resolved = true;
        return (String[]) (items[index].complex = new String[]{getUTF(comp[0]), getUTF(comp[1])});
    }

    public double getDouble(int index) {
        return (Double) items[index].complex;
    }

    public long getLong(int index) {
        return (Long) items[index].complex;
    }

    public int getInteger(int index) {
        return (Integer) items[index].complex;
    }

    private void put(Entry i) {
        if (index + 2 > items.length) {
            int ll = items.length;
            int nl = (ll << 1) + 1;
            Entry[] newItems = new Entry[nl];
            for (int l = ll - 1; l >= 0; --l) {
                Entry j = items[l];
                while (j != null) {
                    Entry k = j.next;
                    j.next = newItems[j.index];
                    newItems[j.index] = j;
                    j = k;
                }
            }
            items = newItems;
        }

        items[i.index] = i;
        if(last != null)items[last.index].next = i;
        last = i;
    }

    private Entry get(int type, Object... args) {
        Entry x = new Entry((short) -1, this);
        x.set((byte) type, args.length > 1 ? args : args[0]);
        Entry i = items[1];
        while (i != null && (i.type != type || !x.equals(i))) {
            System.out.println("i is " + i + ", next is " + i.next);
            i = i.next;
        }
        return i;
    }

    @Override
    public String toString() {
        return "{(" + size() + "):" + Arrays.toString(items) + "}";
    }

    private static final class Entry implements Opcode {
        byte type;
        Object complex;
        Entry next;
        short index;
        boolean resolved = true;
        ConstantPool owner;

        private Entry(short index, ConstantPool owner) {
            this.index = (short) (index + 1);
            this.owner = owner;
        }

        private final void set(byte tag, Object comp) {
            type = tag;
            complex = comp;
        }

        public boolean equals(Entry en) {
            // The fastest, and simplest check
            System.out.println(en.complex + "::" + complex + "::" + en.complex.equals(complex));
            if (en.type == type) {
                if (en.resolved)
                    if (resolved) {

                        return en.complex.equals(complex);
                    } else {
                        switch (type) {
                            case TAG_UTF_STRING:
                                return complex.equals(((String) en.complex).getBytes());
                            case TAG_STRING:
                            case TAG_CLASS:
                                return owner.get(TAG_UTF_STRING, en.complex).index == (Short) complex;
                            default:
                                return true;


                        }
                    }
            }
            return false;
        }

        @Override
        public String toString() {
            return "{" + type + "@" + index + "; " + (complex instanceof Object[] ? Arrays.toString((Object[]) complex) : complex) + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            if (type != entry.type) return false;
            if (complex != null ? !complex.equals(entry.complex) : entry.complex != null) return false;
            if (owner != null ? !owner.equals(entry.owner) : entry.owner != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) type;
            result = 31 * result + (complex != null ? complex.hashCode() : 0);
            return result;
        }
    }
}
