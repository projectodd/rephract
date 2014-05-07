package org.projectodd.rephract;

import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ContextualConstructableInvoker implements Invoker {

    public static ContextualConstructableInvoker INSTANCE = new ContextualConstructableInvoker();
    @Override
    public MethodHandle invokerMethodHandle() throws Exception {
        return lookup().findVirtual( ContextualConstructable.class, "construct", methodType( Object.class, Object.class, Object[].class) );
    }
}
