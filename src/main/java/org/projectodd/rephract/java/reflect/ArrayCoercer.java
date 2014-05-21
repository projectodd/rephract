package org.projectodd.rephract.java.reflect;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class ArrayCoercer {
    public Object[] coerceToObjectInternal(Object value) {
        Object[] objectArray = coerceToObject(value);
        Class<?> target = determineCommonClass( objectArray );
        Object[] targetArray = (Object[]) Array.newInstance(target, objectArray.length);
        return Arrays.asList(objectArray).toArray(targetArray);
    }

    protected Class<?> determineCommonClass(Object[] array) {
        Class<?> common = null;
        for ( int i = 0 ; i < array.length ; ++i ) {
            if ( common == null ) {
                common = array[i].getClass();
                continue;
            }
            Class<?> compare = array[i].getClass();
            if ( common == compare ) {
                continue;
            }
            while ( ! common.isAssignableFrom( compare ) ) {
                common = common.getSuperclass();
            }
        }
        return common;
    }

    public abstract Object[] coerceToObject(Object value);
    public abstract boolean[] coerceToBoolean(Object value);
    public abstract byte[] coerceToByte(Object value);
    public abstract char[] coerceToChar(Object value);
    public abstract double[] coerceToDouble(Object value);
    public abstract float[] coerceToFloat(Object value);
    public abstract int[] coerceToInt(Object value);
    public abstract long[] coerceToLong(Object value);
    public abstract short[] coerceToShort(Object value);
}
