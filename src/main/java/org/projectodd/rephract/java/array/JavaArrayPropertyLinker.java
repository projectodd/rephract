package org.projectodd.rephract.java.array;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.instance.InstancePropertyGetLink;
import org.projectodd.rephract.java.instance.InstancePropertySetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaArrayPropertyLinker extends BaseJavaLinker {

    public JavaArrayPropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaArrayPropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new ArrayPropertyGetLink( invocation.builder() );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new ArrayPropertySetLink( invocation.builder() );
    }
}
