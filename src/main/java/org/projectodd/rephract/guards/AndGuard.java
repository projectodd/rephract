package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class AndGuard extends AbstractBinaryGuard {

    public AndGuard(Guard lhs, Guard rhs) {
        super(lhs, rhs);
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return MethodHandles.guardWithTest(this.lhs.guardMethodHandle(inputType),
                this.rhs.guardMethodHandle(inputType),
                Guards.FALSE.guardMethodHandle(inputType) );
    }
}
