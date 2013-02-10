package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * The <code>monitorenter</code> instruction.
 *
 * @author Abe White
 */
public class MonitorEnterInstruction extends MonitorInstruction {
    MonitorEnterInstruction(Code owner) {
        super(owner, Constants.MONITORENTER);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterMonitorEnterInstruction(this);
        visit.exitMonitorEnterInstruction(this);
    }
}
