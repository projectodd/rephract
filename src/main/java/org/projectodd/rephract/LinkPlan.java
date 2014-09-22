package org.projectodd.rephract;

import com.headius.invokebinder.Binder;
import org.projectodd.rephract.Invocation.Type;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.*;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodHandles.lookup;

class LinkPlan {

    private static boolean debug;
    private static int debugThreshold;

    static {
        String debug = System.getProperty("rephract.debug");
        String threshold = System.getProperty("rephract.debug.threshold");

        if (debug != null || threshold != null) {
            LinkPlan.debug = true;
            if (threshold != null) {
                LinkPlan.debugThreshold = Integer.parseInt(threshold);
            } else {
                LinkPlan.debugThreshold = 1;
            }
        }
    }

    private static MethodHandle LINK_INVOCATION;

    static {
        try {
            LINK_INVOCATION = MethodHandles.lookup().findVirtual(LinkPlan.class, "linkInvocation", MethodType.methodType(Object.class, Object[].class));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private RephractLinker linker;
    private List<Operation> operations;

    private final MutableCallSite callSite;
    private final Lookup lookup;
    private final String name;
    private final MethodType type;
    private final Location location;
    private final MethodHandle relink;

    List<Entry> links = new ArrayList<>();

    public LinkPlan(RephractLinker linker, MutableCallSite callSite, Lookup lookup, String name, MethodType type, Location location) throws NoSuchMethodException, IllegalAccessException {
        this.linker = linker;
        this.callSite = callSite;
        this.lookup = lookup;
        this.name = name;
        this.type = type;
        this.location = location;
        this.relink = Binder.from(this.type)
                .convert(this.type.erase())
                .collect(0, Object[].class)
                .convert(Object.class, Object[].class)
                .insert(0, this)
                .invoke(LINK_INVOCATION);
        determineOperations();
        replan(null, null, null, null);
    }

    private void determineOperations() {
        if (!this.name.startsWith("dyn")) {
            return;
        }

        this.operations = new ArrayList<>();

        int firstColon = this.name.indexOf(":");
        if (firstColon > 0) {
            int secondColon = this.name.indexOf(":", firstColon + 1);
            String opNames = this.name.substring(firstColon + 1, secondColon > 0 ? secondColon : this.name.length());

            StringTokenizer opNameTokens = new StringTokenizer(opNames, "|");

            String parameter = null;
            if (secondColon > 0) {
                parameter = this.name.substring(secondColon + 1);
            }

            while (opNameTokens.hasMoreTokens()) {
                String each = opNameTokens.nextToken();

                Operation op = null;

                switch (each) {
                    case "getProperty":
                        op = new Operation(Type.GET_PROPERTY, parameter);
                        break;
                    case "setProperty":
                        op = new Operation(Type.SET_PROPERTY, parameter);
                        break;
                    case "getElement":
                        op = new Operation(Type.GET_ELEMENT, parameter);
                        break;
                    case "setElement":
                        op = new Operation(Type.SET_ELEMENT, parameter);
                        break;
                    case "getMethod":
                        op = new Operation(Type.GET_METHOD, parameter);
                        break;
                    case "call":
                        op = new Operation(Type.CALL, parameter);
                        break;
                    case "construct":
                        op = new Operation(Type.CONSTRUCT, parameter);
                        break;
                }

                this.operations.add(op);
            }
        }
    }

    public boolean isRephractRequest() {
        return this.operations != null;
    }

    public List<Operation> getOperations() {
        if (this.operations == null) {
            return Collections.emptyList();
        }

        return this.operations;
    }

    public CallSite callSite() {
        return this.callSite;
    }

    public void setTarget(MethodHandle target) {
        this.callSite.setTarget(target);
    }

    public String name() {
        return this.name;
    }

    public MethodType methodType() {
        return this.type;
    }

    public Lookup lookup() {
        return this.lookup;
    }

    public MethodHandle checkForDuplicate(Object[] args) throws Throwable {
        for (Entry each : this.links) {
            if ((boolean) each.guard.invokeWithArguments(args)) {
                return each.target;
            }
        }

        return null;
    }

    public synchronized Object linkInvocation(Object[] args) throws Throwable {

        MethodHandle racing = checkForDuplicate(args);
        if (racing != null) {
            return racing.invokeWithArguments(args);
        }

        List<Operation> operations = getOperations();

        Link link = null;
        Invocation invocation = null;
        Object receiver = (args.length >= 1 ? args[0] : null);
        for (Operation each : operations) {
            for (Linker linker : this.linker.linkers()) {
                invocation = new Invocation(each.type(), this.type, receiver, args);
                link = linker.link(invocation);
                if (link != null) {
                    MethodHandle target = link.test(args);
                    if (target != null) {
                        replan(linker, link, link.guard(), target);
                        return target.invokeWithArguments(args);
                    }
                }
            }
        }

        throw new NoSuchMethodError(name() + ": " + Arrays.asList(args));
    }

    public void replan(Linker linker, Link link, MethodHandle guard, MethodHandle target) throws NoSuchMethodException, IllegalAccessException {
        if (guard != null && target != null) {
            this.links.add(new Entry(linker, link, guard, target));
        }

        MethodHandle current = this.relink;

        if (debug && this.links.size() >= debugThreshold) {
            StringBuffer buf = new StringBuffer();
            buf.append("[" + Thread.currentThread().getName() + "] :: LARGE :: " + this + "\n");
            for (int i = 0; i < this.links.size(); ++i) {
                buf.append("  #").append(i).append(" == ").append(this.links.get(i).link).append("\n");
            }
            System.err.println(buf);
        }

        for (int i = this.links.size() - 1; i >= 0; --i) {
            Entry eachLink = this.links.get(i);
            if (eachLink != null) {
                current = guardWithTest(possiblyDebugGuard(eachLink, i, eachLink.guard), eachLink.target, current);
            }
        }

        this.callSite.setTarget(current);
    }

    public String toString() {
        return "[Request: " + name + ": " + type + "] " + System.identityHashCode(this);
    }

    public static class Entry {
        public final Linker linker;
        public final Link link;
        public final MethodHandle guard;
        public final MethodHandle target;

        public Entry(Linker linker, Link link, MethodHandle guard, MethodHandle target) {
            this.linker = linker;
            this.link = link;
            this.guard = guard;
            this.target = target;
        }

        public String toString() {
            return "[Entry: link=" + this.link + "; linker=" + this.linker + "; target=" + this.target + "; guard=" + this.guard + "]";

        }
    }

    public MethodHandle possiblyDebugGuard(Entry entry, int position, MethodHandle input) throws NoSuchMethodException, IllegalAccessException {
        if (!debug) {
            return input;
        }

        MethodHandle debugMh = MethodHandles.lookup()
                .findStatic(LinkPlan.class, "debugGuard", MethodType.methodType(boolean.class, LinkPlan.class, LinkPlan.Entry.class, int.class, MethodHandle.class, Object[].class));

        debugMh = Binder.from(input.type())
                .convert(input.type().erase())
                .collect(0, Object[].class)
                .insert(0, input)
                .insert(0, position)
                .insert(0, entry)
                .insert(0, this)
                .invoke(debugMh);
        return debugMh;
    }

    public static boolean debugGuard(LinkPlan plan, Entry entry, int position, MethodHandle guard, Object[] params) throws Throwable {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append(Thread.currentThread().getName());
        buf.append("] ");
        buf.append(plan);
        buf.append(" #").append(position).append(" ");
        buf.append("<guard> ");
        buf.append(entry.link);
        buf.append(" (");
        for (int i = 0; i < params.length; ++i) {
            buf.append(params[i]);
            if (i < params.length) {
                buf.append(", ");
            }
        }
        buf.append(")");
        boolean result = (boolean) guard.invokeWithArguments(params);
        buf.append(" -> ").append(result);
        System.err.println(buf.toString());
        return result;
    }

    /*
    public static Object debugTarget(LinkPlan plan, Entry entry, String type, Object[] params, Object result) {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append(Thread.currentThread().getName());
        buf.append("] ");
        buf.append("<guard> ");
        buf.append(plan);
        buf.append(" ## ");
        buf.append(entry.link);
        buf.append("(");
        for (int i = 0; i < params.length; ++i) {
            buf.append(params[i]);
            if (i < params.length) {
                buf.append(", ");
            }
        }
        buf.append(")");
        buf.append(" -> ").append(result);
        System.err.println(buf.toString());
        return result;
    }
    */

}
