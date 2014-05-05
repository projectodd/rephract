package org.projectodd.rephract.filters;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class NoOpFilter implements Filter {

    public static NoOpFilter INSTANCE = new NoOpFilter();

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.identity( inputType.parameterType( 0 ) );
    }
}
