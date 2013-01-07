package core.obfuscator;

import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.internal.members.attributes.Code;
import com.github.Icyene.bytecode.introspection.internal.pools.ExceptionPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class CodeGenerator {

    private final List jumps = Arrays.asList(
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

    public byte[] raw;
    public List<Jump> jumpMap = new ArrayList<Jump>();
    public ExceptionPool exceptionPool = new ExceptionPool();

    public CodeGenerator(Code code) {
        this(code.getCodePool(), 0);
        code.getExceptionPool();
    }

    public CodeGenerator(byte[] bytes, int index) {
        raw = bytes;
        ByteStream in = new ByteStream(bytes);
        System.out.println("Raw: " + Bytes.bytesToString(in.toByteArray()));
        for (int i = 0; i != bytes.length; i++) {
            int opcode = in.readByte() & 0xFF;
            if (jumps.contains(opcode)) {
                i = i + 2;
                jumpMap.add(new Jump(opcode, i + index, in.readShort(), false));
                continue;
            }

            switch (opcode) {
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
                    i++;
                    in.readByte();
                    continue;
                case ANEWARRAY:
                case IINC:
                case SIPUSH:
                case GETFIELD:
                case GETSTATIC:
                case PUTFIELD:
                case PUTSTATIC:
                case INVOKESTATIC:
                case INVOKESPECIAL:
                case INVOKEVIRTUAL:
                case CHECKCAST:
                case LDC_W:
                case LDC2_W:
                case INSTANCEOF:
                case NEW:
                case INVOKEINTERFACE:
                    i = i + 2;
                    in.readShort();
                    continue;
                case GOTO_W:
                case JSR_W:
                    i = i + 4;
                    in.readInt();
                    continue;
            }
        }
        System.out.println("-------------------------------------->" + jumpMap);
        System.out.println("Exited!");
    }

    public byte[] synthesize() {
        for (Jump j : jumpMap) {
            if (jumps.contains(raw[j.address])) {
                byte[] jump = Bytes.toByteArray((short) j.jump);
                raw[j.address + 1] = jump[0];
                raw[j.address + 2] = jump[1];
            }
        }
        return raw;
    }

    public CodeGenerator inject(int pc, byte... bytes) {
        for (Jump j : jumpMap) {
            //Above injection
            if (j.address < pc && j.jump >= pc)
                j.jump += bytes.length;
                //Below injection
            else if (j.address > pc && j.jump < 0 && j.address - Math.abs(j.jump) < pc)
                j.jump += -bytes.length;

            if (j.address >= pc) {
                j.address += bytes.length;
            }
        }

        for (TryCatch e : exceptionPool) {
            if (e.getEndPC() >= pc)
                e.setEndPC(e.getEndPC() + pc);
            if (e.getStartPC() >= pc)
                e.setStartPC(e.getStartPC() + pc);
            if (e.getHandlerPC() >= pc)
                e.setHandlerPC(e.getHandlerPC() + pc);
        }

        jumpMap.addAll(new CodeGenerator(bytes, pc).jumpMap);
        raw = Bytes.concat(Bytes.concat(Bytes.slice(raw, 0, pc), bytes), Bytes.slice(raw, pc, raw.length));
        System.out.println("Injected " + bytes.length + " bytes at location " + pc + ", new jumps are " + jumpMap);
        return this;
    }

    public class Jump {
        public transient int address, jump, opcode;
        public transient boolean wide;

        public Jump(int opcode, int address, int jump, boolean wide) {
            this.opcode = opcode;
            this.address = address;
            this.jump = jump;
            this.wide = wide;
        }

        public String toString() {
            return String.format("[Jump at %s of type %s jumps to %s]", address, opcode, jump);
        }
    }
}