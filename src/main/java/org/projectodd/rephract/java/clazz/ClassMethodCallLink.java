package org.projectodd.rephract.java.clazz;

import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ClassMethodCallLink extends SmartLink implements Guard {

    private InvocationPlan plan;

    public ClassMethodCallLink(LinkBuilder builder) throws Exception {
        super( builder );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object self, Object[] arguments) {
        if (!(receiver instanceof DynamicMethod)) {
            return false;
        }

        if ( ! ((DynamicMethod) receiver).isStatic()) {
            return false;
        }

        InvocationPlan candidatePlan = ((DynamicMethod) receiver).findMethodInvoationPlan(arguments);

        if (candidatePlan == null) {
            return false;
        }

        if (this.plan != null) {
            if (!this.plan.equals(candidatePlan) ) {
                return false;
            }
        }
        this.plan = candidatePlan;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ClassMethodCallLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        Class<?>[] paramTypes = this.plan.getMethodHandle().type().parameterArray();

        Class<?>[] spreadTypes = new Class<?>[paramTypes.length];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = paramTypes[i];
        }

        return this.builder
                .drop(0, 2)
                .spread(spreadTypes)
                .filter(0, this.plan.getFilters() )
                .invoke(this.plan.getMethodHandle()).target();
    }
}
