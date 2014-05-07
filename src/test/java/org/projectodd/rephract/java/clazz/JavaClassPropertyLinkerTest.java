package org.projectodd.rephract.java.clazz;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.java.Dog;

import java.lang.invoke.CallSite;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JavaClassPropertyLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaClassPropertyLinker());
    }

    @Test
    public void testGetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class);

        Object result = null;

        result = callSite.getTarget().invokeWithArguments(Dog.class, "translation");
        assertThat(result).isEqualTo("Chien");
    }

    @Test
    public void testSetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        result = callSite.getTarget().invokeWithArguments(Dog.class, "translation", "loco");
        assertThat( Dog.translation ).isEqualTo( "loco" );
    }


}
