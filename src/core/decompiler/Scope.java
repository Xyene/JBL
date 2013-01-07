package core.decompiler;

public class Scope {
    private final String INDENT_CHARS = "   ";
    public String indent = "";

    public void indent() {
        indent += INDENT_CHARS;
    }

    public void deindent() {
        indent = indent.substring(indent.lastIndexOf(INDENT_CHARS) + 1, indent.length());
    }

    public String getIndent() {
        return indent;
    }
}
