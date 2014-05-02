package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author Bob McWhirter
 */
public class SimpleStatefulGuard implements Guard {

    private final String name;

    public SimpleStatefulGuard() {
        this( "guard" );
    }

    public SimpleStatefulGuard(String name) {
        this.name = name;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws NoSuchMethodException, IllegalAccessException {
        return lookup().findVirtual( getClass(), name, inputType ).bindTo( this );
    }
}
