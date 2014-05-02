package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class NotGuard implements Guard {

    private final Guard guard;

    private static final MethodHandle METHOD_HANDLE;

    static {
        MethodHandle methodHandle = null;
        try {
            methodHandle = lookup().findStatic( NotGuard.class, "filter", methodType( boolean.class, boolean.class ) );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        METHOD_HANDLE = methodHandle;
    }

    public NotGuard(Guard guard) {
        this.guard = guard;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.filterReturnValue( this.guard.methodHandle(inputType), METHOD_HANDLE );
    }

    public static boolean filter(boolean input) {
        return ! input;
    }
}
