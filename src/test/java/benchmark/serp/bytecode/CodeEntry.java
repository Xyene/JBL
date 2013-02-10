package benchmark.serp.bytecode;

/**
 * An entry in a code block.
 *
 * @author Abe White
 */
class CodeEntry {
    CodeEntry next = null;
    CodeEntry prev = null;
    int byteIndex = -1;
}
