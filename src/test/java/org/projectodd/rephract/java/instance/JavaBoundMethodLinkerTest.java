package org.projectodd.rephract.java.instance;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.java.Dog;
import org.projectodd.rephract.java.MockObject;
import org.projectodd.rephract.java.Person;
import org.projectodd.rephract.java.reflect.BoundDynamicMethod;
import org.projectodd.rephract.java.reflect.DynamicMethod;

import java.lang.invoke.CallSite;

import static org.fest.assertions.Assertions.assertThat;

public class JavaBoundMethodLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaBoundMethodLinker());
        this.linker.addLinker(new JavaInstanceMethodLinker());
    }

    @Test
    public void testGetMethod() throws Throwable {
        CallSite getMethod = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class);

        DynamicMethod method = null;

        Person bob = new Person("Bob McWhirter", 40);

        method = (DynamicMethod) getMethod.getTarget().invokeWithArguments(bob, "eat");

        BoundDynamicMethod boundMethod = method.bind( bob );

        CallSite call = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class );

        call.getTarget().invokeWithArguments( boundMethod, bob, new Object[]{ "food" } );
    }


}
