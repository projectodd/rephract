package org.projectodd.rephract.java.instance;

import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;
import static org.projectodd.rephract.guards.Guards.isEqual;
import static org.projectodd.rephract.guards.Guards.isInstanceOf;

/**
 * @author Bob McWhirter
 */
public class InstancePropertyGetLink extends AbstractResolvingLink implements Guard {

    private MethodHandle reader;

    public InstancePropertyGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName) {

        Resolver resolver = resolve(receiver.getClass());
        MethodHandle reader = resolver.getInstanceResolver().getPropertyReader(propertyName);

        if (reader == null) {
            return false;
        }

        if ( this.reader != null ) {
            if ( ! this.reader.equals( reader ) ) {
                return false;
            }
        }

        this.reader = reader;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(InstancePropertyGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .drop(1)
                .convert( this.reader.type().returnType(), this.reader.type().parameterArray() )
                .invoke(this.reader).target();
    }
}
