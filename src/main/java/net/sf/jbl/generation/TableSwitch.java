package net.sf.jbl.generation;

import net.sf.jbl.introspection.Opcode;
import net.sf.jbl.util.ByteStream;

public class TableSwitch extends Switch {

    public TableSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        opcode = Opcode.TABLESWITCH;

        int low = stream.readInt(), high = stream.readInt();

        length = high - low + 1;
        trueLen = (short) ((12 + (length << 2)) + padding);

        for (int match = low; match <= high; match++) {
            addCase(new Case(match, stream.readInt()));
        }
    }

    public TableSwitch() {}

    public byte[] getArguments() {
        ByteStream out = new ByteStream(super.getArguments());
        out.write((length > 0) ? getCase(0).match : 0).write((length > 0) ? getCase(length - 1).match : 0);
        for (int i = 0; i < length; i++)
            out.write(getCase(i).target);
        return out.toByteArray();
    }
}
