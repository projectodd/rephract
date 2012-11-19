package org.projectodd.linkfusion.strategy.javabeans;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class UnboundMethod {
    
    private String name;
    private List<MethodHandle> methods = new ArrayList<MethodHandle>();

    public UnboundMethod(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addMethod(MethodHandle method) {
        this.methods.add( method );
    }

}
