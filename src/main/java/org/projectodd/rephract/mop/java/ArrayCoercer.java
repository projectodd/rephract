package org.projectodd.rephract.mop.java;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayCoercer {
    public Object[] convertToObjectArray(Object value) {
        return (Object[]) value;
    }

    public Object[] coerceFromType(Object value) {
        Object[] objectArray = convertToObjectArray(value);
        Class<?> target = objectArray[0].getClass();
        Object[] targetArray = (Object[]) Array.newInstance(target, objectArray.length);
        return Arrays.asList(objectArray).toArray(targetArray);
    }
}
