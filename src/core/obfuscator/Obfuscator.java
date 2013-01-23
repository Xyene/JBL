package core.obfuscator;

import com.github.Icyene.bytecode.generation.CodeGenerator;
import com.github.Icyene.bytecode.generation.Groups;
import com.github.Icyene.bytecode.generation.Instruction;
import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.internal.code.ExceptionPool;
import com.github.Icyene.bytecode.introspection.internal.pools.MemberPool;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class Obfuscator {
    private static Random rnd = new Random();

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        for (int i = 0; i != 10; i++)
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

                  /*  //Indirect ifs.
                    {
                        for (int i = 0; i != gen.instructions.size(); i++) {
                            Instruction inc = gen.instructions.get(i);
                            if (inc.getOpcode() != GOTO && Groups.IFS.contains(inc.getOpcode())) {
                                short loc = (byte) ((inc.getAddress() + Bytes.toShort(inc.getArguments(), 0)));
                                loc -= gen.raw.length;
                                byte[] back = Bytes.toByteArray(loc);
                                gen.inject(
                                        gen.raw.length,
                                        (byte) GOTO,
                                        back[0],
                                        back[1]
                                );

                                byte[] to = Bytes.toByteArray((short) ((gen.raw.length - inc.getAddress()) - 3));
                                gen.raw[inc.getAddress() + 1] = to[0];
                                gen.raw[inc.getAddress() + 2] = to[1];
                            }
                        }
                    }


                    { //Branch to a JSR instruction at end of method
                        byte[] jumpTo = Bytes.toByteArray((short) (gen.raw.length+3));
                        //Branch back to the POP instruction
                        byte[] jumpBack = Bytes.toByteArray((short) -(gen.raw.length));
                        gen.inject(0,
                                (byte) GOTO,
                                jumpTo[0],
                                jumpTo[1]
                        ).inject(gen.raw.length,
                                (byte) GOTO,
                                jumpBack[0],
                                jumpBack[1],
                                (byte) ATHROW
                        );
                        //Manufacture exception block to protect indirection
                        epool.add(new TryCatch(0, gen.raw.length - 4, gen.raw.length - 1));
                    }          */

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
