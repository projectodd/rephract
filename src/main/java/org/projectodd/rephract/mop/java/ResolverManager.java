package org.projectodd.rephract.mop.java;

import java.util.HashMap;
import java.util.Map;

public class ResolverManager {
    
    private CoercionMatrix coercionMatrix;
    private Map<Class<?>, Resolver> resolvers = new HashMap<>();
    
    public ResolverManager() throws NoSuchMethodException, IllegalAccessException {
        this.coercionMatrix = new CoercionMatrix();
    }
    
    public ResolverManager(CoercionMatrix coercionMatrix) {
        this.coercionMatrix = coercionMatrix;
    }
    
    public Resolver getResolver(Class<?> targetClass) {
        if ( targetClass == null ) {
            return null;
        }
        
        Resolver resolver = resolvers.get( targetClass );
        if ( resolver == null ) {
            resolver = new Resolver( this.coercionMatrix, targetClass );
            this.resolvers.put( targetClass, resolver );
        }
        return resolver;
    }

}
