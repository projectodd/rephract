package org.projectodd.rephract;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.lang.invoke.CallSite;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class NonContextualLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() {
        this.linker = new RephractLinker();
        this.linker.addLinker(new MapLinker());
        this.linker.addLinker(new InvokableLinker());
    }

    @Test
    public void testGetProperty_linkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class);

        Object result = null;

        Map bob = new HashMap<>();
        bob.put("name", "Bob McWhirter");
        bob.put("age", 40);

        result = callSite.getTarget().invokeWithArguments(bob, "name");
        assertThat(result).isEqualTo("Bob McWhirter");

        result = callSite.getTarget().invokeWithArguments(bob, "age");
        assertThat(result).isEqualTo(40);

        result = callSite.getTarget().invokeWithArguments(bob, "nonexistant");
        assertThat(result).isNull();
    }

    @Test
    public void testGetProperty_nonLinkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class);

        Object result = null;

        Object bob = new Object();

        try {
            result = callSite.getTarget().invokeWithArguments(bob, "name");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testSetProperty_linkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        Map bob = new HashMap<>();

        callSite.getTarget().invokeWithArguments(bob, "name", "Bob McWhirter");
        assertThat(bob.get("name")).isEqualTo("Bob McWhirter");

        callSite.getTarget().invokeWithArguments(bob, "age", 40);
        assertThat(bob.get("age")).isEqualTo(40);

        callSite.getTarget().invokeWithArguments(bob, "name", "Armadillo Bob");
        assertThat(bob.get("name")).isEqualTo("Armadillo Bob");
    }

    @Test
    public void testSetProperty_nonLinkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class);

        Object bob = new Object();

        try {
            callSite.getTarget().invokeWithArguments(bob, "name", "Bob McWhirter");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testCall_linkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        MockInvokable invokable = new MockInvokable();

        Object self = "tacos";

        callSite.getTarget().invokeWithArguments(invokable, self, new Object[]{});
        assertThat( invokable.self ).isSameAs( self );
        assertThat( invokable.args ).isEmpty();

        callSite.getTarget().invokeWithArguments(invokable, self, new Object[]{"one", 42});
        assertThat( invokable.self ).isSameAs( self );
        assertThat( invokable.args[0] ).isEqualTo( "one" );
        assertThat( invokable.args[1] ).isEqualTo( 42 );
    }

    @Test
    public void testCall_nonLinkable() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        Object nonInvokable = new Object();

        try {
            callSite.getTarget().invokeWithArguments(nonInvokable, null, new Object[]{});
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testConstruct_linkable() throws Throwable {
        MockConstructable constructable = new MockConstructable();

        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object[].class);

        callSite.getTarget().invokeWithArguments(constructable, new Object[]{});
        assertThat(constructable.args).isEmpty();

        callSite.getTarget().invokeWithArguments(constructable, new Object[]{"one", 42});
        assertThat(constructable.args[0]).isEqualTo("one" );
        assertThat(constructable.args[1]).isEqualTo(42);
    }

    @Test
    public void testConstruct_nonLinkable() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object[].class);

        Invokable invokable = new MockInvokable();

        try {
            callSite.getTarget().invokeWithArguments(invokable, new Object[]{});
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

    }

}
