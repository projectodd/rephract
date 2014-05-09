package org.projectodd.rephract.java.map;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class MapLikePropertySetLink extends AbstractResolvingLink implements Guard {

    private InvocationPlan plan;

    public MapLikePropertySetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName, Object value) {
        Resolver resolver = resolve(receiver.getClass());
        DynamicMethod writer = resolver.getInstanceResolver().getMethod( "put" );

        System.err.println( "WRITER: " + writer );

        if (writer == null) {
            return false;
        }

        InvocationPlan plan = writer.findMethodInvoationPlan(propertyName, value);

        System.err.println( "plan: " + plan );

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
                .findVirtual(MapLikePropertySetLink.class, "guard", methodType(boolean.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        System.err.println( "PUT FILTERS: " + Arrays.asList( this.plan.getFilters() ) );
        MethodHandle methodHandle = this.plan.getMethodHandle();
        System.err.println( "HANDLE: " + methodHandle );

        return this.builder
                .printType()
                .convert( methodHandle.type().returnType(), methodHandle.type().parameterArray() )
                .printType()
                .filter(1, this.plan.getFilters())
                .printType()
                .invoke(this.plan.getMethodHandle()).target();
    }
}
