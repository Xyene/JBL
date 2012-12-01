package com.github.Icyene.BytecodeStudio.Disassembler;

import com.github.Icyene.BytecodeStudio.Disassembler.Indices.Constant;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;

import java.io.File;
import java.io.IOException;

public class Disassembler {

    public static void main(String[] args) {

        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/Test.class");
        try {
            long average = 0;
            int iterations = 500;
            long start = System.nanoTime();
            for (int i = 1; i != iterations; i++) {
                new ClassFile(clazz);

               // System.out.println((System.currentTimeMillis() - start));
            }
            average = (System.nanoTime() - start) / iterations;
            System.out.println("Average time taken over " + iterations + " iterations: " + average + "n");
            System.out.println(new ClassFile(clazz).getConstantPool().toString());

            Bytes.writeBytesToFile(ClassFile.get(clazz).assemble(), clazz.getAbsoluteFile() + ".bytes");

            ConstantPool cp = new ClassFile(clazz).getConstantPool();
            StringBuilder se = new StringBuilder();
            se.append(cp.size());
            se.append("\n===Constant Pool===");
            se.append("\nSize of pool: " + cp.size());
            se.append("\nIndex   Type   Value");
            for(Constant cpi: cp) {
               se.append("\n#" + (cpi.getIndex()+1) + " = " + cpi.getType().name() + ":   " + cpi.prettyPrint()+";");
            }

            Bytes.writeBytesToFile(se.toString().getBytes(), clazz.getAbsoluteFile() + ".prettyprint");

            ClassFile cc = new ClassFile(clazz);
            cc.getConstantPool().set(1, new Constant(2, Tag.STRING, Bytes.getShort((short) 36)));
            Bytes.writeBytesToFile(cc.assemble(), clazz.getAbsoluteFile() + ".test");

            System.out.println("V bytes: " + Bytes.bytesToString(Bytes.read(clazz)));
            System.out.println("M bytes: " + Bytes.bytesToString(new ClassFile(clazz).assemble()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
