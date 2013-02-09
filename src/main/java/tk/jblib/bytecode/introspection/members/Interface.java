package tk.jblib.bytecode.introspection.members;

import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.introspection.Opcode.TAG_UTF_STRING;

public class Interface {

    private Constant classReference;

    /**
     * Constructs an interface from the given constant.
     *
     * @param value the data constant.
     */
    public Interface(Constant value) {
        classReference = value;
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public Interface() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        return Bytes.toByteArray((short) classReference.getIndex());
    }

    /**
     * Gets the class reference that this interface references.
     *
     * @return the class.
     */
    public String getClassReference() {
        return classReference.stringValue();
    }

    /**
     * Sets the class reference that this interface references.
     *
     * @param newRef the new reference.
     */
    public void setClassReference(String newRef) {
        classReference.getOwner().set(classReference.getIndex(), (classReference = new Constant(TAG_UTF_STRING, newRef.getBytes())));
    }
}
