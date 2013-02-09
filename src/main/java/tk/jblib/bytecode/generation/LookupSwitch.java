package tk.jblib.bytecode.generation;

import tk.jblib.bytecode.util.ByteStream;

import static tk.jblib.bytecode.introspection.Opcode.LOOKUPSWITCH;

public class LookupSwitch extends Switch {

    public LookupSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        opcode = LOOKUPSWITCH;

        length = stream.readInt();
        trueLen = (12 + (length << 3) + padding);

        for (int i = 0; i < length; i++) {
            addCase(stream.readInt(), stream.readInt());
        }
    }

    public LookupSwitch() {}

    public byte[] getArguments() {
        ByteStream out = new ByteStream(super.getArguments());
        for (Case c : this) {
            out.write(c.match).write(c.target);
        }
        return out.toByteArray();
    }
}