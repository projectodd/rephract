package org.projectodd.rephract;

import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ConstructableInvoker implements Invoker {

    public static ConstructableInvoker INSTANCE = new ConstructableInvoker();
    @Override
    public MethodHandle invokerMethodHandle() throws Exception {
        return lookup().findVirtual( Constructable.class, "construct", methodType( Object.class, Object[].class) );
    }
}
