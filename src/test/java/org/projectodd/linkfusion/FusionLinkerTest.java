package org.projectodd.linkfusion;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.invoke.CallSite;

import org.junit.Test;
import org.projectodd.linkfusion.strategy.javabeans.JavaBeansLinkStrategy;
import org.projectodd.linkfusion.strategy.javabeans.UnboundMethod;

public class FusionLinkerTest {

    @Test
    public void testBootstrapMethodHandle() throws Throwable {
        FusionLinker linker = new FusionLinker();
        assertThat(linker.getBootstrapMethodHandle())
                .isNotNull();
    }

    @Test
    public void testLinkJavaBeans_getProperty_dynamic() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

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
    }

    @Test
    public void testLinkJavaBeans_getProperty_fixed() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

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

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

        CallSite callSite = linker.bootstrap("fusion:setProperty", Object.class, Object.class, String.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "name", "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "name", "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_setProperty_fixed() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

        CallSite callSite = linker.bootstrap("fusion:setProperty:name", Object.class, Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        callSite.getTarget().invoke(swiss, "aged swiss");
        assertThat(swiss.getName()).isEqualTo("aged swiss");

        callSite.getTarget().invoke(bob, "bob mcwhirter");
        assertThat(bob.getName()).isEqualTo("bob mcwhirter");
    }

    @Test
    public void testLinkJavaBeans_getMethod_dynamic() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

        CallSite callSite = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "melt");
        assertThat(result).isInstanceOf(UnboundMethod.class);

        try {
            result = callSite.getTarget().invoke(bob, "melt");
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

    }
    
    @Test
    public void testLinkJavaBeans_getMethod_fixed() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

        CallSite callSite = linker.bootstrap("fusion:getMethod:melt", Object.class, Object.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss);
        assertThat(result).isInstanceOf(UnboundMethod.class);

        try {
            result = callSite.getTarget().invoke(bob);
            throw new AssertionError("bob can't melt");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

    }
    
    @Test
    public void testLinkJavaBeans_getMethod_call_dynamic() throws Throwable {

        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy(new JavaBeansLinkStrategy());

        CallSite callSite = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object result = null;

        result = callSite.getTarget().invoke(swiss, "melt");
        assertThat(result).isInstanceOf(UnboundMethod.class);
        
        CallSite callSite2 = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object[].class );
        
        //callSite2.getTarget().invoke( result, swiss, new Object[]{} );
    }

}
