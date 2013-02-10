package benchmark.serp.bytecode;

import java.util.*;

import benchmark.serp.bytecode.lowlevel.*;

/**
 * State implementing the behavior of an object type.
 *
 * @author Abe White
 */
class ObjectState extends State {
    private final ConstantPool _pool = new ConstantPool();
    private final NameCache _names;
    private int _index = 0;
    private int _superclassIndex = 0;
    private int _magic = Constants.VALID_MAGIC;
    private int _major = Constants.MAJOR_VERSION;
    private int _minor = Constants.MINOR_VERSION;
    private int _access = Constants.ACCESS_PUBLIC | Constants.ACCESS_SUPER;
    private final List _interfaces = new ArrayList();
    private final List _fields = new ArrayList();
    private final List _methods = new ArrayList();
    private final List _attributes = new ArrayList();

    public ObjectState(NameCache names) {
        _names = names;
    }

    public int getMagic() {
        return _magic;
    }

    public void setMagic(int magic) {
        _magic = magic;
    }

    public int getMajorVersion() {
        return _major;
    }

    public void setMajorVersion(int major) {
        _major = major;
    }

    public int getMinorVersion() {
        return _minor;
    }

    public void setMinorVersion(int minor) {
        _minor = minor;
    }

    public int getAccessFlags() {
        return _access;
    }

    public void setAccessFlags(int access) {
        _access = access;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int index) {
        _index = index;
    }

    public int getSuperclassIndex() {
        return _superclassIndex;
    }

    public void setSuperclassIndex(int index) {
        _superclassIndex = index;
    }

    public List getInterfacesHolder() {
        return _interfaces;
    }

    public List getFieldsHolder() {
        return _fields;
    }

    public List getMethodsHolder() {
        return _methods;
    }

    public Collection getAttributesHolder() {
        return _attributes;
    }

    public ConstantPool getPool() {
        return _pool;
    }

    public String getName() {
        if (_index == 0)
            return null;
        return _names.getExternalForm(((ClassEntry) _pool.getEntry(_index)).
            getNameEntry().getValue(), false);
    }

    public String getSuperclassName() {
        if (_superclassIndex == 0)
            return null;
        return _names.getExternalForm(((ClassEntry) _pool.getEntry
            (_superclassIndex)).getNameEntry().getValue(), false);
    }

    public String getComponentName() {
        return null;
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isArray() {
        return false;
    }
}
