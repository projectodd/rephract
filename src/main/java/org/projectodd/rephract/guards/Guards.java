package org.projectodd.rephract.guards;

import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class Guards {

    public static boolean test(Guard guard, Object... args) throws Throwable {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) {
                argTypes[i] = Object.class;
            } else {
                argTypes[i] = args[i].getClass();
            }
        }

        MethodType inputType = MethodType.methodType(boolean.class, argTypes);
        return (boolean) guard.methodHandle(inputType).invokeWithArguments(args);
    }

    public static Guard and(Guard lhs, Guard rhs) {
        return new AndGuard(lhs, rhs);
    }

    public static Guard or(Guard lhs, Guard rhs) {
        return new OrGuard(lhs, rhs);
    }

    public static Guard not(Guard guard) {
        return new NotGuard(guard);
    }

    public static Guard isInstanceOf(Class<?> cls, int argPos) {
        return new InstanceOfGuard(cls, argPos);
    }

    public static Guard isInstanceOf(Class<?> cls) {
        return new InstanceOfGuard(cls);
    }

    public static Guard isSame(Object object, int argPos) {
        return new SameGuard(object, argPos);
    }

    public static Guard isSame(Object object) {
        return new SameGuard(object);
    }

    public static Guard isEqual(Object object, int argPos) {
        return new EqualGuard(object, argPos);
    }

    public static Guard isEqual(Object object) {
        return new EqualGuard(object);
    }

    public static Guard isNull() {
        return new NullGuard();
    }

    public static Guard isNull(int argPos) {
        return new NullGuard(argPos);
    }

    public static Guard TRUE = TrueGuard.INSTANCE;
    public static Guard FALSE = FalseGuard.INSTANCE;

}
