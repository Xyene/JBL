import org.junit.Test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static org.junit.Assert.*;

public class XMLAttributeTest {

    @Test
    public void verifyEncoding() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("a", "b");
        map.put("c", "d");
        map.put("e", "f");

        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(map);
        enc.close();

        System.out.println("The encoded map:\n" + new String(out.toByteArray()));
        HashMap<String, String> reconstituted = (HashMap<String, String>) new XMLDecoder(new ByteArrayInputStream(out.toByteArray())).readObject();
        System.out.println("Reconstituted map:" + reconstituted);
        System.out.println("Should print 'b': " + map.get("a"));
    }
}
