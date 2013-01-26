package tk.jblib.bytecode.introspection.metadata.readers;

import tk.jblib.bytecode.introspection.Member;

import java.util.LinkedList;

/**
 * Reads field & method signatures, and provides information on them.
 */
public class SignatureReader {

    protected final String returnType;
    protected LinkedList<String> parameterTypes = new LinkedList<String>();
    protected String rawParams;

    /**
     * Constructs a signature reader.
     *
     * @param mem The member whose signature will be read.
     */
    public SignatureReader(Member mem) {
        this(mem.getDescriptor());
    }

    /**
     * Constructs a signature reader.
     *
     * @param descriptor the signature designated for reading.
     */
    public SignatureReader(String descriptor) {
        int separatorPos = descriptor.lastIndexOf(")");
        this.returnType = tokenize(descriptor.substring(separatorPos + 1, descriptor.length())).get(0);
        this.parameterTypes = tokenize((rawParams = descriptor.substring(1, separatorPos)));
    }

    /**
     * Returns the raw augmenting types of the signature in encoded form.
     *
     * @return the augmenting types in encoded form.
     */
    public String getRawAugmentingTypes() {
        return "(" + rawParams + ")";
    }

    /**
     * Gets the main (return) type of this descriptor.
     *
     * @return the type.
     */
    public String getType() {
        return returnType;
    }

    /**
     * Gets a list of the augmenting (parameter) types of this descriptor.
     *
     * @return the types.
     */
    public LinkedList<String> getAugmentingTypes() {
        return parameterTypes;
    }

    protected LinkedList<String> tokenize(String inString) {
        LinkedList<String> params = new LinkedList<String>();
        String remainingParams = inString;
        int arrayDepth = 0;
        while (remainingParams.length() != 0) {
            char c = remainingParams.substring(0, 1).toCharArray()[0];
            switch (c) {
                case '[':
                    arrayDepth++;
                    remainingParams = remainingParams.substring(1, remainingParams.length());
                    break;
                case 'L':
                    int semicolonPosition = remainingParams.indexOf(";");
                    params.add(repeat("[", arrayDepth) + "L" + remainingParams.substring(1, semicolonPosition) + ";");
                    remainingParams = remainingParams.substring(semicolonPosition + 1, remainingParams.length());
                    arrayDepth = 0;
                    break;
                default:
                    params.add(repeat("[", arrayDepth) + c);
                    remainingParams = remainingParams.substring(1, remainingParams.length());
                    arrayDepth = 0;
                    break;
            }
        }
        return params;
    }

    private final String repeat(String s, int t) {
        String o = "";
        for (int i = 0; i != t; i++)
            o += s;
        return o;
    }
}
