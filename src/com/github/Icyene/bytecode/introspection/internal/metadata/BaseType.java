package com.github.Icyene.bytecode.introspection.internal.metadata;

public enum BaseType {

    B("byte"),
    C("char"),
    D("double"),
    F("float"),
    I("int"),
    J("long"),
    S("short"),
    Z("boolean"),
    V("void");

    private String real;

    public String getReal() {
        return real;
    }

    BaseType(String real) {
        this.real = real;
    }
}
