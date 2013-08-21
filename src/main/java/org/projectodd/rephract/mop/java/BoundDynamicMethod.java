package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import com.headius.invokebinder.Binder;

public class BoundDynamicMethod extends DynamicMethod {

    private Object self;

    public BoundDynamicMethod(Object self, DynamicMethod method) {
        super(method.getCoercionMatrix(), method.getName(), false);
        for ( MethodHandle each : method.getMethods() ) {
            addMethodHandle(each);
        }
        this.self = self;
    }

    @Override
    public InvocationPlan findMethodInvoationPlan(Object[] args) {
        InvocationPlan unboundPlan = super.findMethodInvoationPlan(args);
        
        if ( unboundPlan == null ) {
            return null;
        }
        
        Class<?>  selfType = unboundPlan.getMethodHandle().type().parameterType(0);
        
        MethodHandle[] unboundFilters = unboundPlan.getFilters();
        MethodHandle[] filters = new MethodHandle[unboundFilters.length + 1];

        for (int i = 0; i < unboundFilters.length; ++i) {
            filters[i+1] = unboundFilters[i];
        }

        try {
            filters[0] = selfFilter( selfType );
            InvocationPlan plan = new InvocationPlan(unboundPlan.getMethodHandle(), filters);
            return plan;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    protected MethodHandle selfFilter(Class<?> selfType) throws NoSuchMethodException, IllegalAccessException {
        return Binder.from(MethodType.methodType(selfType, Object.class))
                .drop(0)
                .insert(0, this.self)
                .invoke(MethodHandles.identity(Object.class));
    }

    public Object getSelf() {
        return this.self;
    }
    
    
    public String toString() {
        return "[BoundDynamicMethod: self=" + this.self + "; method=" + super.toString() + "]";
    }
}
