package org.projectodd.rephract.java.guards;

import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.SimpleStatefulGuard;
import org.projectodd.rephract.guards.SimpleStatelessGuard;
import org.projectodd.rephract.java.BoundDynamicMethod;
import org.projectodd.rephract.java.DynamicMethod;
import org.projectodd.rephract.java.InvocationPlan;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class NonStaticMethodGuard extends SimpleStatefulGuard {

    private final InvocationPlan plan;

    public NonStaticMethodGuard(InvocationPlan plan) {
        this.plan = plan;
    }

    public boolean guard(Object input, Object self, Object[] args) {
        System.err.println( "guard: " + input );
        if ( ! ( input instanceof DynamicMethod ) ) {
            System.err.println( "not DynMeth" );
            return false;
        }

        if (  input instanceof BoundDynamicMethod ) {
            System.err.println( "is Bound" );
            return false;
        }

        if ( ((DynamicMethod) input).isStatic() ) {
            System.err.println( "not non-static" );
            return false;
        }

        MethodHandle[] filters = this.plan.getFilters();

        for (int i = 0 ; i < args.length ; ++i ) {
            System.err.println( i + "  arg: " + args[i] + " // " + args[i].getClass()  );
            System.err.println( i + " filt: " + filters[i].type() );

            if ( ! args[i].getClass().isAssignableFrom( filters[i].type().parameterType(0)) ) {
                System.err.println( "fail!" );
                return false;
            }
        }

        return true;
    }

}
