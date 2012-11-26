package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.NonContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaLinkStrategy extends NonContextualLinkStrategy {

    private Map<Class<?>, ClassManager> classManagers = new HashMap<>();

    public JavaLinkStrategy() {
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        ClassManager classManager = getClassManager(receiver.getClass());
        MethodHandle reader = classManager.getPropertyReader(propName);

        if (reader == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(1)
                .convert(Object.class, receiver.getClass())
                .invoke(reader);

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {
        
        ClassManager classManager = getClassManager(receiver.getClass());
        MethodHandle writer = classManager.getPropertyWriter(propName, value.getClass());
        
        if (writer == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(1)
                .convert(Object.class, receiver.getClass())
                .invoke(writer);

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder));
    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        ClassManager classManager = getClassManager(receiver.getClass());
        DynamicMethod dynamicMethod = classManager.getMethod(methodName);

        if (dynamicMethod == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(0, 2)
                .insert(0, dynamicMethod)
                .identity();

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), methodName, guardBinder));
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        if (receiver instanceof DynamicMethod) {
            DynamicMethod dynamicMethod = (DynamicMethod) receiver;

            MethodHandle method = dynamicMethod.findMethod(args);

            if (method == null) {
                return chain.nextStrategy();
            }

            Class<?>[] spreadTypes = new Class<?>[args.length];
            for (int i = 0; i < spreadTypes.length; ++i) {
                spreadTypes[i] = Object.class;
            }

            method = binder.drop(0)
                    .spread(spreadTypes)
                    .invoke(method);

            MethodHandle guard = getCallGuard(receiver, args, guardBinder);

            return new StrategicLink(method, guard);

        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (receiver instanceof Class<?>) {
            Class<?> javaClass = (Class<?>) receiver;

            ClassManager classManager = getClassManager(javaClass);

            DynamicConstructor dynamicCtor = classManager.getConstructor();

            MethodHandle ctor = dynamicCtor.findConstructor(args);

            if (ctor == null) {
                return chain.nextStrategy();
            }

            System.err.println("CTOR: " + ctor);

            Class<?>[] spreadTypes = new Class<?>[args.length];
            for (int i = 0; i < spreadTypes.length; ++i) {
                spreadTypes[i] = Object.class;
            }

            System.err.println("_---_");
            ctor = binder.printType()
                    .drop(0)
                    .spread(spreadTypes)
                    .printType()
                    .invoke(ctor);

            MethodHandle guard = getConstructGuard(javaClass, args, guardBinder);

            return new StrategicLink(ctor, guard);
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
                .invokeStatic(lookup(), JavaLinkStrategy.class, "callGuard");
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
                .invokeStatic(lookup(), JavaLinkStrategy.class, "constructGuard");
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

    private ClassManager getClassManager(Class<?> targetClass) {
        ClassManager classManager = this.classManagers.get(targetClass);
        if (classManager == null) {
            classManager = new ClassManager(targetClass);
            this.classManagers.put(targetClass, classManager);
        }
        return classManager;
    }

}
