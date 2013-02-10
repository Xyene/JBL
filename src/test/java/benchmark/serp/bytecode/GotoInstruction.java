package benchmark.serp.bytecode;

/**
 * An instruction that specifies a position in the code block to jump to.
 * Examples include <code>go2, jsr</code>, etc.
 *
 * @author Abe White
 */
public class GotoInstruction extends JumpInstruction {

    GotoInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public int getStackChange() {
        if (getOpcode() == Constants.JSR)
            return 1;
        return 0;
    }

    int getLength() {
        switch (getOpcode()) {
        case Constants.GOTOW:
        case Constants.JSRW:
            return super.getLength() + 4;
        default:
            return super.getLength() + 2;
        }
    }

    public void setOffset(int offset) {
        super.setOffset(offset);
        calculateOpcode();
    }

    /**
     * Calculate our opcode based on the offset size.
     */
    private void calculateOpcode() {
        int len = getLength();
        int offset;
        switch (getOpcode()) {
        case Constants.GOTO:
        case Constants.GOTOW:
            offset = getOffset();
            if (offset < (2 << 16))
                setOpcode(Constants.GOTO);
            else
                setOpcode(Constants.GOTOW);
            break;
        case Constants.JSR:
        case Constants.JSRW:
            offset = getOffset();
            if (offset < (2 << 16))
                setOpcode(Constants.JSR);
            else
                setOpcode(Constants.JSRW);
            break;
        }
        if (len != getLength())
            invalidateByteIndexes();
    }
}
