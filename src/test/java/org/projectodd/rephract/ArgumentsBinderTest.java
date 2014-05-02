package org.projectodd.rephract;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class ArgumentsBinderTest {

    @Test
    public void testPermute() {
        ArgumentsBinder binder = new ArgumentsBinder("a", "b", "c");
        binder.permute(1, 2, 2, 0, 1);

        Object[] args = binder.toArray();
        assertThat(args).hasSize(5);
        assertThat(args[0]).isEqualTo("b");
        assertThat(args[1]).isEqualTo("c");
        assertThat(args[2]).isEqualTo("c");
        assertThat(args[3]).isEqualTo("a");
        assertThat(args[4]).isEqualTo("b");
    }

    @Test
    public void testCollect() {
        ArgumentsBinder binder = new ArgumentsBinder(42, "one", "taco");
        binder.collect(1, Object.class);

        Object[] args = binder.toArray();

        assertThat(args).isNotNull();
        assertThat(args).hasSize(2);
        assertThat(args[0]).isEqualTo(42);

        Object[] collected = (Object[]) args[1];

        assertThat(collected).isNotNull();
        assertThat(collected).hasSize(2);
        assertThat(collected[0]).isEqualTo("one");
        assertThat(collected[1]).isEqualTo("taco");
    }

    @Test
    public void testSpread() {
        ArgumentsBinder binder = new ArgumentsBinder(42, new Object[]{ "one", "two" } );
        binder.spread( Object.class, Object.class );

        Object[] args = binder.toArray();

        assertThat( args ).hasSize( 3 );
        assertThat( args[0] ).isEqualTo(42);
        assertThat( args[1] ).isEqualTo("one");
        assertThat( args[2] ).isEqualTo( "two" );
    }

    @Test
    public void testFilterArray() throws Throwable {
        ArgumentsBinder binder = new ArgumentsBinder( 42, "one", "two", 84 );

        MethodHandle filter = MethodHandles.lookup().findStatic( ArgumentsBinderTest.class, "filter", MethodType.methodType( Object.class, Object.class ) );

        binder.filter( 0, filter, filter, filter, filter );

        Object[] args = binder.toArray();

        assertThat( args ).hasSize( 4 );
        assertThat( args[0] ).isEqualTo(1);
        assertThat( args[1] ).isEqualTo("one-filtered");
        assertThat( args[2] ).isEqualTo("two-filtered");
        assertThat( args[3] ).isEqualTo( 84 );
    }

    public static Object filter(Object value) {
        if ( value instanceof Integer && ((Integer) value).intValue() == 42 ) {
            return 1;
        }
        if ( value instanceof String ) {
            return value + "-filtered";
        }
        return value;
    }
}
