package org.projectodd.linkfusion.mop.java;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.invoke.CallSite;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.linkfusion.FusionLinker;
import org.projectodd.linkfusion.mop.java.DynamicMethod;
import org.projectodd.linkfusion.mop.java.JavaLinkStrategy;

public class JavaBeansLinkStrategyTest {
    
    private FusionLinker linker;

    @Before
    public void setUp() {
        this.linker = new FusionLinker();
        this.linker.addLinkStrategy(new JavaLinkStrategy());
    }

    @Test
    public void testLinkJavaBeans_getProperty_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getProperty", Object.class, Object.class, String.class);

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
    public void testLinkJavaBeans_getProperty_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getProperty:name", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss);
        assertThat(result).isEqualTo("swiss");

        result = callSite.getTarget().invoke(bob);
        assertThat(result).isEqualTo("bob");
    }

    @Test
    public void testLinkJavaBeans_setProperty_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:setProperty", void.class, Object.class, String.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "name", "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "name", "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_setProperty_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:setProperty:name", void.class, Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_getMethod_dynamic() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

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
    public void testLinkJavaBeans_getMethod_fixed() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getMethod:melt", Object.class, Object.class);

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
    public void testLinkJavaBeans_getMethod_call() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object method = null;

        method = callSite.getTarget().invoke(swiss, "melt");
        assertThat(method).isInstanceOf(DynamicMethod.class);
        
        CallSite callSite2 = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object[].class );
        
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
    public void testLinkJavaBeans_construct() throws Throwable {
        
        CallSite callSite = linker.bootstrap("fusion:construct", Object.class, Object.class, Object[].class);
        
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

}
