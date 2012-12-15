package disassembler.instructions;

import com.github.Icyene.bytecode.introspection.internal.metadata.Opcode;

public class Operator {
    private Opcode code;
    private Operand operands;
    public Operator(Opcode code, Operand operands) {
        this.code = code;
        this.operands = operands;
    }

    public String toString() {
      return code.name() + (operands != null ? "[" + operands.toString() + "]" : "");
    }

    public Operand getOperand() {
        return operands;
    }
}
