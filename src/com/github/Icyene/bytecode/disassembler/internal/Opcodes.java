package com.github.Icyene.bytecode.disassembler.internal;

public enum Opcodes {
    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load onto the stack a reference from an array
     */
    AALOAD(0x32),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store into a reference in an array
     */
    AASTORE(0x53),

    /**
     * Stack [before] -> [after]: ? null
     * Description: push a null reference onto the stack
     */
    ACONST_NULL(0x01),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: load a reference onto the stack from a local variable #index
     */
    ALOAD(0x19),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: load a reference onto the stack from local variable 0
     */
    ALOAD_0(0x2a),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: load a reference onto the stack from local variable 1
     */
    ALOAD_1(0x2b),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: load a reference onto the stack from local variable 2
     */
    ALOAD_2(0x2c),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: load a reference onto the stack from local variable 3
     */
    ALOAD_3(0x2d),

    /**
     * Stack [before] -> [after]: count ? arrayref
     * Description: create a new array of references of length count and component type identified by the class reference index (indexbyte1 << 8 + indexbyte2) in the constant pool
     */
    ANEWARRAY(0xbd),

    /**
     * Stack [before] -> [after]: objectref ? [empty]
     * Description: return a reference from a method
     */
    ARETURN(0xb0),

    /**
     * Stack [before] -> [after]: arrayref ? length
     * Description: get the length of an array
     */
    ARRAYLENGTH(0xbe),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: store a reference into a local variable #index
     */
    ASTORE(0x3a),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: store a reference into local variable 0
     */
    ASTORE_0(0x4b),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: store a reference into local variable 1
     */
    ASTORE_1(0x4c),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: store a reference into local variable 2
     */
    ASTORE_2(0x4d),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: store a reference into local variable 3
     */
    ASTORE_3(0x4e),

    /**
     * Stack [before] -> [after]: objectref ? [empty], objectref
     * Description: throws an error or exception (notice that the rest of the stack is cleared, leaving only a reference to the Throwable)
     */
    ATHROW(0xbf),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load a byte or Boolean value from an array
     */
    BALOAD(0x33),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store a byte or Boolean value into an array
     */
    BASTORE(0x54),

    /**
     * Stack [before] -> [after]: ? value
     * Description: push a byte onto the stack as an integer value
     */
    BIPUSH(0x10),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load a char from an array
     */
    CALOAD(0x34),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store a char into an array
     */
    CASTORE(0x55),

    /**
     * Stack [before] -> [after]: objectref ? objectref
     * Description: checks whether an objectref is of a certain type, the class reference of which is in the constant pool at index (indexbyte1 << 8 + indexbyte2)
     */
    CHECKCAST(0xc0),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a double to a float
     */
    D2F(0x90),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a double to an int
     */
    D2I(0x8e),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a double to a long
     */
    D2L(0x8f),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: add two doubles
     */
    DADD(0x63),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load a double from an array
     */
    DALOAD(0x31),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store a double into an array
     */
    DASTORE(0x52),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: compare two doubles
     */
    DCMPG(0x98),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: compare two doubles
     */
    DCMPL(0x97),

    /**
     * Stack [before] -> [after]: ? 0.0
     * Description: push the constant 0.0 onto the stack
     */
    DCONST_0(0x0e),

    /**
     * Stack [before] -> [after]: ? 1.0
     * Description: push the constant 1.0 onto the stack
     */
    DCONST_1(0x0f),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: divide two doubles
     */
    DDIV(0x6f),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a double value from a local variable #index
     */
    DLOAD(0x18),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a double from local variable 0
     */
    DLOAD_0(0x26),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a double from local variable 1
     */
    DLOAD_1(0x27),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a double from local variable 2
     */
    DLOAD_2(0x28),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a double from local variable 3
     */
    DLOAD_3(0x29),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: multiply two doubles
     */
    DMUL(0x6b),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: negate a double
     */
    DNEG(0x77),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: get the remainder from a division between two doubles
     */
    DREM(0x73),

    /**
     * Stack [before] -> [after]: value ? [empty]
     * Description: return a double from a method
     */
    DRETURN(0xaf),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a double value into a local variable #index
     */
    DSTORE(0x39),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a double into local variable 0
     */
    DSTORE_0(0x47),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a double into local variable 1
     */
    DSTORE_1(0x48),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a double into local variable 2
     */
    DSTORE_2(0x49),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a double into local variable 3
     */
    DSTORE_3(0x4a),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: subtract a double from another
     */
    DSUB(0x67),

    /**
     * Stack [before] -> [after]: value ? value, value
     * Description: duplicate the value on top of the stack
     */
    DUP(0x59),

    /**
     * Stack [before] -> [after]: value2, value1 ? value1, value2, value1
     * Description: insert a copy of the top value into the stack two values from the top. value1 and value2 must not be of the type double or long.
     */
    DUP_X1(0x5a),

    /**
     * Stack [before] -> [after]: value3, value2, value1 ? value1, value3, value2, value1
     * Description: insert a copy of the top value into the stack two (if value2 is double or long it takes up the entry of value3, too) or three values (if value2 is neither double nor long) from the top
     */
    DUP_X2(0x5b),

    /**
     * Stack [before] -> [after]: {value2, value1} ? {value2, value1}, {value2, value1}
     * Description: duplicate top two stack words (two values, if value1 is not double nor long, a single value, if value1 is double or long)
     */
    DUP2(0x5c),

    /**
     * Stack [before] -> [after]: value3, {value2, value1} ? {value2, value1}, value3, {value2, value1}
     * Description: duplicate two words and insert beneath third word (see explanation above)
     */
    DUP2_X1(0x5d),

    /**
     * Stack [before] -> [after]: {value4, value3}, {value2, value1} ? {value2, value1}, {value4, value3}, {value2, value1}
     * Description: duplicate two words and insert beneath fourth word
     */
    DUP2_X2(0x5e),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a float to a double
     */
    F2D(0x8d),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a float to an int
     */
    F2I(0x8b),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a float to a long
     */
    F2L(0x8c),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: add two floats
     */
    FADD(0x62),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load a float from an array
     */
    FALOAD(0x30),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store a float in an array
     */
    FASTORE(0x51),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: compare two floats
     */
    FCMPG(0x96),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: compare two floats
     */
    FCMPL(0x95),

    /**
     * Stack [before] -> [after]: ? 0.0f
     * Description: push 0.0f on the stack
     */
    FCONST_0(0x0b),

    /**
     * Stack [before] -> [after]: ? 1.0f
     * Description: push 1.0f on the stack
     */
    FCONST_1(0x0c),

    /**
     * Stack [before] -> [after]: ? 2.0f
     * Description: push 2.0f on the stack
     */
    FCONST_2(0x0d),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: divide two floats
     */
    FDIV(0x6e),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a float value from a local variable #index
     */
    FLOAD(0x17),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a float value from local variable 0
     */
    FLOAD_0(0x22),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a float value from local variable 1
     */
    FLOAD_1(0x23),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a float value from local variable 2
     */
    FLOAD_2(0x24),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a float value from local variable 3
     */
    FLOAD_3(0x25),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: multiply two floats
     */
    FMUL(0x6a),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: negate a float
     */
    FNEG(0x76),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: get the remainder from a division between two floats
     */
    FREM(0x72),

    /**
     * Stack [before] -> [after]: value ? [empty]
     * Description: return a float
     */
    FRETURN(0xae),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a float value into a local variable #index
     */
    FSTORE(0x38),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a float value into local variable 0
     */
    FSTORE_0(0x43),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a float value into local variable 1
     */
    FSTORE_1(0x44),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a float value into local variable 2
     */
    FSTORE_2(0x45),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a float value into local variable 3
     */
    FSTORE_3(0x46),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: subtract two floats
     */
    FSUB(0x66),

    /**
     * Stack [before] -> [after]: objectref ? value
     * Description: get a field value of an object objectref, where the field is identified by field reference in the constant pool index (index1 << 8 + index2)
     */
    GETFIELD(0xb4),

    /**
     * Stack [before] -> [after]: ? value
     * Description: get a static field value of a class, where the field is identified by field reference in the constant pool index (index1 << 8 + index2)
     */
    GETSTATIC(0xb2),

    /**
     * Stack [before] -> [after]: [no change]
     * Description: goes to another instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    GOTO(0xa7),

    /**
     * Stack [before] -> [after]: [no change]
     * Description: goes to another instruction at branchoffset (signed byte constructed from unsigned bytes branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4)
     */
    GOTO_W(0xc8),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a byte
     */
    I2B(0x91),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a character
     */
    I2C(0x92),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a double
     */
    I2D(0x87),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a float
     */
    I2F(0x86),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a long
     */
    I2L(0x85),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert an byte into a short
     */
    I2S(0x93),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: add two ints
     */
    IADD(0x60),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load an byte from an array
     */
    IALOAD(0x2e),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: perform a bitwise and on two integers
     */
    IAND(0x7e),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store an byte into an array
     */
    IASTORE(0x4f),

    /**
     * Stack [before] -> [after]: ? -1
     * Description: load the byte value -1 onto the stack
     */
    ICONST_M1(0x02),

    /**
     * Stack [before] -> [after]: ? 0
     * Description: load the byte value 0 onto the stack
     */
    ICONST_0(0x03),

    /**
     * Stack [before] -> [after]: ? 1
     * Description: load the byte value 1 onto the stack
     */
    ICONST_1(0x04),

    /**
     * Stack [before] -> [after]: ? 2
     * Description: load the byte value 2 onto the stack
     */
    ICONST_2(0x05),

    /**
     * Stack [before] -> [after]: ? 3
     * Description: load the byte value 3 onto the stack
     */
    ICONST_3(0x06),

    /**
     * Stack [before] -> [after]: ? 4
     * Description: load the byte value 4 onto the stack
     */
    ICONST_4(0x07),

    /**
     * Stack [before] -> [after]: ? 5
     * Description: load the byte value 5 onto the stack
     */
    ICONST_5(0x08),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: divide two integers
     */
    IDIV(0x6c),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if references are equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ACMPEQ(0xa5),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if references are not equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ACMPNE(0xa6),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if ints are equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPEQ(0x9f),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if ints are not equal, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPNE(0xa0),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if value1 is less than value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPLT(0xa1),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if value1 is greater than or equal to value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPGE(0xa2),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if value1 is greater than value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPGT(0xa3),

    /**
     * Stack [before] -> [after]: value1, value2 ?
     * Description: if value1 is less than or equal to value2, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPLE(0xa4),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFEQ(0x99),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is not 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNE(0x9a),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is less than 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFLT(0x9b),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is greater than or equal to 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFGE(0x9c),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is greater than 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFGT(0x9d),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is less than or equal to 0, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFLE(0x9e),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is not null, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNONNULL(0xc7),

    /**
     * Stack [before] -> [after]: value ?
     * Description: if value is null, branch to instruction at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNULL(0xc6),

    /**
     * Stack [before] -> [after]: [No change]
     * Description: increment local variable #index by signed byte const
     */
    IINC(0x84),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load an byte value from a local variable #index
     */
    ILOAD(0x15),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load an byte value from local variable 0
     */
    ILOAD_0(0x1a),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load an byte value from local variable 1
     */
    ILOAD_1(0x1b),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load an byte value from local variable 2
     */
    ILOAD_2(0x1c),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load an byte value from local variable 3
     */
    ILOAD_3(0x1d),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: multiply two integers
     */
    IMUL(0x68),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: negate int
     */
    INEG(0x74),

    /**
     * Stack [before] -> [after]: objectref ? result
     * Description: determines if an object objectref is of a given type, identified by class reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INSTANCEOF(0xc1),

    /**
     * Stack [before] -> [after]: [arg1, [arg2 ...]] ?
     * Description: invokes a dynamic method identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKEDYNAMIC(0xba),

    /**
     * Stack [before] -> [after]: objectref, [arg1, arg2, ...] ?
     * Description: invokes an interface method on object objectref, where the interface method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKEINTERFACE(0xb9),

    /**
     * Stack [before] -> [after]: objectref, [arg1, arg2, ...] ?
     * Description: invoke instance method on object objectref, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKESPECIAL(0xb7),

    /**
     * Stack [before] -> [after]: [arg1, arg2, ...] ?
     * Description: invoke a static method, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKESTATIC(0xb8),

    /**
     * Stack [before] -> [after]: objectref, [arg1, arg2, ...] ?
     * Description: invoke virtual method on object objectref, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKEVIRTUAL(0xb6),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise byte or
     */
    IOR(0x80),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: logical byte remainder
     */
    IREM(0x70),

    /**
     * Stack [before] -> [after]: value ? [empty]
     * Description: return an integer from a method
     */
    IRETURN(0xac),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: byte shift left
     */
    ISHL(0x78),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: byte arithmetic shift right
     */
    ISHR(0x7a),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store byte value into variable #index
     */
    ISTORE(0x36),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store byte value into variable 0
     */
    ISTORE_0(0x3b),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store byte value into variable 1
     */
    ISTORE_1(0x3c),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store byte value into variable 2
     */
    ISTORE_2(0x3d),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store byte value into variable 3
     */
    ISTORE_3(0x3e),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: byte subtract
     */
    ISUB(0x64),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: byte logical shift right
     */
    IUSHR(0x7c),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: byte xor
     */
    IXOR(0x82),

    /**
     * Stack [before] -> [after]: ? address
     * Description: jump to subroutine at branchoffset (signed short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2) and place the return address on the stack
     */
    JSR(0xa8),

    /**
     * Stack [before] -> [after]: ? address
     * Description: jump to subroutine at branchoffset (signed byte constructed from unsigned bytes branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4) and place the return address on the stack
     */
    JSR_W(0xc9),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a long to a double
     */
    L2D(0x8a),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a long to a float
     */
    L2F(0x89),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: convert a long to a int
     */
    L2I(0x88),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: add two longs
     */
    LADD(0x61),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load a long from an array
     */
    LALOAD(0x2f),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise and of two longs
     */
    LAND(0x7f),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store a long to an array
     */
    LASTORE(0x50),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: compare two longs values
     */
    LCMP(0x94),

    /**
     * Stack [before] -> [after]: ? 0L
     * Description: push the long 0 onto the stack
     */
    LCONST_0(0x09),

    /**
     * Stack [before] -> [after]: ? 1L
     * Description: push the long 1 onto the stack
     */
    LCONST_1(0x0a),

    /**
     * Stack [before] -> [after]: ? value
     * Description: push a constant #index from a constant pool (String, byte or float) onto the stack
     */
    LDC(0x12),

    /**
     * Stack [before] -> [after]: ? value
     * Description: push a constant #index from a constant pool (String, byte or float) onto the stack (wide index is constructed as indexbyte1 << 8 + indexbyte2)
     */
    LDC_W(0x13),

    /**
     * Stack [before] -> [after]: ? value
     * Description: push a constant #index from a constant pool (double or long) onto the stack (wide index is constructed as indexbyte1 << 8 + indexbyte2)
     */
    LDC2_W(0x14),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: divide two longs
     */
    LDIV(0x6d),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a long value from a local variable #index
     */
    LLOAD(0x16),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a long value from a local variable 0
     */
    LLOAD_0(0x1e),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a long value from a local variable 1
     */
    LLOAD_1(0x1f),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a long value from a local variable 2
     */
    LLOAD_2(0x20),

    /**
     * Stack [before] -> [after]: ? value
     * Description: load a long value from a local variable 3
     */
    LLOAD_3(0x21),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: multiply two longs
     */
    LMUL(0x69),

    /**
     * Stack [before] -> [after]: value ? result
     * Description: negate a long
     */
    LNEG(0x75),

    /**
     * Stack [before] -> [after]: key ?
     * Description: a target address is looked up from a table using a key and execution continues from the instruction at that address
     */
    LOOKUPSWITCH(0xab),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise or of two longs
     */
    LOR(0x81),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: remainder of division of two longs
     */
    LREM(0x71),

    /**
     * Stack [before] -> [after]: value ? [empty]
     * Description: return a long value
     */
    LRETURN(0xad),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise shift left of a long value1 by value2 positions
     */
    LSHL(0x79),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise shift right of a long value1 by value2 positions
     */
    LSHR(0x7b),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a long value in a local variable #index
     */
    LSTORE(0x37),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a long value in a local variable 0
     */
    LSTORE_0(0x3f),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a long value in a local variable 1
     */
    LSTORE_1(0x40),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a long value in a local variable 2
     */
    LSTORE_2(0x41),

    /**
     * Stack [before] -> [after]: value ?
     * Description: store a long value in a local variable 3
     */
    LSTORE_3(0x42),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: subtract two longs
     */
    LSUB(0x65),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise shift right of a long value1 by value2 positions, unsigned
     */
    LUSHR(0x7d),

    /**
     * Stack [before] -> [after]: value1, value2 ? result
     * Description: bitwise exclusive or of two longs
     */
    LXOR(0x83),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: enter monitor for object ("grab the lock" - start of synchronized() section)
     */
    MONITORENTER(0xc2),

    /**
     * Stack [before] -> [after]: objectref ?
     * Description: exit monitor for object ("release the lock" - end of synchronized() section)
     */
    MONITOREXIT(0xc3),

    /**
     * Stack [before] -> [after]: count1, [count2,...] ? arrayref
     * Description: create a new array of dimensions dimensions with elements of type identified by class reference in constant pool index (indexbyte1 << 8 + indexbyte2), the sizes of each dimension is identified by count1, [count2, etc.]
     */
    MULTIANEWARRAY(0xc5),

    /**
     * Stack [before] -> [after]: ? objectref
     * Description: create new object of type identified by class reference in constant pool index (indexbyte1 << 8 + indexbyte2)
     */
    NEW(0xbb),

    /**
     * Stack [before] -> [after]: count ? arrayref
     * Description: create new array with count elements of primitive type identified by atype
     */
    NEWARRAY(0xbc),

    /**
     * Stack [before] -> [after]: [No change]
     * Description: perform no operation
     */
    NOP(0x00),

    /**
     * Stack [before] -> [after]: value ?
     * Description: discard the top value on the stack
     */
    POP(0x57),

    /**
     * Stack [before] -> [after]: {value2, value1} ?
     * Description: discard the top two values on the stack (or one value, if it is a double or long)
     */
    POP2(0x58),

    /**
     * Stack [before] -> [after]: objectref, value ?
     * Description: set field to value in an object objectref, where the field is identified by a field reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    PUTFIELD(0xb5),

    /**
     * Stack [before] -> [after]: value ?
     * Description: set static field to value in a class, where the field is identified by a field reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    PUTSTATIC(0xb3),

    /**
     * Stack [before] -> [after]: [No change]
     * Description: continue execution from address taken from a local variable #index (the asymmetry with jsr is intentional)
     */
    RET(0xa9),

    /**
     * Stack [before] -> [after]: ? [empty]
     * Description: return void from method
     */
    RETURN(0xb1),

    /**
     * Stack [before] -> [after]: arrayref, index ? value
     * Description: load short from array
     */
    SALOAD(0x35),

    /**
     * Stack [before] -> [after]: arrayref, index, value ?
     * Description: store short to array
     */
    SASTORE(0x56),

    /**
     * Stack [before] -> [after]: ? value
     * Description: push a short onto the stack
     */
    SIPUSH(0x11),

    /**
     * Stack [before] -> [after]: value2, value1 ? value1, value2
     * Description: swaps two top words on the stack (note that value1 and value2 must not be double or long)
     */
    SWAP(0x5f),

    /**
     * Stack [before] -> [after]: index ?
     * Description: continue execution from an address in the table at offset index
     */
    TABLESWITCH(0xaa),

    /**
     * Stack [same as for corresponding instructions]
     * Description: execute opcode, where opcode is either iload, fload, aload, lload, dload, istore, fstore, astore, lstore, dstore, or ret, but assume the index is 16 bit, or execute iinc, where the index is 16 bits and the constant to increment by is a signed 16 bit short
     */
    WIDE(0xc4),

    /**
     * Stack [before] -> [after]:
     * Description: reserved for breakpoints in Java debuggers, should not appear in any class file
     */
    BREAKPOINT(0xca);

    Opcodes(int code) {
    }

}