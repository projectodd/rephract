package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class DynamicMethod extends AbstractDynamicMember {

    private String name;
    private List<MethodHandle> methods = new ArrayList<MethodHandle>();

    public DynamicMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addMethod(MethodHandle method) {
        this.methods.add(method);
    }

    public MethodHandle findMethod(Object[] args) {
        for (MethodHandle each : methods) {
            if ((each.type().parameterCount() - 1) == args.length) {
                if (parametersMatch(getPureParameterArray( each ) , args)) {
                    return each;
                }
            }
        }

        return null;
    }
    
    protected Class<?>[] getPureParameterArray(MethodHandle handle) {
        List<Class<?>> paramList = handle.type().parameterList();
        
        if ( paramList.size() == 1 ) {
            return new Class<?>[0];
        }
        
        return paramList.subList(1, paramList.size() ).toArray(new Class<?>[ paramList.size()-1]);
    }

}
