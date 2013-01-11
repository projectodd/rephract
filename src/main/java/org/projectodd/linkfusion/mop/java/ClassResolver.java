package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassResolver extends AbstractResolver {

    private DynamicConstructor constructor = new DynamicConstructor();

    public ClassResolver(Class<?> target) {
        super( target );
        analyze();
    }

    private void analyze() {
        Lookup lookup = MethodHandles.lookup();
        this.constructor = new DynamicConstructor();

        Constructor<?>[] constructors = getTargetClass().getConstructors();

        for (int i = 0; i < constructors.length; ++i) {
            try {
                this.constructor.addConstructorHandle(lookup.unreflectConstructor(constructors[i]));
            } catch (IllegalAccessException e) {
                // ignore
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
    }
    
    public DynamicConstructor getConstructor() {
        return this.constructor;
    }

}
