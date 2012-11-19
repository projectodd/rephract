package org.projectodd.linkfusion.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import com.headius.invokebinder.Binder;

public class Guards {

    static Lookup lookup() {
        return MethodHandles.lookup();
    }

    public static MethodHandle getReceiverClassGuard(Class<?> expected, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Binder.from(type.changeReturnType(boolean.class))
                .printType()
                .drop(1, type.parameterCount() - 1)
                .printType()
                .insert(1, expected)
                .printType()
                .invokeStatic(lookup(), Guards.class, "receiverClassGuard");
    }

    public static MethodHandle getReceiverAndArgumentClassGuard(Class<?> expectedReceiver, Class<?> expectedArgument, MethodType type) throws NoSuchMethodException,
            IllegalAccessException {
        return Binder.from(type.changeReturnType(boolean.class))
                .printType()
                .drop(2, type.parameterCount() - 2)
                .convert(boolean.class, Object.class, Object.class)
                .printType()
                .insert(2, expectedReceiver)
                .insert(3, expectedArgument)
                .printType()
                .invokeStatic(lookup(), Guards.class, "receiverAndArgumentClassGuard");
    }
    
    public static MethodHandle getReceiverClassAndNameGuard(Class<?> expectedReceiver, String expectedName, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Binder.from( type.changeReturnType( boolean.class ))
                .drop(2, type.parameterCount() - 2 )
                .convert( boolean.class, Object.class, String.class )
                .insert(2, expectedReceiver )
                .insert( 3, expectedName )
                .invokeStatic(lookup(), Guards.class, "receiverClassAndNameGuard" );
        
    }
    
    public static boolean receiverClassGuard(Object receiver, Class<?> expected) {
        System.err.println("GUARD receiver: " + receiver);
        System.err.println("GUARD expected: " + expected);
        return (expected.isAssignableFrom(receiver.getClass()));
    }

    public static boolean receiverAndArgumentClassGuard(Object receiver, Object argument, Class<?> expectedReceiver, Class<?> expectedArgument) {
        return (expectedReceiver.isAssignableFrom(receiver.getClass()) && expectedArgument.isAssignableFrom(argument.getClass()));
    }
    
    public static boolean receiverClassAndNameGuard(Object receiver, String name, Class<?> expectedReceiver, String expectedName) {
        return ( expectedReceiver.isAssignableFrom(receiver.getClass()) && ( expectedName.equals( name ) ) );
    }

}
