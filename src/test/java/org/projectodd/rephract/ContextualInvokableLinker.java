package org.projectodd.rephract;

import org.projectodd.rephract.filters.Filter;

import static org.projectodd.rephract.guards.Guards.*;


/**
 * @author Bob McWhirter
 */
public class ContextualInvokableLinker extends ContextualLinker {

    @Override
    public Filter contextFilter() {
        return MockContextFilter.INSTANCE;
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return invocation.builder()
                .guardWith(isInstanceOf(ContextualInvokable.class))
                .guard(1).with(isInstanceOf(MockContext.class))
                .invoke(ContextualInvokableInvoker.INSTANCE);
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return invocation.builder()
                .guardWith(isInstanceOf(ContextualConstructable.class))
                .guard(1).with(isInstanceOf(MockContext.class))
                .invoke(ContextualConstructableInvoker.INSTANCE);
    }
}
