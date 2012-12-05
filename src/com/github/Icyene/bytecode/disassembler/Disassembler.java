package com.github.Icyene.bytecode.disassembler;

import com.github.Icyene.bytecode.disassembler.internal.ClassFile;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.io.File;
import java.io.IOException;

public class Disassembler {

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        try {
            ClassFile cc = new ClassFile(clazz);
            System.out.println(cc.getConstantPool().toString());

            System.out.println("V bytes: " + Bytes.bytesToString(Bytes.read(clazz)));
            System.out.println("M bytes: " + Bytes.bytesToString(cc.assemble()));

            cc.setMajorVersion((short) 5000);
            Bytes.writeBytesToFile(cc.assemble(), clazz.getAbsoluteFile() + ".class");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
