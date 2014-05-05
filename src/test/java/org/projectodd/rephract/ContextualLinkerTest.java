package org.projectodd.rephract;

import org.junit.Before;
import org.junit.Test;

import java.lang.invoke.CallSite;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ContextualLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() {
        this.linker = new RephractLinker();
        this.linker.addLinker(new ContextualMapLinker());
        this.linker.addLinker(new ContextualInvokableLinker());
    }

    @Test
    public void testGetProperty_linkable_noContext() throws Throwable {
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
    public void testGetProperty_linkable_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        MockContext context = new MockContext();

        Map bob = new HashMap<>();
        bob.put("name", "Bob McWhirter");
        bob.put("age", 40);

        result = callSite.getTarget().invokeWithArguments(bob, context, "name");
        assertThat(result).isEqualTo("Bob McWhirter");

        result = callSite.getTarget().invokeWithArguments(bob, context, "age");
        assertThat(result).isEqualTo(40);

        result = callSite.getTarget().invokeWithArguments(bob, context, "nonexistant");
        assertThat(result).isNull();
    }

    @Test
    public void testGetProperty_linkable_withWrongContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        OtherMockContext context = new OtherMockContext();

        Map bob = new HashMap<>();
        bob.put("name", "Bob McWhirter");
        bob.put("age", 40);

        result = callSite.getTarget().invokeWithArguments(bob, context, "name");
        assertThat(result).isEqualTo("Bob McWhirter");

        result = callSite.getTarget().invokeWithArguments(bob, context, "age");
        assertThat(result).isEqualTo(40);

        result = callSite.getTarget().invokeWithArguments(bob, context, "nonexistant");
        assertThat(result).isNull();
    }

    @Test
    public void testGetProperty_nonLinkable_noContext() throws Throwable {
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
    public void testGetProperty_nonLinkable_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class, Object.class);


        MockContext context = new MockContext();

        Object result = null;

        Object bob = new Object();

        try {
            result = callSite.getTarget().invokeWithArguments(bob, context, "name");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testSetProperty_linkable_noContext() throws Throwable {
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
    public void testSetProperty_linkable_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        MockContext context = new MockContext();

        Map bob = new HashMap<>();

        callSite.getTarget().invokeWithArguments(bob, context, "name", "Bob McWhirter");
        assertThat(bob.get("name")).isEqualTo("Bob McWhirter");

        callSite.getTarget().invokeWithArguments(bob, context, "age", 40);
        assertThat(bob.get("age")).isEqualTo(40);

        callSite.getTarget().invokeWithArguments(bob, context, "name", "Armadillo Bob");
        assertThat(bob.get("name")).isEqualTo("Armadillo Bob");
    }

    @Test
    public void testSetProperty_linkable_wrongContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        OtherMockContext context = new OtherMockContext();

        Map bob = new HashMap<>();

        callSite.getTarget().invokeWithArguments(bob, context, "name", "Bob McWhirter");
        assertThat(bob.get("name")).isEqualTo("Bob McWhirter");

        callSite.getTarget().invokeWithArguments(bob, context, "age", 40);
        assertThat(bob.get("age")).isEqualTo(40);

        callSite.getTarget().invokeWithArguments(bob, context, "name", "Armadillo Bob");
        assertThat(bob.get("name")).isEqualTo("Armadillo Bob");
    }

    @Test
    public void testSetProperty_nonLinkable_noContext() throws Throwable {
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
    public void testSetProperty_nonLinkable_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class, Object.class);

        MockContext context = new MockContext();
        Object bob = new Object();

        try {
            callSite.getTarget().invokeWithArguments(bob, context, "name", "Bob McWhirter");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testCall_linkable_noContext() throws Throwable {
        // receiver self args[]
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        MockContextualInvokable invokable = new MockContextualInvokable();

        Object self = "tacos";

        callSite.getTarget().invokeWithArguments(invokable, self, new Object[]{});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args).isEmpty();

        callSite.getTarget().invokeWithArguments(invokable, self, new Object[]{"one", 42});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args[0]).isEqualTo("one");
        assertThat(invokable.args[1]).isEqualTo(42);
    }

    @Test
    public void testCall_linkable_withContext() throws Throwable {
        // receiver context self args[]
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object.class, Object[].class);

        MockContextualInvokable invokable = new MockContextualInvokable();

        Object self = "tacos";
        MockContext context = new MockContext();


        callSite.getTarget().invokeWithArguments(invokable, context, self, new Object[]{});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args).isEmpty();
        assertThat(invokable.context).isSameAs(context);

        callSite.getTarget().invokeWithArguments(invokable, context, self, new Object[]{"one", 42});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args[0]).isEqualTo("one");
        assertThat(invokable.args[1]).isEqualTo(42);
        assertThat(invokable.context).isSameAs(context);
    }

    @Test
    public void testCall_linkable_wrongContext() throws Throwable {
        // receiver context self args[]
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object.class, Object[].class);

        MockContextualInvokable invokable = new MockContextualInvokable();

        OtherMockContext context = new OtherMockContext();

        Object self = "tacos";

        callSite.getTarget().invokeWithArguments(invokable, context, self, new Object[]{});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args).isEmpty();
        assertThat(invokable.context).isInstanceOf(MockContext.class);

        callSite.getTarget().invokeWithArguments(invokable, context, self, new Object[]{"one", 42});
        assertThat(invokable.self).isSameAs(self);
        assertThat(invokable.args[0]).isEqualTo("one");
        assertThat(invokable.args[1]).isEqualTo(42);
        assertThat(invokable.context).isInstanceOf(MockContext.class);
    }

    @Test
    public void testCall_nonLinkable_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object.class, Object[].class);

        Object nonInvokable = new Object();
        MockContext context = new MockContext();
        Object self = "tacos";

        try {
            callSite.getTarget().invokeWithArguments(nonInvokable, context, self, new Object[]{});
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testCall_nonLinkable_noContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        Object nonInvokable = new Object();
        Object self = "tacos";

        try {
            callSite.getTarget().invokeWithArguments(nonInvokable, self, new Object[]{});
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testConstruct_linkable_withContext() throws Throwable {
        MockContextualConstructable constructable = new MockContextualConstructable();

        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object.class, Object[].class);

        MockContext context = new MockContext();

        callSite.getTarget().invokeWithArguments(constructable, context, new Object[]{});
        assertThat(constructable.args).isEmpty();
        assertThat(constructable.context).isSameAs(context);

        callSite.getTarget().invokeWithArguments(constructable, context, new Object[]{"one", 42});
        assertThat(constructable.args[0]).isEqualTo("one");
        assertThat(constructable.args[1]).isEqualTo(42);
        assertThat(constructable.context).isSameAs(context);
    }

    @Test
    public void testConstruct_linkable_wrongContext() throws Throwable {
        MockContextualConstructable constructable = new MockContextualConstructable();

        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object.class, Object[].class);

        OtherMockContext context = new OtherMockContext();

        callSite.getTarget().invokeWithArguments(constructable, context, new Object[]{});
        assertThat(constructable.context).isInstanceOf(MockContext.class);
        assertThat(constructable.args).isEmpty();

        callSite.getTarget().invokeWithArguments(constructable, context, new Object[]{"one", 42});
        assertThat(constructable.context).isInstanceOf(MockContext.class);
        assertThat(constructable.args[0]).isEqualTo("one");
        assertThat(constructable.args[1]).isEqualTo(42);
    }
     /*

    @Test
    public void testConstruct_nonLinkable() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object[].class);

        Invokable<String> invokable = new Invokable<String>() {
            @Override
            public String invoke(Object... args) throws Throwable {
                return "invoked: " + Arrays.asList(args);
            }
        };

        try {
            callSite.getTarget().invokeWithArguments(invokable, new Object[]{});
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

    }


*/
}
