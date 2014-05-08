package org.projectodd.rephract.java.map;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class MapLikePropertyLinker extends BaseJavaLinker {

    public MapLikePropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public MapLikePropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new MapLikePropertyGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new MapLikePropertySetLink( invocation.builder(), this.resolverManager );
    }
}
