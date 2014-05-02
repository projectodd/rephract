package org.projectodd.rephract;

import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.Guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class Link {

    private final MethodHandle guard;
    private final MethodHandle target;

    public Link(MethodHandle guard, MethodHandle target) {
        this.guard = guard;
        this.target = target;
    }

    public MethodHandle guard() {
        return this.guard;
    }

    public MethodHandle target() {
        return this.target;
    }

    public Object tryCall(Object... args) throws Throwable {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i].getClass();
        }

        boolean result = (boolean) this.guard.invokeWithArguments(args);
        if (!result) {
            throw new PreconditionFailedException();
        }
        return this.target.invokeWithArguments(args);
    }
}
