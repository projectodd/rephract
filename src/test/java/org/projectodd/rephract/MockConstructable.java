package org.projectodd.rephract;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class MockConstructable implements Constructable<String> {
    public Object[] args;

    public String construct(Object... args) throws Throwable {
        this.args = args;
        return "constructed: " + Arrays.asList(args);
    }
}
