package org.projectodd.rephract.guards;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class InstanceOfGuard extends AbstractStatefulSingleArgumentGuard<Object> {

    private final Class<?> cls;

    public InstanceOfGuard(Class<?> cls) {
        this( cls, 0 );
    }

    public InstanceOfGuard(Class<?> cls, int argPos) {
        super(argPos, Object.class);
        this.cls = cls;
    }

    public boolean guard(Object input) {
        System.err.println( input + " vs " + this.cls );
        return (this.cls.isInstance(input));
    }
}
