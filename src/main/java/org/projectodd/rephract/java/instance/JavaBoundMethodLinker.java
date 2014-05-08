package org.projectodd.rephract.java.instance;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.java.BaseJavaLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

/**
 * @author Bob McWhirter
 */
public class JavaBoundMethodLinker extends BaseJavaLinker {

    public JavaBoundMethodLinker() throws NoSuchMethodException, IllegalAccessException {
    }

    public JavaBoundMethodLinker(ResolverManager resolverManager) {
        super(resolverManager);
    }


    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new BoundInstanceMethodCallLink( invocation.builder() );
    }
}
