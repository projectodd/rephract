package org.projectodd.rephract;

import org.projectodd.rephract.Invocation.Type;

class Operation {

    private Type type;
    private String parameter;

    Operation(Invocation.Type type, String parameter) {
        this.type = type;
        this.parameter = parameter;
    }

    public Type type() {
        return this.type;
    }

    public String parameter() {
        return this.parameter;
    }
    
    public String toString() {
        return type + ":" + this.parameter;
    }

}
