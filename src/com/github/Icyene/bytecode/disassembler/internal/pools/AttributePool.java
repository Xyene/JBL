package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.attributes.CodeAttribute;
import com.github.Icyene.bytecode.disassembler.internal.attributes.SourceFileAttribute;
import com.github.Icyene.bytecode.disassembler.internal.attributes.UnknownAttribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AttributePool extends LinkedList<Attribute> {

    private final List<String> recognized = Arrays.asList("SourceFile", "Code");

    public AttributePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();

        System.out.println("Attribute table size: " + size);

        for (int i = 0; i != size; i++) {

            Constant name = pool.get(stream.readShort() );
            String realName = name.getStringValue();

            System.out.println("Handling " + name + " attribute!");

            if (!recognized.contains(realName)) {
                add(new UnknownAttribute(stream, name, pool));
            } else if (realName.equals("SourceFile")) {
                add(new SourceFileAttribute(stream, name, pool));
            } else if (realName.equals("Code")) {
                add(new CodeAttribute(stream, name, pool));
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
        for(Attribute a: this)
            if(a.getClass().equals(type))
                out.add(a);
        return out;
    }
}
