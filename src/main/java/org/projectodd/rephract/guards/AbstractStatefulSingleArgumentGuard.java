package org.projectodd.rephract.guards;

import com.headius.invokebinder.Binder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractStatefulSingleArgumentGuard<T> extends SimpleStatefulGuard {

    protected final int argPos;
    protected final Class<?> argType;

    public AbstractStatefulSingleArgumentGuard(int argPos, Class<T> argType) {
        this.argPos = argPos;
        this.argType = argType;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws NoSuchMethodException, IllegalAccessException {
        Binder binder = Binder.from(inputType);

        MethodHandle target = super.methodHandle(methodType(boolean.class, this.argType));

        return binder.permute(this.argPos)
                .convert(target.type())
                .invoke(target);
    }

    public abstract boolean guard(T input);
}
