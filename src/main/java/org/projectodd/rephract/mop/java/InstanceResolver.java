package org.projectodd.rephract.mop.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InstanceResolver extends AbstractResolver {
    
    public InstanceResolver(CoercionMatrix coercionMatrix, Class<?> target) {
        super( coercionMatrix, target );
        analyze( target );
    }
    
    private void analyze(Class<?> cls) {
        Method[] methods = cls.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();

            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                analyzeMethod(methods[i]);
            }
        }

        Field[] fields = cls.getFields();

        for (int i = 0; i < fields.length; ++i) {
            int modifiers = fields[i].getModifiers();

            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                analyzeField(fields[i]);
            }
        }
        
        if ( cls.getSuperclass() != null ) {
            analyze( cls.getSuperclass() );
        }
        
        Class<?>[] interfaces = cls.getInterfaces();
        for ( int i = 0 ; i < interfaces.length ; ++i ) {
            analyze( interfaces[i] );
        }
    }
}