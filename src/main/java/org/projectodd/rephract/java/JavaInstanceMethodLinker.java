package org.projectodd.rephract.java;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandles;

import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class JavaInstanceMethodLinker extends BaseJavaLinker {

    public JavaInstanceMethodLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaInstanceMethodLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetMethod(Invocation invocation, String methodName) throws Exception {

        Object receiver = invocation.receiver();
        Resolver resolver = getResolver(receiver.getClass());
        DynamicMethod dynamicMethod = resolver.getInstanceResolver().getMethod(methodName);

        if (dynamicMethod == null) {
            return null;
        }

        return invocation.builder()
                .guardWith(isInstanceOf(receiver.getClass()))
                .guard(1).with(isEqual(methodName))
                .drop(0, 2)
                .insert(0, dynamicMethod)
                .invoke(MethodHandles.identity(DynamicMethod.class));
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new UnboundInstanceMethodCallLink( invocation.builder() );
    }
}
