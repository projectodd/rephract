package org.projectodd.rephract.filters;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public interface Filter {

    MethodHandle methodHandle(MethodType inputType) throws Exception;
}
