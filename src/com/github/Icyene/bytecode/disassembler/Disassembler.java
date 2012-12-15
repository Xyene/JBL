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

            String inBytes =  Bytes.bytesToString(Bytes.read(clazz));
            String outBytes = Bytes.bytesToString(cc.getBytes());

            System.out.println("V bytes: " + inBytes);
            System.out.println("M bytes: " + outBytes);
            if(!outBytes.equals(inBytes))
                System.out.println("Streams do not match!");

            cc.setSourceFile("DO NOT DECOMPILE THIS.java");
            Bytes.writeBytesToFile(cc.getBytes(), clazz.getAbsoluteFile() + ".class");
            Bytes.writeBytesToFile(Bytes.read(clazz), clazz.getAbsoluteFile() + ".writetest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
