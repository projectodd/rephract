package org.projectodd.rephract;

import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class InvokableInvoker implements Invoker {

    public static InvokableInvoker INSTANCE = new InvokableInvoker();
    @Override
    public MethodHandle methodHandle(MethodType type) throws Exception {
        return lookup().findVirtual( Invokable.class, "invoke", methodType( Object.class, Object.class, Object[].class) );
    }
}
