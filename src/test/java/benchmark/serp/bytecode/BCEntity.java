package benchmark.serp.bytecode;

import benchmark.serp.bytecode.lowlevel.*;

/**
 * Interface implemented by all bytecode entities. Entities must be able
 * to access the project, constant pool, and class loader of the current class.
 *
 * @author Abe White
 */
public interface BCEntity {
    /**
     * Return the project of the current class.
     */
    public Project getProject();

    /**
     * Return the constant pool of the current class.
     */
    public ConstantPool getPool();

    /**
     * Return the class loader to use when loading related classes.
     */
    public ClassLoader getClassLoader();

    /**
     * Return false if this entity has been removed from its parent; in this
     * case the results of any operations on the entity are undefined.
     */
    public boolean isValid();
}
