package org.projectodd.rephract.builder;


import com.headius.invokebinder.Binder;
import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.guards.Guard;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class GuardBuilder {

    private LinkBuilder parent;
    private Binder binder;

    protected GuardBuilder(LinkBuilder parent, Binder binder) {
        this.parent = parent;
        this.binder = binder;
    }

    public LinkBuilder with(Guard guard) throws Exception {
        MethodHandle methodHandle = this.binder.invoke(guard.methodHandle(this.binder.type()));
        return this.parent.guardWith(methodHandle);
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------


    public GuardBuilder insert(int index, boolean value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder filter(int index, MethodHandle... functions) {
        binder = binder.filter(index, functions);
        return this;
    }

    public GuardBuilder filter(int index, Filter filter) throws Exception {
        MethodHandle methodHandle = filter.methodHandle( methodType(binder.type().parameterType(index), binder.type().parameterType(index)) );
        binder = binder.filter(index, methodHandle );
        return this;
    }

    public GuardBuilder drop(int index) {
        binder = binder.drop(index);
        return this;
    }

    public GuardBuilder printType() {
        binder = binder.printType();
        return this;
    }

    public GuardBuilder varargs(int index, Class type) {
        binder = binder.varargs(index, type);
        return this;
    }

    public GuardBuilder insert(int index, long value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder collect(int index, Class type) {
        binder = binder.collect(index, type);
        return this;
    }

    public GuardBuilder insert(int index, Object... values) {
        binder = binder.insert(index, values);
        return this;
    }

    public GuardBuilder convert(Class returnType, Class... argTypes) {
        binder = binder.convert(returnType, argTypes);
        return this;
    }

    public GuardBuilder drop(int index, int count) {
        binder = binder.drop(index, count);
        return this;
    }

    public GuardBuilder insert(int index, double value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder insert(int index, byte value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder insert(int index, int value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder insert(int index, Class[] types, Object... values) {
        binder = binder.insert(index, types, values);
        return this;
    }

    public GuardBuilder printType(PrintStream ps) {
        binder = binder.printType(ps);
        return this;
    }

    public GuardBuilder insert(int index, float value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder insert(int index, short value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder fold(MethodHandle function) {
        binder = binder.fold(function);
        return this;
    }

    public GuardBuilder spread(Class... spreadTypes) {
        binder = binder.spread(spreadTypes);
        return this;
    }

    public GuardBuilder insert(int index, char value) {
        binder = binder.insert(index, value);
        return this;
    }

    public GuardBuilder permute(int... reorder) {
        binder = binder.permute(reorder);
        return this;
    }
}
