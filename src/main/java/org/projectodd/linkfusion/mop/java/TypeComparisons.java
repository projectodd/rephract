package org.projectodd.linkfusion.mop.java;

public class TypeComparisons {
    
    public static boolean isCompatible(Class<?> target, Class<?> objClass) {
        if (target.isAssignableFrom(objClass)) {
            return true;
        }

        if (target == Integer.TYPE && objClass == Integer.class ) {
            return true;
        }

        if (target == Integer.class && objClass == Integer.TYPE) {
            return true;
        }

        return false;
    }

}
