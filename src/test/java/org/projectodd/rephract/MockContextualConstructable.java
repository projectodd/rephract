package org.projectodd.rephract;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class MockContextualConstructable implements ContextualConstructable<String> {

    public Object context;
    public Object[] args;

    public String construct(Object context, Object... args) throws Throwable {
        this.context = context;
        this.args = args;
        return "constructed: " + Arrays.asList(args);
    }
}
