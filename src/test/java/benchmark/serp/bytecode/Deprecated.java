package benchmark.serp.bytecode;

import benchmark.serp.bytecode.visitor.*;

/**
 * Attribute signifying that a method or class is deprecated.
 *
 * @author Abe White
 */
public class Deprecated extends Attribute {
    Deprecated(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterDeprecated(this);
        visit.exitDeprecated(this);
    }
}
