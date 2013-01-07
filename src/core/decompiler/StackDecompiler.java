package core.decompiler;

import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class StackDecompiler {
    private final String source = "";
    private final ImportList importList;

    public StackDecompiler(ByteStream pool, ImportList li, Scope c) {
        this.importList = li;

    }

    public String toString() {
        return source;
    }
}