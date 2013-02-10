package benchmark.serp.bytecode;

import java.util.*;

/**
 * State implementing the behavior of a primitive class.
 *
 * @author Abe White
 */
class PrimitiveState extends State {
    private final Class _type;
    private final NameCache _names;

    public PrimitiveState(Class type, NameCache names) {
        _type = type;
        _names = names;
    }

    public int getMagic() {
        return Constants.VALID_MAGIC;
    }

    public int getMajorVersion() {
        return Constants.MAJOR_VERSION;
    }

    public int getMinorVersion() {
        return Constants.MINOR_VERSION;
    }

    public int getAccessFlags() {
        return Constants.ACCESS_PUBLIC | Constants.ACCESS_FINAL;
    }

    public int getIndex() {
        return 0;
    }

    public int getSuperclassIndex() {
        return 0;
    }

    public List getInterfacesHolder() {
        return Collections.EMPTY_LIST;
    }

    public List getFieldsHolder() {
        return Collections.EMPTY_LIST;
    }

    public List getMethodsHolder() {
        return Collections.EMPTY_LIST;
    }

    public Collection getAttributesHolder() {
        return Collections.EMPTY_LIST;
    }

    public String getName() {
        return _names.getExternalForm(_type.getName(), false);
    }

    public String getSuperclassName() {
        return null;
    }

    public String getComponentName() {
        return null;
    }

    public boolean isPrimitive() {
        return true;
    }

    public boolean isArray() {
        return false;
    }
}
