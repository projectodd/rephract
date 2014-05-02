package org.projectodd.rephract.guards;

import com.headius.invokebinder.Binder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class FalseGuard implements Guard {

    public static final Guard INSTANCE;

    static {
        Guard g = null;
        try {
            g = new FalseGuard();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        INSTANCE = g;
    }


    public FalseGuard() throws NoSuchMethodException, IllegalAccessException {
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        Binder binder = Binder.from(inputType);
        return binder.drop(0, inputType.parameterCount())
                .constant(false);
    }
}
