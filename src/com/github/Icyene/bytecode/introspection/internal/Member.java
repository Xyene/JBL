package com.github.Icyene.bytecode.introspection.internal;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.ACC_NATIVE;
import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.ACC_SYNCHRONIZED;
import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

public class Member extends AccessibleMember {
    protected Constant name;
    protected Constant descriptor;
    protected AttributePool attributePool;
    private final ConstantPool owner;

    public Member(ByteStream stream, ConstantPool pool) {
        flag = stream.readShort();
        name = pool.get(stream.readShort());
        descriptor = pool.get(stream.readShort());
        attributePool = new AttributePool(stream, pool);
        owner = pool;
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short)flag));
        out.write(Bytes.toByteArray((short) name.getIndex()));
        out.write(Bytes.toByteArray((short) descriptor.getIndex()));
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    public String getName() {
        return name.getStringValue();
    }

    public void setName(String newName) {
        int index = name.getIndex();
        System.out.println("Set name of " + name.getStringValue() + " @ " + index + " to " + newName);
        owner.set(index, (name = new Constant(index, TAG_UTF_STRING, newName.getBytes(), owner)));
    }

    public String getDescriptor() {
        return descriptor.getStringValue();
    }

    public void setDescriptor(String newDescriptor) {
        int index = descriptor.getIndex();
        owner.set(index, (descriptor = new Constant(index, TAG_UTF_STRING, newDescriptor.getBytes(), owner)));
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
            Constant dep = new Constant(owner.size() + 2, TAG_UTF_STRING, "Deprecated".getBytes(), owner);
            owner.add(dep);
            attributePool.add(new Attribute(dep, 0));
        }
    }

    public boolean isSynchronized() {
        return is(ACC_SYNCHRONIZED);
    }

    public void setSynchronized(boolean i) {
        flag = i ? flag | ACC_SYNCHRONIZED : flag & ~ACC_SYNCHRONIZED;
    }

    public boolean isNative() {
        return is(ACC_NATIVE);
    }

    public void setNative(boolean i) {
        flag = i ? flag | ACC_NATIVE : flag & ~ACC_NATIVE;
    }
}
