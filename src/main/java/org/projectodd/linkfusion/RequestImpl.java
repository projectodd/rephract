package org.projectodd.linkfusion;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public class RequestImpl implements Request {

    private MutableCallSite callSite;
    private Lookup lookup;
    private String name;
    private MethodType type;

    public RequestImpl(MutableCallSite callSite, Lookup lookup, String name, MethodType type) {
        this.callSite = callSite;
        this.lookup = lookup;
        this.name = name;
        this.type = type;
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
    
    public String toString() {
        return "[Request: " + name + ": " + type + "]";
    }

}
