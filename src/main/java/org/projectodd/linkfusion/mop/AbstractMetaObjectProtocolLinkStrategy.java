package org.projectodd.linkfusion.mop;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.List;

import org.projectodd.linkfusion.InvocationRequest;
import org.projectodd.linkfusion.LinkStrategy;
import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.guards.Guards;

import com.headius.invokebinder.Binder;

public abstract class AbstractMetaObjectProtocolLinkStrategy implements MetaObjectProtocolLinkStrategy {

    @Override
    public StrategicLink link(InvocationRequest request, StrategyChain chain) throws NoSuchMethodException, IllegalAccessException {

        if (request.isFusionRequest()) {
            List<Operation> ops = request.getOperations();

            StrategicLink link = null;

            for (Operation each : ops) {
                switch (each.getType()) {
                case GET_PROPERTY:
                    link = linkGetProperty(chain, each);
                    break;
                case SET_PROPERTY:
                    link = linkSetProperty(chain, each);
                    break;
                case GET_METHOD:
                    link = linkGetMethod(chain, each);
                    break;
                case CALL:
                    link = linkCall(chain, each);
                    break;
                case CONSTRUCT:
                    link = linkConstruct(chain, each);
                    break;
                }

                if (link != null) {
                    return link;
                }
            }
        }

        return null;
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
