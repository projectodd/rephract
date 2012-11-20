package org.projectodd.linkfusion.strategy.javabeans;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.strategy.NonContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaBeansLinkStrategy extends NonContextualLinkStrategy {

    private Map<Class<?>, ClassManager> classManagers = new HashMap<>();

    public JavaBeansLinkStrategy() {
    }

    @Override
    protected StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException {
        ClassManager classManager = getClassManager(receiver);
        MethodHandle reader = classManager.getPropertyReader(propName);

        if (reader == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(1)
                .convert(Object.class, receiver.getClass())
                .invoke(reader);

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder) );
    }
    
    @Override
    protected StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException {
        ClassManager classManager = getClassManager(receiver);
        MethodHandle writer = classManager.getPropertyWriter(propName, value.getClass() );

        if (writer == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(1)
                .convert(Object.class, receiver.getClass())
                .invoke(writer);

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), propName, guardBinder) );
    }
    
    @Override
    protected StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException {
        ClassManager classManager = getClassManager(receiver);
        UnboundMethod unboundMethod = classManager.getMethod(methodName);

        if (unboundMethod == null) {
            return chain.nextStrategy();
        }

        MethodHandle method = binder.drop(0, 2)
                .insert(0, unboundMethod)
                .identity();

        return new StrategicLink(method, getReceiverClassAndNameGuard(receiver.getClass(), methodName, guardBinder) );
    }

    private ClassManager getClassManager(Object obj) {
        Class<?> targetClass = obj.getClass();
        ClassManager classManager = this.classManagers.get(targetClass);
        if (classManager == null) {
            classManager = new ClassManager(targetClass);
            this.classManagers.put(targetClass, classManager);
        }
        return classManager;
    }

}
