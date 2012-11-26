package org.projectodd.linkfusion.mop;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.invoke.CallSite;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.linkfusion.FusionLinker;

public class ContextualLinkStrategyTest {

    private FusionLinker linker;

    @Before
    public void setUp() {
        this.linker = new FusionLinker();
        this.linker.addLinkStrategy(new MockContextualLinkStrategy());
    }

    @Test
    public void testGetProperty_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getProperty", Object.class, Object.class, Object.class, String.class);

        LangContext context = new LangContext();
        LangObject bob = new LangObject();

        bob.put(context, "name", "bob");
        bob.put(context, "age", 39);

        Object result = null;

        result = callSite.getTarget().invoke(bob, context, "name");
        assertThat(result).isEqualTo("bob");

        result = callSite.getTarget().invoke(bob, context, "age");
        assertThat(result).isEqualTo(39);

    }

    @Test
    public void testGetProperty_withoutContext() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getProperty", Object.class, Object.class, String.class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangObject bob = new LangObject();

        bob.put(context, "name", "bob");
        bob.put(context, "age", 39);

        Object result = null;

        result = callSite.getTarget().invoke(bob, "name");
        assertThat(result).isEqualTo("bob");

        result = callSite.getTarget().invoke(bob, "age");
        assertThat(result).isEqualTo(39);

    }

    @Test
    public void testGetProperty_withWrongContext() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getProperty", Object.class, Object.class, Object.class, String.class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangObject bob = new LangObject();

        bob.put(context, "name", "bob");
        bob.put(context, "age", 39);

        Object result = null;

        result = callSite.getTarget().invoke(bob, "not my context", "name");
        assertThat(result).isEqualTo("bob");

        result = callSite.getTarget().invoke(bob, "not my context", "age");
        assertThat(result).isEqualTo(39);
    }

    @Test
    public void testSetProperty_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:setProperty", void.class, Object.class, Object.class, String.class, Object.class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangObject bob = new LangObject();

        callSite.getTarget().invoke(bob, context, "name", "bob");
        callSite.getTarget().invoke(bob, context, "age", 39);

        assertThat(bob.get(context, "name")).isEqualTo("bob");
        assertThat(bob.get(context, "age")).isEqualTo(39);
    }
    
    @Test
    public void testSetProperty_withoutContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:setProperty", void.class, Object.class, String.class, Object.class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangObject bob = new LangObject();

        callSite.getTarget().invoke(bob, "name", "bob");
        callSite.getTarget().invoke(bob, "age", 39);

        assertThat(bob.get(context, "name")).isEqualTo("bob");
        assertThat(bob.get(context, "age")).isEqualTo(39);
    }
    
    @Test
    public void testSetProperty_withWrongContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:setProperty", void.class, Object.class, Object.class, String.class, Object.class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangObject bob = new LangObject();

        callSite.getTarget().invoke(bob, "not my context", "name", "bob");
        callSite.getTarget().invoke(bob, "not my context", "age", 39);

        assertThat(bob.get(context, "name")).isEqualTo("bob");
        assertThat(bob.get(context, "age")).isEqualTo(39);
    }
    
    @Test
    public void testCall_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, context, null, new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }
    
    @Test
    public void testCall_withoutContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, null, new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }
    
    @Test
    public void testCall_withWrongContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, "not my context", null, new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }
    
    @Test
    public void testConstruct_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:construct", Object.class, Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, context, new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }
    
    @Test
    public void testConstruct_withoutContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:construct", Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }
    
    @Test
    public void testConstruct_withWrongContext() throws Throwable {
        CallSite callSite = linker.bootstrap("fusion:construct", Object.class, Object.class, Object.class, Object[].class);

        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        LangFunction function = new LangFunction();

        
        Object result = callSite.getTarget().invoke( function, "not my context", new Object[] { "one", "two" } );
        assertThat( result ).isEqualTo( "one,two" );
    }

}
