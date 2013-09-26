package org.projectodd.rephract.mop.java;

public class NumberThing {
    public static final int FOO = 10;

    public long longMethod(String x) {
        return Long.valueOf(x);
    }

    public String longToString(Long x) {
        return x.toString();
    }

    public static int intMethod(int x) {
        return x;
    }
}
