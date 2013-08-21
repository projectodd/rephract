package org.projectodd.rephract.mop.java;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class ArrayCoercer {
    public Object[] coerceToObjectInternal(Object value) {
        Object[] objectArray = coerceToObject(value);
        Class<?> target = objectArray[0].getClass();
        Object[] targetArray = (Object[]) Array.newInstance(target, objectArray.length);
        return Arrays.asList(objectArray).toArray(targetArray);
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
