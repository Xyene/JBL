package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.*;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class AttributePool extends ArrayList<Attribute> {

    private final List<String> recognized = Arrays.asList("Code", "ConstantValue", "LineNumberTable", "SourceFile");

    public AttributePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++) {
            Constant name = pool.get(stream.readShort());
            String realName = name.getStringValue();
            if (!recognized.contains(realName)) {
                add(new UnknownAttribute(stream, name, pool));
            } else if (realName.equals("Code")) {
                add(new Code(stream, name, pool));
            } else if (realName.equals("ConstantValue")) {
                add(new ConstantValue(stream, name, pool));
            } else if (realName.equals("LineNumberTable")) {
                add(new LineNumberTable(stream, name, pool));
            } else if (realName.equals("LocalVariableTable")) {
                add(new LineNumberTable(stream, name, pool));
            } else if (realName.equals("SourceFile")) {
                add(new SourceFile(stream, name, pool));
            }
        }
    }

    public byte[] getBytes() {
        byte[] ret = Bytes.toByteArray((short) size());
        for (Attribute a : this)
            ret = Bytes.concat(ret, a.getBytes());
        return ret;
    }

    public LinkedList<Attribute> getInstancesOf(Class type) {
        LinkedList<Attribute> out = new LinkedList<Attribute>();
        for (Attribute a : this)
            if (a.getClass().equals(type))
                out.add(a);
        return out;
    }


    public LinkedList<Attribute> getInstancesOf(String type) {
        Pattern t = Pattern.compile(type, Pattern.DOTALL);
        LinkedList<Attribute> out = new LinkedList<Attribute>();
        for (Attribute a : this)
            if (t.matcher(a.getName().getStringValue()).matches())
                out.add(a);
        return out;
    }

    public void removeInstancesOf(Class type) {
        removeAll(getInstancesOf(type));
    }

    public void removeInstancesOf(String type) {
        removeAll(getInstancesOf(type));
    }

    public boolean hasAttribute(String attr) {
        Pattern m = Pattern.compile(attr, Pattern.DOTALL);
        for (Attribute a : this)
            if (m.matcher(a.getName().getStringValue()).matches())
                return true;
        return false;
    }
}
