package core.obfuscator;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.internal.pools.ExceptionPool;
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
        try {
            long start;
            start = System.currentTimeMillis();

            ClassFile cc = new ClassFile(clazz);
            //  System.out.println(cc.getConstantPool());
            // System.out.println(Bytes.bytesToString(Bytes.read(clazz)));

            MemberPool<Member> mp = cc.getMethodPool();
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

                    if (name.equals("main"))
                        continue;

                    System.out.println("Handling method " + name);

                    AttributePool attributes = c.getAttributePool();
                    attributes.removeInstancesOf("LocalVariableTable");
                    attributes.removeInstancesOf("LineNumberTable");
                    attributes.removeInstancesOf("Deprecated");
                    attributes.removeInstancesOf("Exceptions");

                    Code code = (Code) attributes.getInstancesOf(Code.class).get(0);
                    ExceptionPool epool = code.getExceptionPool();

                    byte[] opcodes = code.getCodePool();
                    CodeGenerator gen = new CodeGenerator(opcodes, 0);

                    // if(new Random().nextInt(5) < 50) continue;

                    //GOTO a JSR after RETURN, which goes to a POP after the GOTO, removing the return address


                    { //Jump to a JSR instruction at end of method
                        byte[] jumpToJSR = Bytes.toByteArray((short) (gen.raw.length + 4));
                        //Jump back to the POP instruction
                        byte[] jumpBack = Bytes.toByteArray((short) -(gen.raw.length - 3));
                        gen.inject(0,
                                (byte) GOTO,
                                jumpToJSR[0],
                                jumpToJSR[1],
                                (byte) POP
                        ).inject(gen.raw.length,
                                (byte) JSR,
                                jumpBack[0],
                                jumpBack[1],
                                (byte) ATHROW //Allow any uncaught exception to propagate
                        );
                        //Manufacture exception block to protect indirection
                        epool.add(new TryCatch(0, gen.raw.length - 4, gen.raw.length - 1));
                        code.setMaxStack(code.getMaxStack() + 1);
                    }

                    code.setCodePool(gen.synthesize());

                    if (new Random().nextInt(5) < 50) continue;  //TODO remove for renaming

                    System.out.println("Handling " + name + ":" + c.getDescriptor());
                    if (name.equals("<init>") || name.equals("<clinit>") || name.equals("main"))
                        continue;


                    final String descriptor = new SignatureReader(c).getRawAugmentingTypes();

                    for (Map.Entry<String, HashSet<String>> en : overloads.entrySet()) {
                        System.out.println("Checking for potential overloads");
                        if (!en.getValue().contains(descriptor)) {
                            String key = en.getKey();
                            c.setName(key);
                            overloads.get(key).add(descriptor);
                            continue _outer;
                        }
                    }

                    String obfuscatedName = "";
                    while (true) {
                        System.out.println("Generating random name");
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
                MemberPool<Member> fieldPool = cc.getFieldPool();
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
