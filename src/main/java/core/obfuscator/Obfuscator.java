package core.obfuscator;

import core.disassembler.Disassembler;
import net.sf.jbl.generation.Branch;
import net.sf.jbl.generation.CodeGenerator;
import net.sf.jbl.generation.Instruction;
import net.sf.jbl.introspection.ClassFile;
import net.sf.jbl.introspection.Member;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.members.TryCatch;
import net.sf.jbl.introspection.members.attributes.Code;
import net.sf.jbl.introspection.metadata.SignatureReader;
import net.sf.jbl.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Obfuscator {
    private static Random rnd = new Random();

    public static void main(String[] args) {
        File clazz = new File(args.length > 1 ? args[0] : System.getenv("USERPROFILE") + "/Desktop/PhantomTest.class");
        for (int z = 0; z != 1; z++)
            try {
                Disassembler.main(args);
                long start;
                start = System.currentTimeMillis();

                ClassFile cc = new ClassFile(clazz);

                Pool<Member> mp = cc.getMethods();
                Pool<Constant> cpool = cc.getConstants();

                //Class obfuscation
                {
                    cc.removeMetadata("SourceFile");
                }


                //Method obfuscation
                {
                    HashMap<String, HashSet<String>> overloads = new HashMap<String, HashSet<String>>();

                    _outer:
                    for (Member c : mp) {

                       // if (c.getName().equals("main")) continue;

                        String name = c.getName();

                        System.out.println("Handling method " + name);

                        // attributes.removeMetadata("LocalVariableTable");
                        //  attributes.removeMetadata("Deprecated");
                        //  attributes.removeMetadata("Exceptions");

                        Code code = (Code) c.getMetadata("Code");
                        Pool<TryCatch> epool = code.getExceptionPool();

                        CodeGenerator gen = new CodeGenerator(code, c);

                        //Indirect ifs.
                        {
                            for (int i = 0; i != gen.instructions.size(); i++) {
                                Instruction inc = gen.instructions.get(i);
                              /*  if (inc.getOpcode() != GOTO && Groups.IFS.contains(inc.getOpcode())) {
                                    short loc = (byte) ((inc.getAddress() + Bytes.toShort(inc.getArguments(), 0)));
                                    loc -= gen.size();
                                    byte[] back = Bytes.toByteArray(loc);
                                    gen.inject(
                                            END,
                                            (byte) GOTO,
                                            back[0],
                                            back[1]
                                    );   */

                                    ((Branch) inc).setTarget(gen.size() - inc.getAddress() - 3);
                                }
                            }


                        { //Branch to a JSR instruction at end of method
                            byte[] jumpTo = Bytes.toByteArray((short) (gen.size()));
                            //Branch back to the POP instruction
                            byte[] jumpBack = Bytes.toByteArray((short) -(gen.size()));
                            /*gen.inject(START,
                                    (byte) GOTO,
                                    jumpTo[0],
                                    jumpTo[1]
                            );
                            gen.inject(END,
                                    (byte) GOTO,
                                    jumpBack[0],
                                    jumpBack[1],
                                    (byte) ATHROW
                            ); */
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
                    Pool<Member> fieldPool = cc.getFields();
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
                Disassembler.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
