package profiler.AgentLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

public class AgentUtils {
    
    /**
     * Instantiates a utils class.
     */

    public AgentUtils() {

    }
    
    /**
     * Gets the current JVM PID.
     * 
     * @return Returns the PID.
     * @throws Exception
     */

    public String getCurrentPID() {
	String jvm = ManagementFactory.getRuntimeMXBean().getName();
	String pid = jvm.substring(0, jvm.indexOf('@'));
	return pid;
    }
    
    public byte[] getBytesFromIS(InputStream stream) throws IOException {

  	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

  	int nRead;
  	byte[] data = new byte[65536]; // Have large buffer for fast speeds

  	while ((nRead = stream.read(data, 0, data.length)) != -1) {
  	    buffer.write(data, 0, nRead);
  	}

  	buffer.flush();

  	return buffer.toByteArray();

      }

      /**
       * Gets bytes from class
       * 
       * @param clazz
       *            The class.
       * @return Returns a byte[] representation of given class.
       * @throws IOException
       */

      public byte[] getBytesFromClass(Class<?> clazz) throws IOException {
  	return getBytesFromIS(clazz.getClassLoader().getResourceAsStream(
  		clazz.getName().replace('.', '/') + ".class"));
      }

      /**
       * Gets bytes from resource
       * 
       * @param resource
       *            The resource string.
       * @return Returns a byte[] representation of given resource.
       * @throws IOException
       * 
       */

      public byte[] getBytesFromResource(ClassLoader clazzLoader,
  	    String resource) throws IOException {
  	return getBytesFromIS(clazzLoader.getResourceAsStream(resource)); // Simple
  									  // wrapper
      }

      /**
       * Adds a a path to the current java.library.path.
       * 
       * @param path
       *            The path.
       * @throws SecurityException
       * @throws NoSuchFieldException
       * @throws IllegalAccessException
       * @throws IllegalArgumentException
       */

      public void addToLibPath(String path) throws NoSuchFieldException,
  	    SecurityException, IllegalArgumentException, IllegalAccessException {
  	if (System.getProperty("java.library.path") != null) {
  	    // If java.library.path is not empty, we will prepend our path
  	    // Note that path.separator is ; on Windows and : on *nix,
  	    // so we can't hard code it.
  	    System.setProperty("java.library.path",
  		    path + System.getProperty("path.separator")
  			    + System.getProperty("java.library.path"));
  	} else {
	    System.setProperty("java.library.path", path);
  	}

  	// Important: java.library.path is cached
  	// We will be using reflection to clear the cache

  	Field fieldSysPath = ClassLoader.class
  		.getDeclaredField("sys_paths");
  	fieldSysPath.setAccessible(true);
  	fieldSysPath.set(null, null);

      }

      /**
       * Extracts a resource to specified path.
       * 
       * @param loader
       *            A
       * @param resourceName
       * @param targetName
       * @param targetDir
       * @throws IOException
       */

      public void extractResourceToDirectory(ClassLoader loader,
  	    String resourceName, String targetName, String targetDir)
  	    throws IOException {
  	InputStream source = loader.getResourceAsStream(resourceName);
  	File tmpdir = new File(targetDir);
  	File target = new File(tmpdir, targetName);
  	target.createNewFile();

  	FileOutputStream stream = new FileOutputStream(target);
  	byte[] buf = new byte[65536];
  	int read;
  	while ((read = source.read(buf)) != -1)
  	    stream.write(buf, 0, read);
  	stream.close();
  	source.close();
      }
      
      /**
       * Attempts to load an attach library.
       */
      
      public void loadAgentLibrary() {
	 System.loadLibrary("attach"); 
      }
    
}
