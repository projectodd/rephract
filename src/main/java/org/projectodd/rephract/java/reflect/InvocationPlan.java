package org.projectodd.rephract.java.reflect;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;

public class InvocationPlan {

    private MethodHandle methodHandle;
    private MethodHandle[] filters;

    public InvocationPlan(MethodHandle methodHandle, MethodHandle[] filters) {
        this.methodHandle = methodHandle;
        this.filters = filters;
    }

    public MethodHandle getMethodHandle() {
        return this.methodHandle;
    }

    public MethodHandle[] getFilters() {
        return this.filters;
    }

    public String toString() {
        return "[InvocationPlan: " + methodHandle + "; filters=" + Arrays.asList(this.filters) + "]";
    }

}
