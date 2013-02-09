package core.disassembler;

public class ScopedWriter {
    private String pad = "";
    private String txt = "";

    public void increasePad(int times) {
        for(int i = 0; i != times; i++)
            pad += " ";
    }

    public void write(String text) {
        txt += pad + text;
    }

    public void writeNoPad(String text) {
        txt += text;
    }

    public void writeln(String text) {
        write(text + "\n");
    }

    public void decreasePad(int times) {
        pad = pad.substring(0, pad.length()-times);
    }

    public String getText() {
        return txt;
    }
}
