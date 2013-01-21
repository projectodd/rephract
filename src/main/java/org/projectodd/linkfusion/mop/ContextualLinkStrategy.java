package org.projectodd.linkfusion.mop;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import org.projectodd.linkfusion.LinkLogger;
import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.guards.Guards;

import com.headius.invokebinder.Binder;

public abstract class ContextualLinkStrategy<T> extends BaseMetaObjectProtocolLinkStrategy {

    private Class<T> runtimeContextClass;

    public ContextualLinkStrategy(Class<T> runtimeContextClass) {
        this.runtimeContextClass = runtimeContextClass;
    }
    
    public ContextualLinkStrategy(Class<T> runtimeContextClass, LinkLogger logger) {
        super( logger );
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

    protected MethodHandle contextAcquisitionFilter() throws NoSuchMethodException, IllegalAccessException {
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
         * 
         * Should produce:
         * 
         * [ object(receiver), T(context), string(name) ]
         */
        Object receiver = chain.getRequest().receiver();

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        String propName = op.getParameter();

        if (args.length == 1) {
            // no name, no context
            binder = insertContext(binder);
            binder = insertName(binder, propName);

            guardBinder = insertContext(guardBinder);
            guardBinder = insertName(guardBinder, propName);
        } else if (args.length == 2) {
            if (propName == null) {
                // name, no context
                propName = (String) args[1];
                binder = insertContext(binder);
                guardBinder = insertContext(guardBinder);
            } else {
                // context, no name
                binder = insertName(binder, propName);
                guardBinder = insertName(guardBinder, propName);
            }
        } else if (args.length == 3) {
            // name and context
            propName = (String) args[2];
            binder = filterContext(binder);
            guardBinder = filterContext(guardBinder);
        }

        binder = binder.convert( Object.class, Object.class, getRuntimeContextClass(), String.class );
        guardBinder = guardBinder.convert( boolean.class, Object.class, getRuntimeContextClass(), String.class );
        
        log( "[GET_PROPERTY] receiver=" + receiver + "; propName=" + propName );
        
        return linkGetProperty(chain, receiver, propName, binder, guardBinder);
    }

    @Override
    protected StrategicLink linkGetMethod(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be either:
         * 
         * [ object(receiver) ] with name encoded in operation
         * [ object(receiver), string(name) ]
         * [ object(receiver), T(context) ] with name encoded in operation
         * [ object(receiver), T(context), string(name) ]
         * 
         * Should produce:
         * 
         * [ object(receiver), T(context), string(name)
         */

        Object receiver = chain.getRequest().receiver();

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        String propName = op.getParameter();

        if (args.length == 1) {
            // no name, no context
            binder = insertContext(binder);
            binder = insertName(binder, propName);

            guardBinder = insertContext(guardBinder);
            guardBinder = insertName(guardBinder, propName);
        } else if (args.length == 2) {
            if (propName == null) {
                // name, no context
                propName = (String) args[1];
                binder = insertContext(binder);
                guardBinder = insertContext(guardBinder);
            } else {
                // context, no name
                binder = insertName(binder, propName);
                guardBinder = insertName(guardBinder, propName);
            }
        } else if (args.length == 3) {
            // name and context
            binder = filterContext(binder);
            guardBinder = filterContext(guardBinder);
            propName = (String) args[2];
        }

        binder = binder.convert( Object.class, Object.class, getRuntimeContextClass(), String.class );
        guardBinder = guardBinder.convert( boolean.class, Object.class, getRuntimeContextClass(), String.class );
        
        log( "[GET_METHOD] receiver=" + receiver + "; propName=" + propName );
        return linkGetMethod(chain, receiver, propName, binder, guardBinder);
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
            // no context, no name
            binder = insertContext(binder);
            binder = insertName( binder, propName );
            guardBinder = insertContext(guardBinder);
            guardBinder = insertName( guardBinder, propName );
        } else if (args.length == 3) {
            if (propName == null) {
                // name, no context
                propName = (String) args[1];
                binder = insertContext(binder);
                guardBinder = insertContext(guardBinder);
            } else {
                // context, no name
                binder = insertName(binder, propName);
                guardBinder = insertName(guardBinder, propName);
            }
        } else if (args.length == 4) {
            // name and context
            propName = (String) args[2];

            binder = filterContext(binder);
            guardBinder = filterContext(guardBinder);
        }
        
        binder = binder.convert( void.class, Object.class, getRuntimeContextClass(), String.class, Object.class );
        guardBinder = guardBinder.convert( boolean.class, Object.class, getRuntimeContextClass(), String.class, Object.class );

        log( "[SET_PROPERTY] receiver=" + receiver + "; propName=" + propName );
        return linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    @Override
    protected StrategicLink linkCall(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException {
        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) object(self) object[](args)
         * [ object(receiver) ?(context) object(self) object[](args)
         * 
         * Should result in:
         * 
         * [ object(receiver) T(context) object(self) object[](args)
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        Object self = null;

        Object[] callArgs = (Object[]) args[args.length - 1];

        if (args.length == 3) {
            // no context
            self = args[1];
            binder = insertContext(binder);
            guardBinder = insertContext(guardBinder);
        } else if (args.length == 4) {
            // context
            self = args[2];
            binder = filterContext(binder);
            guardBinder = filterContext(guardBinder);
        }
        
        binder = binder.convert( Object.class, Object.class, getRuntimeContextClass(), Object.class, Object[].class );
        guardBinder = guardBinder.convert( boolean.class, Object.class, getRuntimeContextClass(), Object.class, Object[].class );

        log( "[CALL] receiver=" + receiver );
        return linkCall(chain, receiver, self, callArgs, binder, guardBinder);
    }

    @Override
    protected StrategicLink linkConstruct(StrategyChain chain, Operation each) throws NoSuchMethodException, IllegalAccessException {
        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) object[](args)
         * [ object(receiver) ?(context) object[](args)
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];

        Object[] callArgs = (Object[]) args[args.length - 1];

        if (args.length == 2) {
            binder = binder.insert(1, new Class[] { Object.class }, (Object) null);
            binder = binder.filter(1, contextAcquisitionFilter());

            guardBinder = guardBinder.insert(1, new Class[] { Object.class }, (Object) null);
            guardBinder = guardBinder.filter(1, contextAcquisitionFilter());
        } else if (args.length == 3) {
            binder = binder.filter(1, contextAcquisitionFilter());
            guardBinder = guardBinder.filter(1, contextAcquisitionFilter());
        }

        log( "[CONSTRUCT] receiver=" + receiver );
        return linkConstruct(chain, receiver, callArgs, binder, guardBinder);
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

    private Binder filterContext(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.filter(1, contextAcquisitionFilter());
    }
    
    private Binder insertContext(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return filterContext( binder.insert(1, new Class[] { Object.class }, (Object) null) );
    }

    private Binder insertName(Binder binder, String name) {
        return binder.insert(2, name);
    }

}
