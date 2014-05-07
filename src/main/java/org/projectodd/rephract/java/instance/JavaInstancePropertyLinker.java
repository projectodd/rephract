package org.projectodd.rephract.java.instance;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaInstancePropertyLinker extends BaseJavaLinker {

    public JavaInstancePropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaInstancePropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new InstancePropertyGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new InstancePropertySetLink( invocation.builder(), this.resolverManager );
    }
}
