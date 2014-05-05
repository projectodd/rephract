package org.projectodd.rephract;

import static org.projectodd.rephract.guards.Guards.*;


/**
 * @author Bob McWhirter
 */
public class InvokableLinker extends NonContextualLinker {

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return invocation.builder()
                .guardWith( isInstanceOf( Invokable.class ) )
                .invoke( InvokableInvoker.INSTANCE );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return invocation.builder()
                .guardWith(isInstanceOf( Constructable.class ) )
                .invoke( ConstructableInvoker.INSTANCE );
    }
}
