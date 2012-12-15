package disassembler.instructions;

public class Operand {
    private Object val;

    public Operand(Object val) {
        this.val = val;
    }

    public String toString() {
        return "" +  (val instanceof Integer ? (Integer)val : (val instanceof Short ? (Short)val : (val instanceof Byte ? (Byte)val : "")));
    }
}
