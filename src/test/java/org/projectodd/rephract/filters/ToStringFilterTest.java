package org.projectodd.rephract.filters;

import org.junit.Test;
import org.projectodd.rephract.builder.LinkBuilder;

import java.lang.invoke.MethodHandle;

import static org.fest.assertions.Assertions.*;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ToStringFilterTest {

    @Test
    public void testFilter() throws Throwable {

        ToStringFilter filter = new ToStringFilter();
        MethodHandle methodHandle = filter.methodHandle(methodType(Object.class, Object.class));
        Object result = methodHandle.invokeWithArguments( 42 );

        assertThat( result ).isInstanceOf( String.class );
        assertThat( result ).isEqualTo( "42" );

    }
}
