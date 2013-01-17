package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class DynamicMethod extends AbstractDynamicMember {

    private String name;
    private List<MethodHandle> methods = new ArrayList<MethodHandle>();
    private boolean isStatic;

    public DynamicMethod(CoercionMatrix coercionMatrix, String name, boolean isStatic) {
        super( coercionMatrix );
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

    public MethodHandle findMethodHandle(Object[] args) {
        for (MethodHandle each : methods) {
            if (isStatic) {
                if ((each.type().parameterCount()) == args.length) {
                    if (parametersMatch(getPureParameterArray(each), args)) {
                        return each;
                    }
                }
            } else {
                if ((each.type().parameterCount() - 1) == args.length) {
                    if (parametersMatch(getPureParameterArray(each), args)) {
                        return each;
                    }
                }
            }
        }

        return null;
    }

    protected Class<?>[] getPureParameterArray(MethodHandle handle) {
        List<Class<?>> paramList = handle.type().parameterList();

        if (isStatic) {
            return paramList.toArray( new Class<?>[ paramList.size()]);

        } else {
            if (paramList.size() == 1) {
                return new Class<?>[0];
            }
            return paramList.subList(1, paramList.size()).toArray(new Class<?>[paramList.size() - 1]);
        }

    }

}
