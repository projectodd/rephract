package org.projectodd.linkfusion.mop.java;

public class Resolver {
    
    private ClassResolver classResolver;
    private InstanceResolver instanceResolver;

    public Resolver(CoercionMatrix coercionMatrix, Class<?> targetClass) {
        this.classResolver = new ClassResolver( coercionMatrix, targetClass );
        this.instanceResolver = new InstanceResolver( coercionMatrix, targetClass );
    }
    
    public ClassResolver getClassResolver() {
        return this.classResolver;
    }
    
    public InstanceResolver getInstanceResolver() {
        return this.instanceResolver;
    }

}
