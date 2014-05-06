package org.projectodd.rephract;

import org.projectodd.rephract.builder.LinkBuilder;

import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class Invocation {


    public enum Type {
        GET_PROPERTY,
        SET_PROPERTY,
        GET_ELEMENT,
        SET_ELEMENT,
        GET_METHOD,
        CALL,
        CONSTRUCT,
        OTHER
    }

    private final Type type;
    private final String parameter;
    private final MethodType methodType;
    private final LinkBuilder builder;
    private final Object receiver;
    private final Object[] arguments;


    public Invocation(Type type, MethodType methodType, Object receiver, Object[] arguments) {
        this(type, null, methodType, receiver, arguments );
    }

    public Invocation(Type type, String parameter, MethodType methodType, Object receiver, Object[] arguments ) {
        this.type = type;
        this.parameter = parameter;
        this.methodType = methodType;
        this.builder = new LinkBuilder( methodType, arguments );
        this.receiver = receiver;
        this.arguments = arguments;
    }

    public Type type() {
        return this.type;
    }

    public String parameter() {
        return this.parameter;
    }

    public MethodType methodType() {
        return this.methodType;
    }

    public Object receiver() {
        return this.receiver;
    }

    public Object[] arguments() {
        return this.arguments;
    }

    public LinkBuilder builder() {
        return this.builder;
    }

}
