package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Returns a value (or void) from a method.
 *
 * @author Abe White
 */
public class ReturnInstruction extends TypedInstruction {
    private static final Class[][] _mappings = new Class[][] {
        { byte.class, int.class },
        { char.class, int.class },
        { short.class, int.class },
        { boolean.class, int.class },
    };

    ReturnInstruction(Code owner) {
        super(owner);
    }

    ReturnInstruction(Code owner, int opcode) {
        super(owner, opcode);
    }

    public String getTypeName() {
        switch (getOpcode()) {
        case Constants.IRETURN:
            return int.class.getName();
        case Constants.LRETURN:
            return long.class.getName();
        case Constants.FRETURN:
            return float.class.getName();
        case Constants.DRETURN:
            return double.class.getName();
        case Constants.ARETURN:
            return Object.class.getName();
        case Constants.RETURN:
            return void.class.getName();
        default:
            return null;
        }
    }

    public TypedInstruction setType(String type) {
        type = mapType(type, _mappings, true);
        if (type == null)
            return (TypedInstruction) setOpcode(Constants.NOP);

        switch (type.charAt(0)) {
        case 'i':
            return (TypedInstruction) setOpcode(Constants.IRETURN);
        case 'l':
            return (TypedInstruction) setOpcode(Constants.LRETURN);
        case 'f':
            return (TypedInstruction) setOpcode(Constants.FRETURN);
        case 'd':
            return (TypedInstruction) setOpcode(Constants.DRETURN);
        case 'v':
            return (TypedInstruction) setOpcode(Constants.RETURN);
        default:
            return (TypedInstruction) setOpcode(Constants.ARETURN);
        }
    }

    public int getLogicalStackChange() {
        switch (getOpcode()) {
        case Constants.NOP:
            return 0;
        default:
            return -1;
        }
    }

    public int getStackChange() {
        switch (getOpcode()) {
        case Constants.RETURN:
        case Constants.NOP:
            return 0;
        case Constants.LRETURN:
        case Constants.DRETURN:
            return -2;
        default:
            return -1;
        }
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterReturnInstruction(this);
        visit.exitReturnInstruction(this);
    }
}
