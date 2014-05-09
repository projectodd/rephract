package org.projectodd.rephract;

import com.headius.invokebinder.Binder;
import org.projectodd.rephract.Invocation.Type;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

class LinkPlan {
    private RephractLinker linker;
    private List<Operation> operations;

    private MutableCallSite callSite;
    private Lookup lookup;
    private String name;
    private MethodType type;

    List<Entry> links = new ArrayList<Entry>();

    public LinkPlan(RephractLinker linker, MutableCallSite callSite, Lookup lookup, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        this.linker = linker;
        this.callSite = callSite;
        this.lookup = lookup;
        this.name = name;
        this.type = type;
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

    public void replan(Linker linker, Link link, MethodHandle guard, MethodHandle target) throws NoSuchMethodException, IllegalAccessException {
        if (target != null) {
            this.links.add(new Entry(guard, target));
        }

        MethodHandle relink = Binder.from(this.type)
                .convert(this.type.erase())
                .collect(0, Object[].class)
                .convert(Object.class, Object[].class)
                .insert(0, this.linker)
                .insert(1, this)
                .invokeVirtual(MethodHandles.lookup(), "linkInvocation");

        MethodHandle current = relink;

        for (int i = this.links.size() - 1; i >= 0; --i) {
            Entry eachLink = this.links.get(i);
            current = MethodHandles.guardWithTest(eachLink.guard, eachLink.target, current);
        }

        this.callSite.setTarget(current);
    }

    public String toString() {
        return "[Request: " + name + ": " + type + "]";
    }

    public static class Entry {
        public final MethodHandle guard;
        public final MethodHandle target;

        public Entry(MethodHandle guard, MethodHandle target) {
            this.guard = guard;
            this.target = target;
        }
    }

}
