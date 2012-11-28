package org.projectodd.linkfusion.mop.java;

import static org.projectodd.linkfusion.mop.java.TypeComparisons.*;

public abstract class AbstractDynamicMember {

    protected boolean parametersMatch(Class<?>[] paramTypes, Object[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (!isCompatible(paramTypes[i], args[i].getClass())) {
                return false;
            }
        }

        return true;
    }

}
