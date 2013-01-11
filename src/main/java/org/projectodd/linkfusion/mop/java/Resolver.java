package org.projectodd.linkfusion.mop.java;

public class Resolver {
    
    private ClassResolver classResolver;
    private InstanceResolver instanceResolver;

    public Resolver(Class<?> targetClass) {
        this.classResolver = new ClassResolver( targetClass );
        this.instanceResolver = new InstanceResolver( targetClass );
    }
    
    public ClassResolver getClassResolver() {
        return this.classResolver;
    }
    
    public InstanceResolver getInstanceResolver() {
        return this.instanceResolver;
    }

}
