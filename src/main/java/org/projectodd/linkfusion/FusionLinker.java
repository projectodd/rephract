package org.projectodd.linkfusion;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.headius.invokebinder.Binder;

public class FusionLinker {

    private List<LinkStrategy> linkStrategies = new ArrayList<>();

    public FusionLinker() {

    }

    public void addLinkStrategy(LinkStrategy strategy) {
        this.linkStrategies.add(strategy);
    }

    public MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        return Binder.from(CallSite.class, Lookup.class, String.class, MethodType.class)
                .insert(0, this)
                .invokeVirtual(lookup, "bootstrap");
    }

    public CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        MutableCallSite callSite = new MutableCallSite(type);
        LinkPlan plan = new LinkPlan(this, callSite, lookup, name, type);
        return plan.getCallSite();
    }

    public CallSite bootstrap(String name, MethodType type) throws Throwable {
        return bootstrap(MethodHandles.lookup(), name, type);
    }
    
    public CallSite bootstrap(String name, Class<?> returnType, Class<?>...paramTypes) throws Throwable {
        return bootstrap( name, MethodType.methodType(returnType, paramTypes ) );
    }

    public Object linkInvocation(LinkPlan plan, Object[] args) throws Throwable {
        InvocationRequestImpl request = new InvocationRequestImpl(plan, args );
        StrategyChainImpl chain = new StrategyChainImpl(request, this.linkStrategies);

        StrategicLink link = chain.linkCurrent();

        if (link == null) {
            throw new NoSuchMethodError( plan.getName() + ": " + Arrays.asList( args ) );
        }
        
        plan.replan(link);
        
        return link.getTarget().invokeWithArguments(args);
    }

}
