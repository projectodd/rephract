package org.projectodd.rephract.java;

import org.projectodd.rephract.NonContextualLinker;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class BaseJavaLinker extends NonContextualLinker {

    protected final ResolverManager resolverManager;

    public BaseJavaLinker() throws NoSuchMethodException, IllegalAccessException {
        this( new ResolverManager() );
    }

    public BaseJavaLinker(ResolverManager resolverManager) {
        this.resolverManager = resolverManager;
    }

    protected Resolver getResolver(Class<?> targetClass) {
        return this.resolverManager.getResolver( targetClass );
    }
}
