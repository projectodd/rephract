package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.projectodd.linkfusion.mop.java.TypeComparisons.*;


public class ClassManager {

    private Class<?> target;

    private DynamicConstructor constructor = new DynamicConstructor();
    private Map<String, DynamicMethod> methods = new HashMap<>();
    private Map<String, MethodHandle> propertyReaders = new HashMap<>();
    private Map<String, List<MethodHandle>> propertyWriters = new HashMap<>();

    public ClassManager(Class<?> target) {
        this.target = target;
        analyze();
    }

    private void analyze() {
        Lookup lookup = MethodHandles.lookup();
        
        Constructor<?>[] constructors = this.target.getConstructors();
        
        for ( int i =0 ; i < constructors.length ; ++i ) {
            try {
                this.constructor.addConstructor( lookup.unreflectConstructor( constructors[i] ) );
            } catch (IllegalAccessException e) {
                // ignore
            }
        }

        Method[] methods = this.target.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                String name = methods[i].getName();

                DynamicMethod unboundMethod = this.methods.get(name);
                if (unboundMethod == null) {
                    unboundMethod = new DynamicMethod(name);
                    this.methods.put(name, unboundMethod);
                }

                try {
                    MethodHandle methodHandle = lookup.unreflect(methods[i]);
                    unboundMethod.addMethod(methodHandle);

                    if (name.startsWith("get") && name.length() > 3) {
                        propertyReaders.put(propertyName(name), methodHandle);
                    } else if (name.startsWith("set") && name.length() > 3) {
                        List<MethodHandle> writers = this.propertyWriters.get(propertyName(name));
                        if (writers == null) {
                            writers = new ArrayList<>();
                            this.propertyWriters.put(propertyName(name), writers);
                        }
                        writers.add(methodHandle);
                    }
                } catch (IllegalAccessException e1) {
                    // ignore
                }
            }
        }

    }

    public Class<?> getTarget() {
        return this.target;
    }

    public MethodHandle getPropertyReader(String propertyName) {
        return this.propertyReaders.get(propertyName);
    }

    public MethodHandle getPropertyWriter(String propertyName, Class<?> valueClass) {
        List<MethodHandle> writers = this.propertyWriters.get(propertyName);
        if (writers == null) {
            return null;
        }

        for (MethodHandle each : writers) {
            if (each.type().parameterCount() == 2 && isCompatible(each.type().parameterType(1), valueClass ) ) {
                return each;
            }
        }
        return null;
    }

    public DynamicMethod getMethod(String methodName) {
        return this.methods.get(methodName);
    }
    
    public DynamicConstructor getConstructor() {
        return this.constructor;
    }
    
    public static String propertyName(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }

}