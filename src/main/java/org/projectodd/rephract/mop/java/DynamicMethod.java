package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicMethod extends AbstractDynamicMember {

    private String name;
    private List<MethodHandle> methods = new ArrayList<MethodHandle>();
    private boolean isStatic;

    public DynamicMethod(CoercionMatrix coercionMatrix, String name, boolean isStatic) {
        super(coercionMatrix);
        this.name = name;
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public String getName() {
        return this.name;
    }

    public void addMethodHandle(MethodHandle method) {
        this.methods.add(method);
    }
    
    public BoundDynamicMethod bind(Object self) {
        return new BoundDynamicMethod( self, this );
    }
    
    public List<MethodHandle> getMethods() {
        return this.methods;
    }

    public InvocationPlan findMethodInvoationPlan(Object[] args) {
        CoercionMatrix matrix = getCoercionMatrix();

        int matchedIndex = -1;

        int bestDistance = Integer.MAX_VALUE;

        int numMethods = this.methods.size();
        loop: for (int i = 0; i < numMethods; ++i) {
            MethodHandle each = this.methods.get(i);
            int methodDistance = 0;
            Class<?>[] paramTypes = getPureParameterArray(each);
            if (paramTypes.length == args.length) {
                for (int j = 0; j < paramTypes.length; ++j) {
                    int paramDistance = (args[j] == null ? 0 : matrix.isCompatible(paramTypes[j], args[j]));
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
            MethodHandle matchedMethod = this.methods.get(matchedIndex);
            Class<?>[] paramTypes = getPureParameterArray(matchedMethod);
            MethodHandle[] filters = new MethodHandle[paramTypes.length];

            for (int j = 0; j < paramTypes.length; ++j) {
                filters[j] = matrix.getFilter(paramTypes[j], args[j]);
            }
            return new InvocationPlan(matchedMethod, filters);

        }
        return null;
    }

    protected Class<?>[] getPureParameterArray(MethodHandle handle) {
        List<Class<?>> paramList = handle.type().parameterList();

        if (isStatic) {
            return paramList.toArray(new Class<?>[paramList.size()]);

        } else {
            if (paramList.size() == 1) {
                return new Class<?>[0];
            }
            return paramList.subList(1, paramList.size()).toArray(new Class<?>[paramList.size() - 1]);
        }

    }

    public String toString() {
        return "[DynamicMethod: name=" + this.name + "; isStatic=" + isStatic + "; methods=" + Arrays.asList( this.methods ) + "]";
    }

}
