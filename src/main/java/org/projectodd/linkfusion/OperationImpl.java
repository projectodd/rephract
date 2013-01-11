package org.projectodd.linkfusion;

class OperationImpl implements Operation {
    
    private Type type;
    private String parameter;

    OperationImpl(Type type, String parameter) {
        this.type = type;
        this.parameter = parameter;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String getParameter() {
        return this.parameter;
    }
    
    public String toString() {
        return type + ":" + this.parameter;
    }

}
