package org.projectodd.linkfusion;

import static org.fest.assertions.Assertions.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;

import org.junit.Test;

public class FusionLinkerTest {
    
    @Test
    public void testBootstrapMethodHandle() throws Throwable {
        FusionLinker linker = new FusionLinker();
        assertThat( linker.getBootstrapMethodHandle() )
            .isNotNull();
    }
    
    @Test
    public void testLinkInitial() throws Throwable {
        FusionLinker linker = new FusionLinker();
        CallSite callSite = linker.bootstrap( "fusion:call", MethodType.methodType( Object.class, Object.class, Object.class, Object.class ) );
        System.err.println( "callSite: " + callSite );
        
        
        Object value = callSite.getTarget().invoke(  new Thing(), 42L, "taco" );
        System.err.println( "value: " + value );
        
        Object value2 = callSite.getTarget().invoke(  new Thing(), 42L, "taco" );
        System.err.println( "value: " + value2 );
    }
    
    public static class Thing {
        
    }

}
