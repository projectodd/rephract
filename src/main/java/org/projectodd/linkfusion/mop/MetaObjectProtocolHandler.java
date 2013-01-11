package org.projectodd.linkfusion.mop;

import java.lang.invoke.CallSite;

import org.projectodd.linkfusion.FusionLinker;

public class MetaObjectProtocolHandler {

    private FusionLinker linker = new FusionLinker();

    private CallSite getProperty;
    private CallSite setProperty;

    private CallSite getMethod;
    private CallSite call;
    private CallSite construct;

    public MetaObjectProtocolHandler() throws Throwable {
        this.getProperty = linker.bootstrap("fusion:getProperty", Object.class, Object.class, String.class);
        this.setProperty = linker.bootstrap("fusion:setProperty", void.class, Object.class, String.class, Object.class);

        this.getMethod = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

        this.call = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object[].class);
        this.construct = linker.bootstrap("fusion:construct", Object.class, Object.class, Object[].class);
    }

    public void addLinkStrategy(MetaObjectProtocolLinkStrategy linkStrategy) {
        this.linker.addLinkStrategy(linkStrategy);
    }

    public Object getProperty(Object object, String propertyName) throws Throwable {
        return this.getProperty.getTarget().invoke(object, propertyName);
    }

    public void setProperty(Object object, String propertyName, Object value) throws Throwable {
        this.setProperty.getTarget().invoke(object, propertyName, value);
    }

    public Object getMethod(Object object, String methodName) throws Throwable {
        return this.getMethod.getTarget().invoke(object, methodName);
    }

    public Object call(Object method, Object self, Object... args) throws Throwable {
        return this.call.getTarget().invoke(method, self, args);
    }
    
    public Object callMethod(Object object, String methodName, Object... args) throws Throwable {
        Object method = getMethod(object, methodName);
        return call(method, object, args);
    }
    
    public Object construct(Object method, Object... args) throws Throwable {
        return this.construct.getTarget().invoke(method, args);
    }
    
    public ContextualMetaObjectProtocolHandler withContext(Object context) throws Throwable {
        return new ContextualMetaObjectProtocolHandler( this.linker, context );
    }

}
