import org.junit.Test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class XMLAttributeTest {

    @Test
    public void verifyEncoding() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "b");
        map.put(2, "d");
        map.put(3, "f");

        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(map);
        enc.close();

        System.out.println("The encoded map:\n" + new String(out.toByteArray()));
        HashMap<Integer, String> reconstituted = (HashMap<Integer, String>) new XMLDecoder(new ByteArrayInputStream(out.toByteArray())).readObject();
        System.out.println("Reconstituted map:" + reconstituted);
        System.out.println("Should print 'b': " + map.get(1));
    }
}
