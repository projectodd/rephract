package org.projectodd.rephract.java;

import com.apple.laf.AquaButtonBorder;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.invokers.Invoker;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;
import static org.projectodd.rephract.guards.Guards.isEqual;
import static org.projectodd.rephract.guards.Guards.isInstanceOf;

/**
 * @author Bob McWhirter
 */
public class UnboundInstanceMethodGetLink extends Link implements Guard {

    private final ResolverManager resolverManager;
    private LinkBuilder builder;
    private DynamicMethod method;

    public UnboundInstanceMethodGetLink(ResolverManager resolverManager, LinkBuilder builder) throws Exception {
        this.resolverManager = resolverManager;
        this.builder = builder;
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String methodName) {

        Resolver resolver = this.resolverManager.getResolver(receiver.getClass());
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
