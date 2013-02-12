package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.HashMap;
import java.util.Map;

import static java.lang.invoke.MethodType.*;

public class CoercionMatrix {
    
    private static class CoercionEntry {
        
        int distance;
        MethodHandle filter;

        CoercionEntry(int distance, MethodHandle filter) {
            this.distance = distance;
            this.filter = filter;
        }
        
    }
    
    private Map<Class<?>,Map<Class<?>,CoercionEntry>> matrix = new HashMap<>();
    
    public CoercionMatrix() throws NoSuchMethodException, IllegalAccessException {
        initDefaultIntegerCoercions();
        initDefaultPrimitiveIntegerCoercions();
        initDefaultLongCoercions();
        initDefaultPrimitiveLongCoercions();
        initDefaultBooleanCoercions();
        initDefaultPrimitiveBooleanCoercions();
    }
    
    protected void addCoercion(int distance, Class<?> toType, Class<?> fromType, MethodHandle filter) {
        Map<Class<?>, CoercionEntry> row = this.matrix.get(toType);
        
        if ( row == null ) {
            row = new HashMap<>();
            this.matrix.put( toType, row );
        }
        
        row.put( fromType, new CoercionEntry( distance, filter ) );
    }
    
    public int isCompatible(Class<?> target, Class<?> actual) {
        if ( actual == null || target.isAssignableFrom( actual ) ) {
            return 0;
        }
        
        Map<Class<?>, CoercionEntry> row = this.matrix.get(target);
        
        if ( row == null ) {
            return -1;
        }
        
        CoercionEntry entry = row.get( actual );
        if ( entry != null ) {
            return entry.distance;
        }
        return -1;
    }
    
    public MethodHandle getFilter(Class<?> target, Class<?> actual) {
        Map<Class<?>, CoercionEntry> row = this.matrix.get(target);
        
        if ( row == null ) {
            return MethodHandles.identity( target );
        }
        
        CoercionEntry entry = row.get(actual);
        if ( entry != null ) {
            return entry.filter;
        }
        
        return MethodHandles.identity(target);
    }

    private void initDefaultIntegerCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion(0, Integer.class, int.class, MethodHandles.identity(int.class));
        addCoercion(0, Integer.class, Integer.class, MethodHandles.identity(Integer.class));
        addCoercion( 1, Integer.class, Long.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        addCoercion( 1, Integer.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        addCoercion( 2, Integer.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
        addCoercion( 2, Integer.class, Double.class, lookup.findStatic(CoercionMatrix.class, "numberToInteger", methodType( Integer.class, Number.class ) ) );
    }
    
    private void initDefaultPrimitiveIntegerCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion( 0, int.class, int.class, MethodHandles.identity(int.class) );
        addCoercion( 0, int.class, Integer.class, MethodHandles.identity(int.class) );
        addCoercion( 1, int.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        addCoercion( 1, int.class, Long.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        addCoercion( 2, int.class, Double.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
        addCoercion( 2, int.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveInteger", methodType( int.class, Number.class ) ) );
    }
    
    private void initDefaultLongCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion( 0, Long.class, long.class, MethodHandles.identity(long.class) );
        addCoercion( 0, Long.class, Long.class, MethodHandles.identity(Long.class) );
        addCoercion( 1, Long.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        addCoercion( 1, Long.class, Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        addCoercion( 2, Long.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
        addCoercion( 2, Long.class, Double.class, lookup.findStatic(CoercionMatrix.class, "numberToLong", methodType( Long.class, Number.class ) ) );
    }
    
    private void initDefaultPrimitiveLongCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion( 0, long.class, long.class, MethodHandles.identity(long.class) );
        addCoercion( 0, long.class, Long.class, MethodHandles.identity(long.class) );
        addCoercion( 1, long.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        addCoercion( 1, long.class, Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        addCoercion( 2, long.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
        addCoercion( 2, long.class, Double.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveLong", methodType( long.class, Number.class ) ) );
    }
    
    private void initDefaultBooleanCoercions() throws NoSuchMethodException, IllegalAccessException {
        addCoercion( 0, Boolean.class, boolean.class, MethodHandles.identity(boolean.class) );
        addCoercion( 0, Boolean.class, Boolean.class, MethodHandles.identity(Boolean.class) );
    }
    
    private void initDefaultPrimitiveBooleanCoercions() throws NoSuchMethodException, IllegalAccessException {
        addCoercion( 0, boolean.class, boolean.class, MethodHandles.identity(boolean.class) );
        addCoercion( 0, boolean.class, Boolean.class, MethodHandles.identity(boolean.class) );
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
