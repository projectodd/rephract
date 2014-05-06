package org.projectodd.rephract.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassResolver extends AbstractResolver {

    private DynamicConstructor constructor;

    public ClassResolver(CoercionMatrix coercionMatrix, Class<?> target) {
        super(coercionMatrix, target);
        this.constructor = new DynamicConstructor(coercionMatrix);
        analyze(target, true);
    }

    private void analyze(Class<?> cls, boolean topLevel) {
        if (!Modifier.isPublic(cls.getModifiers())) {
            return;
        }

        Lookup lookup = MethodHandles.lookup();

        if (topLevel) {
            this.constructor = new DynamicConstructor(getCoercionMatrix());
            Constructor<?>[] constructors = cls.getConstructors();

            for (int i = 0; i < constructors.length; ++i) {
                try {
                    this.constructor.addConstructorHandle(lookup.unreflectConstructor(constructors[i]));
                } catch (IllegalAccessException e) {
                }
            }
        }

        Method[] methods = getTargetClass().getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();

            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                analyzeMethod(methods[i]);
            }
        }

        Field[] fields = getTargetClass().getFields();

        for (int i = 0; i < fields.length; ++i) {
            int modifiers = fields[i].getModifiers();

            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                analyzeField(fields[i]);
            }
        }

        if (cls.getSuperclass() != null) {
            analyze(cls.getSuperclass(), false);
        }

        Class<?>[] interfaces = cls.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            analyze(interfaces[i], false);
        }
    }

    public DynamicConstructor getConstructor() {
        return this.constructor;
    }

}
