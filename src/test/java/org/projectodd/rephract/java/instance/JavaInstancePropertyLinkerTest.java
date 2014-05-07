package org.projectodd.rephract.java.instance;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.*;
import org.projectodd.rephract.java.Dog;
import org.projectodd.rephract.java.Person;

import java.lang.invoke.CallSite;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JavaInstancePropertyLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaInstancePropertyLinker());
    }

    @Test
    public void testGetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class);

        Object result = null;

        Person bob = new Person("Bob McWhirter", 40);
        Dog moses = new Dog("Moses", 7 );

        result = callSite.getTarget().invokeWithArguments(bob, "name");
        assertThat(result).isEqualTo("Bob McWhirter");

        result = callSite.getTarget().invokeWithArguments(bob, "age");
        assertThat(result).isEqualTo(40);

        result = callSite.getTarget().invokeWithArguments(moses, "name");
        assertThat(result).isEqualTo("Moses");

        result = callSite.getTarget().invokeWithArguments(moses, "age");
        assertThat(result).isEqualTo(7);

        result = callSite.getTarget().invokeWithArguments(bob, "name");
        assertThat(result).isEqualTo("Bob McWhirter");

        result = callSite.getTarget().invokeWithArguments(bob, "age");
        assertThat(result).isEqualTo(40);

        result = callSite.getTarget().invokeWithArguments(moses, "name");
        assertThat(result).isEqualTo("Moses");

        result = callSite.getTarget().invokeWithArguments(moses, "age");
        assertThat(result).isEqualTo(7);

        try {
            callSite.getTarget().invokeWithArguments(bob, "nonexistant");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }

        try {
            callSite.getTarget().invokeWithArguments(moses, "nonexistant");
            fail("should have thrown no-such-method");
        } catch (NoSuchMethodError e) {
            // expected and correct
        }
    }

    @Test
    public void testSetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        Person bob = new Person("Bob McWhirter", 40);
        Dog moses = new Dog("Moses", 7 );

        result = callSite.getTarget().invokeWithArguments(bob, "name", "Armadillo Bob");
        assertThat(bob.getName()).isEqualTo("Armadillo Bob");

        result = callSite.getTarget().invokeWithArguments(bob, "age", 41);
        assertThat(bob.getAge()).isEqualTo(41);
    }

}
