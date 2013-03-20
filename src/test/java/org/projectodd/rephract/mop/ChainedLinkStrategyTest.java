package org.projectodd.rephract.mop;

import static org.fest.assertions.Assertions.*;

import java.lang.invoke.CallSite;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.mop.MockFrontLinkStrategy.WrappedObject;
import org.projectodd.rephract.mop.java.Cheese;
import org.projectodd.rephract.mop.java.DynamicMethod;
import org.projectodd.rephract.mop.java.JavaInstanceLinkStrategy;
import org.projectodd.rephract.mop.java.Person;

public class ChainedLinkStrategyTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinkStrategy(new MockFrontLinkStrategy());
        this.linker.addLinkStrategy(new JavaInstanceLinkStrategy());
    }

    @Test
    public void testWrapCallMethod() throws Throwable {

        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, String.class);

        Cheese swiss = new Cheese("swiss", 2);
        Person bob = new Person("bob", 39);

        Object method = null;

        method = callSite.getTarget().invoke(swiss, "melt");
        assertThat(method).isInstanceOf(DynamicMethod.class);

        CallSite callSite2 = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        Object result = null;

        result = callSite2.getTarget().invoke(method, swiss, new Object[] { "taco" });
        assertThat(result).isInstanceOf(WrappedObject.class);

        assertThat(((WrappedObject) result).getObject()).isEqualTo("melting for: taco");
        
        result = callSite2.getTarget().invoke(method, swiss, new Object[] { bob });
        assertThat(result).isInstanceOf(WrappedObject.class);

        assertThat(((WrappedObject) result).getObject()).isEqualTo("melted by: bob");

    }

}
