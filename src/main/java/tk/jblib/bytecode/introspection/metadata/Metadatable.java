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
            if (ab instanceof UnknownAttribute && dispatch.containsKey(ab.getName())) {
                UnknownAttribute data = (UnknownAttribute) ab;
                switch (dispatch.get(data.getName())) {
                    case TAG_UTF_STRING:
                        return new String(data.getValue());
                    case TAG_DOUBLE:
                        return Bytes.toDouble(data.getValue(), 0);
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

                    UnknownAttribute encoded = new UnknownAttribute();
                    encoded.setName(meta);
                    encoded.setValue(data);
                    attributes.add(encoded);
                } catch (IOException e) {
                    throw new RuntimeException("could not encode attribute '" + meta + "' with value '" + value + "'", e);
                }
        }

        public Pool<Attribute> getAttributes() {
            save(); //Update keystore
            return attributes;
        }

        public void setAttributes(Pool<Attribute> attributes) {
            this.attributes = attributes;
        }

        public void save() {
            try {
                UnknownAttribute saved = new UnknownAttribute();
                saved.setName("__JBLMeta__");
                saved.setValue(xmlBytes(dispatch));
                attributes.add(saved);
            } catch (IOException e) {
                throw new RuntimeException("could not save to JBL metadata store", e);
            }
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
