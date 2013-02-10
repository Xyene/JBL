package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Loads a value from an array onto the stack.
 *
 * @author Abe White
 */
public class ArrayLoadInstruction extends ArrayInstruction {
    private static final Class[][] _mappings = new Class[][] {
        { boolean.class, int.class },
        { void.class, int.class },
    };

    ArrayLoadInstruction(Code owner) {
        super(owner);
    }

    ArrayLoadInstruction(Code owner, int opcode) {
        super(owner, opcode);
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
        case Constants.DALOAD:
        case Constants.LALOAD:
        case Constants.NOP:
            return 0;
        default:
            return -1;
        }
    }

    public String getTypeName() {
        switch (getOpcode()) {
        case Constants.IALOAD:
            return int.class.getName();
        case Constants.LALOAD:
            return long.class.getName();
        case Constants.FALOAD:
            return float.class.getName();
        case Constants.DALOAD:
            return double.class.getName();
        case Constants.AALOAD:
            return Object.class.getName();
        case Constants.BALOAD:
            return byte.class.getName();
        case Constants.CALOAD:
            return char.class.getName();
        case Constants.SALOAD:
            return short.class.getName();
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
            return (TypedInstruction) setOpcode(Constants.IALOAD);
        case 'l':
            return (TypedInstruction) setOpcode(Constants.LALOAD);
        case 'f':
            return (TypedInstruction) setOpcode(Constants.FALOAD);
        case 'd':
            return (TypedInstruction) setOpcode(Constants.DALOAD);
        case 'b':
            return (TypedInstruction) setOpcode(Constants.BALOAD);
        case 'c':
            return (TypedInstruction) setOpcode(Constants.CALOAD);
        case 's':
            return (TypedInstruction) setOpcode(Constants.SALOAD);
        default:
            return (TypedInstruction) setOpcode(Constants.AALOAD);
        }
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterArrayLoadInstruction(this);
        visit.exitArrayLoadInstruction(this);
    }
}
