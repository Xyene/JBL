package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;
import benchmark.serp.util.*;

/**
 * A field of a class.
 *
 * @author Abe White
 */
public class BCField extends BCMember implements VisitAcceptor {
    BCField(BCClass owner) {
        super(owner);
        if (owner.isEnum())
            setEnum(true);
    }

    /**
     * Manipulate the field access flags.
     */
    public boolean isVolatile() {
        return (getAccessFlags() & Constants.ACCESS_VOLATILE) > 0;
    }

    /**
     * Manipulate the field access flags.
     */
    public void setVolatile(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_VOLATILE);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_VOLATILE);
    }

    /**
     * Manipulate the field access flags.
     */
    public boolean isTransient() {
        return (getAccessFlags() & Constants.ACCESS_TRANSIENT) > 0;
    }

    /**
     * Manipulate the field access flags.
     */
    public void setTransient(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_TRANSIENT);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_TRANSIENT);
    }

    /**
     * Manipulate the field access flags. Defaults to true for fields added
     * to enum classes.
     */
    public boolean isEnum() {
        return (getAccessFlags() & Constants.ACCESS_ENUM) > 0;
    }

    /**
     * Manipulate the field access flags. Defaults to true for fields added
     * to enum classes.
     */
    public void setEnum(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_ENUM);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_ENUM);
    }

    /**
     * Return the name of the type of this field. The name will be given in
     * a form suitable for a {@link Class#forName} call.
     *
     * @see BCMember#getDescriptor
     */
    public String getTypeName() {
        return getProject().getNameCache().getExternalForm
            (getDescriptor(), false);
    }

    /**
     * Return the {@link Class} object for the type of this field.
     */
    public Class getType() {
        return Strings.toClass(getTypeName(), getClassLoader());
    }

    /**
     * Return the bytecode for the type of this field.
     */
    public BCClass getTypeBC() {
        return getProject().loadClass(getTypeName(), getClassLoader());
    }

    /**
     * Set the name of the type of this field.
     *
     * @see BCMember#setDescriptor
     */
    public void setType(String type) {
        setDescriptor(type);
    }

    /**
     * Set the type of this field.
     *
     * @see BCMember#setDescriptor
     */
    public void setType(Class type) {
        setType(type.getName());
    }

    /**
     * Set the type of this field.
     *
     * @see BCMember#setDescriptor
     */
    public void setType(BCClass type) {
        setType(type.getName());
    }

    /**
     * Return the constant value information for the field.
     * Acts internally through the {@link Attributes} interface.
     *
     * @param add if true, a new constant value attribute will be added
     * if not already present
     * @return the constant value information, or null if none and the
     * <code>add</code> param is set to false
     */
    public ConstantValue getConstantValue(boolean add) {
        ConstantValue constant = (ConstantValue) getAttribute
            (Constants.ATTR_CONST);
        if (!add || (constant != null))
            return constant;
        if (constant == null)
            constant = (ConstantValue) addAttribute(Constants.ATTR_CONST);
        return constant;
    }

    /**
     * Remove the constant value attribute for the field.
     * Acts internally through the {@link Attributes} interface.
     *
     * @return true if there was a value to remove
     */
    public boolean removeConstantValue() {
        return removeAttribute(Constants.ATTR_CONST);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterBCField(this);
        visitAttributes(visit);
        visit.exitBCField(this);
    }

    void initialize(String name, String descriptor) {
        super.initialize(name, descriptor);
        makePrivate();
    }
}
