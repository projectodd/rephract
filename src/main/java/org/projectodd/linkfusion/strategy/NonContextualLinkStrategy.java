package org.projectodd.linkfusion.strategy;

import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;

import com.headius.invokebinder.Binder;

public class NonContextualLinkStrategy extends BaseLinkStrategy {

    public NonContextualLinkStrategy() {
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
            binder = binder.insert(1, op.getParameter());
            guardBinder = guardBinder.insert(1, op.getParameter());
        } else if (args.length == 2) {
            if ( propName == null ) {
                propName = (String) args[1];
            }
        } else if (args.length == 3) {
            binder = binder.drop(1);
            guardBinder = guardBinder.drop(1);
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
         * [ object(receiver) string(name) object(value) ]
         */

        Binder binder = Binder.from(chain.getRequest().type());
        Binder guardBinder = Binder.from(chain.getRequest().type().changeReturnType(boolean.class));

        Object[] args = chain.getRequest().arguments();
        Object receiver = args[0];
        Object value = args[ args.length - 1 ];
        String propName = op.getParameter();

        if (args.length == 2) {
            propName = op.getParameter();
            binder = binder.insert(1, op.getParameter());
            guardBinder = guardBinder.insert(1, op.getParameter());
        } else if (args.length == 3) {
            if ( propName == null ) {
                propName = (String) args[1];
            }
        } else if (args.length == 4) {
            propName = (String) args[2];
            binder = binder.drop(1);
            guardBinder = guardBinder.drop(1);
        }
        
        return linkSetProperty(chain, receiver, propName, value, binder, guardBinder );
    }
    
    protected StrategicLink linkGetMethod(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {

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
            binder = binder.insert(1, op.getParameter());
            guardBinder = guardBinder.insert(1, op.getParameter());
        } else if (args.length == 2) {
            if ( propName == null ) {
                propName = (String) args[1];
            }
        } else if (args.length == 3) {
            binder = binder.drop(1);
            guardBinder = guardBinder.drop(1);
        }

        return linkGetMethod(chain, receiver, propName, binder, guardBinder);
    }

    // ----------------------------------------
    // ----------------------------------------

}
