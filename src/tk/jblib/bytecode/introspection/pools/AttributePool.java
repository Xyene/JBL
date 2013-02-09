package tk.jblib.bytecode.introspection.pools;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.attributes.*;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A pool to hold all Attribute structures.
 */
public class AttributePool extends ArrayList<Attribute> {

    private final List<String> recognized = Arrays.asList("Code", "ConstantValue", "LineNumberTable", "LocalVariableTable", "SourceFile");

    /**
     * Constructs an attribute pool.
     *
     * @param stream The stream of bytes containing the pool data.
     * @param pool   An associated constant pool.
     */
    public AttributePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++) {
            Constant name = pool.get(stream.readShort());
            String realName = name.getStringValue();
            if (!recognized.contains(realName)) {
                add(new UnknownAttribute(stream, name));
            } else if (realName.equals("Code")) {
                add(new Code(stream, name, pool));
            } else if (realName.equals("ConstantValue")) {
                add(new ConstantValue(stream, name, pool));
            } else if (realName.equals("LineNumberTable")) {
                add(new LineNumberTable(stream, name));
            } else if (realName.equals("LocalVariableTable")) {
                add(new LocalVariableTable(stream, name, pool));
            } else if (realName.equals("SourceFile")) {
                add(new SourceFile(stream, name, pool));
            }
        }
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public AttributePool() {
    }

    ;

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        byte[] ret = Bytes.toByteArray((short) size());
        for (Attribute a : this)
            ret = Bytes.concat(ret, a.getBytes());
        return ret;
    }

    /**
     * Gets all the attributes which match the passed String. Supports regex. For example, if one wanted to get all annotations, they would do .*Runtime.*Annotations.*
     *
     * @param type The regex to match attribute names.
     * @return a list containing the latter attributes
     */
    public List<Attribute> getInstancesOf(String type) {
        Pattern t = Pattern.compile(type, Pattern.DOTALL);
        List<Attribute> out = new ArrayList<Attribute>();
        for (Attribute a : this)
            if (t.matcher(a.getName()).matches())
                out.add(a);
        return out;
    }

    /**
     * Removes all the attributes which match the passed String. Supports regex. For example, if one wanted to remove all annotations, they would do .*Runtime.*Annotations.*
     *
     * @param type The regex to match attribute names.
     */
    public void removeInstancesOf(String type) {
        removeAll(getInstancesOf(type));
    }

    /**
     * Checks if this pool contains a certain attribute name. Supports regex.
     *
     * @param type The regex to match attribute names.
     * @return True if attribute is found, false otherwise.
     */
    public boolean contains(String type) {
        Pattern m = Pattern.compile(type, Pattern.DOTALL);
        for (Attribute a : this)
            if (m.matcher(a.getName()).matches())
                return true;
        return false;
    }
}
