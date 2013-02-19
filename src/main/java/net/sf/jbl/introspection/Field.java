package net.sf.jbl.introspection;

import java.util.Collections;

public class Field extends Member {

    public Field(int access, String name, String descriptor, Container attributes) {
        super(access, name, descriptor, attributes);
    }

    public Field(int access, String name, String descriptor) {
        this(access, name, descriptor, new Container(Collections.EMPTY_LIST)); //TODO: null constantpool....
    }

    public boolean isVolatile() {
        return is(ACC_VOLATILE);
    }

    public void setVolatile(boolean i) {
        flag = i ? flag | ACC_VOLATILE : flag & ~ACC_VOLATILE;
    }

    public boolean isTransient() {
        return is(ACC_TRANSIENT);
    }

    public void setTransient(boolean i) {
        flag = i ? flag | ACC_TRANSIENT : flag & ~ACC_TRANSIENT;
    }
}
