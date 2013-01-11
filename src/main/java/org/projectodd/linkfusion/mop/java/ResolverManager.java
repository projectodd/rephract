package org.projectodd.linkfusion.mop.java;

import java.util.HashMap;
import java.util.Map;

public class ResolverManager {
    
    private Map<Class<?>, Resolver> resolvers = new HashMap<>();
    
    public ResolverManager() {
        
    }
    
    public Resolver getResolver(Class<?> targetClass) {
        Resolver resolver = resolvers.get( targetClass );
        if ( resolver == null ) {
            resolver = new Resolver( targetClass );
            this.resolvers.put( targetClass, resolver );
        }
        return resolver;
    }

}
