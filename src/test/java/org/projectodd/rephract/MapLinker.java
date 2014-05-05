package org.projectodd.rephract;

import org.projectodd.rephract.guards.Guards;

import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class MapLinker extends NonContextualLinker {

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return invocation.builder()
                .guardWith(Guards.isInstanceOf( Map.class ) )
                .invoke( MapGetInvoker.INSTANCE );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return invocation.builder()
                .guardWith(Guards.isInstanceOf(Map.class ) )
                .invoke( MapPutInvoker.INSTANCE );
    }
}
