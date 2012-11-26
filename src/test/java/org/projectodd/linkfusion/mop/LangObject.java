package org.projectodd.linkfusion.mop;

import java.util.HashMap;
import java.util.Map;

public class LangObject {
    
    private Map<String,Object> properties = new HashMap<>();
    
    public void put(LangContext context, String name, Object value) {
        if ( context == null ) {
            throw new IllegalArgumentException( "context must be provided" );
        }
        this.properties.put( name, value );
    }
    
    public Object get(LangContext context, String name) {
        if ( context == null ) {
            throw new IllegalArgumentException( "context must be provided" );
        }
        return this.properties.get( name );
    }

}
