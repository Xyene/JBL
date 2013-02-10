package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Attribute marking a member as synthetic, or not present in the class
 * source code.
 *
 * @author Abe White
 */
public class Synthetic extends Attribute {
    Synthetic(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterSynthetic(this);
        visit.exitSynthetic(this);
    }
}
