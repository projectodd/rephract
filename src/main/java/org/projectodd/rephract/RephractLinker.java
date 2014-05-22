package org.projectodd.rephract;

import com.headius.invokebinder.Binder;
import org.projectodd.rephract.guards.Guards;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RephractLinker {

    private List<Linker> linkers = new ArrayList<>();
    private LinkLogger logger;
    private List<LinkPlan> linkPlans = new ArrayList<>();

    public RephractLinker() {
        this(new NullLinkLogger());

    }

    public RephractLinker(LinkLogger logger) {
        this.logger = logger;
    }

    public void addLinker(Linker strategy) {
        this.linkers.add(strategy);
    }

    public void dumpStatistics() {
        for ( LinkPlan each : this.linkPlans ) {
            each.dumpStatistics();
        }
    }

    public MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        return Binder.from(CallSite.class, Lookup.class, String.class, MethodType.class)
                .insert(0, this)
                .invokeVirtual(lookup, "bootstrap");
    }

    public CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        MutableCallSite callSite = new MutableCallSite(type);
        LinkPlan plan = new LinkPlan(this, callSite, lookup, name, type, null);
        return plan.callSite();
    }

    public CallSite bootstrap(Lookup lookup, String name, MethodType type, String file, int line, int column) throws Throwable {
        MutableCallSite callSite = new MutableCallSite(type);
        LinkPlan plan = new LinkPlan(this, callSite, lookup, name, type, new Location(file, line, column) );
        this.linkPlans.add( plan );
        return plan.callSite();
    }

    public CallSite bootstrap(String name, MethodType type) throws Throwable {
        return bootstrap(MethodHandles.lookup(), name, type);
    }

    public CallSite bootstrap(String name, Class<?> returnType, Class<?>... paramTypes) throws Throwable {
        return bootstrap(name, MethodType.methodType(returnType, paramTypes));
    }

    public Object linkInvocation(LinkPlan plan, Object[] args) throws Throwable {

        List<Operation> operations = plan.getOperations();

        Link link = null;
        Invocation invocation = null;
        Object receiver = (args.length >= 1 ? args[0] : null);
        for (Operation each : operations) {
            for (Linker linker : this.linkers) {
                invocation = new Invocation(each.type(), plan.methodType(), receiver, args);
                link = linker.link(invocation);
                if (link != null) {
                    MethodHandle target = link.test( args );
                    if (target != null) {
                        plan.replan(linker, link, link.guard(), target);
                        return target.invokeWithArguments(args);
                    }
                }
            }
        }

        throw new NoSuchMethodError(plan.name() + ": " + Arrays.asList(args));
    }

}
