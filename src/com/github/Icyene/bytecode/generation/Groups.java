package com.github.Icyene.bytecode.generation;

import java.util.Arrays;
import java.util.List;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public interface Groups {

    List JUMPS = Arrays.asList(
            GOTO,
            JSR,
            IF_ACMPEQ,
            IF_ACMPNE,
            IF_ICMPEQ,
            IF_ICMPGE,
            IF_ICMPGT,
            IF_ICMPLE,
            IF_ICMPLT,
            IF_ICMPNE,
            IFEQ,
            IFNE,
            IFLT,
            IFGE,
            IFGT,
            IFLE,
            IFNONNULL,
            IFNULL
    );

    List JUMPS_W = Arrays.asList(GOTO_W, JSR_W);

    List IFS = Arrays.asList(
            IFEQ,
            IFGE,
            IFGT,
            IFLE,
            IFLT,
            IFNE,
            IFNONNULL,
            IFNULL,
            IF_ACMPEQ,
            IF_ACMPNE,
            IF_ICMPEQ,
            IF_ICMPGE,
            IF_ICMPGT,
            IF_ICMPLE,
            IF_ICMPLT,
            IF_ICMPNE
    );
}
