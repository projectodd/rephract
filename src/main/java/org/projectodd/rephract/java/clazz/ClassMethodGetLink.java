package org.projectodd.rephract.java.clazz;

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
public class ClassMethodGetLink extends AbstractResolvingLink implements Guard {

    private DynamicMethod method;

    public ClassMethodGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String methodName) {

        if ( !( receiver instanceof Class ) ) {
            return false;
        }

        Resolver resolver = resolve((Class<?>) receiver);
        DynamicMethod dynamicMethod = resolver.getClassResolver().getMethod(methodName);

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
                .findVirtual(ClassMethodGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
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
