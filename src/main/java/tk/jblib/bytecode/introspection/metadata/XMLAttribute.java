package tk.jblib.bytecode.introspection.metadata;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.util.ByteStream;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XMLAttribute<T> extends Attribute {
    ByteStream stream = new ByteStream();

    public XMLAttribute(String key, T value) throws IOException {
        writeObject(value);
    }

    public XMLAttribute(String key) {
        //TODO: set name
    }

    public XMLAttribute(ByteStream t) throws IOException {
        length = t.readInt();
        stream.write(t.read(length));
    }

    public byte[] getBytes() {
        byte[] raw = stream.toByteArray();
        int len = raw.length;
        return new ByteStream().write(len).write(raw).toByteArray();
    }

    protected void writeObject(T value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(stream.toByteArray());
        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(value);
        stream.flush();
        stream.write(out.toByteArray());
    }

    public T readObject() {
        return (T)  new XMLDecoder(new ByteArrayInputStream(stream.toByteArray())).readObject();
    }
}