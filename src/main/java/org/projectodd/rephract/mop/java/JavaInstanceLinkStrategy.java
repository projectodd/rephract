package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.NonContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaInstanceLinkStrategy extends NonContextualLinkStrategy {

    private ResolverManager manager;

    public JavaInstanceLinkStrategy() throws NoSuchMethodException, IllegalAccessException {
        this(new ResolverManager());
    }

    public JavaInstanceLinkStrategy(ResolverManager manager) {
        this.manager = manager;
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        Resolver resolver = getResolver(receiver.getClass());

        MethodHandle reader = resolver.getInstanceResolver().getPropertyReader(propName);

        if (reader != null) {
            MethodHandle method = binder.drop(1)
                    .convert(Object.class, receiver.getClass())
                    .invoke(reader);

            return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
        }

        DynamicMethod candidateMethod = resolver.getInstanceResolver().getMethod(propName);

        if (candidateMethod == null) {
            DynamicMethod get = resolver.getInstanceResolver().getMethod("get");
            if (get == null) {
                return chain.nextStrategy();
            }
            InvocationPlan plan = get.findMethodInvoationPlan(new Object[] { propName });

            if (plan == null) {
                return chain.nextStrategy();
            }

            MethodHandle method = binder
                    .convert(Object.class, Object.class, Object.class)
                    .invoke(plan.getMethodHandle());
            return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
        }

        return chain.nextStrategy();

    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        Resolver resolver = getResolver(receiver.getClass());

        DynamicMethod dynamicMethod = resolver.getInstanceResolver().getMethod(methodName);

        if (dynamicMethod == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(0, 2)
                .insert(0, dynamicMethod)
                .identity();

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), methodName, guardBinder));
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {

        Resolver resolver = getResolver(receiver.getClass());
        DynamicMethod dynamicWriter = resolver.getInstanceResolver().getPropertyWriter(propName);
        
        if (dynamicWriter != null) {
            InvocationPlan plan = dynamicWriter.findMethodInvoationPlan(new Object[] { value });
            
            if (plan != null) {
                MethodHandle method = binder
                        .drop(1)
                        .convert( void.class, Object.class, Object.class )
                        .filter(1, plan.getFilters())
                        .invoke(plan.getMethodHandle());

                return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
            }
        }

        DynamicMethod candidateMethod = resolver.getInstanceResolver().getMethod(propName);

        if (candidateMethod == null) {
            DynamicMethod put = resolver.getInstanceResolver().getMethod("put");

            if (put == null) {
                return chain.nextStrategy();
            }

            InvocationPlan plan = put.findMethodInvoationPlan(new Object[] { propName, value });

            if (plan == null) {
                return chain.nextStrategy();
            }

            MethodHandle method = binder
                    .convert(void.class, Object.class, Object.class, Object.class)
                    .filter(1, plan.getFilters()).invoke(plan.getMethodHandle());

            return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        if (receiver instanceof DynamicMethod && !((DynamicMethod) receiver).isStatic()) {
            DynamicMethod dynamicMethod = (DynamicMethod) receiver;

            InvocationPlan plan = dynamicMethod.findMethodInvoationPlan(args);

            if (plan == null) {
                return chain.nextStrategy();
            }

            Class<?>[] spreadTypes = new Class<?>[args.length];
            for (int i = 0; i < spreadTypes.length; ++i) {
                spreadTypes[i] = Object.class;
            }

            if (receiver instanceof BoundDynamicMethod) {
                MethodHandle method = binder.drop(0)
                        .spread(spreadTypes)
                        .filter(0, plan.getFilters())
                        .invoke(plan.getMethodHandle());

                MethodHandle guard = getCallGuard(receiver, args, guardBinder);
                return new StrategicLink(method, guard);
            } else {
                MethodHandle method = binder.drop(0)
                        .spread(spreadTypes)
                        .filter(1, plan.getFilters())
                        .invoke(plan.getMethodHandle());

                MethodHandle guard = getCallGuard(self, args, guardBinder);
                return new StrategicLink(method, guard);
            }

        }

        return chain.nextStrategy();
    }

    private MethodHandle getCallGuard(Object self, Object[] args, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        Class<?>[] argClasses = new Class<?>[args.length];

        for (int i = 0; i < args.length; ++i) {
            argClasses[i] = args[i].getClass();
        }

        return binder.drop(0)
                .insert(2, self.getClass())
                .insert(3, (Object) argClasses)
                .invokeStatic(lookup(), JavaInstanceLinkStrategy.class, "callGuard");
    }

    public static boolean callGuard(Object self, Object[] args, Class<?> expectedReceiverClass, Class<?>[] expectedArgClasses) {
        if (!expectedReceiverClass.isAssignableFrom(self.getClass())) {
            return false;
        }

        if (args.length != expectedArgClasses.length) {
            return false;
        }

        for (int i = 0; i < args.length; ++i) {
            if (!expectedArgClasses[i].isAssignableFrom(args[i].getClass())) {
                return false;
            }
        }

        return true;
    }

    private Resolver getResolver(Class<?> targetClass) {
        return this.manager.getResolver(targetClass);
    }

}
