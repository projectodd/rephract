package org.projectodd.rephract.guards;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class NullGuardTest {

    @Test
    public void testSimple() throws Throwable {
        Guard g = null;


        g = isNull();
        assertThat(test(g, new Object[]{null})).isTrue();
        assertThat(test(g, "not null")).isFalse();

        g = not(isNull());
        assertThat(test(g, new Object[]{null})).isFalse();
        assertThat(test(g, "not null")).isTrue();
    }
}
