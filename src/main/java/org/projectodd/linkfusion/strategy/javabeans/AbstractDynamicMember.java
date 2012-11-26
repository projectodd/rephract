package org.projectodd.linkfusion.strategy.javabeans;

import java.util.Arrays;

public abstract class AbstractDynamicMember {

    protected boolean parametersMatch(Class<?>[] paramTypes, Object[] args) {
        System.err.println(Arrays.asList(paramTypes) + " // " + Arrays.asList(args));
        for (int i = 0; i < args.length; ++i) {
            if (!isCompatible(paramTypes[i], args[i].getClass())) {
                System.err.println(i + ": FALSE: " + paramTypes[i] + " vs " + args[i].getClass());
                return false;
            }
        }

        System.err.println("TRUE");
        return true;
    }

    protected boolean isCompatible(Class<?> target, Class<?> objClass) {
        if (target.isAssignableFrom(objClass)) {
            return true;
        }

        if (target == Integer.TYPE && objClass == Integer.class) {
            return true;
        }

        if (target == Integer.class && objClass == Integer.TYPE) {
            return true;
        }

        return false;
    }
}
