package org.projectodd.linkfusion.strategy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.projectodd.linkfusion.InvocationRequest;
import org.projectodd.linkfusion.LinkStrategy;
import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.guards.Guards;

import com.headius.invokebinder.Binder;

public class ContextualLinkStrategy<T> extends BaseLinkStrategy {

    private Class<T> runtimeContextClass;

    public ContextualLinkStrategy(Class<T> runtimeContextClass) {
        this.runtimeContextClass = runtimeContextClass;
    }

    public Class<T> getRuntimeContextClass() {
        return this.runtimeContextClass;
    }

    @SuppressWarnings("unchecked")
    public T acquireContext(Object candidateContext) {
        if (candidateContext != null && this.runtimeContextClass.isAssignableFrom(candidateContext.getClass())) {
            return (T) candidateContext;
        }

        return null;
    }

    protected MethodHandle getContextAcquisitionFilter() throws NoSuchMethodException, IllegalAccessException {
        if (this.runtimeContextClass == null) {
            return MethodHandles.identity(Object.class);
        }
        return Binder.from(Object.class, getClass(), Object.class)
                .invokeVirtual(MethodHandles.lookup(), "acquireContext").bindTo(this);
    }

    @Override
    protected StrategicLink linkGetProperty(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be either:
         * 
         * [ object(receiver) ] with name encoded in operation
         * [ object(receiver), string(name) ]
         * [ object(receiver), T(context) ] with name encoded in operation
         * [ object(receiver), T(context), string(name) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();

        if (args.length == 1) {
            binder = binder.insert(1, (Object) null);
            binder = binder.filter(1, getContextAcquisitionFilter());
            binder.insert(2, op.getParameter());

            guardBinder = guardBinder.insert(1, (Object) null);
            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
            guardBinder.insert(2, op.getParameter());
        } else if (args.length == 2) {
            if (args[1] instanceof String) {
                binder = binder.insert(1, new Class[] { Object.class }, (Object) null);
                binder = binder.filter(1, getContextAcquisitionFilter());

                guardBinder = guardBinder.insert(1, new Class[] { Object.class }, (Object) null);
                guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
            }
        } else if (args.length == 3) {
            binder = binder.filter(1, getContextAcquisitionFilter());

            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
        }

        Object receiver = chain.getRequest().receiver();
        String propName = op.getParameter();
        if (propName == null) {
            propName = chain.getRequest().arguments()[1].toString();
        }
        return linkGetProperty(chain, receiver, propName, binder, guardBinder);
    }

    @Override
    protected StrategicLink linkSetProperty(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {
        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) object(value) ] with name encoded in operation
         * [ object(receiver) string(name) object(value) ]
         * [ object(receiver) ?(context) object(value) ] with name encoded in operation
         * [ object(receiver) ?(context) string(name) object(value) ]
         * 
         * result should be:
         * 
         * [ object(receiver) ?(context) string(name) object(value) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        Object value = args[args.length - 1];
        String propName = op.getParameter();

        if (args.length == 2) {
            binder = binder.insert(1, new Class[] { Object.class }, (Object) null);
            binder = binder.filter(1, getContextAcquisitionFilter());
            binder = binder.insert(2, op.getParameter());

            guardBinder = guardBinder.insert(1, new Class[] { Object.class }, (Object) null);
            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
            guardBinder = guardBinder.insert(2, op.getParameter());
        } else if (args.length == 3) {
            if (propName == null) {
                propName = (String) args[1];
                binder = binder.insert(1, new Class[] { Object.class }, (Object) null);
                binder = binder.filter(1, getContextAcquisitionFilter());

                guardBinder = guardBinder.insert(1, new Class[] { Object.class }, (Object) null);
                guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
            } else {
                binder = binder.insert(2, op.getParameter());
                guardBinder = guardBinder.insert(2, op.getParameter());
            }
        } else if (args.length == 4) {
            propName = (String) args[2];

            binder = binder.filter(1, getContextAcquisitionFilter());
            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
        }

        return linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    @Override
    protected StrategicLink linkCall(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException {
        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) object(self) object[](args)
         * [ object(receiver) ?(context) object(self) object[](args)
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        Object self = null;

        Object[] callArgs = (Object[]) args[args.length - 1];

        if (args.length == 3) {
            self = args[1];
            binder = binder.insert(1, new Class[] { Object.class }, (Object) null);
            binder = binder.filter(1, getContextAcquisitionFilter());

            guardBinder = guardBinder.insert(1, new Class[] { Object.class }, (Object) null);
            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
        } else if (args.length == 4) {
            self = args[2];
            binder = binder.filter(1, getContextAcquisitionFilter());
            guardBinder = guardBinder.filter(1, getContextAcquisitionFilter());
        }
        
        return linkCall(chain, receiver, self, callArgs, binder, guardBinder);
    }

    public static MethodHandle getReceiverClassAndNameAndValueClassGuard(Class<?> expectedReceiverClass, String expectedName, Class<?> expectedValueClass, Binder binder)
            throws NoSuchMethodException,
            IllegalAccessException {
        return binder
                .drop(1)
                .insert(3, expectedReceiverClass)
                .insert(4, expectedName)
                .insert(5, expectedValueClass)
                .invokeStatic(lookup(), Guards.class, "receiverClassAndNameAndValueClassGuard");
    }

}
