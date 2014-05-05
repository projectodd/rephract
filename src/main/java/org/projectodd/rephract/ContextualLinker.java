package org.projectodd.rephract;

import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.filters.NoOpFilter;

import java.lang.invoke.MethodType;

public abstract class ContextualLinker extends Linker {

    public ContextualLinker() {
    }

    public ContextualLinker(LinkLogger logger) {
        super(logger);
    }

    public Filter contextFilter() {
        return NoOpFilter.INSTANCE;
    }

    public Link preLinkGetProperty(Invocation invocation) throws Exception {


        // Arguments must be one of:
        //
        // [ object(receiver) ] with name encoded in operation
        // [ object(receiver), string(name) ]
        // [ object(receiver), ?(context) ] with name encoded in operation
        // [ object(receiver), ?(context), string(name) ]

        MethodType methodType = invocation.methodType();
        int paramCount = methodType.parameterCount();
        String propertyName = invocation.parameter();

        if (paramCount == 1) {
            // no context, no name
            invocation.builder().insert(1, contextFilter());
            invocation.builder().insert(2, propertyName);
        } else if (paramCount == 2) {
            if (propertyName == null) {
                // name, not context
                propertyName = (String) invocation.arguments()[1];
                invocation.builder().insert(1, contextFilter());
            } else {
                // context, not name
                invocation.builder().insert(2, propertyName);
            }
        } else if (paramCount == 3) {
            // name and context
            propertyName = (String) invocation.arguments()[2];
            invocation.builder().filter(1, contextFilter());
        }

        log("[GET_PROPERTY] receiver=%s; propName=%s", invocation.receiver(), propertyName);
        return linkGetProperty(invocation, propertyName);
    }


    @Override
    public Link preLinkSetProperty(Invocation invocation) throws Exception {
        //
        // Arguments must be one of:
        //
        // [ object(receiver) object(value) ] with name encoded in operation
        // [ object(receiver) string(name) object(value) ]
        // [ object(receiver) ?(context) object(value) ] with name encoded in operation
        // [ object(receiver) ?(context) string(name) object(value) ]
        //
        // result should be:
        //
        // [ object(receiver) string(name) object(value) ]

        MethodType methodType = invocation.methodType();
        int paramCount = methodType.parameterCount();
        String propertyName = invocation.parameter();

        Object receiver = invocation.receiver();

        if (paramCount == 2) {
            // no context, no name
            invocation.builder().insert(1, contextFilter());
            invocation.builder().insert(2, propertyName);
        } else if (paramCount == 3) {
            if (propertyName == null) {
                // name, not context
                invocation.builder().insert(1, contextFilter());
                propertyName = (String) invocation.arguments()[1];
            } else {
                // context, not name
                invocation.builder().insert(2, propertyName);
            }
        } else if (paramCount == 4) {
            // name and context
            propertyName = (String) invocation.arguments()[2];
            invocation.builder().filter(1, contextFilter());
        }

        log("[SET_PROPERTY] receiver=%s; propName=%s", receiver, propertyName);

        return linkSetProperty(invocation, propertyName);
    }


    public Link preLinkGetMethod(Invocation invocation) throws Exception {


        // Arguments must be one of:
        //
        // [ object(receiver) ] with name encoded in operation
        // [ object(receiver), string(name) ]
        // [ object(receiver), ?(context) ] with name encoded in operation
        // [ object(receiver), ?(context), string(name) ]

        MethodType methodType = invocation.methodType();
        int paramCount = methodType.parameterCount();
        String propertyName = invocation.parameter();

        if (paramCount == 1) {
            // no context, no name
            invocation.builder().insert(1, propertyName);
        } else if (paramCount == 2) {
            if (propertyName == null) {
                // name, not context
                propertyName = (String) invocation.arguments()[1];
            } else {
                // context, not name
                invocation.builder().drop(1);
                invocation.builder().insert(1, propertyName);
            }
        } else if (paramCount == 3) {
            // name and context
            propertyName = (String) invocation.arguments()[2];
            invocation.builder().drop(1);
        }

        log("[GET_PROPERTY] receiver=%s; propName=%s", invocation.receiver(), propertyName);
        return linkGetMethod(invocation, propertyName);
    }

    public Link preLinkCall(Invocation invocation) throws Exception {


        //  Arguments must be one of:
        //
        //  [ object(receiver) self object[](args) ]
        //  [ object(receiver) context self object[](args) ]
        //
        //  Should result in:
        //
        //  [ object(receiver) context object(self) object[](args) ]
        //

        MethodType methodType = invocation.methodType();
        int paramCount = methodType.parameterCount();

        if (paramCount == 3) {
            // no context
            invocation.builder().insert(1, contextFilter());
        } else if (paramCount == 4) {
            // with context
            invocation.builder().filter(1, contextFilter());
        }

        log("[CALL] receiver=%s", invocation.receiver());

        return linkCall(invocation);
    }

    public Link preLinkConstruct(Invocation invocation) throws Exception {


        //  Arguments must be one of:
        //
        //  [ object(receiver) object[](args) ]
        //  [ object(receiver) context object[](args) ]
        //
        //  Should result in:
        //
        //  [ object(receiver) context object[](args) ]
        //

        MethodType methodType = invocation.methodType();
        int paramCount = methodType.parameterCount();

        if (paramCount == 2) {
            // no context
            invocation.builder().insert(1, contextFilter());
        } else if (paramCount == 3) {
            // with context
            invocation.builder().filter(1, contextFilter());
        }

        log("[CONSTRUCT] receiver=%s", invocation.receiver());

        return linkConstruct(invocation);
    }

}
