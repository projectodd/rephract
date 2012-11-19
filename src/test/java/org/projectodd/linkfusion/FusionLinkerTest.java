package org.projectodd.linkfusion;

import static org.fest.assertions.Assertions.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;

import org.junit.Test;
import org.projectodd.linkfusion.strategy.javabeans.JavaBeansLinkStrategy;

public class FusionLinkerTest {
    
    @Test
    public void testBootstrapMethodHandle() throws Throwable {
        FusionLinker linker = new FusionLinker();
        assertThat( linker.getBootstrapMethodHandle() )
            .isNotNull();
    }
    
    @Test
    public void testLinkJavaBeans_getProperty_dynamic() throws Throwable {
        
        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy( new JavaBeansLinkStrategy() );
        
        CallSite callSite = linker.bootstrap("fusion:getProperty", MethodType.methodType(Object.class, Object.class, String.class) );
        
        Cheese swiss = new Cheese( "swiss", 2 );
        Person bob = new Person( "bob", 39 );
        
        Object result = null;
        
        result = callSite.getTarget().invoke( swiss, "name" );
        assertThat( result ).isEqualTo( "swiss" );
        
        result = callSite.getTarget().invoke( bob, "name" );
        assertThat( result ).isEqualTo( "bob" );
    }
    
    @Test
    public void testLinkJavaBeans_getProperty_fixed() throws Throwable {
        
        FusionLinker linker = new FusionLinker();
        linker.addLinkStrategy( new JavaBeansLinkStrategy() );
        
        CallSite callSite = linker.bootstrap("fusion:getProperty:name", MethodType.methodType(Object.class, Object.class) );
        
        Cheese swiss = new Cheese( "swiss", 2 );
        Person bob = new Person( "bob", 39 );
        
        Object result = null;
        
        result = callSite.getTarget().invoke( swiss );
        assertThat( result ).isEqualTo( "swiss" );
        
        result = callSite.getTarget().invoke( bob );
        assertThat( result ).isEqualTo( "bob" );
    }
    
    
}
