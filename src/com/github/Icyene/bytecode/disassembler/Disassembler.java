package com.github.Icyene.bytecode.disassembler;

import com.github.Icyene.bytecode.disassembler.internal.ClassFile;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.io.File;
import java.io.IOException;

public class Disassembler {

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        try {
            System.out.println(new ClassFile(clazz).getConstantPool().toString());

            System.out.println("V bytes: " + Bytes.bytesToString(Bytes.read(clazz)));
            System.out.println("M bytes: " + Bytes.bytesToString(new ClassFile(clazz).assemble()));

            Bytes.writeBytesToFile(new ClassFile(clazz).getConstantPool().toString().getBytes(), clazz.getAbsoluteFile() + ".bytes");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
