package org.projectodd.rephract.java.reflect;

public class DefaultArrayCoercer extends ArrayCoercer {

    @Override
    public Object[] coerceToObject(Object value) {
        return (Object[]) value;
    }

    @Override
    public boolean[] coerceToBoolean(Object value) {
        return (boolean[]) value;
    }

    @Override
    public byte[] coerceToByte(Object value) {
        return (byte[]) value;
    }

    @Override
    public char[] coerceToChar(Object value) {
        return (char[]) value;
    }

    @Override
    public double[] coerceToDouble(Object value) {
        return (double[]) value;
    }

    @Override
    public float[] coerceToFloat(Object value) {
        return (float[]) value;
    }

    @Override
    public int[] coerceToInt(Object value) {
        return (int[]) value;
    }

    @Override
    public long[] coerceToLong(Object value) {
        return (long[]) value;
    }

    @Override
    public short[] coerceToShort(Object value) {
        return (short[]) value;
    }

}
