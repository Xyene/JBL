package net.sf.jbl.introspection.metadata;

import net.sf.jbl.introspection.Attribute;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static net.sf.jbl.introspection.Opcode.*;

public interface Metadatable<T> {

    void removeMetadata(String meta);

    Object getMetadata(String meta);

    boolean hasMetadata(String meta);

    <V> void addMetadata(String meta, V value);

    class Container {
        protected List<Attribute> attributes;

        public Container(List<Attribute> attributePool) {
            attributes = attributePool;
        }

        protected Attribute getMetadataInstance(String meta) {
            if(meta == null) throw new IllegalArgumentException("metadata lookup may not be null");
            int size = attributes.size();
            for (int i = 0; i != size; i++) {
                Attribute a = attributes.get(i);
                /*
                   if (a instanceof UnknownAttribute) {
                        UnknownAttribute ua = (UnknownAttribute)
                        if ("Code".equals(name)) return new Code(ByteStream.readStream(), name, constants);
                        else if ("LineNumberTable".equals(name)) return new LineNumberTable(stream, name);
                        else if ("LocalVariableTable".equals(name))
                            return new LocalVariableTable(stream, name, constants);
                        return a;
                    }

                 */
                if (a.getName().equals(meta)) {
                    return a;
                }
            }
            return null;
        }

        public void removeMetadata(String meta) {
            Attribute a;
            if ((a = getMetadataInstance(meta)) != null) attributes.remove(a);
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

        public List<Attribute> getAttributes() {
            //TODO: FINISH
            //Update keystore
            // try {
            //   UnknownAttribute saved = new UnknownAttribute();
//                saved.setName("__JBLMeta__");
            //   saved.setValue(xmlBytes(dispatch));
            //   attributes.add(saved);
            // } catch (IOException e) {
            //   throw new RuntimeException("could not save to JBL metadata store", e);
            //  }
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
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
