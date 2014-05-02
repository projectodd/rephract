package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractBinaryGuard implements Guard {

    protected Guard lhs;
    protected Guard rhs;

    protected AbstractBinaryGuard(Guard lhs, Guard rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
