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
        return new UnboundInstanceMethodGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new UnboundInstanceMethodCallLink( invocation.builder() );
    }
}
