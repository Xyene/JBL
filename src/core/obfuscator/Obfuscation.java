package core.obfuscator;

import com.github.Icyene.bytecode.introspection.internal.Member;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
        String obfuscatedName = "";
        while (true) {
            System.out.println("Generating random name");
            obfuscatedName = Obfuscation.randomString(new Random().nextInt(1) + 1);
            if (!overloads.containsKey(obfuscatedName))
                break;
        }
        return obfuscatedName;
    }
}
