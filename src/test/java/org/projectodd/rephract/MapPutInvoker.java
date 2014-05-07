package org.projectodd.rephract;

import org.projectodd.rephract.invokers.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class MapPutInvoker implements Invoker {

    public static MapPutInvoker INSTANCE = new MapPutInvoker();

    @Override
    public MethodHandle invokerMethodHandle() throws Exception {
        return lookup().findVirtual( Map.class, "put", methodType( Object.class, Object.class, Object.class ) );
    }
}
