package org.projectodd.linkfusion.strategy.javabeans;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.guards.Guards;
import org.projectodd.linkfusion.strategy.BaseLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaBeansLinkStrategy extends BaseLinkStrategy {

    private Map<Class<?>, ClassManager> classManagers = new HashMap<>();

    @Override
    protected StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName) {
        ClassManager classManager = getClassManager(receiver);

        MethodHandle reader = classManager.getPropertyReader(propName);

        if (reader == null) {
            return chain.nextStrategy();
        }

        try {
            MethodHandle bridge = Binder.from(chain.getRequest().type())
                    .drop(1, chain.getRequest().type().parameterCount() - 1)
                    .convert(Object.class, receiver.getClass())
                    .invoke(reader);

            return new StrategicLink(bridge, Guards.getReceiverClassGuard(receiver.getClass(), chain.getRequest().type()));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value) {
        ClassManager classManager = getClassManager(receiver);

        MethodHandle writer = classManager.getPropertyWriter(propName, value.getClass());
        
        if (writer == null) {
            return chain.nextStrategy();
        }

        try {
            MethodHandle bridge = Binder.from(chain.getRequest().type())
                    .drop(1, chain.getRequest().type().parameterCount() - 2 )
                    .invoke(writer);

            return new StrategicLink(bridge, Guards.getReceiverAndArgumentClassGuard(receiver.getClass(), value.getClass(), chain.getRequest().type()));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    @Override
    protected StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName) {
        ClassManager classManager = getClassManager(receiver);

        UnboundMethod method = classManager.getMethod(methodName);

        if (method == null) {
            return chain.nextStrategy();
        }

        try {
            MethodHandle bridge = Binder.from(chain.getRequest().type())
                    .printType()
                    .drop(0, chain.getRequest().type().parameterCount() )
                    .printType()
                    .insert(0, method )
                    .printType()
                    .identity();

            return new StrategicLink(bridge, Guards.getReceiverClassAndMethodNameGuard(receiver.getClass(), methodName, chain.getRequest().type()));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
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
