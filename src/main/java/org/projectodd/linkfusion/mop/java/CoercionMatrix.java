package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.HashMap;
import java.util.Map;

import static java.lang.invoke.MethodType.*;

public class CoercionMatrix {
    
    private Map<Class<?>,Map<Class<?>,MethodHandle>> matrix = new HashMap<>();
    
    public CoercionMatrix() throws NoSuchMethodException, IllegalAccessException {
        matrix.put( Integer.class, integerCoercions() );
        matrix.put( int.class, primitiveIntegerCoercions() );
        matrix.put( Long.class, longCoercions() );
        matrix.put( long.class, primitiveLongCoercions() );
    }
    
    protected void addCoercion(Class<?> toType, Class<?> fromType, MethodHandle filter) {
        Map<Class<?>, MethodHandle> row = this.matrix.get(toType);
        
        if ( row == null ) {
            row = new HashMap<>();
            this.matrix.put( toType, row );
        }
        
        row.put( fromType, filter );
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
        coercions.put( Long.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        coercions.put( Short.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        coercions.put( Float.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        coercions.put( Double.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        return coercions;
    }
    
    private Map<Class<?>, MethodHandle> primitiveIntegerCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        Map<Class<?>, MethodHandle> coercions = new HashMap<>();
        coercions.put( int.class, MethodHandles.identity(int.class) );
        coercions.put( Integer.class, MethodHandles.identity(int.class) );
        coercions.put( Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        coercions.put( Long.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        coercions.put( Double.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        coercions.put( Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        return coercions;
    }
    
    private Map<Class<?>, MethodHandle> longCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        Map<Class<?>, MethodHandle> coercions = new HashMap<>();
        coercions.put( long.class, MethodHandles.identity(long.class) );
        coercions.put( Long.class, MethodHandles.identity(Long.class) );
        coercions.put( Short.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        coercions.put( Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        coercions.put( Float.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        coercions.put( Double.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        return coercions;
    }
    
    private Map<Class<?>, MethodHandle> primitiveLongCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        Map<Class<?>, MethodHandle> coercions = new HashMap<>();
        coercions.put( long.class, MethodHandles.identity(long.class) );
        coercions.put( Long.class, MethodHandles.identity(Long.class) );
        coercions.put( Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        coercions.put( Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        coercions.put( Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        coercions.put( Double.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        return coercions;
    }
    
    // -------------------------------------------------
    
    public static Integer numberToInteger(Number value) {
        return value.intValue();
    }
    
    public static int numberToPrimitiveInteger(Number value) {
        return value.intValue();
    }
    
    public static Long numberToLong(Number value) {
        return value.longValue();
    }
    
    public static long numberToPrimitiveLong(Number value) {
        return value.longValue();
    }
    
    public static Short numberToShort(Number value) {
        return value.shortValue();
    }
    
    public static short numberToPrimitiveShort(Number value) {
        return value.shortValue();
    }
    
    public static Double numberToDouble(Number value) {
        return value.doubleValue();
    }
    
    public static double numberToDoublePrimitiveDouble(Number value) {
        return value.doubleValue();
    }
    
    public static Float numberToFloat(Number value) {
        return value.floatValue();
    }
    
    public static float numberToPrimitiveFloat(Number value) {
        return value.floatValue();
    }
    
    

}
