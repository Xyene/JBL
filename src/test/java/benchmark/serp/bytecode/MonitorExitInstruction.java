package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * The <code>monitorexit</code> instruction.
 *
 * @author Abe White
 */
public class MonitorExitInstruction extends MonitorInstruction {
    MonitorExitInstruction(Code owner) {
        super(owner, Constants.MONITOREXIT);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterMonitorExitInstruction(this);
        visit.exitMonitorExitInstruction(this);
    }
}
