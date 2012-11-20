package org.projectodd.linkfusion.strategy;

import java.lang.invoke.MethodHandle;

import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;

import com.headius.invokebinder.Binder;

public class MockContextualLinkStrategy extends ContextualLinkStrategy<LangContext> {

    public MockContextualLinkStrategy() {
        super(LangContext.class);
    }

    @Override
    public LangContext acquireContext(Object candidateContext) {
        LangContext context = super.acquireContext(candidateContext);
        if (context == null) {
            context = LangContext.getThreadContext();
        }
        return context;
    }

    @Override
    protected StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        if (receiver instanceof LangObject) {
            MethodHandle method = binder.convert(Object.class, LangObject.class, LangContext.class, String.class)
                    .invokeVirtual(lookup(), "get");

            return new StrategicLink(method, getReceiverClassGuard(LangObject.class, guardBinder));
        }

        return chain.nextStrategy();
    }

    @Override
    protected StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {
        System.err.println( "propName: " + propName );
        if (receiver instanceof LangObject) {
            MethodHandle method = binder.convert(void.class, LangObject.class, LangContext.class, String.class, Object.class)
                    .invokeVirtual(lookup(), "put");
            return new StrategicLink(method, getReceiverClassAndNameAndValueClassGuard(LangObject.class, propName, value.getClass(), guardBinder));
        }

        return chain.nextStrategy();
    }

}
