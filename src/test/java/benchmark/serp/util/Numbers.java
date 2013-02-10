package benchmark.serp.util;

/**
 * Number utilities.
 *
 * @author Abe White
 */
public class Numbers {
    private static final Integer INT_NEGONE = new Integer(-1);
    private static final Long LONG_NEGONE = new Long(-1);
    private static final Integer[] INTEGERS = new Integer[50];
    private static final Long[] LONGS = new Long[50];

    static {
        for (int i = 0; i < INTEGERS.length; i++)
            INTEGERS[i] = new Integer(i);
        for (int i = 0; i < LONGS.length; i++)
            LONGS[i] = new Long(i);
    }

    /**
     * Return the wrapper for the given number, taking advantage of cached
     * common values.
     */
    public static Integer valueOf(int n) {
        if (n == -1)
            return INT_NEGONE;
        if (n >= 0 && n < INTEGERS.length)
            return INTEGERS[n];
        return new Integer(n);
    }

    /**
     * Return the wrapper for the given number, taking advantage of cached
     * common values.
     */
    public static Long valueOf(long n) {
        if (n == -1)
            return LONG_NEGONE;
        if (n >= 0 && n < LONGS.length)
            return LONGS[(int) n];
        return new Long(n);
    }
}
