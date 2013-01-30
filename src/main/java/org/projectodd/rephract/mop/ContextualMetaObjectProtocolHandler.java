package org.projectodd.rephract.mop;

import java.lang.invoke.CallSite;

import org.projectodd.rephract.FusionLinker;

public class ContextualMetaObjectProtocolHandler {

    private FusionLinker linker;
    private Object context;
    
    private CallSite getProperty;
    private CallSite setProperty;
    private CallSite getMethod;
    private CallSite call;
    private CallSite construct;

    public ContextualMetaObjectProtocolHandler(FusionLinker linker, Object context) throws Throwable {
        this.linker = linker;
        this.context = context;
        
        
        this.getProperty = linker.bootstrap("fusion:getProperty", Object.class, Object.class, Object.class, String.class);
        this.setProperty = linker.bootstrap("fusion:setProperty", void.class, Object.class, Object.class, String.class, Object.class);

        this.getMethod = linker.bootstrap("fusion:getMethod", Object.class, Object.class, Object.class, String.class);

        this.call = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object.class, Object[].class);
        this.construct = linker.bootstrap("fusion:construct", Object.class, Object.class, Object.class, Object[].class);
    }

    public Object getProperty(Object object, String propertyName) throws Throwable {
        return this.getProperty.getTarget().invoke(object, this.context, propertyName);
    }

    public void setProperty(Object object, String propertyName, Object value) throws Throwable {
        this.setProperty.getTarget().invoke(object, this.context, propertyName, value);
    }

    public Object getMethod(Object object, String methodName) throws Throwable {
        return this.getMethod.getTarget().invoke(object, this.context, methodName);
    }

    public Object call(Object method, Object self, Object... args) throws Throwable {
        return this.call.getTarget().invoke(method, this.context, self, args);
    }
    
    public Object callMethod(Object object, String methodName, Object... args) throws Throwable {
        Object method = getMethod(object, methodName);
        return call(method, object, args);
    }
    
    public Object construct(Object method, Object... args) throws Throwable {
        return this.construct.getTarget().invoke(method, this.context, args);
    }
    
    public ContextualMetaObjectProtocolHandler withContext(Object context) throws Throwable {
        return new ContextualMetaObjectProtocolHandler(this.linker, context);
        
    }
}
