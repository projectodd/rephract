package org.projectodd.rephract.mop.java;

public class NumberThing {
    public long longMethod(String x) {
        return Long.valueOf(x);
    }

    public String intMethod(Long x) {
        return x.toString();
    }
}
