package com.github.Icyene.BytecodeStudio.Disassembler;

import com.github.Icyene.BytecodeStudio.Disassembler.Types.Constant;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;

import java.io.File;
import java.io.IOException;

public class Disassembler {

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/Test.class");
        try {
            new ClassFile(clazz);
            long average;
            int iterations = 500;
            long start = System.nanoTime();
            for (int i = 1; i != iterations; i++) {
                new ClassFile(clazz);
            }

            average = (System.nanoTime() - start) / iterations;
            System.out.println("Average time taken over " + iterations + " iterations: " + average + "n");
            System.out.println(new ClassFile(clazz).getConstantPool().toString());

            Bytes.writeBytesToFile(new ClassFile(clazz).assemble(), clazz.getAbsoluteFile() + ".bytes");

            ConstantPool cp = new ClassFile(clazz).getConstantPool();
            StringBuilder se = new StringBuilder();
            se.append(cp.size());
            se.append("\n===Constant Pool===");
            se.append("\nSize of pool: ").append(cp.size());
            se.append("\nIndex   Type   Value");
            for(Constant cpi: cp) {
               se.append("\n#").append(cpi.getIndex() + 1).append(" = ").append(cpi.getType().name()).append(":   ").append(cpi.prettyPrint()).append(";");
            }

            Bytes.writeBytesToFile(se.toString().getBytes(), clazz.getAbsoluteFile() + ".prettyprint");

            ClassFile cc = new ClassFile(clazz);
            System.out.println(cc.getAccessFlags().isPublic());
            System.out.println(cc.getAccessFlags().isAbstract());
            cc.getConstantPool().set(1, new Constant(2, Tag.STRING, Bytes.getShort((short) 36)));
            Bytes.writeBytesToFile(cc.assemble(), clazz.getAbsoluteFile() + ".test");

            System.out.println("V bytes: " + Bytes.bytesToString(Bytes.read(clazz)));
            System.out.println("M bytes: " + Bytes.bytesToString(new ClassFile(clazz).assemble()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
