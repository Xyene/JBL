package disassembler.readers;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.UnknownAttribute;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class AnnotationReader {

    /*
    annotation {
     u2 type_index;
     u2 num_element_value_pairs;
     {
       u2 element_name_index;
       element_value value;
     } element_value_pairs[num_element_value_pairs];
   }

     */

    private Constant type;
    private String name;

    public AnnotationReader(UnknownAttribute attr, ConstantPool pool) {
        ByteStream bytes = new ByteStream(attr.getValue());
        type = pool.get(bytes.readShort());
        System.out.println("Type: " + type.getStringValue());
        short size = bytes.readShort();
        System.out.println("Annotation size: " + size);
        for(int i = 0; i != size; i++) {
            System.out.println("Name: " + pool.get(bytes.readShort()).getStringValue());
           // System.out.println("Value: " + bytes.readShort());
        }
    }

    public String getName() {
        return name;
    }
}
