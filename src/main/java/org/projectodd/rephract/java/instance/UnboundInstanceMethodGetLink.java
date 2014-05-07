package org.projectodd.rephract.java.instance;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class UnboundInstanceMethodGetLink extends AbstractResolvingLink implements Guard {

    private DynamicMethod method;

    public UnboundInstanceMethodGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String methodName) {

        Resolver resolver = resolve(receiver.getClass());
        DynamicMethod dynamicMethod = resolver.getInstanceResolver().getMethod(methodName);

        if (dynamicMethod == null) {
            return false;
        }

        if ( this.method != null ) {
            if ( this.method != dynamicMethod) {
                return false;
            }
        }

        this.method = dynamicMethod;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(UnboundInstanceMethodGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .drop(0,2)
                .insert( 0, this.method )
                .invoke(MethodHandles.identity(DynamicMethod.class)).target();
    }
}
