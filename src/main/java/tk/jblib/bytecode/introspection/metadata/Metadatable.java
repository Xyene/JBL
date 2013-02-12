package tk.jblib.bytecode.introspection.metadata;

import tk.jblib.bytecode.introspection.Pool;
import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.attributes.UnknownAttribute;
import tk.jblib.bytecode.util.Bytes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static tk.jblib.bytecode.introspection.Opcode.*;

public interface Metadatable<T> {
    Collection<T> getMetadataInstances(String meta);

    void removeMetadata(String meta);

    Object getMetadata(String meta);

    boolean hasMetadata(String meta);

    <V> void addMetadata(String meta, V value);

    class Container {
        protected Pool<Attribute> attributes;
        protected Pool<Constant> constants;
        protected HashMap<String, Integer> dispatch;

        public Container(Pool<Attribute> attributePool, Pool<Constant> constantPool) {
            attributes = attributePool;
            constants = constantPool;
            dispatch = hasMetadata("__JBLMeta__") ? (HashMap<String, Integer>) getMetadata("__JBLMeta__") : new HashMap<String, Integer>();
        }

        public Set<Attribute> getMetadataInstances(String meta) {
            Set<Attribute> out = new HashSet<Attribute>();
            for (Attribute a : attributes)
                if (a.getName().equals(meta))
                    out.add(a);
            return out;
        }

        public void removeMetadata(String meta) {
            attributes.removeAll(getMetadataInstances(meta));
        }

        public boolean hasMetadata(String meta) {
            return getMetadataInstances(meta).size() > 0;
        }

        public Object getMetadata(String meta) {
            Attribute ab = getMetadataInstances(meta).iterator().next();
            String name = ab.getName();
            if (ab instanceof UnknownAttribute && dispatch.containsKey(name)) {
                UnknownAttribute data = (UnknownAttribute) ab;

                if (name.equals("SourceFile") || name.equals("Signature"))
                    return constants.get(Bytes.toShort(data.getValue(), 0)).stringValue();
                if (name.equals("ConstantValue"))   {
                    Constant value = constants.get(Bytes.toShort(data.getValue(), 0));
                    switch(value.getType()) {
                        case TAG_UTF_STRING:
                            return new String(value.getRawValue());
                        case TAG_DOUBLE:
                        case TAG_LONG:
                            return Bytes.toDouble(value.getRawValue(), 0);
                        case TAG_FLOAT:
                        case TAG_INTEGER:
                            return Bytes.toInteger(value.getRawValue(), 0);
                    }
                }

                switch (dispatch.get(name)) {
                    case TAG_UTF_STRING:
                        return new String(data.getValue());
                    case TAG_DOUBLE:
                    case TAG_LONG:
                        return Bytes.toLong(data.getValue(), 0);
                    case TAG_INTEGER:
                        return Bytes.toInteger(data.getValue(), 0);
                    case 13: //Short
                        return Bytes.toShort(data.getValue(), 0);
                    case 14: //Byte
                        return data.getValue()[0];
                    case 15: //Bool
                        return data.getValue()[0] == 1 ? true : false;
                    case 16: //XML
                        return new XMLDecoder(new ByteArrayInputStream(data.getValue())).readObject();
                }
            }
            return ab;
        }

        public <V> void addMetadata(String meta, V value) {
            if (!hasMetadata(meta))
                try {
                    if (value instanceof Attribute) {
                        attributes.add((Attribute) value);
                        return;
                    }

                    byte[] data;

                    //SourceFile & Signature share the same structure: to avoid their own class we can just do this
                    if (meta.equals("SourceFile") || meta.equals("Signature")) {
                        constants.add(new Constant(TAG_UTF_STRING, meta.getBytes()));
                        data = Bytes.toByteArray((short) constants.size());
                    } else if (meta.equals("ConstantValue")) {
                        //Constant values depend on the type of generic value
                        if (value instanceof Long)         constants.add(new Constant(TAG_LONG, Bytes.toByteArray((Long) value)));
                        else if (value instanceof Double)  constants.add(new Constant(TAG_DOUBLE, Bytes.toByteArray((Double) value)));
                        else if (value instanceof Integer) constants.add(new Constant(TAG_INTEGER, Bytes.toByteArray((Integer) value)));
                        else if (value instanceof Float)   constants.add(new Constant(TAG_FLOAT, Bytes.toByteArray((Float) value)));
                        else throw new IllegalArgumentException("value for ConstantValue must be of type long, double, integer, or float");
                        data = Bytes.toByteArray((short) constants.getLast().getIndex()); //Point to the last index: the constant we just added
                    } else {
                        int tag;
                        if (value != null) {
                            if (value instanceof String) {
                                data = ((String) value).getBytes();
                                tag = TAG_UTF_STRING;
                            } else if (value instanceof Double || value instanceof Float) {
                                data = Bytes.toByteArray(((Double) value));
                                tag = TAG_DOUBLE;
                            } else if (value instanceof Short) {
                                data = Bytes.toByteArray(((Short) value));
                                tag = 13;
                            } else if (value instanceof Byte) {
                                data = new byte[]{(Byte) value};
                                tag = 14;
                            } else if (value instanceof Boolean) {
                                data = new byte[]{(byte) (((Boolean) value) ? 1 : 0)};
                                tag = 15;
                            } else if (value instanceof Long) {
                                data = Bytes.toByteArray((Long) value);
                                tag = TAG_LONG;
                            } else {
                                data = xmlBytes(value);
                                tag = 16;
                            }
                            dispatch.put(meta, tag);
                        } else
                            data = new byte[]{}; //Some attributes, like Deprecated, Synthetic etc work on the basis of being present, nothing else.
                    }
                    UnknownAttribute encoded = new UnknownAttribute();
                    encoded.setName(meta);
                    encoded.setValue(data);
                    attributes.add(encoded);
                } catch (IOException e) {
                    throw new RuntimeException("could not encode attribute '" + meta + "' with value '" + value + "'", e);
                }
        }

        public Pool<Attribute> getAttributes() {
            //Update keystore
            try {
                UnknownAttribute saved = new UnknownAttribute();
                saved.setName("__JBLMeta__");
                saved.setValue(xmlBytes(dispatch));
                attributes.add(saved);
            } catch (IOException e) {
                throw new RuntimeException("could not save to JBL metadata store", e);
            }
            return attributes;
        }

        public void setAttributes(Pool<Attribute> attributes) {
            this.attributes = attributes;
        }

        protected byte[] xmlBytes(Object value) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLEncoder enc = new XMLEncoder(out);
            enc.writeObject(value);
            enc.close();
            return out.toByteArray();
        }
    }
}
