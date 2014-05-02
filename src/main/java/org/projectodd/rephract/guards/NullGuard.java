package org.projectodd.rephract.guards;

/**
 * @author Bob McWhirter
 */
public class NullGuard extends AbstractStatelessSingleArgumentGuard {

    public NullGuard() {
        this(0);
    }

    public NullGuard(int argPos) {
        super(argPos, Object.class);
    }

    public static boolean guard(Object arg) {
        return ( arg == null );
    }
}
