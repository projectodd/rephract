package org.projectodd.rephract.mop;

import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.Operation;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;

import com.headius.invokebinder.Binder;

public abstract class NonContextualLinkStrategy extends BaseMetaObjectProtocolLinkStrategy {

    public NonContextualLinkStrategy() {
    }
    
    public NonContextualLinkStrategy(LinkLogger logger) {
        super( logger );
    }

    protected StrategicLink linkGetProperty(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) ] with name encoded in operation
         * [ object(receiver), string(name) ]
         * [ object(receiver), ?(context) ] with name encoded in operation
         * [ object(receiver), ?(context), string(name) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        String propName = op.getParameter();

        if (args.length == 1) {
            // no context, no name
            binder = insertName( binder, op.getParameter() );
            guardBinder = insertName( guardBinder, op.getParameter() );
        } else if (args.length == 2) {
            if (propName == null) {
                // name, not context
                propName = (String) args[1];
            } else {
                // context, not name
                binder = dropContext(binder);
                binder = insertName( binder, propName );
                guardBinder = dropContext(guardBinder);
                guardBinder = insertName( guardBinder, propName );
            }
        } else if (args.length == 3) {
            // name and context
            propName = (String) args[2];
            binder = dropContext(binder);
            guardBinder = dropContext(guardBinder);
        }
        
        log( "[GET_PROPERTY] receiver=" + receiver + "; propName=" + propName );
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
         * [ object(receiver) string(name) object(value) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        Object value = args[args.length - 1];
        String propName = op.getParameter();

        if (args.length == 2) {
            // no context, no name
            binder = insertName( binder, op.getParameter() );
            guardBinder = insertName( guardBinder, op.getParameter() );
        } else if (args.length == 3) {
            if (propName == null) {
                // name, not context
                propName = (String) args[1];
            } else {
                // context, not name
                binder = dropContext(binder);
                binder = insertName( binder, propName );
                guardBinder = dropContext(guardBinder);
                guardBinder = insertName( guardBinder, propName );
            }
        } else if (args.length == 4) {
            // name and context
            propName = (String) args[2];
            binder = dropContext(binder);
            guardBinder = dropContext(guardBinder);
        }

        binder = binder.convert(Object.class, Object.class, String.class, Object.class);
        guardBinder = guardBinder.convert(boolean.class, Object.class, String.class, Object.class);

        log( "[SET_PROPERTY] receiver=" + receiver + "; propName=" + propName );
        return linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    protected StrategicLink linkGetMethod(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) ] with name encoded in operation
         * [ object(receiver), string(name) ]
         * [ object(receiver), ?(context) ] with name encoded in operation
         * [ object(receiver), ?(context), string(name) ]
         * 
         * Should result in:
         * 
         * [ object(receiver), string(name) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        String propName = op.getParameter();

        if (args.length == 1) {
            // no context, no name
            binder = insertName( binder, op.getParameter() );
            guardBinder = insertName( guardBinder, op.getParameter() );
        } else if (args.length == 2) {
            if (propName == null) {
                // name, not context
                propName = (String) args[1];
            } else {
                // context, not name
                binder = dropContext(binder);
                binder = insertName( binder, propName );
                guardBinder = dropContext(guardBinder);
                guardBinder = insertName( guardBinder, propName );
            }
        } else if (args.length == 3) {
            // name and context
            propName = (String) args[2];
            binder = dropContext(binder);
            guardBinder = dropContext(guardBinder);
        }
        
        binder = binder.convert(Object.class, Object.class, String.class);
        guardBinder = guardBinder.convert(boolean.class, Object.class, String.class);

        log( "[GET_METHOD] receiver=" + receiver + "; propName=" + propName );
        return linkGetMethod(chain, receiver, propName, binder, guardBinder);
    }

    protected StrategicLink linkCall(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) self object[](args) ]
         * [ object(receiver) context self object[](args) ]
         * 
         * Should result in:
         * 
         * [ object(receiver) object(self) object[](args) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];

        Object self = null;
        Object[] callArgs = null;

        if (args.length == 3) {
            // no context
            self = args[1];
            callArgs = (Object[]) args[2];
        } else if (args.length == 4) {
            // with context
            binder = dropContext(binder);
            guardBinder = dropContext(guardBinder);
            self = args[2];
            callArgs = (Object[]) args[3];
        }

        log( "[CALL] receiver=" + receiver );
        return linkCall(chain, receiver, self, callArgs, binder, guardBinder);
    }

    protected StrategicLink linkConstruct(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

        /*
         * Arguments must be one of:
         * 
         * [ object(receiver) object[](args) ]
         * [ object(receiver) context object[](args) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];

        Object[] callArgs = null;

        if (args.length == 2) {
            callArgs = (Object[]) args[1];
        } else if (args.length == 3) {
            binder = binder.drop(1);
            guardBinder = guardBinder.drop(1);
            callArgs = (Object[]) args[2];
        }

        log( "[CONSTRUCT] receiver=" + receiver );
        return linkConstruct(chain, receiver, callArgs, binder, guardBinder);
    }
    
    private Binder dropContext(Binder binder) {
        return binder.drop(1);
    }
    
    private Binder insertName(Binder binder, String name) {
        return binder.insert(1, name);
    }

    // ----------------------------------------
    // ----------------------------------------

}
