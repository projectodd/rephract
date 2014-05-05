package org.projectodd.rephract.builder;

import org.projectodd.rephract.Link;
import org.projectodd.rephract.MultiBinder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.Guards;
import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * @author Bob McWhirter
 */
public class ChildLinkBuilder extends LinkBuilder {

    private final MethodHandle guard;

    public ChildLinkBuilder(MultiBinder binder, MethodHandle guard) {
        super(binder);
        this.guard = guard;
    }

    @Override
    public LinkBuilder guardWith(Guard guard) throws Exception {
        MethodHandle methodHandle = binder().guardBinder().invoke(guard.methodHandle(binder().guardBinder().type()));
        methodHandle = MethodHandles.guardWithTest( this.guard, methodHandle, Guards.FALSE.methodHandle(binder().guardInputType()));
        return new ChildLinkBuilder( new MultiBinder( binder() ), methodHandle );
    }

    LinkBuilder guardWith(MethodHandle methodHandle) throws Exception {
        methodHandle = MethodHandles.guardWithTest( this.guard, methodHandle, Guards.FALSE.methodHandle(binder().guardInputType()));
        return new ChildLinkBuilder( new MultiBinder( binder() ), methodHandle );
    }

    public Link invoke(Invoker invoker) throws Exception {
        //return new Link(this.guard, binder().invokeBinder().invoke(invoker.methodHandle(binder().invokeBinder().type())));
        return new Link(this.guard, binder().invokeBinder().invoke(invoker.methodHandle(binder().invokeBinder().type())));
    }
}
