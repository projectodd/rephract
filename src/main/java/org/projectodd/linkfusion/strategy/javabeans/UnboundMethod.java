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
    
    public MethodHandle findMethod(Object[] args) {
        for ( MethodHandle each : methods ) {
            if ( (each.type().parameterCount()-1) == args.length ) {
                if ( parametersMatch( each.type().parameterArray(), args )) {
                return each;
                }
            }
        }
        
        return null;
    }

    private boolean parametersMatch(Class<?>[] paramTypes, Object[] args) {
        for ( int i = 0 ; i < args.length ; ++i ) {
            if ( ! paramTypes[i+1].isAssignableFrom( args[i].getClass() ) ) {
                return false;
            }
        }
        
        return true;
    }

}
