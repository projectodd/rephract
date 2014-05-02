package org.projectodd.rephract;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public abstract class Filter<IN,OUT> {

    private final Class<IN> inType;
    private final Class<OUT> outType;

    public Filter(Class<IN> inType, Class<OUT> outType) {
        this.inType = inType;
        this.outType = outType;
    }

    public abstract OUT perform(IN object);

    public MethodHandle methodHandle() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findVirtual(Filter.class, "perform", MethodType.methodType(this.outType, this.inType)).bindTo( this );
    }
}
