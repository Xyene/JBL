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

import net.sf.jbl.core.attributes.Code;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AttributePool extends ArrayList<Attribute> {
    public AttributePool(int size) {
        super(size);
    }

    public AttributePool() {
        this(0);
    }

    protected Attribute getMetadataInstance(String meta) {
        if (meta == null) throw new IllegalArgumentException("metadata lookup may not be null");
        int size = size();
        for (int i = 0; i != size; i++) {
            Attribute a = get(i);
            if (a.getName().equals(meta)) {
                return a;
            }
        }
        return null;
    }

    public void removeMetadata(String meta) {
        Attribute a;
        if ((a = getMetadataInstance(meta)) != null) remove(a);
    }

    public boolean hasMetadata(String meta) {
        return getMetadataInstance(meta) != null;
    }

    public Object getMetadata(String meta) {
        Attribute ab = getMetadataInstance(meta);
        //TODO not done
        return ab;
    }

    public <V> void addMetadata(String meta, V value) {
          /*       if (!hasMetadata(meta))
                try {
                    if (value instanceof Attribute) {
                        attributes.add((Attribute) value);
                        return;
                    }

                    byte[] data;

                    //SourceFile & Signature share the same structure: to avoid their own class we can just do this
               if (meta.equals("SourceFile") || meta.equals("Signature")) {
                        constants.add(new Constant(TAG_UTF_STRING, meta.getBytes()));
                        data = Bytes.getBuffer((short) constants.size());
                    } else if (meta.equals("ConstantValue")) {
                        //Constant values depend on the type of generic value
                        if (value instanceof Long)
                            constants.add(new Constant(TAG_LONG, Bytes.getBuffer((Long) value)));
                        else if (value instanceof Double)
                            constants.add(new Constant(TAG_DOUBLE, Bytes.getBuffer((Double) value)));
                        else if (value instanceof Integer)
                            constants.add(new Constant(TAG_INTEGER, Bytes.getBuffer((Integer) value)));
                        else if (value instanceof Float)
                            constants.add(new Constant(TAG_FLOAT, Bytes.getBuffer((Float) value)));
                        else
                            throw new IllegalArgumentException("value for ConstantValue must be of type long, double, integer, or float");
                        data = Bytes.getBuffer((short) (constants.size() + 1)); //Point to the last index: the constant we just added
                    } else {
                        int tag;
                        if (value != null) {
                            if (value instanceof String) {
                                data = ((String) value).getBytes();
                                tag = TAG_UTF_STRING;
                            } else if (value instanceof Double || value instanceof Float) {
                                data = Bytes.getBuffer(((Double) value));
                                tag = TAG_DOUBLE;
                            } else if (value instanceof Short) {
                                data = Bytes.getBuffer(((Short) value));
                                tag = 13;
                            } else if (value instanceof Byte) {
                                data = new byte[]{(Byte) value};
                                tag = 14;
                            } else if (value instanceof Boolean) {
                                data = new byte[]{(byte) (((Boolean) value) ? 1 : 0)};
                                tag = 15;
                            } else if (value instanceof Long) {
                                data = Bytes.getBuffer((Long) value);
                                tag = TAG_LONG;
                            } else {
                                data = xmlBytes(value);
                                tag = 16;
                            }
                            dispatch.put(meta, tag);
                        } else
                            data = new byte[]{}; //Some attributes, like Deprecated, Synthetic etc work on the basis of being present, nothing else.
                    }
                    UnknownAttribute encoded = new UnknownAttribute(meta);
                    encoded.setValue(data);
                    attributes.add(encoded);
                } catch (IOException e) {
                    throw new RuntimeException("could not encode attribute '" + meta + "' with value '" + value + "'", e);
                }*/
    }

    @Override
    public String toString() {
        return "{(" + size() + "):" + super.toString() + "}";
    }

    protected byte[] xmlBytes(Object value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(value);
        enc.close();
        return out.toByteArray();
    }

    public static class Handler {
        public static AttributePool readPool(ConstantPool constants, ByteStream in) {
            int size = in.readShort();
            AttributePool out = new AttributePool(size);
            for (int i = 0; i != size; i++) {
                int b = in.readShort();
                String name = constants.getUTF(b);
                int len = in.readInt();
                if ("Code".equals(name)) out.add(new Code(constants, in));
                else out.add(new Attribute(name, in.read(len)));
            }
            return out;
        }

        public static void writePool(AttributePool pool, ConstantPool constants, ByteStream out) {
            int size;
            out.writeShort(size = pool.size());
            for (int a = 0; a != size; a++) {
                pool.get(a).dump(out, constants);
            }
        }
    }
}
