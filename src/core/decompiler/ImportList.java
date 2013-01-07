package core.decompiler;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ImportList extends LinkedList<String> {

    private final List<String> primitive = Arrays.asList("int", "boolean", "byte", "void", "long", "double", "long", "float");

    public String getWImport(String s) {
        String aLess = (s = s.replace("/", ".")).replaceAll("\\[\\]", "");
        if (!primitive.contains(aLess) && !s.startsWith("java.lang")) add(aLess);
        return s.contains(".") ? s.substring(s.lastIndexOf(".") + 1, s.length()) : s;
    }

    public boolean addAll(Collection<? extends String> col) {
        for (String s : col)
            if (!contains(s))
                add(s);
        return true;
    }

    public boolean add(String sa) {
        return !contains(sa) && super.add(sa);
    }
}
