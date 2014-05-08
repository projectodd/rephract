package org.projectodd.rephract.java.map;

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
public class MapLikePropertyGetLink extends AbstractResolvingLink implements Guard {

    private InvocationPlan plan;

    public MapLikePropertyGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName) {
        if ( propertyName.equals( "get" ) || propertyName.equals( "put" ) ) {
            return false;
        }

        Resolver resolver = resolve(receiver.getClass());
        DynamicMethod method = resolver.getInstanceResolver().getMethod( "get" );

        if (method == null) {
            return false;
        }

        InvocationPlan plan = method.findMethodInvoationPlan(propertyName);

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
                .findVirtual(MapLikePropertyGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        MethodHandle methodHandle = this.plan.getMethodHandle();
        return this.builder
                .filter( 1, this.plan.getFilters() )
                .convert(methodHandle.type().returnType(), methodHandle.type().parameterArray())
                .invoke(methodHandle)
                .target();
    }
}
