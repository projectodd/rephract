package org.projectodd.rephract;

import org.projectodd.rephract.filters.Filter;

import static org.projectodd.rephract.guards.Guards.*;

import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class ContextualMapLinker extends ContextualLinker {

    @Override
    public Filter contextFilter() {
        return MockContextFilter.INSTANCE;
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return invocation.builder()
                .guardWith(isInstanceOf( Map.class ) )
                .guard(1).with(isInstanceOf(MockContext.class))
                .drop(1)
                .invoke(MapGetInvoker.INSTANCE);
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return invocation.builder()
                .guardWith(isInstanceOf(Map.class))
                .guard(1).with(isInstanceOf(MockContext.class))
                .drop(1)
                .invoke(MapPutInvoker.INSTANCE);
    }
}
