package org.projectodd.rephract.java.clazz;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaClassPropertyLinker extends BaseJavaLinker {

    public JavaClassPropertyLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaClassPropertyLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new ClassPropertyGetLink( invocation.builder(), this.resolverManager );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new ClassPropertySetLink( invocation.builder(), this.resolverManager );
    }
}
