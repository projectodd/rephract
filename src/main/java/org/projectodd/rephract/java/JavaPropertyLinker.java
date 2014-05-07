package org.projectodd.rephract.java;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaPropertyLinker extends BaseJavaLinker {

    public JavaPropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaPropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new PropertyGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new PropertySetLink( invocation.builder(), this.resolverManager );
    }
}
