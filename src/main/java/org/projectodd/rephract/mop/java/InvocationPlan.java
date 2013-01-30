package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;

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
    
}
