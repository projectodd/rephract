package org.projectodd.linkfusion;

import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

class InvocationRequestImpl implements InvocationRequest {
    
    private LinkPlan plan;
    private Object[] arguments;

    InvocationRequestImpl(LinkPlan plan, Object[] arguments) {
        this.plan = plan;
        this.arguments = arguments;
    }
    
    public boolean isFusionRequest() {
        return this.plan.isFusionRequest();
    }
    
    public List<Operation> getOperations() {
        return this.plan.getOperations();
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
