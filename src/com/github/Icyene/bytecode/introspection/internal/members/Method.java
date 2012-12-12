package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.members.Member;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

import java.util.LinkedList;

public class Method extends Member {

    public Method(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
    }

    public LinkedList<String> getRequiredImports() {
        LinkedList<String> imports = new LinkedList<String>();
        return imports;
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
}
