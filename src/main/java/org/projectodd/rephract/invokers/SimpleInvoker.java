package org.projectodd.rephract.invokers;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author Bob McWhirter
 */
public class SimpleInvoker implements Invoker {

    private final String name;
    private final MethodType inputType;

    public SimpleInvoker(MethodType inputType) {
        this( "invoke", inputType );
    }

    public SimpleInvoker(String name, MethodType inputType) {
        this.name = name;
        this.inputType = inputType;
    }

    @Override
    public MethodHandle invokerMethodHandle() throws Exception {
        return lookup().findVirtual( getClass(), this.name, this.inputType ).bindTo( this );
    }
}
