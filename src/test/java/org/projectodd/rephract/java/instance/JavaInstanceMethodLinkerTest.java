package org.projectodd.rephract.java.instance;

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

public class JavaInstanceMethodLinkerTest {

    private RephractLinker linker;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        this.linker = new RephractLinker();
        this.linker.addLinker(new JavaInstanceMethodLinker());
    }

    @Test
    public void testGetMethod() throws Throwable {
        CallSite callSite = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class);

        DynamicMethod result = null;

        Person bob = new Person("Bob McWhirter", 40);
        Dog moses = new Dog("Moses", 7);

        result = (DynamicMethod) callSite.getTarget().invokeWithArguments(bob, "eat");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        assertThat(result.getName()).isEqualTo("eat");
        assertThat(result.getMethods()).hasSize(2);

        result = (DynamicMethod) callSite.getTarget().invokeWithArguments(bob, "drink");
        assertThat(result).isInstanceOf(DynamicMethod.class);

        assertThat(result.getName()).isEqualTo("drink");
        assertThat(result.getMethods()).hasSize(2);
    }

    @Test
    public void testCallMethod() throws Throwable {
        CallSite getMethod = linker.bootstrap("dyn:getMethod", Object.class, Object.class, Object.class);

        MockObject obj = new MockObject();

        DynamicMethod method = (DynamicMethod) getMethod.getTarget().invokeWithArguments(obj, "foo");

        CallSite call = linker.bootstrap("dyn:call", Object.class, Object.class, Object.class, Object[].class);

        Object result = call.getTarget().invokeWithArguments( method, obj, new Object[]{ 42 } );

        assertThat( obj.longFoo ).isNull();
        assertThat( obj.intFoo ).isEqualTo(42);
        assertThat( obj.stringFoo ).isNull();

        obj.reset();

        result = call.getTarget().invokeWithArguments( method, obj, new Object[]{ "forty-two"} );

        assertThat( obj.longFoo ).isNull();
        assertThat( obj.intFoo ).isNull();
        assertThat( obj.stringFoo ).isEqualTo( "forty-two" );

    }


}
