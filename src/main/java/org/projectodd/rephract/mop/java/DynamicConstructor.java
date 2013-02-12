package org.projectodd.rephract.mop.java;

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

        int matchedIndex = -1;

        int bestDistance = Integer.MAX_VALUE;

        int numMethods = this.constructors.size();
        loop: for (int i = 0; i < numMethods; ++i) {
            MethodHandle each = this.constructors.get(i);
            int methodDistance = 0;
            Class<?>[] paramTypes = each.type().parameterArray();
            if (paramTypes.length == args.length) {
                for (int j = 0; j < paramTypes.length; ++j) {
                    int paramDistance = (args[j] == null ? 0 : matrix.isCompatible(paramTypes[j], args[j].getClass()));
                    if (paramDistance < 0) {
                        continue loop;
                    }
                    methodDistance += paramDistance;
                }
                if (methodDistance < bestDistance) {
                    matchedIndex = i;
                    bestDistance = methodDistance;
                }
            }
        }

        if (matchedIndex >= 0) {
            MethodHandle matchedMethod = this.constructors.get(matchedIndex);
            Class<?>[] paramTypes = matchedMethod.type().parameterArray();
            MethodHandle[] filters = new MethodHandle[paramTypes.length];

            for (int j = 0; j < paramTypes.length; ++j) {
                filters[j] = matrix.getFilter(paramTypes[j], args[j].getClass());
            }
            return new InvocationPlan(matchedMethod, filters);

        }
        return null;
    }

}
