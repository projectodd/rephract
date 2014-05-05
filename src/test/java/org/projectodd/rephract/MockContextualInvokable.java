package org.projectodd.rephract;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class MockContextualInvokable implements ContextualInvokable<String> {

    public Object context;
    public Object self;
    public Object[] args;

    public String invoke(Object context, Object self, Object... args) throws Throwable {
        this.context = context;
        this.self = self;
        this.args = args;
        return "invoked: self=" + self + "; " + Arrays.asList(args);
    }

}
