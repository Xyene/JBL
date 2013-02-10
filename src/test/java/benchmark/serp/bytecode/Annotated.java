package benchmark.serp.bytecode;

/**
 * An annotated entity.
 *
 * @author Abe White
 */
public abstract class Annotated extends Attributes {
    /**
     * Return runtime <b>invisible</b> annotation information for the entity.
     * Acts internally through the {@link Attributes} interface.
     *
     * @param add if true, a new annotations attribute will be added if not 
     * already present
     * @return the annotation information, or null if none and the
     * <code>add</code> param is set to false
     */
    public Annotations getDeclaredAnnotations(boolean add) {
        Annotations ann = (Annotations) getAttribute
            (Constants.ATTR_ANNOTATIONS);
        if (!add || ann != null)
            return ann;
        ensureBytecodeVersion();
        return (Annotations) addAttribute(Constants.ATTR_ANNOTATIONS);
    }

    /**
     * Remove the runtime <b>invisible</b> annotations attribute for the entity.
     * Acts internally through the {@link Attributes} interface.
     *
     * @return true if there was an attribute to remove
     */
    public boolean removeDeclaredAnnotations() {
        return removeAttribute(Constants.ATTR_ANNOTATIONS);
    }

    /**
     * Return runtime visible annotation information for the entity.
     * Acts internally through the {@link Attributes} interface.
     *
     * @param add if true, a new runtime annotations attribute will be
     * added if not already present
     * @return the annotation information, or null if none and the
     * <code>add</code> param is set to false
     */
    public Annotations getDeclaredRuntimeAnnotations(boolean add) {
        Annotations ann = (Annotations) getAttribute
            (Constants.ATTR_RUNTIME_ANNOTATIONS);
        if (!add || ann != null)
            return ann;
        ensureBytecodeVersion();
        return (Annotations) addAttribute(Constants.ATTR_RUNTIME_ANNOTATIONS);
    }

    /**
     * Remove the runtime visible annotations attribute for the entity.
     * Acts internally through the {@link Attributes} interface.
     *
     * @return true if there was an attribute to remove
     */
    public boolean removeDeclaredRuntimeAnnotations() {
        return removeAttribute(Constants.ATTR_RUNTIME_ANNOTATIONS);
    }

    /**
     * When adding annotations, make sure the bytecode spec supports them.
     */
    private void ensureBytecodeVersion() {
        BCClass bc = getBCClass();
        if (bc.getMajorVersion() < Constants.MAJOR_VERSION_JAVA5) {
            bc.setMajorVersion(Constants.MAJOR_VERSION_JAVA5);
            bc.setMinorVersion(Constants.MINOR_VERSION_JAVA5);
        }
    }
 
    /**
     * Internal access to the owning class.
     */
    abstract BCClass getBCClass();
}
