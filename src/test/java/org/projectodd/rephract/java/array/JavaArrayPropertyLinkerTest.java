package org.projectodd.rephract.java.array;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.java.Dog;
import org.projectodd.rephract.java.Person;
import org.projectodd.rephract.java.instance.JavaInstancePropertyLinker;

import java.lang.invoke.CallSite;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JavaArrayPropertyLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaArrayPropertyLinker());
    }

    @Test
    public void testGetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getProperty", Object.class, Object.class, Object.class);

        Object result = null;

        int array[] = {1, 2, 3, 5};

        result = callSite.getTarget().invokeWithArguments(array, "length");
        assertThat(result).isEqualTo(4);

        result = callSite.getTarget().invokeWithArguments(array, "3");
        assertThat(result).isEqualTo(5);

        try {
            callSite.getTarget().invokeWithArguments(array, "42");
            fail("should have thrown array-index-out-of-bounds");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected and correct

        }
    }

    @Test
    public void testSetProperty() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:setProperty", Object.class, Object.class, Object.class, Object.class);

        Object result = null;

        int array[] = {1, 2, 3, 5};

        result = callSite.getTarget().invokeWithArguments(array, "0", 42 );
        assertThat( array[0] ).isEqualTo( 42 );

        try {
            callSite.getTarget().invokeWithArguments(array, "42", 99 );
            fail("should have thrown array-index-out-of-bounds");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected and correct

        }
    }


}
