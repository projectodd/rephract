package org.projectodd.rephract;

import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.Guards;
import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public abstract class Link {

    public abstract MethodHandle guard() throws Exception;
    public abstract MethodHandle target() throws Exception;

    public MethodHandle test(Object... args) throws Throwable {
        if (!(boolean) guard().invokeWithArguments(args)) {
            return null;
        }
        return target();
    }

    public Object tryInvoke(Object... args) throws Throwable {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i].getClass();
        }

        MethodHandle target = test(args);
        if (target == null) {
            throw new PreconditionFailedException();
        }
        return target.invokeWithArguments(args);
    }
}
