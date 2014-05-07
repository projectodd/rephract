package org.projectodd.rephract.java.instance;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;
import static org.projectodd.rephract.guards.Guards.isInstanceOf;

/**
 * @author Bob McWhirter
 */
public class InstancePropertySetLink extends AbstractResolvingLink implements Guard {

    private InvocationPlan plan;

    public InstancePropertySetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName, Object value) {
        Resolver resolver = resolve(receiver.getClass());
        DynamicMethod writer = resolver.getInstanceResolver().getPropertyWriter(propertyName);

        if (writer == null) {
            return false;
        }

        InvocationPlan plan = writer.findMethodInvoationPlan(value);

        if (plan == null) {
            return false;
        }

        if ( this.plan != null ) {
            if ( this.plan != plan ) {
                return false;
            }
        }

        this.plan = plan;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(InstancePropertySetLink.class, "guard", methodType(boolean.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .drop(1)
                .filter(1, this.plan.getFilters() )
                .invoke(this.plan.getMethodHandle()).target();
    }
}
