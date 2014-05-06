package org.projectodd.rephract.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;

public class ReturnFilters {
    private final MethodHandle noOp;
    private final Map<Class<?>, MethodHandle> filters = new HashMap<>();

    private static void noOp() {
    }

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
        MethodHandle result = getMethodHandleInner(returnType, new LinkedHashSet<>(Arrays.asList(returnType.getInterfaces())));
        if (result == null) {
            return inferNoOpReturnFilter(returnType);
        } else {
            return result;
        }
    }

    private MethodHandle getMethodHandleInner(Class<?> returnType, Set<Class<?>> classes) {
        if (filters.containsKey(returnType)) {
            return filters.get(returnType);
        } else if (!classes.isEmpty()) {
            Class<?> next = classes.iterator().next();
            classes.remove(next);
            classes.addAll(Arrays.asList(next.getInterfaces()));
            return getMethodHandleInner(next, classes);
        } else {
            return null;
        }
    }

    public MethodHandle getReturnFilter(MethodHandle handle) {
        return getReturnFilter(handle.type().returnType());
    }

    private MethodHandle inferNoOpReturnFilter(Class<?> returnType) {
        if (returnType == void.class) {
            return noOp;
        } else {
            return MethodHandles.identity(returnType);
        }
    }

    MethodHandle getNoOp() {
        return noOp;
    }
}
