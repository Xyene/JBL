package core.decompiler;

import java.util.Stack;

public class InstructionStack extends Stack<String> {
    public String getFromLast(int pos) {
        return get(size() - pos);
    }

    public String getSecondLast() {
        return getFromLast(size() - 2);
    }

    public void setLast(String s) {
        setFromLast(1, s);
    }

    public void setFromLast(int pos, String s) {
        set(size() - pos, s);
    }
}
