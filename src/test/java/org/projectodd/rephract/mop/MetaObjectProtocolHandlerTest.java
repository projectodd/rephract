package org.projectodd.rephract.mop;

import static org.fest.assertions.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.mop.MetaObjectProtocolHandler;
import org.projectodd.rephract.mop.java.DynamicMethod;
import org.projectodd.rephract.mop.java.JavaInstanceLinkStrategy;
import org.projectodd.rephract.mop.java.Person;

public class MetaObjectProtocolHandlerTest {

    private MetaObjectProtocolHandler handler;
    private Person javaBob;
    private LangObject langBob;

    @Before
    public void setUp() throws Throwable {
        this.handler = new MetaObjectProtocolHandler();
        this.handler.addLinkStrategy(new MockContextualLinkStrategy());
        this.handler.addLinkStrategy(new JavaInstanceLinkStrategy());

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);

        this.javaBob = new Person("bob", 39);
        this.langBob = new LangObject();

        this.langBob.put(context, "name", "lang: bob mcwhirter");
        this.langBob.put(context, "age", 93);
        this.langBob.put(context, "doSomething", new LangFunction());
    }

    @Test
    public void testGetProperty() throws Throwable {

        Object result = null;

        result = handler.getProperty(javaBob, "name");
        assertThat(result).isEqualTo("bob");

        result = handler.getProperty(javaBob, "age");
        assertThat(result).isEqualTo(39);

        result = handler.getProperty(langBob, "name");
        assertThat(result).isEqualTo("lang: bob mcwhirter");

        result = handler.getProperty(langBob, "age");
        assertThat(result).isEqualTo(93);

        result = handler.getProperty(javaBob, "name");
        assertThat(result).isEqualTo("bob");

        result = handler.getProperty(javaBob, "age");
        assertThat(result).isEqualTo(39);

        result = handler.getProperty(langBob, "name");
        assertThat(result).isEqualTo("lang: bob mcwhirter");

        result = handler.getProperty(langBob, "age");
        assertThat(result).isEqualTo(93);
    }

    @Test
    public void testSetProperty() throws Throwable {
        Object result = null;

        handler.setProperty(javaBob, "name", "bob mcwhirter");
        handler.setProperty(javaBob, "age", 40);

        result = handler.getProperty(javaBob, "name");
        assertThat(result).isEqualTo("bob mcwhirter");

        result = handler.getProperty(javaBob, "age");
        assertThat(result).isEqualTo(40);

        assertThat(javaBob.getName()).isEqualTo("bob mcwhirter");
        assertThat(javaBob.getAge()).isEqualTo(40);

        handler.setProperty(langBob, "name", "bob mcwhirter, esquire");
        handler.setProperty(langBob, "age", 109);

        result = handler.getProperty(langBob, "name");
        assertThat(result).isEqualTo("bob mcwhirter, esquire");

        result = handler.getProperty(langBob, "age");
        assertThat(result).isEqualTo(109);
    }

    @Test
    public void testGetMethod() throws Throwable {
        Object result = null;

        result = handler.getMethod(javaBob, "doSomething");
        assertThat(result).isInstanceOf(DynamicMethod.class);
    }

    @Test
    public void testCall() throws Throwable {
        Object result = null;

        result = handler.getMethod(javaBob, "doSomething");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        result = handler.call(result, javaBob);
        assertThat(result).isEqualTo("something: bob");
        
        result = handler.getMethod(langBob, "doSomething" );
        assertThat( result).isInstanceOf( LangFunction.class );
        
        result = handler.call( result, langBob );
        assertThat( result ).isEqualTo("");
    }

    @Test
    public void testCallMethod() throws Throwable {
        Object result = null;

        result = handler.callMethod(javaBob, "doSomething");
        assertThat(result).isEqualTo("something: bob");
        
        result = handler.callMethod(langBob, "doSomething" );
        assertThat(result).isEqualTo("");

    }
}
