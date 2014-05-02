package org.projectodd.rephract;

import com.headius.invokebinder.Binder;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ArgumentsBinder {
    private Binder invokeBinder;
    private List<Object> arguments = new ArrayList<>();

    public ArgumentsBinder(Object... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
    }

    public Object[] toArray() {
        return this.arguments.toArray( new Object[ this.arguments.size() ] );
    }

    public ArgumentsBinder fold(MethodHandle function) throws Throwable {
        Object result = function.invoke(this.arguments.toArray());
        this.arguments.add(0, result);
        return this;
    }

    public ArgumentsBinder filter(int index, MethodHandle... functions) throws Throwable {
        for ( int i = 0 ; i < functions.length ; ++i ) {
            Object result = functions[i].invoke(this.arguments.get(index + i));
            this.arguments.set( index+i, result );
        }
        return this;
    }

    public ArgumentsBinder drop(int index) {
        this.arguments.remove(index);
        return this;
    }

    public ArgumentsBinder drop(int index, int count) {
        for ( int i = 0 ; i < count ; ++i ) {
            this.arguments.remove( index );
        }
        return this;
    }

    public ArgumentsBinder permute(int... reorder) {
        List<Object> reordered = new ArrayList<>();
        for ( int i = 0 ; i < reorder.length ; ++i ) {
            reordered.add( this.arguments.get( reorder[i] ) );
        }
        this.arguments = reordered;
        return this;
    }

    public ArgumentsBinder collect(int index, Class type) {
        int len = this.arguments.size() - index;
        Object[] array = this.arguments.subList(index, this.arguments.size()).toArray((Object[]) Array.newInstance(type, len));
        this.arguments.subList( index, this.arguments.size() ).clear();
        this.arguments.add( array );
        return this;
    }


    public ArgumentsBinder spread(Class... spreadTypes) {
        Object last = this.arguments.get(this.arguments.size() - 1);
        if ( last.getClass().isArray() ) {
            this.arguments.remove( this.arguments.size() - 1 );
            Object[] lastArray = (Object[]) last;
            for ( int i = 0 ; i < lastArray.length ; ++i ) {
                this.arguments.add( lastArray[i] );
            }
        }
        return this;
    }

    public ArgumentsBinder insert(int index, byte value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, MethodHandle filter) throws Throwable {
        this.arguments.add( index, filter.invokeWithArguments( new Object[] { null } ) );
        return this;
    }

    public ArgumentsBinder insert(int index, Object... values) {
        this.arguments.addAll( index, Arrays.asList( values ) );
        return this;
    }

    public ArgumentsBinder insert(int index, Class[] types, Object... values) {
        this.arguments.addAll( index, Arrays.asList( values ) );
        return this;
    }

    public ArgumentsBinder insert(int index, boolean value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, long value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, float value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, double value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, short value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, char value) {
        this.arguments.add( index, value );
        return this;
    }

    public ArgumentsBinder insert(int index, int value) {
        this.arguments.add( index, value );
        return this;
    }

}
