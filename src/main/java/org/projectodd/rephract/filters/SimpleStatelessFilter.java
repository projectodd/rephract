package org.projectodd.rephract.filters;

import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author Bob McWhirter
 */
public class SimpleStatelessFilter implements Filter {

    private final String name;

    public SimpleStatelessFilter() {
        this( "filter" );
    }

    public SimpleStatelessFilter(String name) {
        this.name = name;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws NoSuchMethodException, IllegalAccessException {
        return lookup().findStatic(getClass(), name, inputType);
    }
}
