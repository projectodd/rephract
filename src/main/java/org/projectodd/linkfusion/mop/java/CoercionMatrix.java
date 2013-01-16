package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.HashMap;
import java.util.Map;

import static java.lang.invoke.MethodType.*;

public class CoercionMatrix {
    
    private static CoercionMatrix INSTANCE;
    
    static {
        try {
            INSTANCE = new CoercionMatrix();
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public static CoercionMatrix getInstance() {
        return INSTANCE;
    }
    
    private Map<Class<?>,Map<Class<?>,MethodHandle>> matrix = new HashMap<>();
    
    public CoercionMatrix() throws NoSuchMethodException, IllegalAccessException {
        matrix.put( Integer.class, integerCoercions() );
        matrix.put( int.class, primitiveIntegerCoercions() );
    }
    
    public boolean isCompatible(Class<?> target, Class<?> actual) {
        if ( target.isAssignableFrom( actual ) ) {
            return true;
        }
        
        Map<Class<?>, MethodHandle> row = this.matrix.get(target);
        
        if ( row == null ) {
            return false;
        }
        
        if ( row.containsKey( actual ) ) {
            return true;
        }
        return false;
    }
    
    public MethodHandle getFilter(Class<?> target, Class<?> actual) {
        Map<Class<?>, MethodHandle> row = this.matrix.get(target);
        if ( row == null ) {
            return MethodHandles.identity( target );
        }
        
        return row.get(actual);
    }

    private Map<Class<?>, MethodHandle> integerCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        Map<Class<?>, MethodHandle> coercions = new HashMap<>();
        coercions.put( int.class, MethodHandles.identity(int.class) );
        coercions.put( Integer.class, MethodHandles.identity(Integer.class) );
        coercions.put( Long.class, lookup.findStatic(CoercionMatrix.class, "longToInteger", methodType( Integer.class, Long.class ) ) );
        return coercions;
    }
    
    private Map<Class<?>, MethodHandle> primitiveIntegerCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        Map<Class<?>, MethodHandle> coercions = new HashMap<>();
        coercions.put( int.class, MethodHandles.identity(int.class) );
        coercions.put( Integer.class, MethodHandles.identity(int.class) );
        coercions.put( Long.class, lookup.findStatic(CoercionMatrix.class, "longToPrimitiveInteger", methodType( int.class, Long.class ) ) );
        return coercions;
    }
    
    // -------------------------------------------------
    
    public static Integer longToInteger(Long value) {
        return value.intValue();
    }
    
    public static int longToPrimitiveInteger(Long value) {
        return value.intValue();
    }
    
    

}
