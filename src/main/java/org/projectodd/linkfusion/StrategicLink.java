package org.projectodd.linkfusion;

import java.lang.invoke.MethodHandle;

public class StrategicLink {
    
    private MethodHandle target;
    private MethodHandle guard;

    public StrategicLink(MethodHandle target, MethodHandle guard) {
        this.target = target;
        this.guard = guard;
    }
    
    public MethodHandle getTarget() {
        return this.target;
    }
    
    public MethodHandle getGuard() {
        return this.guard;
    }
    
    public String toString() {
        return "[StrategicLink: target=" + target + "; guard=" + guard + "]";
    }

}
