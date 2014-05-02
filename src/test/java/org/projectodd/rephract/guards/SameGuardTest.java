package org.projectodd.rephract.guards;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class SameGuardTest {

    @Test
    public void testSimple() throws Throwable {
        Guard g = null;

        Object object = new Object();
        Object different = new Object();

        g = isSame(object);
        assertThat(test(g, object)).isTrue();
        assertThat(test(g, different)).isFalse();

        g = not(isSame(object));
        assertThat(test(g, object)).isFalse();
        assertThat(test(g, different)).isTrue();
    }
}
