package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * An if instruction such as <code>ifnull, ifeq</code>, etc.
 *
 * @author Abe White
 */
public class IfInstruction extends JumpInstruction {
    IfInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public int getStackChange() {
        switch (getOpcode()) {
        case Constants.IFACMPEQ:
        case Constants.IFACMPNE:
        case Constants.IFICMPEQ:
        case Constants.IFICMPNE:
        case Constants.IFICMPLT:
        case Constants.IFICMPGT:
        case Constants.IFICMPLE:
        case Constants.IFICMPGE:
            return -2;
        case Constants.IFEQ:
        case Constants.IFNE:
        case Constants.IFLT:
        case Constants.IFGT:
        case Constants.IFLE:
        case Constants.IFGE:
        case Constants.IFNULL:
        case Constants.IFNONNULL:
            return -1;
        default:
            return super.getStackChange();
        }
    }

    int getLength() {
        return super.getLength() + 2;
    }

    public String getTypeName() {
        switch (getOpcode()) {
        case Constants.IFACMPEQ:
        case Constants.IFACMPNE:
        case Constants.IFNULL:
        case Constants.IFNONNULL:
            return "java.lang.Object";
        default:
            return "I";
        }
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterIfInstruction(this);
        visit.exitIfInstruction(this);
    }
}
