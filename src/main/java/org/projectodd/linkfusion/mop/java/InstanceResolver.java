package org.projectodd.linkfusion.mop.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InstanceResolver extends AbstractResolver {
    
    public InstanceResolver(Class<?> target) {
        super( target );
        analyze();
    }

    private void analyze() {
        Method[] methods = getTargetClass().getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();

            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                analyzeMethod(methods[i]);
            }
        }

        Field[] fields = getTargetClass().getFields();

        for (int i = 0; i < fields.length; ++i) {
            int modifiers = fields[i].getModifiers();

            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                analyzeField(fields[i]);
            }
        }
    }
}