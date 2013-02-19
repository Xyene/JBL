package net.sf.jbl.introspection;

import net.sf.jbl.util.ByteStream;

import java.util.Arrays;

public class ConstantPool implements Opcode {
    // In this implementation of a constant pool, we assume that the latter is can be read, but only appended
    // to. This allows us to optimize it to an array rather than its slower ArrayList counterpart. Constants
    // are loaded lazily from this array.
    private Entry[] items;
    private int threshold;
    private short index = 0;
    final ByteStream cache;

    public ConstantPool(int size) {
        items = new Entry[size];
        threshold = (int) (size * 0.75D);
        cache = ByteStream.writeStream(threshold);
    }

    ConstantPool(ByteStream in) {
        short len = in.readShort();
        items = new Entry[len];
        // Store the current index of the positioned stream to allow forking of the stream after all constants
        // Are parsed and loaded into the constant array.
        int start = in.position();
        for (short i = 1; i != len; i++) {
            byte tag = in.readByte();
            Entry item = new Entry(i, tag);
            switch (tag) {
                case TAG_FIELD:
                case TAG_METHOD:
                case TAG_INTERFACE_METHOD:
                    item.set_(tag, new Object[]{Short.valueOf(in.readShort()), Short.valueOf(in.readShort())});
                    item.resolved = false;
                    break;
                case TAG_INTEGER:
                    item.set(tag, Integer.valueOf(in.readInt()));
                    break;
                case TAG_FLOAT:
                    item.set(tag, Float.valueOf(in.readFloat()));
                    break;
                case TAG_DESCRIPTOR:
                    item.set_(tag, new Object[]{Short.valueOf(in.readShort()), Short.valueOf(in.readShort())});
                    item.resolved = false;
                    break;
                // Long and double values are considered wide by the JVM, meaning that they take up two slots in
                // the constant pool. We simply increment the index. Any program accessing the phantom index in
                // such a constant will be met with an appropriate null pointer.
                case TAG_LONG:
                    item.set(tag, Long.valueOf(in.readLong()));
                    i++;
                    break;
                case TAG_DOUBLE:
                    item.set(tag, Double.valueOf(in.readDouble()));
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
                    item.set(tag, Short.valueOf(in.readShort()));
                    item.resolved = false;
                    break;
                case 2:
                default:
                    // Adding this here allows the JVM to make this a tableswitch, since the values now range from
                    // 1-13. Otherwise, 2 would not be included and this optimization could not be possible.
                    // Since this is part of the default, invalid case,
                    throw new ClassFormatError("unknown constant @ " + i + " with tag " + tag);
            }
            item.next = items[i];
            items[i] = item;
        }
        threshold = (int) (0.75D * len);
        index = len;
        // By deep forking the stream to a writable form, we save time on dumping, because the contents of pre-existing
        // constants are already stored in it. This means we no longer have to start from scratch every time the
        // pool is dumped. All internal constant generators write to this stream: it is always up to date.
        int size = in.position() - start;
        cache = ByteStream.writeStream(size);
        System.arraycopy(in.getBuffer(), start, cache.getBuffer(), 0, size);
    }

    public int size() {
        return items.length;
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
            (result = new Entry(index++)).set(TAG_UTF_STRING, value);
            put(result);
        }
        return result.index;
    }

    public int newString(String value) {
        Entry result = get(TAG_STRING, value);
        if (result == null) {
            cache.writeByte(TAG_STRING).writeShort(newUTF(value));
            (result = new Entry(index++)).set(TAG_UTF_STRING, value);
            put(result);
        }
        return result.index;
    }

    public int newClass(String value) {
        Entry result = get(TAG_CLASS, value);
        if (result == null) {
            cache.writeByte(TAG_CLASS).writeShort(newUTF(value));
            (result = new Entry(index++)).set(TAG_CLASS, value);
            put(result);
        }
        return result.index;
    }

    public int newField(String owner, String name, String desc) {
        Entry result = get(TAG_FIELD, owner, name, desc);
        if (result == null) {
            cache.writeByte(TAG_FIELD).writeShort(newClass(owner)).writeShort(newNameType(name, desc));
            (result = new Entry(index++)).set_(TAG_FIELD, owner, name, desc);
            put(result);
        }
        return result.index;
    }

    public int newMethod(String owner, String name, String desc) {
        Entry result = get(TAG_METHOD, owner, name, desc);
        if (result == null) {
            cache.writeByte(TAG_METHOD).writeShort(newClass(owner)).writeShort(newNameType(name, desc));
            (result = new Entry(index++)).set_(TAG_METHOD, owner, name, desc);
            put(result);
        }
        return result.index;
    }

    public int newInt(int value) {
        Entry result = get(TAG_INTEGER, value);
        if (result == null) {
            cache.writeByte(TAG_INTEGER).writeInt(value);
            (result = new Entry(index++)).set(TAG_INTEGER, value);
            put(result);
        }
        return result.index;
    }

    public int newFloat(float value) {
        Entry result = get(TAG_FLOAT, value);
        if (result == null) {
            cache.writeByte(TAG_FLOAT).writeInt((int) value);
            (result = new Entry(index++)).set(TAG_FLOAT, value);
            put(result);
        }
        return result.index;
    }

    public int newLong(long value) {
        Entry result = get(TAG_LONG, value);
        if (result == null) {
            cache.writeByte(TAG_LONG).writeDouble(value);
            (result = new Entry(index)).set(TAG_LONG, value);
            index += 2;
            put(result);
        }
        return result.index;
    }

    public int newDouble(double value) {
        Entry result = get(TAG_DOUBLE, value);
        if (result == null) {
            cache.writeByte(TAG_DOUBLE).writeDouble(value);
            (result = new Entry(index)).set(TAG_DOUBLE, value);
            index += 2;
            put(result);
        }
        return result.index;
    }

    public int newNameType(String name, String desc) {
        Entry result = get(TAG_DESCRIPTOR, name, desc);
        if (result == null) {
            cache.writeByte(TAG_DESCRIPTOR).writeShort(newUTF(name)).writeShort(newUTF(desc));
            (result = new Entry(index++)).set_(TAG_DESCRIPTOR, name, desc);
            put(result);
        }
        return result.index;
    }

    private Entry get(byte tag, Object... value) {
        Entry c = items[0];
        // Here we use two almost identical while loops based on if the constant is initialized.
        // This is to prevent the overhead of comparing a boolean in a value/resolved ternary
        // possibly thousands of times when this method is called. Depending on the scenario,
        // this micro-optimization's effects are large, resulting up to entire milliseconds of time saved.
        while (c != null && c.type != tag) {
            Object complex = c.complex;
            if (c.resolved)
                switch (tag) {
                    case TAG_INTEGER:
                    case TAG_LONG:
                    case TAG_DOUBLE:
                    case TAG_FLOAT:
                        // Primitive types are parsed as singluar values, not an object[] as implied by
                        // the fact that value is varargs. We can use the equality comparator
                        // since complex and value[0] should be primitive ints.
                        if (complex == value[0])
                            return c;
                }
            else {
                switch (tag) {
                    case TAG_UTF_STRING:
                        // Here we can simply compare via the byte array representation of this string
                        if (complex.equals(((String) value[0]).getBytes()))
                            return c;
                    case TAG_DESCRIPTOR:
                        // However, for descriptors, we must actually load the descriptor, and this
                        // entails loading their referenced UTF-8 components.
                        // The format of an unresolved descriptor is an index to name and params, respectively.
                        // The need for the indexes means that this call must recurse. When it does, it will fall on the
                        // TAG_UTF_STRING case.
                        if (complex.equals(new Short[]{get(TAG_UTF_STRING, value[0]).index, get(TAG_UTF_STRING, value[1]).index}))
                            return c;
                    case TAG_FIELD:
                    case TAG_METHOD:
                    case TAG_INTERFACE_METHOD:
                        if (complex.equals(new Short[]{get(TAG_CLASS, value[0]).index, get(TAG_DESCRIPTOR, value[1], value[2]).index}))
                            return c;
                    case TAG_CLASS:
                    case TAG_STRING:
                        if (complex.equals(get(TAG_CLASS, value[0]).index))
                            return c;
                }
            }
            if (complex == value)
                return c;
            c = c.next;
        }
        return c;
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
        Entry dec;
        if((dec = this.items[index]).resolved) {
            return (String)dec.complex;
        } else {
            byte[] bc;
            int size = (bc = (byte[])dec.complex).length;
            int pos = 0;
            char[] buf = new char[size];
            int strLen = 0;

            while(pos < size) {
                int c;
                if((c = bc[pos++] & 0xFF) < 0x80) {
                    buf[strLen++] = (char)c;
                } else {
                    char cc;
                    if(c < 0xE0 && c > 0xBF) {
                        cc = (char)(c & 0x1F);
                    } else {
                        cc = (char)((c & 0xF) << 0x6 | c & 0x3F);
                    }
                    buf[strLen++] = (char)(cc << 0x6 | c & 0x3F);
                }
            }

            String str = new String(buf, 0, strLen);
            this.items[index].complex = str;
            this.items[index].resolved = true;
            return str;
        }
    }

    public String getString(int index) {
        Entry con = items[index];
        if (con.resolved)
            return (String) con.complex;
        items[index].resolved = true;
        return (String) (items[index].complex = getUTF((Short) con.complex));
    }

    public String[] getDescriptor(int index) {
        Entry con = items[index];
        if (con.resolved)
            return (String[]) con.complex;
        Object[] comp = (Object[]) con.complex;
        items[index].resolved = true;
        return (String[]) (items[index].complex = new String[]{getUTF((Short) comp[0]), getUTF((Short) comp[1])});
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
        if (index > threshold) {
            int len = items.length;
            int size = (len << 1) + 1;
            Entry[] newItems = new Entry[size];
            for (int l = len - 1; l >= 0; --l) {
                Entry j = items[l];
                while (j != null) {
                    short index = j.index;
                    Entry k = j.next;
                    j.next = newItems[index];
                    newItems[index] = j;
                    j = k;
                }
            }
            items = newItems;
            // Since everything is written to the cache the moment it is created, the only backdraw in guessing
            // the size of the threshold is the possibility that some memory might be allocated that will never
            // actually be used.
            threshold = (int) (size * 0.75);
        }
        short index = i.index;
        i.next = items[index];
        items[index] = i;
    }

    private static final class Entry implements Opcode {
        byte type;
        Entry next;
        Object complex;
        short index;
        boolean resolved = true;

        private Entry(short index) {
            this.index = index;
        }

        private Entry(short index, byte type) {
            this.index = index;
            this.type = type;
        }

        private final void set_(byte tag, Object... comp) {
            type = tag;
            complex = comp;
        }

        private final void set(byte tag, Object comp) {
            type = tag;
            complex = comp;
        }

        @Override
        public String toString() {
            return "{" + type + "@" + index + "; " + (complex instanceof Object[] ? Arrays.toString((Object[]) complex) : complex) + "}";
        }
    }
}
