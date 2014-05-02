package org.projectodd.rephract.guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public interface Guard {

    MethodHandle methodHandle(MethodType inputType) throws Exception;

}
