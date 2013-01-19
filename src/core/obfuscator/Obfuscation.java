package core.obfuscator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Obfuscation {

    static final String CHARS = "abcdefghijklmnopqrstuvwxyzACBDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()_+<>:?{}|=-[]\\ +.";
    static final Random rnd = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sb.toString();
    }

    public static String genRandomName(HashMap<String, HashSet<String>> overloads) {
        String str;
        do {
            System.out.println("Generating random name");
            str = randomString(new Random().nextInt(1) + 1);
        }
        while (overloads.containsKey(str));
        return str;
    }
}
