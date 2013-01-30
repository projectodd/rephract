package org.projectodd.rephract;

import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

class InvocationRequestImpl implements InvocationRequest {
    
    private LinkPlan plan;
    private Object[] arguments;
    private Operation operation;

    InvocationRequestImpl(LinkPlan plan, Operation operation, Object[] arguments) {
        this.plan = plan;
        this.arguments = arguments;
        this.operation = operation;
    }
    
    public boolean isFusionRequest() {
        return this.plan.isFusionRequest();
    }
    
    public Operation getOperation() {
        return this.operation;
    }

    @Override
    public String getName() {
        return this.plan.getName();
    }

    @Override
    public MethodType type() {
        return this.plan.type();
    }

    @Override
    public Object receiver() {
        if ( this.arguments.length >= 1 ) {
            return this.arguments[0];
        }
        return null;
    }

    @Override
    public Object[] arguments() {
        return this.arguments.clone();
    }

    @Override
    public List<Object> argumentsList() {
        return Arrays.asList( this.arguments );
    }

}
