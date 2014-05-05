package org.projectodd.rephract;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class MockInvokable implements Invokable<String> {

    protected Object self;
    protected Object[] args;

    public String invoke(Object self, Object... args) throws Throwable {
        this.self = self;
        this.args = args;
        return "invoked: self=" + self + "; " + Arrays.asList(args);
    }

}
