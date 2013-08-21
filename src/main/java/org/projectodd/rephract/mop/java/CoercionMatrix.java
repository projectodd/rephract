package org.projectodd.rephract.mop.java;

import com.headius.invokebinder.Binder;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static class ArrayCoercionEntry extends CoercionEntry {
        Class<?> fromType;
        ArrayCoercer arrayCoercer;

        ArrayCoercionEntry(int distance, MethodHandle filter, Class<?> fromType, ArrayCoercer arrayCoercer) {
            super(distance, filter);
            this.fromType = fromType;
            this.arrayCoercer = arrayCoercer;
        }
    }
    
    private Map<Class<?>,Map<Class<?>,CoercionEntry>> matrix = new HashMap<>();
    private List<ArrayCoercionEntry> arrayCoercions = new ArrayList<>();
    
    public CoercionMatrix() throws NoSuchMethodException, IllegalAccessException {
        initDefaultIntegerCoercions();
        initDefaultPrimitiveIntegerCoercions();
        initDefaultLongCoercions();
        initDefaultPrimitiveLongCoercions();
        initDefaultDoubleCoercions();
        initDefaultPrimitiveDoubleCoercions();
        initDefaultBooleanCoercions();
        initDefaultPrimitiveBooleanCoercions();
        initDefaultPrimitiveByteCoercions();
        initDefaultArrayCoercions();
    }
    
    protected void addCoercion(int distance, Class<?> toType, Class<?> fromType, MethodHandle filter) {
        Map<Class<?>, CoercionEntry> row = this.matrix.get(toType);
        
        if ( row == null ) {
            row = new HashMap<>();
            this.matrix.put( toType, row );
        }
        
        row.put( fromType, new CoercionEntry( distance, filter ) );
    }
    
    protected void addArrayCoercion(int distance, Class<?> fromType, ArrayCoercer arrayCoercer) throws IllegalAccessException, NoSuchMethodException {
        Lookup lookup = MethodHandles.lookup();
        MethodHandle filter = Binder.from(Object[].class, Object.class)
                .insert(0, arrayCoercer)
                .invokeVirtual(lookup, "coerceFromType");
        this.arrayCoercions.add( new ArrayCoercionEntry( distance, filter, fromType, arrayCoercer ) );
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

    public int isCompatible(Class<?> target, Object actual) {
        int typesAreCompatible = isCompatible(target, actual.getClass());

        if (typesAreCompatible < 0) {
            ArrayCoercionEntry arrayCoercion = getArrayCoercion(target, actual);
            if (arrayCoercion != null) {
                return arrayCoercion.distance;
            }
        }
        return typesAreCompatible;
    }

    protected ArrayCoercionEntry getArrayCoercion(Class<?> target, Object actual) {
        loop: for (ArrayCoercionEntry arrayCoercion : arrayCoercions) {
            if (Object[].class.isAssignableFrom(target) && arrayCoercion.fromType.isAssignableFrom(actual.getClass())) {
                Object[] arrayArgs = arrayCoercion.arrayCoercer.convertToObjectArray(actual);
                for (int i = 0; i < arrayArgs.length; i++) {
                    int arrayParamDistance = (arrayArgs[i] == null ? 0 : isCompatible(target.getComponentType(), arrayArgs[i]));
                    if (arrayParamDistance < 0) {
                        continue loop;
                    }
                }
                return arrayCoercion;
            }
        }
        return null;
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

    public MethodHandle getFilter(Class<?> target, Object actual) {
        Map<Class<?>, CoercionEntry> row = this.matrix.get(target);

        if ( row == null ) {
            ArrayCoercionEntry arrayCoercion = getArrayCoercion(target, actual);
            if (arrayCoercion != null) {
                return arrayCoercion.filter.asType( methodType(target, actual.getClass()));
            }
            return MethodHandles.identity(target);
        }
        
        CoercionEntry entry = row.get(actual.getClass());
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
    
    private void initDefaultPrimitiveByteCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion( 0, byte.class, byte.class, MethodHandles.identity(byte.class) );
        addCoercion( 0, byte.class, Byte.class, MethodHandles.identity(byte.class) );
        addCoercion( 1, byte.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveByte", methodType( byte.class, Number.class ) ) );
        addCoercion( 1, byte.class, Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveByte", methodType( byte.class, Number.class ) ) );
        addCoercion( 1, byte.class, Long.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveByte", methodType( byte.class, Number.class ) ) );
        addCoercion( 2, byte.class, Double.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveByte", methodType( byte.class, Number.class ) ) );
        addCoercion( 2, byte.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveByte", methodType( byte.class, Number.class ) ) );
    }
        
    private void initDefaultDoubleCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion(0, Double.class, double.class, MethodHandles.identity(double.class));
        addCoercion(0, Double.class, Double.class, MethodHandles.identity(Double.class));
        addCoercion( 1, Double.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToDouble", methodType( Double.class, Number.class ) ) );
        addCoercion( 2, Double.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToDouble", methodType( Double.class, Number.class ) ) );
        addCoercion( 2, Double.class, Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToDouble", methodType( Double.class, Number.class ) ) );
        addCoercion( 2, Double.class, Long.class, lookup.findStatic(CoercionMatrix.class, "numberToDouble", methodType( Double.class, Number.class ) ) );
    }
    
    private void initDefaultPrimitiveDoubleCoercions() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        
        addCoercion( 0, double.class, double.class, MethodHandles.identity(double.class) );
        addCoercion( 0, double.class, Double.class, MethodHandles.identity(double.class) );
        addCoercion( 1, double.class, Float.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveDouble", methodType( double.class, Number.class ) ) );
        addCoercion( 2, double.class, Short.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveDouble", methodType( double.class, Number.class ) ) );
        addCoercion( 2, double.class, Integer.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveDouble", methodType( double.class, Number.class ) ) );
        addCoercion( 2, double.class, Long.class, lookup.findStatic(CoercionMatrix.class, "numberToPrimitiveDouble", methodType( double.class, Number.class ) ) );
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

    private void initDefaultArrayCoercions() throws NoSuchMethodException, IllegalAccessException {
        addArrayCoercion( 0, Object[].class, new ArrayCoercer());
    }
    
    // -------------------------------------------------
    
    public static Integer numberToInteger(Number value) {
        return value.intValue();
    }
    
    public static int numberToPrimitiveInteger(Number value) {
        return value.intValue();
    }
    
    public static byte numberToPrimitiveByte(Number value) {
        return value.byteValue();
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
    
    public static double numberToPrimitiveDouble(Number value) {
        return value.doubleValue();
    }
    
    public static Float numberToFloat(Number value) {
        return value.floatValue();
    }
    
    public static float numberToPrimitiveFloat(Number value) {
        return value.floatValue();
    }
    
    

}
