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
        LinkBuilder builder = new LinkBuilder(methodType(String.class));

        String howdy = "howdy";

        Link link = builder.insert(0, howdy)
                .guardWith(isSame(howdy))
                .call(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryCall();
        assertThat(result).isSameAs(howdy);
    }

    @Test
    public void testNonManipulation() throws Throwable {
        LinkBuilder builder = new LinkBuilder(methodType(String.class, String.class));

        Link link = builder.guardWith(isEqual("howdy"))
                .call(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryCall("howdy");
        assertThat(result).isEqualTo("howdy");
        try {
            link.tryCall("not-howdy");
            fail("should have thrown precondition-failed");
        } catch (PreconditionFailedException e) {
            // expected and correct
        }
    }

    @Test
    public void testMultipleManipulation() throws Throwable {

        LinkBuilder builder = new LinkBuilder(methodType(String.class, String.class));

        String howdy = "howdy";
        String hello = "hello";
        String whatup = "whatup";

        Link link = builder
                .insert(0, howdy)
                .guardWith(isSame(howdy))
                .drop(0)
                .guardWith(isSame(hello))
                .call(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryCall(hello);
        assertThat(hello).isSameAs(hello);

        try {
            link.tryCall( whatup );
            fail("should have thrown precondition-failed");
        } catch (PreconditionFailedException e) {
            // expected and correct
        }
    }

    @Test
    public void testSubManipulation() throws Throwable {

        LinkBuilder builder = new LinkBuilder(methodType(Object.class, Object.class));

        String howdy = "howdy";

        Link link = builder
                .insert(0, howdy)
                .guardWith(isSame(howdy))
                .drop(0)
                .guard().filter(0, Filters.string())
                    .with(isEqual("42"))
                .call(new PrintAndReturnInvoker());
        assertThat(link).isNotNull();

        Object result = link.tryCall(42);
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
