package com.github.Icyene.bytecode.introspection.internal.pools.instructions;

public class Operator {
    private final int code;
    private byte[] operand;
    public Operator(int code, byte[] op) {
        this.code = code;
        this.operand = op;
    }

    public String toString() {
      return Integer.toHexString(code) + (operand != null ? "[" + operand + "]" : "");
    }

    public byte[] getOperand() {
        return operand;
    }

    public int getOpcode() {
        return code;
    }

    public void setOperand(byte[] op) {
        operand = op;
    }
}
