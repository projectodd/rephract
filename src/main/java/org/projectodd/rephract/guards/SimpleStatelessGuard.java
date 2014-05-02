package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author Bob McWhirter
 */
public class SimpleStatelessGuard implements Guard {

    private final String name;

    public SimpleStatelessGuard() {
        this( "guard" );
    }

    public SimpleStatelessGuard(String name) {
        this.name = name;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws NoSuchMethodException, IllegalAccessException {
        return lookup().findStatic( getClass(), name, inputType );
    }
}
