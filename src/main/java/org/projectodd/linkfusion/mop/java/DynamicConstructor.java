package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class DynamicConstructor extends AbstractDynamicMember {

    private List<MethodHandle> constructors = new ArrayList<MethodHandle>();

    public DynamicConstructor() {
    }

    public void addConstructorHandle(MethodHandle method) {
        this.constructors.add(method);
    }

    public MethodHandle findConstructorHandle(Object[] args) {
        for (MethodHandle each : this.constructors) {
            if ((each.type().parameterCount()) == args.length) {
                if (parametersMatch(each.type().parameterArray(), args)) {
                    return each;
                }
            }
        }

        return null;
    }

}
