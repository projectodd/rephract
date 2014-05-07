package org.projectodd.rephract.java.clazz;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodCallLink;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaClassMethodLinker extends BaseJavaLinker {

    public JavaClassMethodLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaClassMethodLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetMethod(Invocation invocation, String methodName) throws Exception {
        return new ClassMethodGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new ClassMethodCallLink( invocation.builder() );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new ConstructLink( invocation.builder(), this.resolverManager );
    }
}
