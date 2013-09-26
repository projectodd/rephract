package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class ReturnFilters {
    private final MethodHandle noOp;
    private final Map<Class<?>, MethodHandle> filters = new HashMap<>();

    static void noOp() {}

    public ReturnFilters() {
        try {
            this.noOp = MethodHandles.lookup().findStatic(ReturnFilters.class, "noOp", MethodType.methodType(void.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void addReturnFilter(Class<?> clazz, MethodHandle handle) {
        filters.put(clazz, handle);
    }

    public MethodHandle getReturnFilter(Class<?> returnType) {
        MethodHandle result = getMethodHandleInner(returnType);
        if (result == null) {
            for (Class<?> iface : returnType.getInterfaces()) {
                result = getMethodHandleInner(iface);
                if (result != null) {
                    break;
                }
            }
        }

        if (result == null) {
            return inferReturnFilter(returnType);
        } else {
            return result;
        }
    }

    private MethodHandle getMethodHandleInner(Class<?> returnType) {
        if (filters.containsKey(returnType)) {
            return filters.get(returnType);
        } else {
            return null;
        }
    }

    public MethodHandle getReturnFilter(MethodHandle handle) {
        return getReturnFilter(handle.type().returnType());
    }

    private MethodHandle inferReturnFilter(Class<?> returnType) {
        if(returnType == void.class) {
            return noOp;
        } else {
            return MethodHandles.identity(returnType);
        }
    }

    MethodHandle getNoOp() {
        return noOp;
    }
}
