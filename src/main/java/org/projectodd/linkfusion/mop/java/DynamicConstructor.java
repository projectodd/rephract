package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class DynamicConstructor extends AbstractDynamicMember {

    private List<MethodHandle> constructors = new ArrayList<MethodHandle>();

    public DynamicConstructor(CoercionMatrix coercionMatrix) {
        super( coercionMatrix );
    }

    public void addConstructorHandle(MethodHandle method) {
        this.constructors.add(method);
    }

    public InvocationPlan findConstructorInvocationPlan(Object[] args) {
        CoercionMatrix matrix = getCoercionMatrix();
        loop: for (MethodHandle each : this.constructors) {
            if ((each.type().parameterCount()) == args.length) {
                Class<?>[] paramTypes = each.type().parameterArray();
                MethodHandle[] filters = new MethodHandle[paramTypes.length];
                for (int i = 0; i < paramTypes.length; ++i) {
                    if (matrix.isCompatible(paramTypes[i], args[i].getClass())) {
                        filters[i] = matrix.getFilter(paramTypes[i], args[i].getClass());
                    } else {
                        continue loop;
                    }
                }
                return new InvocationPlan(each, filters);
            }
        }
        return null;

    }

}
