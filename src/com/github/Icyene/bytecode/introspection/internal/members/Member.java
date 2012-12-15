package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.metadata.AccessFlag;
import com.github.Icyene.bytecode.introspection.internal.metadata.Tag;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class Member {
    protected AccessFlag accessFlags;
    protected Constant name;
    protected Constant descriptor;
    protected AttributePool attributePool;
    private ConstantPool owner;

    public Member(ByteStream stream, ConstantPool pool) {
        accessFlags = new AccessFlag(stream.readShort());
        name = pool.get(stream.readShort());
        descriptor = pool.get(stream.readShort());
        attributePool = new AttributePool(stream, pool);
        owner = pool;
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(accessFlags.getBytes());
        out.write(Bytes.toByteArray((short) name.getIndex()));
        out.write(Bytes.toByteArray((short) descriptor.getIndex()));
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    public AccessFlag getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlag accessFlags) {
        this.accessFlags = accessFlags;
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public Constant getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Constant descriptor) {
        this.descriptor = descriptor;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }

    public String getModifiers() {
        String atFront = "", atEnd = "", name = "";

        char[] chars = descriptor.getStringValue().toCharArray();

        boolean isRef = false;
        for (char c : chars) {
            if (isRef && c != ';')
                atFront += c == '/' ? '.' : c;
            else {
                isRef = false;
                switch (c) {
                    case 'B':
                        atFront += "byte";
                        continue;
                    case 'C':
                        atFront += "char";
                        continue;
                    case 'D':
                        atFront += "double";
                        continue;
                    case 'F':
                        atFront += "float";
                        continue;
                    case 'I':
                        atFront += "int";
                        continue;
                    case 'J':
                        atFront += "long";
                        continue;
                    case 'S':
                        atFront += "short";
                        continue;
                    case 'Z':
                        atFront += "boolean";
                        continue;
                    case 'L':
                        isRef = true;
                        continue;
                    case '[':
                        atEnd += "[]";
                        continue;
                }
            }
        }
        return (atFront + atEnd + " " + name).trim();
    }

    public boolean isDeprecated() {
        return attributePool.hasAttribute("Deprecated");
    }

    public void setDeprecated() {
        if (!attributePool.hasAttribute("Deprecated")) {
            Constant dep = new Constant(owner.size() + 2, Tag.UTF_STRING, "Deprecated".getBytes(), owner);
            owner.add(dep);
            attributePool.add(new Attribute(dep, 0));
        }
    }
}
