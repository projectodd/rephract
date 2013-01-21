package org.projectodd.linkfusion.mop;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.projectodd.linkfusion.InvocationRequest;
import org.projectodd.linkfusion.LinkLogger;
import org.projectodd.linkfusion.NullLinkLogger;
import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.guards.Guards;

import com.headius.invokebinder.Binder;

public abstract class AbstractMetaObjectProtocolLinkStrategy implements MetaObjectProtocolLinkStrategy {

    private LinkLogger logger;

    public AbstractMetaObjectProtocolLinkStrategy(LinkLogger logger) {
        this.logger = logger;
    }

    public AbstractMetaObjectProtocolLinkStrategy() {
        this.logger = new NullLinkLogger();
    }

    public void log(String message) {
        this.logger.log(Thread.currentThread().getName() + ": " + getClass().getSimpleName() + ": " + message);
    }

    @Override
    public StrategicLink link(InvocationRequest request, StrategyChain chain) throws NoSuchMethodException, IllegalAccessException {
        StrategicLink link = null;

        if (request.isFusionRequest()) {
            Operation op = request.getOperation();
            switch (op.getType()) {
            case GET_PROPERTY:
                link = linkGetProperty(chain, op);
                break;
            case SET_PROPERTY:
                link = linkSetProperty(chain, op);
                break;
            case GET_METHOD:
                link = linkGetMethod(chain, op);
                break;
            case CALL:
                link = linkCall(chain, op);
                break;
            case CONSTRUCT:
                link = linkConstruct(chain, op);
                break;
            }

        }
        return link;
    }

    abstract protected StrategicLink linkGetProperty(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException;

    abstract protected StrategicLink linkSetProperty(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException;

    abstract protected StrategicLink linkGetMethod(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException;

    abstract protected StrategicLink linkCall(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException;

    abstract protected StrategicLink linkConstruct(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException;

    // ----------------------------------------
    // ----------------------------------------

    public static MethodHandle getReceiverClassGuard(Class<?> expectedReceiverClass, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder
                .drop(1, binder.type().parameterCount() - 1)
                .insert(1, expectedReceiverClass)
                .invokeStatic(lookup(), Guards.class, "receiverClassGuard");
    }

    public static MethodHandle getReceiverClassAndNameGuard(Class<?> expectedReceiverClass, String expectedName, Binder binder) throws NoSuchMethodException,
            IllegalAccessException {
        return binder
                .drop(2, binder.type().parameterCount() - 2)
                .insert(2, expectedReceiverClass)
                .insert(3, expectedName)
                .invokeStatic(lookup(), Guards.class, "receiverClassAndNameGuard");
    }

    public static MethodHandle getIdentityGuard(Object object, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .insert(1, new Class[] { Object.class }, object)
                .invokeStatic(lookup(), Guards.class, "identityGuard");

    }

    // ----------------------------------------
    // ----------------------------------------

    public static Lookup lookup() {
        return MethodHandles.lookup();
    }

}
