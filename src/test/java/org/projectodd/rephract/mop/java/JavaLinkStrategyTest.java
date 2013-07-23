package org.projectodd.rephract.mop.java;

import static org.fest.assertions.Assertions.*;

import java.lang.invoke.CallSite;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;

public class JavaLinkStrategyTest {
    
    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        ResolverManager manager = new ResolverManager();
        this.linker.addLinkStrategy(new JavaClassLinkStrategy( manager ));
        this.linker.addLinkStrategy(new JavaInstanceLinkStrategy( manager ));
    }

    @Test
    public void testLinkJavaBeans_getProperty_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;
        
        result = callSite.getTarget().invoke(swiss, "name");
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob, "name");
        assertThat(result).isEqualTo("bob");
        
        result = callSite.getTarget().invoke( swiss, "age" );
        assertThat(result).isEqualTo(2);
        
        result = callSite.getTarget().invoke( bob, "age" );
        assertThat(result).isEqualTo(39);

        result = callSite.getTarget().invoke(swiss, "name");
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob, "name");
        assertThat(result).isEqualTo("bob");
        
        result = callSite.getTarget().invoke( swiss, "age" );
        assertThat(result).isEqualTo(2);
        
        result = callSite.getTarget().invoke( bob, "age" );
        assertThat(result).isEqualTo(39);
    }
    
    @Test
    public void testLinkJavaBeans_getProperty_dynamic_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;
        
        result = callSite.getTarget().invoke(swiss, "random context", "name");
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob, "random context", "name");
        assertThat(result).isEqualTo("bob");
        
        result = callSite.getTarget().invoke( swiss, "random context", "age" );
        assertThat(result).isEqualTo(2);
        
        result = callSite.getTarget().invoke( bob, "random context", "age" );
        assertThat(result).isEqualTo(39);

        result = callSite.getTarget().invoke(swiss, "random context", "name");
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob, "random context", "name");
        assertThat(result).isEqualTo("bob");
        
        result = callSite.getTarget().invoke( swiss, "random context", "age" );
        assertThat(result).isEqualTo(2);
        
        result = callSite.getTarget().invoke( bob, "random context", "age" );
        assertThat(result).isEqualTo(39);
    }

    @Test
    public void testLinkJavaBeans_getProperty_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getProperty:name", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss);
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob);
        assertThat(result).isEqualTo("bob");
    }
    
    @Test
    public void testLinkJavaBeans_getProperty_fixed_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getProperty:name", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "random context");
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob, "random context" );
        assertThat(result).isEqualTo("bob");
    }

    @Test
    public void testLinkJavaBeans_setProperty_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:setProperty", void.class, Object.class, String.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "name", "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "name", "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }
    
    @Test
    public void testLinkJavaBeans_setProperty_dynamic_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:setProperty", void.class, Object.class, Object.class, String.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "random context", "name", "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "random context", "name", "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_setProperty_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:setProperty:name", void.class, Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }
    
    @Test
    public void testLinkJavaBeans_setProperty_fixed_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:setProperty:name", void.class, Object.class, Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "random context", "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "random context", "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_getMethod_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "melt");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        try {
            result = callSite.getTarget().invoke(bob, "melt");
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

    }
    
    @Test
    public void testLinkJavaBeans_getMethod_dynamic_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "random context", "melt");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        try {
            result = callSite.getTarget().invoke(bob, "random context", "melt");
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }
    
    @Test
    public void testLinkJavaBeans_getMethod_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod:melt", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss);
        assertThat(result).isInstanceOf(DynamicMethod.class);

        try {
            result = callSite.getTarget().invoke(bob);
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }
    
    @Test
    public void testMethodsOnDynamicMethod() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getMethod:melt", Object.class, Object.class);
        CallSite nameCallSite = linker.bootstrap("dyn:getMethod:getName", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);

        Object result = null;

        result = callSite.getTarget().invoke(swiss);
        assertThat(result).isInstanceOf(DynamicMethod.class);
        
        Object nameResult = nameCallSite.getTarget().invoke( result );
        assertThat( nameResult ).isInstanceOf( DynamicMethod.class );
        assertThat( ((DynamicMethod)nameResult).getName() ).isEqualTo( "getName" );
    }
    
    @Test
    public void testBoundMethod() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getMethod:melt", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);

        DynamicMethod result = null;

        result = (DynamicMethod) callSite.getTarget().invoke(swiss);
        assertThat(result).isInstanceOf(DynamicMethod.class);
        
        BoundDynamicMethod boundMethod = new BoundDynamicMethod(swiss, (DynamicMethod) result);
        
        CallSite callCallSite = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class );
        
        Object meltResult = callCallSite.getTarget().invoke( boundMethod, null, new Object[] { "taco" } );
        
        assertThat( meltResult ).isEqualTo( "melting for: taco" );
    }
    
    @Test
    public void testLinkJavaBeans_getMethod_fixed_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod:melt", Object.class, Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "random context");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        try {
            result = callSite.getTarget().invoke(bob, "random context");
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }
    
    @Test
    public void testLinkJavaBeans_getMethod_call() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object method = null;

        method = callSite.getTarget().invoke(swiss, "melt");
        assertThat(method).isInstanceOf(DynamicMethod.class);
        
        CallSite callSite2 = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class );
        
        Object result = null;
        
        result = callSite2.getTarget().invoke( method, swiss, new Object[] { "taco" } );
        assertThat( result ).isEqualTo( "melting for: taco" );
        
        result = callSite2.getTarget().invoke( method, swiss, new Object[] { bob } );
        assertThat( result ).isEqualTo( "melted by: bob" );
        
        result = callSite2.getTarget().invoke( method, swiss, new Object[] { "taco" } );
        assertThat( result ).isEqualTo( "melting for: taco" );
        
        result = callSite2.getTarget().invoke( method, swiss, new Object[] { bob } );
        assertThat( result ).isEqualTo( "melted by: bob" );
    }
    
    @Test
    public void testLinkJavaBeans_getMethod_call_withContext() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object method = null;

        method = callSite.getTarget().invoke(swiss, "random context", "melt");
        assertThat(method).isInstanceOf(DynamicMethod.class);
        
        CallSite callSite2 = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object.class, Object[].class );
        
        Object result = null;
        
        result = callSite2.getTarget().invoke( method, "random context", swiss, new Object[] { "taco" } );
        assertThat( result ).isEqualTo( "melting for: taco" );
        
        result = callSite2.getTarget().invoke( method, "random context", swiss, new Object[] { bob } );
        assertThat( result ).isEqualTo( "melted by: bob" );
        
        result = callSite2.getTarget().invoke( method, "random context", swiss, new Object[] { "taco" } );
        assertThat( result ).isEqualTo( "melting for: taco" );
        
        result = callSite2.getTarget().invoke( method, "random context", swiss, new Object[] { bob } );
        assertThat( result ).isEqualTo( "melted by: bob" );
    }
    
    @Test
    public void testLinkJavaBeans_construct() throws Throwable {
        
        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object[].class);
        
        Object result = null;
        
        result = callSite.getTarget().invoke( Cheese.class, new Object[] { "swiss", 2 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Cheese.class);
        assertThat( ((Cheese)result).getName() ).isEqualTo( "swiss" );
        assertThat( ((Cheese)result).getAge() ).isEqualTo( 2 );
        
        result = callSite.getTarget().invoke( Person.class, new Object[] { "bob", 39 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Person.class);
        assertThat( ((Person)result).getName() ).isEqualTo( "bob" );
        assertThat( ((Person)result).getAge() ).isEqualTo( 39 );
        
        result = callSite.getTarget().invoke( Cheese.class, new Object[] { "swiss", 2 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Cheese.class);
        assertThat( ((Cheese)result).getName() ).isEqualTo( "swiss" );
        assertThat( ((Cheese)result).getAge() ).isEqualTo( 2 );
        
        result = callSite.getTarget().invoke( Person.class, new Object[] { "bob", 39 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Person.class);
        assertThat( ((Person)result).getName() ).isEqualTo( "bob" );
        assertThat( ((Person)result).getAge() ).isEqualTo( 39 );
        
    }
    
    @Test
    public void testLinkJavaBeans_construct_withContext() throws Throwable {
        
        CallSite callSite = linker.bootstrap("dyn:construct", Object.class, Object.class, Object.class, Object[].class);
        
        Object result = null;
        
        result = callSite.getTarget().invoke( Cheese.class, "random context", new Object[] { "swiss", 2 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Cheese.class);
        assertThat( ((Cheese)result).getName() ).isEqualTo( "swiss" );
        assertThat( ((Cheese)result).getAge() ).isEqualTo( 2 );
        
        result = callSite.getTarget().invoke( Person.class, "random context", new Object[] { "bob", 39 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Person.class);
        assertThat( ((Person)result).getName() ).isEqualTo( "bob" );
        assertThat( ((Person)result).getAge() ).isEqualTo( 39 );
        
        result = callSite.getTarget().invoke( Cheese.class, "random context", new Object[] { "swiss", 2 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Cheese.class);
        assertThat( ((Cheese)result).getName() ).isEqualTo( "swiss" );
        assertThat( ((Cheese)result).getAge() ).isEqualTo( 2 );
        
        result = callSite.getTarget().invoke( Person.class, "random context", new Object[] { "bob", 39 } );
        
        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(Person.class);
        assertThat( ((Person)result).getName() ).isEqualTo( "bob" );
        assertThat( ((Person)result).getAge() ).isEqualTo( 39 );
        
    }

    @Test
    public void testLinkJavaClass_getProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, String.class);

        Object result = null;
        
        result = callSite.getTarget().invoke(Float.class, "MAX_VALUE");
        assertThat(result).isEqualTo(Float.MAX_VALUE);
        
        result = callSite.getTarget().invoke(Double.class, "MAX_VALUE");
        assertThat(result).isEqualTo(Double.MAX_VALUE);
    }
    
    @Test
    public void testLinkJavaClass_getProperty_dynamic_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class, String.class);
        Object result = null;
        
        result = callSite.getTarget().invoke(Float.class, "random context", "MAX_VALUE");
        assertThat(result).isEqualTo(Float.MAX_VALUE);
        
        result = callSite.getTarget().invoke(Double.class, "random context", "MAX_VALUE");
        assertThat(result).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    public void testLinkJavaClass_getProperty_fixed() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty:MAX_VALUE", Object.class, Object.class);

        Object result = null;

        result = callSite.getTarget().invoke(Float.class);
        assertThat(result).isEqualTo(Float.MAX_VALUE);

        result = callSite.getTarget().invoke(Double.class);
        assertThat(result).isEqualTo(Double.MAX_VALUE);
    }
    
    @Test
    public void testLinkJavaClass_getProperty_fixed_withContext() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty:MAX_VALUE", Object.class, Object.class, String.class);

        Object result = null;

        result = callSite.getTarget().invoke(Float.class, "random context");
        assertThat(result).isEqualTo(Float.MAX_VALUE);

        result = callSite.getTarget().invoke(Double.class, "random context" );
        assertThat(result).isEqualTo(Double.MAX_VALUE);
    }
}
