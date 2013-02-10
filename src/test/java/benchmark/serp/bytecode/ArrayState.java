package benchmark.serp.bytecode;

import java.util.*;

/**
 * State implementing the behavior of an array class.
 *
 * @author Abe White
 */
class ArrayState extends State {
    private String _name = null;
    private String _componentName = null;

    public ArrayState(String name, String componentName) {
        _name = name;
        _componentName = componentName;
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
        return _name;
    }

    public String getSuperclassName() {
        return Object.class.getName();
    }

    public String getComponentName() {
        return _componentName;
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isArray() {
        return true;
    }
}
