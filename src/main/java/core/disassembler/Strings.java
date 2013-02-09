package core.disassembler;

public class Strings {
   public static String repeat(String s, int times) {
       String out = "";
       for(int i = 0; i < times; i++) {
           out += s;
       }
       return out;
   }

    public static int occurrenceOf(char c, String in) {
        int oc = 0;
        for(char ch: in.toCharArray())
            if(c == ch)
                oc++;
        return oc;
    }
}
