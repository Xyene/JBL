package com.github.Icyene.bytecode.introspection.internal.pools.instructions;

public class Operand<T> {
    private final T val;

    public Operand(T val) {
        this.val = val;
    }

    public String toString() {
        return "" + val;
    }

    public T get() {
        return val;
    }


}
