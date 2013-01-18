package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;

import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.NonContextualLinkStrategy;

import static java.lang.invoke.MethodType.*;
import com.headius.invokebinder.Binder;

public class JavaArrayLinkStrategy extends NonContextualLinkStrategy {

    public JavaArrayLinkStrategy() {
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (!receiver.getClass().isArray()) {
            return chain.nextStrategy();
        }

        if (propName.equals("length")) {
            MethodHandle method = binder
                    .drop(1)
                    .invoke(lookup().findStatic(Array.class, "getLength", methodType(int.class, Object.class)));
            return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
        } else {
            try {
                // attempt parse and throwaway, just to see if it's numerically parseable
                int index = Integer.parseInt(propName);
                MethodHandle method = binder
                        .filter(1, arrayIndexFilter())
                        .invoke(lookup().findStatic(Array.class, "get", methodType(Object.class, Object.class, int.class)));
                return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        return chain.nextStrategy();
    }

    public static MethodHandle arrayIndexFilter() throws NoSuchMethodException, IllegalAccessException {
        return lookup().findStatic(Integer.class, "parseInt", methodType(int.class, String.class));
    }

    /*
     * @Override
     * public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
     * throws NoSuchMethodException, IllegalAccessException {
     * 
     * Resolver resolver = getResolver(receiver.getClass());
     * DynamicMethod dynamicWriter = resolver.getInstanceResolver().getPropertyWriter(propName);
     * 
     * if (dynamicWriter == null) {
     * return chain.nextStrategy();
     * }
     * 
     * // MethodHandle method = dynamicWriter.findMethodHandle(new Object[] { value });
     * InvocationPlan plan = dynamicWriter.findMethodInvoationPlan(new Object[] { value });
     * 
     * if (plan == null) {
     * return chain.nextStrategy();
     * }
     * 
     * MethodHandle method = binder
     * .drop(1)
     * .filter(1, plan.getFilters())
     * .invoke(plan.getMethodHandle());
     * 
     * return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
     * }
     */

}
