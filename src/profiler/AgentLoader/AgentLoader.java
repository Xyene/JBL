package profiler.AgentLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class AgentLoader extends AgentUtils {

    /**
     * Instantiates a agent loader.
     */

    public AgentLoader() {

    }

    /**
     * Loads an agent into a JVM.
     * 
     * @param agent
     *            The main agent class.
     * @param resources
     *            Array of classes to be included with agent.
     * @param JVMPid
     *            The ID of the target JVM.
     * @throws IOException
     * @throws AttachNotSupportedException
     * @throws AgentLoadException
     * @throws AgentInitializationException
     */

    public void attachAgentToJVM(Class<?> agent,
	    Class<?>[] resources, String JVMPid) throws IOException,
	    AttachNotSupportedException, AgentLoadException,
	    AgentInitializationException {

	VirtualMachine vm = VirtualMachine.attach(JVMPid);
	vm.loadAgent(generateAgentJar(agent, resources).getAbsolutePath());
	vm.detach();

    }

    /**
     * Generates a temporary agent file to be loaded.
     * 
     * @param agent
     *            The main agent class.
     * @param resources
     *            Array of classes to be included with agent.
     * @return Returns a temporary jar file with the specified classes included.
     * @throws FileNotFoundException
     * @throws IOException
     */

    public File generateAgentJar(Class<?> agent,
	    Class<?>[] resources) throws FileNotFoundException, IOException {

	// Create temp file
	final File jarFile = File.createTempFile("agent", ".jar");
	jarFile.deleteOnExit();

	final Manifest manifest = new Manifest();
	final Attributes mainAttributes = manifest.getMainAttributes();
	// Create manifest stating that agent is allowed to modify
	mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
	mainAttributes.put(new Attributes.Name("Agent-Class"),
		agent.getName());
	mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"),
		"true");
	mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"),
		"true");

	final JarOutputStream jos = new JarOutputStream(
		new FileOutputStream(
			jarFile), manifest);

	jos.putNextEntry(new JarEntry(agent.getName().replace(
		'.',
		'/')
		+ ".class"));

	jos.write(getBytesFromIS(agent.getClassLoader()
		.getResourceAsStream(
			agent.getName().replace('.', '/') + ".class")));
	jos.closeEntry();

	for (Class<?> clazz : resources) {
	    jos.putNextEntry(new JarEntry(clazz.getName().replace(
		    '.',
		    '/')
		    + ".class"));

	    jos.write(getBytesFromIS(clazz.getClassLoader()
		    .getResourceAsStream(
			    clazz.getName().replace('.', '/') + ".class")));
	    jos.closeEntry();
	}

	jos.close();

	return jarFile;

    }

}
