package core.obfuscator;

import tk.jblib.bytecode.generation.CodeGenerator;
import tk.jblib.bytecode.introspection.ClassFile;
import tk.jblib.bytecode.introspection.Member;
import tk.jblib.bytecode.introspection.members.TryCatch;
import tk.jblib.bytecode.introspection.members.attributes.Code;
import tk.jblib.bytecode.introspection.metadata.readers.SignatureReader;
import tk.jblib.bytecode.introspection.pools.AttributePool;
import tk.jblib.bytecode.introspection.pools.ConstantPool;
import tk.jblib.bytecode.introspection.code.ExceptionPool;
import tk.jblib.bytecode.introspection.pools.MemberPool;
import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.generation.instructions.CodePoint.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static tk.jblib.bytecode.introspection.metadata.Opcode.*;

public class Obfuscator {
    private static Random rnd = new Random();

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        for (int z = 0; z != 1; z++)
            try {
                long start;
                start = System.currentTimeMillis();

                ClassFile cc = new ClassFile(clazz);
                System.out.println(cc.getConstantPool());
                System.out.println(Bytes.bytesToString(Bytes.read(clazz)));

                MemberPool mp = cc.getMethodPool();
                ConstantPool cpool = cc.getConstantPool();

                //Class obfuscation
                {
                    AttributePool apool = cc.getAttributePool();
                    apool.removeInstancesOf("SourceFile");
                }


                //Method obfuscation
                {
                    HashMap<String, HashSet<String>> overloads = new HashMap<String, HashSet<String>>();

                    _outer:
                    for (Member c : mp) {

                        String name = c.getName();

                        System.out.println("Handling method " + name);

                        AttributePool attributes = c.getAttributePool();
                        // attributes.removeInstancesOf("LocalVariableTable");
                        //  attributes.removeInstancesOf("Deprecated");
                        //  attributes.removeInstancesOf("Exceptions");

                        Code code = (Code) attributes.getInstancesOf("Code").get(0);
                        ExceptionPool epool = code.getExceptionPool();

                        CodeGenerator gen = new CodeGenerator(code, c);

                 /*   //Indirect ifs.
                    {
                        for (int i = 0; i != gen.instructions.size(); i++) {
                            Instruction inc = gen.instructions.get(i);
                            if (inc.getOpcode() != GOTO && Groups.IFS.contains(inc.getOpcode())) {
                                short loc = (byte) ((inc.getAddress() + Bytes.toShort(inc.getArguments(), 0)));
                                loc -= gen.size();
                                byte[] back = Bytes.toByteArray(loc);
                                gen.inject(
                                        START,
                                        (byte) GOTO,
                                        back[0],
                                        back[1]
                                );

                            //    byte[] to = Bytes.toByteArray((short) ((gen.size() - inc.getAddress()) - 3));
                                ((Branch)inc).setTarget(gen.size() - inc.getAddress() - 3);
                             //   gen.raw[inc.getAddress() + 1] = to[0];
                               // gen.raw[inc.getAddress() + 2] = to[1];
                            }
                        }
                    }        */


                        { //Branch to a JSR instruction at end of method
                            byte[] jumpTo = Bytes.toByteArray((short) (gen.size() + 3));
                            //Branch back to the POP instruction
                            byte[] jumpBack = Bytes.toByteArray((short) -(gen.size()));
                            gen.inject(START,
                                    (byte) GOTO,
                                    jumpTo[0],
                                    jumpTo[1]
                            );
                            gen.inject(END,
                                    (byte) GOTO,
                                    jumpBack[0],
                                    jumpBack[1],
                                    (byte) ATHROW
                            );
                            //Manufacture exception block to protect indirection
                            epool.add(new TryCatch(0, gen.size() - 4, gen.size() - 1));
                        }

                        code.setMaxStack(gen.computeStackSize());
                        code.setCodePool(gen.synthesize());

                        if (name.equals("<init>") || name.equals("<clinit>") || name.equals("main"))
                            continue;

                        final String descriptor = new SignatureReader(c).getRawAugmentingTypes();

                        for (Map.Entry<String, HashSet<String>> en : overloads.entrySet()) {
                            if (!en.getValue().contains(descriptor)) {
                                String key = en.getKey();
                                c.setName(key);
                                overloads.get(key).add(descriptor);
                                continue _outer;
                            }
                        }

                        String obfuscatedName = "";
                        while (true) {
                            obfuscatedName = Obfuscation.randomString(new Random().nextInt(1) + 1);
                            if (!overloads.containsKey(obfuscatedName))
                                break;
                        }
                        c.setName(obfuscatedName);
                        overloads.put(obfuscatedName, new HashSet<String>() {{
                            add(descriptor);
                        }});
                    }
                }

                {
                    Set<String> overloads = new HashSet<String>();
                    MemberPool fieldPool = cc.getFieldPool();
                    for (Member f : fieldPool) {
                        String obfuscatedName = "";
                        while (true) {
                            obfuscatedName = Obfuscation.randomString(new Random().nextInt(1) + 1);
                            if (!overloads.contains(obfuscatedName))
                                break;
                        }
                        f.setName(obfuscatedName);
                        overloads.add(obfuscatedName);
                    }
                }

                //   System.out.println(Bytes.bytesToString(cc.getBytes()));
                System.out.println(System.currentTimeMillis() - start + "ms");
                Bytes.writeBytesToFile(cc.getBytes(), clazz.getAbsolutePath() + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
