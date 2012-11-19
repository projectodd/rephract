package org.projectodd.linkfusion.strategy.javabeans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassManager {
    
    private Class<?> target;
    
    private Map<String, MethodHandle> propertyReaders = new HashMap<>();
    private Map<String, List<MethodHandle>> propertyWriters = new HashMap<>();

    public ClassManager(Class<?> target) {
        this.target = target;
        analyze();
    }
    
    private void analyze() {
        Lookup lookup = MethodHandles.lookup();

        Method[] methods = this.target.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                String name = methods[i].getName();
                
                if ( name.startsWith( "get" ) && name.length() > 3 ) {
                    try {
                        propertyReaders.put( propertyName( name ), lookup.unreflect( methods[i] ) );
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if ( name.startsWith( "set" ) && name.length() > 3 ) {
                    List<MethodHandle> writers = this.propertyWriters.get( propertyName( name ) );
                    if ( writers == null ) {
                        writers = new ArrayList<>();
                        this.propertyWriters.put( propertyName( name ), writers );
                    }
                    try {
                        writers.add( lookup.unreflect(methods[i] ) );
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
    
    public Class<?> getTarget() {
        return this.target;
    }
    
    public MethodHandle getPropertyReader(String propertyName) {
        return this.propertyReaders.get( propertyName );
    }
    
    public MethodHandle getPropertyWriter(String propertyName, Class<?> valueClass) {
        List<MethodHandle> writers = this.propertyWriters.get( propertyName );
        System.err.println( "writers for " + propertyName + " // " + writers );
        if ( writers == null ) {
            return null;
        }
        
        for ( MethodHandle each : writers ) {
            if ( each.type().parameterCount() == 2 && each.type().parameterType(1).isAssignableFrom( valueClass ) ) {
                return each;
            }
        }
        return null;
    }
    
    public static String propertyName(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }

}
