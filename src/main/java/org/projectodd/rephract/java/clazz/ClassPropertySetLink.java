package org.projectodd.rephract.java.clazz;

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

/**
 * @author Bob McWhirter
 */
public class ClassPropertySetLink extends AbstractResolvingLink implements Guard {

    private InvocationPlan plan;

    public ClassPropertySetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName, Object value) {
        if ( !(receiver instanceof Class ) ) {
            return false;
        }
        Resolver resolver = resolve((Class<?>) receiver);
        DynamicMethod writer = resolver.getClassResolver().getPropertyWriter(propertyName);

        if (writer == null) {
            return false;
        }

        InvocationPlan plan = writer.findMethodInvoationPlan(value);

        if (plan == null) {
            return false;
        }

        if ( this.plan != null ) {
            if ( ! this.plan.equals( plan ) ) {
                return false;
            }
        }

        this.plan = plan;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ClassPropertySetLink.class, "guard", methodType(boolean.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .drop(0,2)
                .filter(0, this.plan.getFilters() )
                .invoke(this.plan.getMethodHandle()).target();
    }
}
