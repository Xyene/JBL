package core.decompiler;

public class ScopedWriter extends Scope {
    String written = "";

    public void write(String s) {
        written += getIndent() + s;
    }

    public void nWrite(String s) {
        written += s;
    }

    public String getWritten() {
        return written;
    }
}
