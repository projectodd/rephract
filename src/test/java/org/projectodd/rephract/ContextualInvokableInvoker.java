package org.projectodd.rephract;

import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ContextualInvokableInvoker implements Invoker {

    public static ContextualInvokableInvoker INSTANCE = new ContextualInvokableInvoker();
    @Override
    public MethodHandle invokerMethodHandle() throws Exception {
        return lookup().findVirtual( ContextualInvokable.class, "invoke", methodType( Object.class, Object.class, Object.class, Object[].class) );
    }
}
