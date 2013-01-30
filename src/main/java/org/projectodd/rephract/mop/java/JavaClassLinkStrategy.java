package org.projectodd.rephract.mop.java;

import java.lang.invoke.MethodHandle;

import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.NullLinkLogger;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.NonContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaClassLinkStrategy extends NonContextualLinkStrategy {

    private ResolverManager manager;

    public JavaClassLinkStrategy() throws NoSuchMethodException, IllegalAccessException {
        this(new ResolverManager());
    }
    
    public JavaClassLinkStrategy(ResolverManager manager) {
        this( new NullLinkLogger(), manager );
    }
    
    public JavaClassLinkStrategy(LinkLogger logger, ResolverManager manager) {
        super( logger );
        this.manager = manager;
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        
        log( "receiver: " + receiver + " // " + propName );

        if (!(receiver instanceof Class)) {
            return chain.nextStrategy();
        }

        Resolver resolver = getResolver((Class<?>) receiver);
        MethodHandle reader = resolver.getClassResolver().getPropertyReader(propName);
        
        log( "reader: " + reader );

        if (reader == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder
                .drop(0, 2)
                .convert(Object.class, receiver.getClass())
                .invoke(reader);

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {

        if (!(receiver instanceof Class)) {
            return chain.nextStrategy();
        }

        Resolver resolver = getResolver((Class<?>) receiver);
        DynamicMethod writer = resolver.getClassResolver().getPropertyWriter(propName);

        if (writer == null) {
            return chain.nextStrategy();
        }

        InvocationPlan plan = writer.findMethodInvoationPlan(new Object[] { value });
        if (plan == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(1)
                .convert(Object.class, receiver.getClass())
                .filter(1, plan.getFilters())
                .invoke(plan.getMethodHandle());

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (!(receiver instanceof Class)) {
            return chain.nextStrategy();
        }

        Resolver resolver = getResolver((Class<?>) receiver);
        DynamicMethod dynamicMethod = resolver.getClassResolver().getMethod(methodName);

        if (dynamicMethod == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(0, 2)
                .insert(0, dynamicMethod)
                .identity();

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), methodName, guardBinder));
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (!(receiver instanceof Class<?>)) {
            return chain.nextStrategy();
        }
        Resolver resolver = getResolver((Class<?>) receiver);

        DynamicConstructor dynamicCtor = resolver.getClassResolver().getConstructor();

        // System.err.println( "dynamic ctor: " + dynamicCtor );

        // MethodHandle ctor = dynamicCtor.findConstructorHandle(args);
        InvocationPlan plan = dynamicCtor.findConstructorInvocationPlan(args);

        // System.err.println("PLAN: " + plan);

        if (plan == null) {
            return chain.nextStrategy();
        }

        Class<?>[] spreadTypes = new Class<?>[args.length];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = Object.class;
        }

        // System.err.println("FILTERS: " + Arrays.asList(plan.getFilters()));

        MethodHandle ctor = binder
                .drop(0)
                .spread(spreadTypes)
                .filter(0, plan.getFilters())
                .invoke(plan.getMethodHandle());

        MethodHandle guard = getConstructGuard((Class<?>) receiver, args, guardBinder);

        return new StrategicLink(ctor, guard);

    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        if (receiver instanceof DynamicMethod && ((DynamicMethod) receiver).isStatic()) {
            DynamicMethod dynamicMethod = (DynamicMethod) receiver;

            InvocationPlan plan = dynamicMethod.findMethodInvoationPlan(args);

            if (plan == null) {
                return chain.nextStrategy();
            }

            Class<?>[] spreadTypes = new Class<?>[args.length];
            for (int i = 0; i < spreadTypes.length; ++i) {
                spreadTypes[i] = Object.class;
            }

            MethodHandle method = binder
                    .drop(0, 2)
                    .spread(spreadTypes)
                    .filter(0, plan.getFilters() )
                    .invoke(plan.getMethodHandle());

            MethodHandle guard = getCallGuard(receiver, args, guardBinder);

            return new StrategicLink(method, guard);

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
                .invokeStatic(lookup(), JavaClassLinkStrategy.class, "callGuard");
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

    private MethodHandle getConstructGuard(Class<?> targetClass, Object[] args, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        Class<?>[] argClasses = new Class<?>[args.length];

        for (int i = 0; i < args.length; ++i) {
            argClasses[i] = args[i].getClass();
        }

        return binder
                .insert(2, targetClass)
                .insert(3, (Object) argClasses)
                .invokeStatic(lookup(), JavaClassLinkStrategy.class, "constructGuard");
    }

    public static boolean constructGuard(Object targetClass, Object[] args, Class<?> expectedTargetClass, Class<?>[] expectedArgClasses) {
        if (targetClass != expectedTargetClass) {
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
