package org.projectodd.rephract;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.projectodd.rephract.Operation.Type;

import com.headius.invokebinder.Binder;

class LinkPlan {
    private FusionLinker linker;
    private List<Operation> operations;

    private MutableCallSite callSite;
    private Lookup lookup;
    private String name;
    private MethodType type;

    List<StrategicLink> links = new ArrayList<StrategicLink>();

    public LinkPlan(FusionLinker linker, MutableCallSite callSite, Lookup lookup, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        this.linker = linker;
        this.callSite = callSite;
        this.lookup = lookup;
        this.name = name;
        this.type = type;
        determineOperations();
        replan(null);
    }

    private void determineOperations() {
        if (!this.name.startsWith("fusion")) {
            return;
        }
        
        this.operations = new ArrayList<>();

        int firstColon = this.name.indexOf(":");
        if (firstColon > 0) {
            int secondColon = this.name.indexOf(":", firstColon + 1);
            String opNames = this.name.substring(firstColon + 1, secondColon > 0 ? secondColon : this.name.length());

            StringTokenizer opNameTokens = new StringTokenizer(opNames, "|");
            
            String parameter = null;
            if ( secondColon > 0 ) {
                parameter = this.name.substring( secondColon + 1 );
            }
            
            while (opNameTokens.hasMoreTokens()) {
                String each = opNameTokens.nextToken();

                Operation op = null;

                switch (each) {
                case "getProperty":
                    op = new OperationImpl( Type.GET_PROPERTY, parameter);
                    break;
                case "setProperty":
                    op = new OperationImpl( Type.SET_PROPERTY, parameter);
                    break;
                case "getElement":
                    op = new OperationImpl( Type.GET_ELEMENT, parameter);
                    break;
                case "setElement":
                    op = new OperationImpl( Type.SET_ELEMENT, parameter);
                    break;
                case "getMethod":
                    op = new OperationImpl( Type.GET_METHOD, parameter);
                    break;
                case "call":
                    op = new OperationImpl( Type.CALL, parameter);
                    break;
                case "construct":
                    op = new OperationImpl( Type.CONSTRUCT, parameter);
                    break;
                }
                
                this.operations.add( op );
            }
        }
    }

    public boolean isFusionRequest() {
        return this.operations != null;
    }

    public List<Operation> getOperations() {
        if (this.operations == null) {
            return Collections.emptyList();
        }

        return this.operations;
    }

    public CallSite getCallSite() {
        return this.callSite;
    }

    public void setTarget(MethodHandle target) {
        this.callSite.setTarget(target);
    }

    public String getName() {
        return this.name;
    }

    public MethodType type() {
        return this.type;
    }

    public Lookup lookup() {
        return this.lookup;
    }
    
    public void replan(StrategicLink link) throws NoSuchMethodException, IllegalAccessException {
        if (link != null) {
            this.links.add(link);
        }

        MethodHandle relink = Binder.from(this.type)
                .convert( this.type.erase() )
                .collect(0, Object[].class)
                .convert(Object.class, Object[].class)
                .insert(0, linker)
                .insert(1, this)
                .invokeVirtual(MethodHandles.lookup(), "linkInvocation");

        MethodHandle current = relink;

        for (int i = this.links.size() - 1; i >= 0; --i) {
            StrategicLink eachLink = this.links.get(i);
            current = MethodHandles.guardWithTest(eachLink.getGuard(), eachLink.getTarget(), current);
        }

        this.callSite.setTarget(current);
    }

    public String toString() {
        return "[Request: " + name + ": " + type + "]";
    }

}
