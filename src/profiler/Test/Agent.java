package profiler.Test;

import com.github.Icyene.bytecode.disassembler.internal.ClassFile;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import profiler.AgentLoader.AgentLoader;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;
import profiler.asm.*;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Primary class used by tests to allow for method profiling information. Client
 * code will interact with this method only. This class is NOT Thread safe. But
 * can be made so if required. This code is licensed under the WTFPL.
 *
 * @author Icyene
 */
public class Agent implements ClassFileTransformer {

    private static Instrumentation instrumentation = null;
    private static Agent transformer;
    private static AgentLoader agent;

    public static void agentmain(String string, Instrumentation instrument) {

	System.out.println("Agent loaded!");

	// initialization code:
	transformer = new Agent();
	instrumentation = instrument;
	instrumentation.addTransformer(transformer);
	// to instrument, first revert all added bytecode:
	// call retransformClasses() on all modifiable classes...

	agent = new AgentLoader();

	try {

	    instrumentation.redefineClasses(new ClassDefinition(Test.class,
		    agent.getBytesFromClass(ClassFile.class)));
        instrumentation.redefineClasses(new ClassDefinition(Test.class,
                agent.getBytesFromClass(ConstantPool.class)));
        instrumentation.redefineClasses(new ClassDefinition(Test.class,
                agent.getBytesFromClass(ByteStream.class)));
        instrumentation.redefineClasses(new ClassDefinition(Test.class,
                agent.getBytesFromClass(Bytes.class)));

	} catch (Exception e) {
	    System.out.println("Failed to redefine class!");
	}

    };

    /**
     * Kills this agent
     */

    public static void killAgent() {
	instrumentation.removeTransformer(transformer);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className,
	    Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
	    byte[] classfileBuffer) throws IllegalClassFormatException {

	System.out.println("Instrumenting class: " + className);

	// We can only profile classes that we can see. If a class uses a custom
	// classloader we will nto be able to see it and crash if we try to
	// profile it.
	if (loader != ClassLoader.getSystemClassLoader()) {
	    return classfileBuffer;
	}

	// Don't profile yourself, otherwise you'll stackoverflow.

	if (className.startsWith("profiler/Test/Agen")) {
	   return classfileBuffer;
	}

	byte[] result = classfileBuffer;
	try {
	    // Create class reader from buffer
	    ClassReader reader = new ClassReader(classfileBuffer);
	    // Make writer
	    ClassWriter writer = new ClassWriter(true);
	    ClassAdapter profiler = new ProfileClassAdapter(writer, className);
	    // Add the class adapter as a modifier
	    reader.accept(profiler, true);
	    result = writer.toByteArray();
	    System.out.println("Returning reinstrumented class: " + className);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    // The class adapter
    public class ProfileClassAdapter extends ClassAdapter {

	private String className;

	public ProfileClassAdapter(ClassVisitor visitor, String theClass) {
	    super(visitor);
	    this.className = theClass;
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
		String signature, String[] exceptions) {

	    MethodVisitor mv = super.visitMethod(access,
		    name,
		    desc,
		    signature,
		    exceptions);

	    return new ProfileMethodAdapter(mv, className, name);
	}

    }

    // The method adapter
    public class ProfileMethodAdapter extends MethodAdapter {
	private String _className, _methodName;

	public ProfileMethodAdapter(MethodVisitor visitor,
		String className,
		String methodName) {
	    super(visitor);
	    _className = className;
	    _methodName = methodName;
	  //  System.out.println("Profiled " + methodName + " in class "
		 //   + className
		 //   + ".");
	}

	public void visitCode() {
	    // Push values into stack, then invoke the profile function
	    this.visitLdcInsn(_className);
	    this.visitLdcInsn(_methodName);
	    this.visitMethodInsn(Opcodes.INVOKESTATIC, // Change to INVOKEDYNAMIC if
					       // called method is not static
                "profiler/Test/Agent$Profile",
		    "start",
		    "(Ljava/lang/String;Ljava/lang/String;)V"); // Start accepts
								// two strings;
								// call it so
	    super.visitCode();
	}

	public void visitInsn(int inst) {
	    switch (inst) {
	    // Match all return codes
	    case Opcodes.ARETURN:
	    case Opcodes.DRETURN:
	    case Opcodes.FRETURN:
	    case Opcodes.IRETURN:
	    case Opcodes.LRETURN:
	    case Opcodes.RETURN:
	    case Opcodes.ATHROW:
		this.visitLdcInsn(_className);
		this.visitLdcInsn(_methodName);
		this.visitMethodInsn(Opcodes.INVOKESTATIC,
                "profiler/Test/Agent$Profile",
			"end",
			"(Ljava/lang/String;Ljava/lang/String;)V");

		break;
	    default:
		break;
	    }

	    // Visit the actual function
	    super.visitInsn(inst);
	}

    }

    // Base profiling class
    public static class Profile {

	public static void start(String className, String methodName) {
	    System.out.println(className + "\t" + methodName + "\tstart\t"
		    + System.currentTimeMillis());
	}

	public static void end(String className, String methodName) {
	    System.out.println(className + "\t" + methodName + "\tend\t"
		    + System.currentTimeMillis());
	}
    }

}
