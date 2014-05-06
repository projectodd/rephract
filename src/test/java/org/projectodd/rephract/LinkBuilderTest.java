package org.projectodd.rephract;

import org.junit.Test;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.filters.Filters;
import org.projectodd.rephract.invokers.SimpleInvoker;

import static java.lang.invoke.MethodType.methodType;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.projectodd.rephract.guards.Guards.*;

/**
 * @author Bob McWhirter
 */
public class LinkBuilderTest {

    @Test
    public void testManipulation() throws Throwable {
        LinkBuilder builder = new LinkBuilder(methodType(String.class), new Object[]{});

        String howdy = "howdy";

        Link link = builder.insert(0, howdy)
                .guardWith(isSame(howdy))
                .invoke(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryInvoke();
        assertThat(result).isSameAs(howdy);
    }

    @Test
    public void testNonManipulation() throws Throwable {
        LinkBuilder builder = new LinkBuilder(methodType(String.class, String.class), new Object[]{"howdy"});

        Link link = builder.guardWith(isEqual("howdy"))
                .invoke(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryInvoke("howdy");
        assertThat(result).isEqualTo("howdy");
        try {
            link.tryInvoke("not-howdy");
            fail("should have thrown precondition-failed");
        } catch (PreconditionFailedException e) {
            // expected and correct
        }
    }

    @Test
    public void testMultipleManipulation() throws Throwable {

        String howdy = "howdy";
        String hello = "hello";
        String whatup = "whatup";

        LinkBuilder builder = new LinkBuilder(methodType(String.class, String.class), new Object[]{ "hello"});

        Link link = builder
                .insert(0, howdy)
                .guardWith(isSame(howdy))
                .drop(0)
                .guardWith(isSame(hello))
                .invoke(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryInvoke(hello);
        assertThat(hello).isSameAs(hello);

        try {
            link.tryInvoke(whatup);
            fail("should have thrown precondition-failed");
        } catch (PreconditionFailedException e) {
            // expected and correct
        }
    }

    @Test
    public void testSubManipulation() throws Throwable {

        LinkBuilder builder = new LinkBuilder(methodType(Object.class, Object.class), new Object[]{42});

        String howdy = "howdy";

        Link link = builder
                .insert(0, howdy)
                .guardWith(isSame(howdy))
                .drop(0)
                .guard().filter(0, Filters.string())
                    .with(isEqual("42"))
                .invoke(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryInvoke(42);
        assertThat(result).isInstanceOf(Integer.class);
        assertThat(result).isEqualTo(42);

    }

    public static final class PrintAndReturnInvoker extends SimpleInvoker {

        public PrintAndReturnInvoker() {
            super(methodType(Object.class, Object.class));
        }

        public Object invoke(Object arg) {
            System.err.println(arg);
            return arg;
        }
    }

}
