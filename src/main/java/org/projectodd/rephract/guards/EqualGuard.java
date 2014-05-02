package org.projectodd.rephract.guards;

/**
 * @author Bob McWhirter
 */
public class EqualGuard extends AbstractStatefulSingleArgumentGuard<Object> {

    private final Object object;

    public EqualGuard(Object object) {
        this( object, 0 );
    }

    public EqualGuard(Object object, int argPos) {
        super(argPos, Object.class);
        this.object = object;
    }

    public boolean guard(Object input) {
        return this.object.equals( input );
    }
}
