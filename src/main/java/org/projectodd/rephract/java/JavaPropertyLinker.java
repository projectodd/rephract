package org.projectodd.rephract.java;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;

import static org.projectodd.rephract.guards.Guards.isEqual;
import static org.projectodd.rephract.guards.Guards.isInstanceOf;

/**
 * @author Bob McWhirter
 */
public class JavaPropertyLinker extends BaseJavaLinker {

    public JavaPropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaPropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {

        Object receiver = invocation.receiver();
        Resolver resolver = getResolver(receiver.getClass());
        MethodHandle reader = resolver.getInstanceResolver().getPropertyReader(propertyName);

        if (reader == null) {
            return null;
        }

        return invocation.builder()
                .guardWith(isInstanceOf(receiver.getClass()))
                .guard(1).with(isEqual(propertyName))
                .drop(1)
                .invoke(reader);
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        Object receiver = invocation.receiver();
        Resolver resolver = getResolver(receiver.getClass());
        DynamicMethod writer = resolver.getInstanceResolver().getPropertyWriter(propertyName);

        if (writer == null) {
            return null;
        }

        Object value = invocation.arguments()[2];

        InvocationPlan plan = writer.findMethodInvoationPlan(value);

        if (plan == null) {
            return null;
        }

        return invocation.builder()
                .guardWith(isInstanceOf(receiver.getClass()))
                .guard(2).with(isInstanceOf(value.getClass()))
                .drop(1)
                .filter(1, plan.getFilters())
                .invoke(plan.getMethodHandle());
    }
}
