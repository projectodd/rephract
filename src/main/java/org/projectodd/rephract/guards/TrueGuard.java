package org.projectodd.rephract.guards;

import com.headius.invokebinder.Binder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class TrueGuard implements Guard {

    public static final Guard INSTANCE;

    static {
        Guard g = null;
        try {
            g = new TrueGuard();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        INSTANCE = g;
    }

    public TrueGuard() throws NoSuchMethodException, IllegalAccessException {
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        Binder binder = Binder.from(inputType);
        return binder.drop(0, inputType.parameterCount())
                .constant(true);
    }
}
