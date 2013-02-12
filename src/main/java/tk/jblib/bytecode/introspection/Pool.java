package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;
import tk.jblib.bytecode.introspection.members.TryCatch;
import tk.jblib.bytecode.introspection.members.attributes.*;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static tk.jblib.bytecode.introspection.Opcode.*;
import static tk.jblib.bytecode.introspection.Opcode.TAG_DOUBLE;
import static tk.jblib.bytecode.introspection.Opcode.TAG_PHANTOM;

public class Pool<T> extends LinkedList<T> {
    private int offset;
    private Parser<T> parser;

    public Pool(ByteStream in, Parser<T> poolParser, Pool<Constant> pool) {
        parser = poolParser;

        if (parser == null) throw new IllegalArgumentException("parser cannot be null!");
        if (in == null) throw new IllegalArgumentException("in bytestream cannot be null!");

        offset = parser.getOffset();
        int size = in.readShort();

        for (int i = offset; i != size; ) {
            int growth = size();
            T parsed = parser.parse(in, pool, this);
            if (parsed != null) {
                add(parsed);
            }
            i += size() - growth; //In case owner.add was called inside parse(), and null was returned
        }
    }

    public Pool(ByteStream in, Parser<T> poolParser) {
        this(in, poolParser, null);
    }

    public Pool() {

    }

    @Override
    public T get(int i) {
        return super.get(i - offset);
    }

    @Override
    public void add(int i, T t) {
        super.add(i - offset, t);
    }

    @Override
    public T remove(int i) {
        return super.remove(i - offset);
    }

    @Override
    public T set(int i, T t) {
        return super.set(i - offset, t);
    }

    public int getOffset() {
        return offset;
    }

    public Parser<T> getParser() {
        return parser;
    }

    public void setParser(Parser<T> parser) {
        this.parser = parser;
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream(Bytes.toByteArray((short) (size() + offset)));
        for (T t : this) {
            out.write(parser.getBytes(t));
        }
        return out.toByteArray();
    }

    public abstract static class Parser<T> {
        public abstract T parse(ByteStream stream, Pool<Constant> constantPool, Pool<T> owner);

        public abstract byte[] getBytes(T t);

        public int getOffset() {
            return 0;
        }
    }

    public static final Parser<Attribute> ATTRIBUTE_PARSER = new Parser<Attribute>() {
        @Override
        public Attribute parse(ByteStream stream, Pool<Constant> pool, Pool<Attribute> owner) {
            Constant name = pool.get(stream.readShort());
            String str = name.stringValue();
            if (str.equals("Code")) return new Code(stream, name, pool);
            else if (str.equals("LineNumberTable")) return new LineNumberTable(stream, name);
            else if (str.equals("LocalVariableTable")) return new LocalVariableTable(stream, name, pool);
            else return new UnknownAttribute(stream, name);
        }

        @Override
        public byte[] getBytes(Attribute a) {
            return a.getBytes();
        }
    };

    public static final Parser<Member> MEMBER_PARSER = new Parser<Member>() {
        @Override
        public Member parse(ByteStream stream, Pool<Constant> pool, Pool<Member> owner) {
            return new Member(stream, pool);
        }

        @Override
        public byte[] getBytes(Member m) {
            return m.getBytes();
        }
    };

    public static final Parser<Interface> INTERFACE_PARSER = new Parser<Interface>() {
        @Override
        public Interface parse(ByteStream stream, Pool<Constant> pool, Pool<Interface> owner) {
            return new Interface(pool.get(stream.readShort()));
        }

        @Override
        public byte[] getBytes(Interface i) {
            return i.getBytes();
        }
    };

    public static final Parser<Constant> CONSTANT_PARSER = new Parser<Constant>() {
        @Override
        public Constant parse(ByteStream stream, Pool<Constant> unused, Pool<Constant> owner) {
            byte info;
            Constant par;
            switch ((int) (info = stream.readByte())) {
                case TAG_UTF_STRING:
                    par = new Constant(info, stream.read(stream.readShort()));
                    break;
                case TAG_CLASS:
                case TAG_STRING:
                case TAG_METHOD_TYPE:
                    par = new Constant(info, stream.read(2));
                    break;
                case TAG_INTEGER:
                case TAG_FLOAT:
                case TAG_FIELD:
                case TAG_METHOD:
                case TAG_INTERFACE_METHOD:
                case TAG_DESCRIPTOR:
                case TAG_INVOKEDYNAMIC:
                    par = new Constant(info, stream.read(4));
                    break;
                case TAG_LONG:
                case TAG_DOUBLE:
                    Constant phantom = new Constant(owner.size() + 1, info, stream.read(8));
                    phantom.setOwner(owner);
                    owner.add(phantom);
                    par = new Constant(TAG_PHANTOM, null);
                    break;
                case TAG_METHOD_HANDLE:
                    par = new Constant(info, stream.read(3));
                    break;
                default:
                    par = new Constant(TAG_PHANTOM, null);
            }

            par.setOwner(owner);
            par.setIndex(owner.size() + 1);
            return par;
        }

        @Override
        public byte[] getBytes(Constant c) {
            return c.getBytes();
        }

        @Override
        public int getOffset() {
            return 1;
        }
    };

    public static final Parser<TryCatch> EXCEPTION_PARSER = new Parser<TryCatch>() {
        @Override
        public TryCatch parse(ByteStream stream, Pool<Constant> pool, Pool<TryCatch> owner) {
            return new TryCatch(stream);
        }

        @Override
        public byte[] getBytes(TryCatch tryCatch) {
            return tryCatch.getBytes();
        }
    };
}
