package org.projectodd.rephract.builder;

import org.projectodd.rephract.Link;
import org.projectodd.rephract.MultiBinder;
import org.projectodd.rephract.SimpleLink;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.Guards;
import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class ChildLinkBuilder extends LinkBuilder {

    private final MethodHandle guard;

    public ChildLinkBuilder(MultiBinder binder, MethodHandle guard) {
        super(binder);
        this.guard = guard;
    }

    public MethodHandle getGuard() throws Exception {
        return this.guard;
    }

    @Override
    public LinkBuilder guardWith(Guard guard) throws Exception {
        MethodHandle methodHandle = binder().guardBinder().invoke(guard.guardMethodHandle(binder().guardBinder().type()));
        methodHandle = MethodHandles.guardWithTest(this.guard, methodHandle, Guards.FALSE.guardMethodHandle(binder().guardInputType()));
        return new ChildLinkBuilder(new MultiBinder(binder()), methodHandle);
    }

    LinkBuilder guardWith(MethodHandle methodHandle) throws Exception {
        methodHandle = MethodHandles.guardWithTest(this.guard, methodHandle, Guards.FALSE.guardMethodHandle(binder().guardInputType()));
        return new ChildLinkBuilder(new MultiBinder(binder()), methodHandle);
    }

    public Link invoke(Invoker invoker) throws Exception {
        return new SimpleLink(this.guard, binder().invokeBinder().invoke(invoker.invokerMethodHandle()));
    }

    public Link invoke(MethodHandle invoker) {
        return new SimpleLink(this.guard, binder().invokeBinder().invoke(invoker));
    }
}
