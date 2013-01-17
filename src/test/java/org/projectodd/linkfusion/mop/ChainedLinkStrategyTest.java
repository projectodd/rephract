package org.projectodd.linkfusion.mop;

import static org.fest.assertions.Assertions.*;

import java.lang.invoke.CallSite;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.linkfusion.FusionLinker;
import org.projectodd.linkfusion.mop.MockFrontLinkStrategy.WrappedObject;
import org.projectodd.linkfusion.mop.java.Cheese;
import org.projectodd.linkfusion.mop.java.DynamicMethod;
import org.projectodd.linkfusion.mop.java.JavaInstanceLinkStrategy;
import org.projectodd.linkfusion.mop.java.Person;

public class ChainedLinkStrategyTest {

    private FusionLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new FusionLinker();
        this.linker.addLinkStrategy(new MockFrontLinkStrategy());
        this.linker.addLinkStrategy(new JavaInstanceLinkStrategy());
    }

    @Test
    public void testWrapCallMethod() throws Throwable {

        CallSite callSite = linker.bootstrap("fusion:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object method = null;

        method = callSite.getTarget().invoke(swiss, "melt");
        assertThat(method).isInstanceOf(DynamicMethod.class);

        CallSite callSite2 = linker.bootstrap("fusion:call", Object.class, Object.class, Object.class, Object[].class);

        Object result = null;

        result = callSite2.getTarget().invoke(method, swiss, new Object[] { "taco" });
        assertThat(result).isInstanceOf(WrappedObject.class);

        assertThat(((WrappedObject) result).getObject()).isEqualTo("melting for: taco");
        
        result = callSite2.getTarget().invoke(method, swiss, new Object[] { bob });
        assertThat(result).isInstanceOf(WrappedObject.class);

        assertThat(((WrappedObject) result).getObject()).isEqualTo("melted by: bob");

    }

}
