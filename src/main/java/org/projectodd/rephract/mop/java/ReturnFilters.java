package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class ReturnFilters {
    private final MethodHandle nop;
    private final Map<Class<?>, MethodHandle> filters = new HashMap<>();

    private static void nop() {}

    public ReturnFilters() {
        try {
            this.nop = MethodHandles.lookup().findStatic(ReturnFilters.class, "nop", MethodType.methodType(void.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void addReturnFilter(Class<?> clazz, MethodHandle handle) {
        filters.put(clazz, handle);
    }

    public MethodHandle getReturnFilter(MethodHandle handle) {
        Class<?> returnType = handle.type().returnType();
        if (filters.containsKey(returnType)) {
            return filters.get(returnType);
        } else {
            return inferReturnFilter(handle);
        }
    }

    private MethodHandle inferReturnFilter(MethodHandle handle) {
        Class<?> returnType = handle.type().returnType();
        if(returnType == void.class) {
            return nop;
        } else {
            return MethodHandles.identity(returnType);
        }
    }
}
