package org.projectodd.rephract.guards;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import static org.projectodd.rephract.guards.Guards.*;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class GuardsTest {

    @Test
    public void testBoolean() throws Throwable {
        Guard guard = null;
        boolean result;

        guard = Guards.TRUE;
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class, String.class, String.class, int.class)).invoke("one", "two", 42);
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();

        guard = Guards.FALSE;
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class, String.class, String.class, int.class)).invoke("one", "two", 42);
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();
    }

    @Test
    public void testNot() throws Throwable {
        Guard guard = null;
        boolean result;

        guard = not(TRUE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class, String.class, String.class, int.class)).invoke("one", "two", 42);
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();

        guard = not(FALSE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class, String.class, String.class, int.class)).invoke("one", "two", 42);
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();
    }

    @Test
    public void testOr() throws Throwable {
        Guard guard = null;
        boolean result;

        guard = or(TRUE, TRUE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();

        guard = or(FALSE, FALSE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();

        guard = or(FALSE, TRUE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();

        guard = or(TRUE, FALSE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();
    }

    @Test
    public void testAnd() throws Throwable {

        Guard guard = null;
        boolean result;

        guard = and(TRUE, TRUE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isTrue();
        assertThat(test(guard)).isTrue();

        guard = and(FALSE, FALSE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();

        guard = and(FALSE, TRUE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();

        guard = and(TRUE, FALSE);
        result = (boolean) guard.guardMethodHandle(methodType(boolean.class)).invoke();
        assertThat(result).isFalse();
        assertThat(test(guard)).isFalse();

    }
}
