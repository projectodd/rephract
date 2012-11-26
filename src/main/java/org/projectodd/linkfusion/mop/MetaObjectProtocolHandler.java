package org.projectodd.linkfusion.mop;

import java.lang.invoke.CallSite;

import org.projectodd.linkfusion.FusionLinker;

public class MetaObjectProtocolHandler {

    private FusionLinker linker = new FusionLinker();

    private CallSite getProperty;
    private CallSite setProperty;

    public MetaObjectProtocolHandler() throws Throwable {
        this.getProperty = linker.bootstrap("fusion:getProperty", Object.class, Object.class, String.class);
        this.setProperty = linker.bootstrap("fusion:setProperty", void.class, Object.class, String.class, Object.class);

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

}
