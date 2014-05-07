package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class OrGuard extends AbstractBinaryGuard {

    public OrGuard(Guard lhs, Guard rhs) {
        super( lhs, rhs );
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return MethodHandles.guardWithTest(this.lhs.guardMethodHandle(inputType),
                Guards.TRUE.guardMethodHandle(inputType),
                this.rhs.guardMethodHandle(inputType));
    }
}
