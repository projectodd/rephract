package org.projectodd.rephract.java;

import com.headius.invokebinder.Binder;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.guards.NonStaticMethodGuard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class JavaInstanceMethodLinker extends BaseJavaLinker {

    public JavaInstanceMethodLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaInstanceMethodLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetMethod(Invocation invocation, String methodName) throws Exception {

        Object receiver = invocation.receiver();
        Resolver resolver = getResolver(receiver.getClass());
        DynamicMethod dynamicMethod = resolver.getInstanceResolver().getMethod(methodName);

        if (dynamicMethod == null) {
            return null;
        }

        return invocation.builder()
                .guardWith(isInstanceOf(receiver.getClass()))
                .guard(1).with(isEqual(methodName))
                .drop(0, 2)
                .insert(0, dynamicMethod)
                .invoke(MethodHandles.identity(DynamicMethod.class));
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        Object receiver = invocation.receiver();

        if ( ! ( receiver instanceof DynamicMethod )) {
            return null;
        }

        Object[] args = (Object[]) invocation.arguments()[2];

        DynamicMethod dynamicMethod = (DynamicMethod) invocation.receiver();
        InvocationPlan plan = dynamicMethod.findMethodInvoationPlan(args);

        if ( plan == null ) {
            return null;
        }

        Class<?>[] spreadTypes = new Class<?>[args.length];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = Object.class;
        }

        System.err.println( "plan: " + plan.getMethodHandle() );
        System.err.println( "filters: " + Arrays.asList(plan.getFilters()) );

        return invocation.builder()
                .guardWith(new NonStaticMethodGuard(plan))
                .drop(0)
                .spread( spreadTypes )
                .filter(1, plan.getFilters())
                .invoke(plan.getMethodHandle());


                /*
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
                        .filterReturn(returnFilters.getReturnFilter(plan.getMethodHandle()))
                        .invoke(plan.getMethodHandle());

                MethodHandle guard = getCallGuard(((BoundDynamicMethod) receiver).getSelf(), dynamicMethod, args, guardBinder);
                return new StrategicLink(method, guard);
            } else {
                MethodHandle method = binder.drop(0)
                        .spread(spreadTypes)
                        .filter(1, plan.getFilters())
                        .filterReturn(returnFilters.getReturnFilter(plan.getMethodHandle()))
                        .invoke(plan.getMethodHandle());

                MethodHandle guard = getCallGuard(self, dynamicMethod, args, guardBinder);
                return new StrategicLink(method, guard);
            }

        }

        return chain.nextStrategy();
        */
    }
}
