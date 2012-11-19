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

    public static MethodHandle getReceiverInstanceOfGuard(Class<?> expected, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Binder.from( type.changeReturnType( boolean.class ) )
                .printType()
                .drop(1, type.parameterCount() -1 )
                .printType()
                .insert(1, expected)
                .printType()
                .invokeStatic(lookup(), Guards.class, "receiverInstanceOf" );
    }
    
    public static boolean receiverInstanceOf(Object receiver, Class<?> expected) {
        System.err.println( "GUARD receiver: " + receiver );
        System.err.println( "GUARD expected: " + expected );
        return ( expected.isAssignableFrom( receiver.getClass() ) );
    }

}
