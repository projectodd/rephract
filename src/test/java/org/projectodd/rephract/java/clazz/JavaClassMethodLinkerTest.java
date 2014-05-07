package org.projectodd.rephract.java.clazz;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.java.Dog;
import org.projectodd.rephract.java.MockObject;
import org.projectodd.rephract.java.Person;
import org.projectodd.rephract.java.instance.JavaInstanceMethodLinker;
import org.projectodd.rephract.java.reflect.DynamicMethod;

import java.lang.invoke.CallSite;

import static org.fest.assertions.Assertions.assertThat;

public class JavaClassMethodLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaClassMethodLinker());
    }

    @Test
    public void testGetMethod() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class);

        DynamicMethod result = null;

        result = (DynamicMethod) callSite.getTarget().invokeWithArguments(Dog.class, "bark");
        assertThat(result).isInstanceOf(DynamicMethod.class);
    }

    @Test
    public void testCallMethod() throws Throwable {
        CallSite getMethod = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class);

        DynamicMethod method = null;

        method = (DynamicMethod) getMethod.getTarget().invokeWithArguments(Dog.class, "bark");
        assertThat(method).isInstanceOf(DynamicMethod.class);

        CallSite call = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        Object result = call.getTarget().invokeWithArguments( method, null, new Object[] { "French" } );
        assertThat( result ).isEqualTo( "bark in French!" );
    }

    @Test
    public void testConstruct() throws Throwable {
        CallSite callSite = linker.bootstrap( "dyn:construct", Object.class, Object.class, Object[].class);

        Object result = callSite.getTarget().invokeWithArguments( Dog.class, new Object[] { "Moses", 7 } );

        assertThat( result ).isInstanceOf( Dog.class );
        assertThat( ((Dog)result).getName() ).isEqualTo("Moses");
        assertThat( ((Dog)result).getAge() ).isEqualTo(7);
    }



}
