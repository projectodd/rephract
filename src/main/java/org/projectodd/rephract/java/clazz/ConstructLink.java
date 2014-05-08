package org.projectodd.rephract.java.clazz;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.DynamicConstructor;
import org.projectodd.rephract.java.reflect.InvocationPlan;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ConstructLink extends AbstractResolvingLink implements Guard {

    private InvocationPlan plan;

    public ConstructLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object[] arguments) {
        if (!(receiver instanceof Class)) {
            return false;
        }

        DynamicConstructor ctor = resolve((Class<?>) receiver).getClassResolver().getConstructor();

        if ( ctor == null ) {
            return false;
        }

        InvocationPlan candidatePlan = ctor.findConstructorInvocationPlan( arguments );

        if (candidatePlan == null) {
            return false;
        }

        if (this.plan != null) {
            if (this.plan != candidatePlan) {
                return false;
            }
        }
        this.plan = candidatePlan;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ConstructLink.class, "guard", methodType(boolean.class, Object.class, Object[].class))
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
                .drop(0, 1)
                .spread(spreadTypes)
                .filter( 0, this.plan.getFilters() )
                .invoke(this.plan.getMethodHandle()).target();
    }
}
