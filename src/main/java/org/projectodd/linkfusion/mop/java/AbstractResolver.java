package org.projectodd.linkfusion.mop.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class AbstractResolver {

    private Class<?> targetClass;

    private Map<String, MethodHandle> propertyReaders = new HashMap<>();
    private Map<String, DynamicMethod> propertyWriters = new HashMap<>();
    private Map<String, DynamicMethod> methods = new HashMap<>();

    public AbstractResolver(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }
    
    protected void analyzeMethod(Method method) {
        Lookup lookup = MethodHandles.lookup();

        String name = method.getName();
        DynamicMethod dynamicMethod = this.methods.get(name);
        if (dynamicMethod == null) {
            dynamicMethod = new DynamicMethod(name, Modifier.isStatic( method.getModifiers() ));
            this.methods.put(name, dynamicMethod);
        }

        try {
            MethodHandle unreflectedMethod = lookup.unreflect(method);

            dynamicMethod.addMethodHandle(unreflectedMethod);

            if (name.length() >= 4) {
                if (name.startsWith("set")) {
                    name = convertToPropertyName(name);

                    DynamicMethod writer = this.propertyWriters.get(name);
                    if (writer == null) {
                        writer = new DynamicMethod(name, Modifier.isStatic( method.getModifiers() ));
                        this.propertyWriters.put(name, writer);
                    }
                    writer.addMethodHandle(unreflectedMethod);
                } else if (name.startsWith("get")) {
                    name = convertToPropertyName(name);
                    this.propertyReaders.put(name, unreflectedMethod);
                }
            }
        } catch (IllegalAccessException e1) {
            // ignore
        }
    }

    protected void analyzeField(Field field) {
        Lookup lookup = MethodHandles.lookup();

        String name = field.getName();

        try {
            DynamicMethod writer = propertyWriters.get(name);
            if (writer == null) {
                writer = new DynamicMethod(name, Modifier.isStatic(field.getModifiers()));
                this.propertyWriters.put(name, writer);
            }
            writer.addMethodHandle(lookup.unreflectSetter(field));
            this.propertyReaders.put(name, lookup.unreflectGetter(field));
        } catch (IllegalAccessException e) {
            // ignore
        }
    }

    protected String convertToPropertyName(String name) {
        String propName = null;
        if (name.length() == 4) {
            propName = name.substring(3).toLowerCase();
        } else {
            propName = name.substring(3,4).toLowerCase() + name.substring(4);
        }
        return propName;
    }

    public DynamicMethod getMethod(String name) {
        return this.methods.get(name);
    }

    public MethodHandle getPropertyReader(String name) {
        return this.propertyReaders.get(name);
    }

    public DynamicMethod getPropertyWriter(String name) {
        return this.propertyWriters.get(name);
    }

}
