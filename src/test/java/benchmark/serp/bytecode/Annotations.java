package benchmark.serp.bytecode;

import java.io.*;
import java.util.*;

import benchmark.serp.bytecode.visitor.*;

/**
 * Java annotation out.
 *
 * @author Abe White
 */
public class Annotations extends Attribute {
    private final List _annotations = new ArrayList();

    Annotations(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    /**
     * Whether these annotations are runtime-visible.
     */
    public boolean isRuntime() {
        return getName().equals(Constants.ATTR_RUNTIME_ANNOTATIONS);
    }

    /**
     * All declared annotations.
     */
    public Annotation[] getAnnotations() {
        return (Annotation[]) _annotations.toArray
            (new Annotation[_annotations.size()]);
    }

    /**
     * Set the annotations.  This method is useful when
     * importing annotations from another instance.
     */
    public void setAnnotations(Annotation[] annos) {
        clear();
        if (annos != null)
            for (int i = 0; i < annos.length; i++)
                addAnnotation(annos[i]);
    }

    /**
     * Return the annotation of the given type, or null if none.
     */
    public Annotation getAnnotation(Class type) {
        return (type == null) ? null : getAnnotation(type.getName());
    }

    /**
     * Return the annotation of the given type, or null if none.
     */
    public Annotation getAnnotation(BCClass type) {
        return (type == null) ? null : getAnnotation(type.getName());
    }

    /**
     * Return the annotation of the given type, or null if none.
     */
    public Annotation getAnnotation(String type) {
        Annotation anno;
        for (int i = 0; i < _annotations.size(); i++) {
            anno = (Annotation) _annotations.get(i);
            if (anno.getTypeName().equals(type))
                return anno;
        }
        return null;
    }

    /**
     * Import an annotation from another instance.
     *
     * @return the newly added annotation
     */
    public Annotation addAnnotation(Annotation an) {
        Annotation anno = addAnnotation(an.getTypeName());
        anno.setProperties(an.getProperties());
        return anno;
    }

    /**
     * Add a new annotation.
     */
    public Annotation addAnnotation(Class type) {
        return addAnnotation(type.getName());
    }

    /**
     * Add a new annotation.
     */
    public Annotation addAnnotation(BCClass type) {
        return addAnnotation(type.getName());
    }

    /**
     * Add a new annotation.
     */
    public Annotation addAnnotation(String type) {
        Annotation anno = new Annotation(this);
        anno.setType(type);
        _annotations.add(anno);
        return anno;
    }

    /**
     * Remove all annotations.
     */
    public void clear() {
        for (int i = 0; i < _annotations.size(); i++)
            ((Annotation) _annotations.get(i)).invalidate();
        _annotations.clear();
    }

    /**
     * Remove the given annotation.
     *
     * @return true if an annotation was removed, false otherwise
     */
    public boolean removeAnnotation(Annotation anno) {
        return anno != null && removeAnnotation(anno.getTypeName());
    }

    /**
     * Remove the annotation of the given type.   
     *
     * @return true if an annotation was removed, false otherwise
     */
    public boolean removeAnnotation(Class type) {
        return type != null && removeAnnotation(type.getName());
    }

    /**
     * Remove the annotation of the given type.   
     *
     * @return true if an annotation was removed, false otherwise
     */
    public boolean removeAnnotation(BCClass type) {
        return type != null && removeAnnotation(type.getName());
    }

    /**
     * Remove the annotation of the given type.   
     *
     * @return true if an annotation was removed, false otherwise
     */
    public boolean removeAnnotation(String type) {
        if (type == null)
            return false;
        Annotation anno;
        for (int i = 0; i < _annotations.size(); i++) {
            anno = (Annotation) _annotations.get(i);
            if (anno.getTypeName().equals(type)) {
                anno.invalidate();
                _annotations.remove(i);
                return true;
            }
        }
        return false;
    }

    int getLength() {
        int len = 2;
        for (int i = 0; i < _annotations.size(); i++)
            len += ((Annotation) _annotations.get(i)).getLength();
        return len;
    }

    void read(Attribute other) {
        setAnnotations(((Annotations) other).getAnnotations());
    }

    void read(DataInput in, int length) throws IOException {
        _annotations.clear();
        int annos = in.readUnsignedShort();
        Annotation anno;
        for (int i = 0; i < annos; i++) {
            anno = new Annotation(this);
            anno.read(in);
            _annotations.add(anno);
        }
    }

    void write(DataOutput out, int length) throws IOException {
        out.writeShort(_annotations.size());
        for (int i = 0; i < _annotations.size(); i++)
            ((Annotation) _annotations.get(i)).write(out);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterAnnotations(this);
        for (int i = 0; i < _annotations.size(); i++)
            ((Annotation) _annotations.get(i)).acceptVisit(visit);
        visit.exitAnnotations(this);
    }
}
