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
public class MapGetInvoker implements Invoker {

    public static MapGetInvoker INSTANCE = new MapGetInvoker();

    @Override
    public MethodHandle methodHandle(MethodType type) throws Exception {
        return lookup().findVirtual( Map.class, "get", methodType( Object.class, Object.class ) );
    }
}
