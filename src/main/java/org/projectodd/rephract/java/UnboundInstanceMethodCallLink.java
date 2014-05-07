package org.projectodd.rephract.java;

import org.projectodd.rephract.Link;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.invokers.Invoker;
import org.projectodd.rephract.java.reflect.DynamicMethod;
import org.projectodd.rephract.java.reflect.InvocationPlan;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class UnboundInstanceMethodCallLink extends Link implements Guard {

    private LinkBuilder builder;
    private InvocationPlan plan;

    public UnboundInstanceMethodCallLink(LinkBuilder builder) throws Exception {
        this.builder = builder;
        this.builder = this.builder.guardWith(this);
    }

    public MethodHandle test(Object... args) throws Throwable {
        if ((boolean) guard().invokeWithArguments(args)) {
            return target();
        }
        return null;
    }


    public boolean guard(Object receiver, Object self, Object[] arguments) {
        if (!(receiver instanceof DynamicMethod)) {
            return false;
        }

        if (((DynamicMethod) receiver).isStatic()) {
            return false;
        }

        InvocationPlan candidatePlan = ((DynamicMethod) receiver).findMethodInvoationPlan(arguments);

        if (candidatePlan == null) {
            return false;
        }

        if (this.plan != null) {
            if (this.plan != candidatePlan) {
                return false;
            }
        }
        this.plan = candidatePlan;
        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(UnboundInstanceMethodCallLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        Class<?>[] paramTypes = this.plan.getMethodHandle().type().parameterArray();
        Class<?>[] spreadTypes = new Class<?>[paramTypes.length - 1];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = paramTypes[i + 1];
        }

        return this.builder
                .drop(0)
                .spread(spreadTypes)
                .invoke(this.plan.getMethodHandle()).target();
    }
}
