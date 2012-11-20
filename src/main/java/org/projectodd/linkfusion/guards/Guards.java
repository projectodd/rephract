package org.projectodd.linkfusion.guards;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class Guards {

    static Lookup lookup() {
        return MethodHandles.lookup();
    }


    public static boolean receiverClassGuard(Object receiver, Class<?> expected) {
        return (expected.isAssignableFrom(receiver.getClass()));
    }

    
    public static boolean receiverClassAndNameGuard(Object receiver, String name, Class<?> expectedReceiver, String expectedName) {
        return ( expectedReceiver.isAssignableFrom(receiver.getClass()) && ( expectedName.equals( name ) ) );
    }
    
    public static boolean receiverClassAndNameAndValueClassGuard(Object receiver, String name, Object value, Class<?> expectedReceiver, String expectedName, Class<?> expectedValueClass ) {
        return ( expectedReceiver.isAssignableFrom(receiver.getClass()) && ( expectedName.equals( name ) ) && expectedValueClass.isAssignableFrom( value.getClass() ) );
    }
    
    public static boolean identityGuard(Object object, Object expectedObject) {
        return object == expectedObject;
    }

}
