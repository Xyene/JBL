package benchmark.serp.bytecode;

/**
 * Class loader that will attempt to find requested classes in a given
 * {@link Project}.
 *
 * @author Abe White
 */
public class BCClassLoader extends ClassLoader {
    private Project _project = null;

    /**
     * Constructor. Supply the project to use when looking for classes.
     */
    public BCClassLoader(Project project) {
        _project = project;
    }

    /**
     * Constructor. Supply the project to use when looking for classes.
     *
     * @param parent the parent classoader
     */
    public BCClassLoader(Project project, ClassLoader loader) {
        super(loader);
        _project = project;
    }

    /**
     * Return this class loader's project.
     */
    public Project getProject() {
        return _project;
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        byte[] bytes;
        try {
            BCClass type;
            if (!_project.containsClass(name))
                type = createClass(name);
            else
                type = _project.loadClass(name);
            if (type == null)
                throw new ClassNotFoundException(name);
            bytes = type.toByteArray();
        } catch (RuntimeException re) {
            throw new ClassNotFoundException(re.toString());
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * Override this method if unfound classes should be created on-the-fly.
     * Returns null by default.
     */
    protected BCClass createClass(String name) {
        return null;
    }
}
