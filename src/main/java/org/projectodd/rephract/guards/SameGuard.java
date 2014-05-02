package org.projectodd.rephract.guards;

/**
 * @author Bob McWhirter
 */
public class SameGuard extends AbstractStatefulSingleArgumentGuard<Object> {

    private final Object object;

    public SameGuard(Object object) {
        this( object, 0 );
    }

    public SameGuard(Object object, int argPos) {
        super(argPos, Object.class);
        this.object = object;
    }

    public boolean guard(Object input) {
        return input == this.object;
    }
}
