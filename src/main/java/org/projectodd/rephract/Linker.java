package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public abstract class Linker {

    protected final LinkLogger logger;

    public Linker() {
        this(new NullLinkLogger());
    }

    public Linker(LinkLogger logger) {
        this.logger = logger;
    }

    public void log(String message, Object... arguments) {
        this.logger.log(message, arguments);
    }

    public Link link(Invocation invocation) throws Exception {
        switch (invocation.type()) {
            case GET_PROPERTY:
                return preLinkGetProperty(invocation);
            case SET_PROPERTY:
                return preLinkSetProperty(invocation);
            case GET_METHOD:
                return preLinkGetMethod(invocation);
            case CALL:
                return preLinkCall(invocation);
            case CONSTRUCT:
                return preLinkConstruct(invocation);
            case OTHER:
                return preLinkOther(invocation);
        }
        return null;
    }

    public Link preLinkGetProperty(Invocation invocation) throws Exception {
        return null;
    }

    public Link preLinkSetProperty(Invocation invocation) throws Exception {
        return null;
    }

    public Link preLinkGetMethod(Invocation invocation) throws Exception {
        return null;
    }

    public Link preLinkCall(Invocation invocation) throws Exception {
        return null;
    }

    public Link preLinkConstruct(Invocation invocation) throws Exception {
        return null;
    }

    public Link preLinkOther(Invocation invocation) throws Exception {
        return null;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return null;
    }

    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return null;
    }

    public Link linkGetMethod(Invocation invocation, String propertyName) throws Exception {
        return null;
    }

    public Link linkCall(Invocation invocation) throws Exception {
        return null;
    }

    public Link linkConstruct(Invocation invocation) throws Exception {
        return null;
    }

}
