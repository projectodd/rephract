package org.projectodd.rephract.java;

import org.projectodd.rephract.Link;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractResolvingLink extends SmartLink {

    private final ResolverManager resolverManager;

    public AbstractResolvingLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder );
        this.resolverManager = resolverManager;
    }

    public Resolver resolve(Class<?> targetClass) {
        return this.resolverManager.getResolver( targetClass );
    }
}
