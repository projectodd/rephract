package org.projectodd.rephract.java.clazz;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ClassPropertyGetLink extends AbstractResolvingLink implements Guard {

    private MethodHandle reader;

    public ClassPropertyGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName) {
        if ( !( receiver instanceof Class ) ) {
            return false;
        }
        Resolver resolver = resolve((Class<?>) receiver);
        MethodHandle reader = resolver.getClassResolver().getPropertyReader(propertyName);

        if (reader == null) {
            return false;
        }

        if ( this.reader != null ) {
            if ( this.reader != reader ) {
                return false;
            }
        }

        this.reader = reader;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ClassPropertyGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .drop(0,2)
                .invoke(this.reader).target();
    }
}
