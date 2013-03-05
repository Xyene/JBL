/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.commons.generation;

import net.sf.jbl.core.ByteStream;
import net.sf.jbl.core.ConstantPool;
import net.sf.jbl.core.Method;
import net.sf.jbl.core.attributes.Code;
import net.sf.jbl.core.metadata.SignatureReader;

import java.util.LinkedList;

import static net.sf.jbl.core.Opcode.*;
import static net.sf.jbl.core.Opcode.INVOKEDYNAMIC;
import static net.sf.jbl.core.Opcode.TABLESWITCH;

public class CodeParser {
    int length;
    public LinkedList<Instruction> instructions = new LinkedList<Instruction>();
    public Method method;
    private ConstantPool store;

    public CodeParser(Method code, ConstantPool consts) {
        this(code.getBytecode(), 0, consts);
        method = code;
    }

    public CodeParser(byte[] bytes, ConstantPool consts) {
        this(bytes, 0, consts);
    }

    protected CodeParser(byte[] bytes, int pos, ConstantPool consts) {
        length = bytes.length;
        store = consts;
        ByteStream in = ByteStream.readStream(bytes);
        boolean wide = false;
        System.out.println(consts);
        for (int i = 0; i != bytes.length; i++) {
            int opcode = in.readByte() & 0xFF;
            int address = i + pos;

            switch (opcode) {
                case WIDE:
                    wide = true;
                    continue;
                case ALOAD:
                case AASTORE:
                case BIPUSH:
                case DLOAD:
                case DSTORE:
                case FLOAD:
                case FSTORE:
                case ILOAD:
                case ISTORE:
                case LSTORE:
                case NEWARRAY:
                case RET:
                case LDC:
                    instructions.add(new Instruction(opcode, wide, address, in.read(wide ? 2 : 1)));
                    i += wide ? 2 : 1;
                    wide = false;
                    continue;
                case ANEWARRAY:
                case IINC:
                case SIPUSH:
                case CHECKCAST:
                case LDC_W:
                case LDC2_W:
                case INSTANCEOF:
                case NEW:
                    instructions.add(new Instruction(opcode, address, in.read(2)));
                    i += 2;
                    continue;
                case INVOKEINTERFACE:
                case INVOKESTATIC:
                case INVOKESPECIAL:
                case INVOKEVIRTUAL:
                case GETFIELD:
                case GETSTATIC:
                case PUTFIELD:
                case PUTSTATIC:
                    instructions.add(new Invocation(opcode, address, consts.getMember(in.readShort())));
                    i += 2;
                    continue;
                case LOOKUPSWITCH:
                    Switch look = new Switch(LOOKUPSWITCH, address);

                    look.padding = (4 - (in.position() % 4)) % 4;
                    in.read(look.padding);
                    look.defaultJump = in.readInt();

                    look.length = in.readInt();
                    look.trueLen = (12 + (length << 3) + look.padding);

                    for (int c = 0; c < length; c++) {
                        look.addCase(in.readInt(), in.readInt());
                    }
                    instructions.add(look);
                    i += look.trueLen;
                    continue;
                case TABLESWITCH:
                    Switch table = new Switch(TABLESWITCH, address);

                    table.padding = (4 - (in.position() % 4)) % 4;
                    in.read(table.padding);
                    table.defaultJump = in.readInt();

                    int low = in.readInt(), high = in.readInt();

                    length = high - low + 1;
                    table.trueLen = (short) ((12 + (length << 2)) + table.padding);

                    for (int match = low; match <= high; match++) {
                        table.addCase(match, in.readInt());
                    }

                    instructions.add(table);
                    i += table.trueLen;
                    continue;
                case INVOKEDYNAMIC:
                    instructions.add(new Instruction(opcode, address, in.read(4)));
                    i += 4;
                    continue;
                case IFEQ:
                case IFGE:
                case IFGT:
                case IFLE:
                case IFLT:
                case IFNE:
                case IFNONNULL:
                case IFNULL:
                case IF_ACMPEQ:
                case IF_ACMPNE:
                case IF_ICMPEQ:
                case IF_ICMPGE:
                case IF_ICMPGT:
                case IF_ICMPLE:
                case IF_ICMPLT:
                case IF_ICMPNE:
                case GOTO_W:
                case JSR_W:
                    instructions.add(new Branch(opcode, address, in.readShort()));
                    i += 2;
                    continue;
                default:
                    instructions.add(new Instruction(opcode, address, new byte[]{}));
            }
        }
    }

    public Code generate() {
        Code code = new Code();
        ByteStream out = ByteStream.writeStream();

        int maxStackSize = 0;
        int currentStackSize = 0;
        boolean[] lvt = new boolean[256];

        for (int i = 0; i != instructions.size(); i++) {
            Instruction instrct = instructions.get(i);
            int op = instrct.opcode;
            out.writeByte(op);

            if (instrct instanceof Branch) {
                Branch jmp = (Branch) instrct;
                switch (op) {
                    case GOTO_W:
                    case JSR_W:
                        out.writeInt(jmp.jump);
                        break;
                    default:
                        out.writeShort(jmp.jump);
                }
            } else if (instrct instanceof Invocation) {
                Invocation inv = (Invocation) instrct;
                switch (op) {
                    case INVOKEINTERFACE:
                    case INVOKESTATIC:
                    case INVOKESPECIAL:
                    case INVOKEVIRTUAL:
                        SignatureReader sig = new SignatureReader(((Invocation) instrct).descriptor);
                        currentStackSize -= (op == INVOKESTATIC ? 0 : 1) + sig.getAugmentingTypes().size();
                        if (!sig.getType().equals("V"))
                            currentStackSize++;
                        out.writeShort(store.newMethod(inv.owner, inv.name, inv.descriptor));
                        continue;
                    case GETSTATIC:
                        currentStackSize++;
                        break;
                    case PUTSTATIC:
                        currentStackSize -= 1;
                        break;
                    case PUTFIELD:
                        currentStackSize -= 2;
                        break;
                    // GETFIELD
                }
                out.writeShort(store.newField(inv.owner, inv.name, inv.descriptor));
            } else if (instrct instanceof Switch) {
                Switch swi = (Switch) instrct;
                currentStackSize -= 1;
                out.writeBytes(new byte[swi.padding]).writeInt(swi.defaultJump);
                switch (op) {
                    case TABLESWITCH:
                        out.writeInt((length > 0) ? swi.getCase(0).match : 0).writeInt((length > 0) ? swi.getCase(length - 1).match : 0);
                        for (int c = 0; c < length; c++)
                            out.writeInt(swi.getCase(c).target);
                        break;
                    case LOOKUPSWITCH:
                        int size = swi.cases.size();
                        for (int c = 0; c != size; c++) {
                            Switch.Case jmp = swi.cases.get(i);
                            out.writeInt(jmp.match).writeInt(jmp.target);
                        }
                }
            } else {
                currentStackSize += STACK_GROWTH[instrct.opcode];
                out.writeBytes(instrct.args);
            }

            if (currentStackSize > maxStackSize) {
                maxStackSize = currentStackSize;
            }
        }
        code.setBytecode(out.getBuffer());

        int args = new SignatureReader(method.getDescriptor()).getAugmentingTypes().size() + 1;
        code.setMaxStack(maxStackSize + args);
        int locals = args;
        for(int i = 0; i != 256; i++)
               if(lvt[i])
                   locals++;
        code.setMaxLocals(locals);
        return code;
    }
}
