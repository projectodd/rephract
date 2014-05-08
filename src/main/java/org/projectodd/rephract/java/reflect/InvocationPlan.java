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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InvocationPlan)) {
            return false;
        }
        if (((InvocationPlan) obj).methodHandle != this.methodHandle) {
            return false;
        }

        if ( ((InvocationPlan) obj).filters.length != this.filters.length ) {
            return false;
        }

        for ( int i = 0 ; i < this.filters.length ; ++i ) {
            if ( ! ((InvocationPlan) obj).filters[i].equals( this.filters[i])) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        return "[InvocationPlan: " + methodHandle + "; filters=" + Arrays.asList(this.filters) + "]";
    }

}
