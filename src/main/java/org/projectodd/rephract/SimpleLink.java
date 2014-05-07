package org.projectodd.rephract;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class SimpleLink extends Link {

    private final MethodHandle guard;
    private MethodHandle target;

    public SimpleLink(MethodHandle guard, MethodHandle target) {
        this.guard = guard;
        this.target = target;
    }

    public MethodHandle guard() throws Exception {
        return this.guard;
    }

    public MethodHandle target() throws Exception {
        return this.target;
    }

}
