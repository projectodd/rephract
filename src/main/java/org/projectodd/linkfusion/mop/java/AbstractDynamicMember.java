package org.projectodd.linkfusion.mop.java;

import java.util.Arrays;

import static org.projectodd.linkfusion.mop.java.TypeComparisons.*;

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

}
