package org.projectodd.rephract.guards;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class InstanceOfGuardTest {

    @Test
    public void testOnlyParameter() throws Throwable {
        Guard g = null;

        g = isInstanceOf(String.class);
        assertThat(test(g, "taco")).isTrue();
        assertThat(test(g, 42)).isFalse();

        g = not(isInstanceOf(String.class));
        assertThat(test(g, "taco")).isFalse();
        assertThat(test(g, 42)).isTrue();
    }

    @Test
    public void testMiddleParameter() throws Throwable {
        Guard g = null;

        g = isInstanceOf(String.class, 2);
        assertThat(test(g, 0, 1, "taco", 3, 4)).isTrue();
        assertThat(test(g, "taco", "taco", 42, "taco", "taco")).isFalse();

        g = not(isInstanceOf(String.class, 2));
        assertThat(test(g, 0, 1, "taco", 3, 4)).isFalse();
        assertThat(test(g, "taco", "taco", 42, "taco", "taco")).isTrue();
    }

    @Test
    public void testLastParameter() throws Throwable {
        Guard g = null;

        g = isInstanceOf(String.class, 4);
        assertThat(test(g, 0, 1, 2, 3, "taco")).isTrue();
        assertThat(test(g, "taco", "taco", "taco", "taco", 42)).isFalse();

        g = not(isInstanceOf(String.class, 4));
        assertThat(test(g, 0, 1, 2, 3, "taco")).isFalse();
        assertThat(test(g, "taco", "taco", "taco", "taco", 42)).isTrue();
    }
}
