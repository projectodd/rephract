package org.projectodd.rephract.builder;


import org.projectodd.rephract.Link;
import org.projectodd.rephract.MultiBinder;
import org.projectodd.rephract.SimpleLink;
import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.guards.Guards;
import org.projectodd.rephract.invokers.Invoker;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class LinkBuilder {

    private final MultiBinder binder;

    public LinkBuilder(MethodType inputType) {
        this.binder = new MultiBinder(inputType);
    }

    protected LinkBuilder(MultiBinder binder) {
        this.binder = binder;
    }

    protected MultiBinder binder() {
        return this.binder;
    }

    public MethodHandle getGuard() throws Exception {
        return Guards.TRUE.guardMethodHandle(this.binder.guardInputType());
    }

    public LinkBuilder guardWith(Guard guard) throws Exception {
        MethodHandle methodHandle = this.binder.guardBinder().invoke(guard.guardMethodHandle(this.binder.guardBinder().type()));
        return guardWith(methodHandle);
    }

    LinkBuilder guardWith(MethodHandle methodHandle) throws Exception {
        return new ChildLinkBuilder(new MultiBinder(this.binder), methodHandle);
    }

    public GuardBuilder guard() {
        return new GuardBuilder(this, this.binder.guardBinder());
    }

    public GuardBuilder guard(int... reorder) {
        return new GuardBuilder(this, this.binder.guardBinder().permute(reorder));
    }

    public Link invoke(Invoker invoker) throws Exception {
        return new SimpleLink(null, this.binder.invokeBinder().invoke(invoker.invokerMethodHandle()));
    }

    public Link invoke(MethodHandle invoker) throws Exception {
        return new SimpleLink(null, this.binder.invokeBinder().invoke(invoker));
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------


    public LinkBuilder insert(int index, boolean value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder filter(int index, MethodHandle... functions) {
        binder.filter(index, functions);
        return this;
    }

    public LinkBuilder filter(int index, Filter filter) throws Exception {
        binder.filter(index, filter.methodHandle(methodType(Object.class, Object.class)));
        return this;
    }

    public LinkBuilder drop(int index) {
        binder.drop(index);
        return this;
    }

    public LinkBuilder printType() {
        binder.printType(System.err);
        return this;
    }

    public LinkBuilder varargs(int index, Class type) {
        binder.varargs(index, type);
        return this;
    }

    public LinkBuilder insert(int index, long value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder collect(int index, Class type) {
        binder.collect(index, type);
        return this;
    }

    public LinkBuilder insert(int index, Object... values) {
        binder.insert(index, values);
        return this;
    }

    public LinkBuilder insert(int index, Filter filter) throws Exception {
        binder.insert(index, new Class[]{Object.class}, new Object[]{null});
        binder.filter(index, filter.methodHandle(methodType(Object.class, Object.class)));
        return this;
    }

    public LinkBuilder convert(Class returnType, Class... argTypes) {
        binder.convert(returnType, argTypes);
        return this;
    }


    public LinkBuilder drop(int index, int count) {
        binder.drop(index, count);
        return this;
    }

    public LinkBuilder insert(int index, double value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder insert(int index, byte value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder insert(int index, int value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder insert(int index, Class[] types, Object... values) {
        binder.insert(index, types, values);
        return this;
    }

    public LinkBuilder printType(PrintStream ps) {
        binder.printType(ps);
        return this;
    }

    public LinkBuilder insert(int index, float value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder insert(int index, short value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder fold(MethodHandle function) {
        binder.fold(function);
        return this;
    }

    public LinkBuilder spread(Class... spreadTypes) {
        binder.spread(spreadTypes);
        return this;
    }

    public LinkBuilder insert(int index, char value) {
        binder.insert(index, value);
        return this;
    }

    public LinkBuilder permute(int... reorder) {
        binder.permute(reorder);
        return this;
    }
}
