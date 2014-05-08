package org.projectodd.rephract;

import com.headius.invokebinder.Binder;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class MultiBinder {

    private final MethodType invokeInputType;
    private final MethodType guardInputType;
    private Binder invokeBinder;
    private Binder guardBinder;

    public MultiBinder(MethodType type) {
        this.invokeInputType = type;
        this.guardInputType = methodType( boolean.class, type.parameterArray() );
        this.invokeBinder = Binder.from(type);
        this.guardBinder = Binder.from(boolean.class, type.parameterArray());
    }

    public MultiBinder(MultiBinder parent) {
        this.invokeInputType = parent.invokeInputType;
        this.guardInputType = parent.guardInputType;
        this.invokeBinder = parent.invokeBinder;
        this.guardBinder = parent.guardBinder;
    }

    public MethodType invokeInputType() {
        return this.invokeInputType;
    }

    public MethodType guardInputType() {
        return this.guardInputType;
    }

    public Binder invokeBinder() {
        return this.invokeBinder;
    }

    public Binder guardBinder() {
        return this.guardBinder;
    }

    // ----------------------------------------------------------------------
    // delegating methods
    // ----------------------------------------------------------------------


    public MethodType type() {
        return this.invokeBinder.type();
    }

    public MultiBinder printType() {
        printType( System.err );
        return this;
    }

    public MultiBinder printType(PrintStream ps) {
        ps.println("invoke: ");
        invokeBinder.printType(ps);
        ps.println("guard: ");
        guardBinder.printType(ps);
        return this;
    }

    public MultiBinder fold(MethodHandle function) {
        this.invokeBinder = invokeBinder.fold(function);
        this.guardBinder = guardBinder.fold(function);
        return this;
    }

    public MultiBinder filter(int index, MethodHandle... functions) {
        this.invokeBinder = invokeBinder.filter(index, functions);
        this.guardBinder = guardBinder.filter(index, functions);
        return this;
    }

    public MultiBinder drop(int index) {
        this.invokeBinder = invokeBinder.drop(index);
        this.guardBinder = guardBinder.drop(index);
        return this;
    }

    public MultiBinder drop(int index, int count) {
        this.invokeBinder = invokeBinder.drop(index, count);
        this.guardBinder = guardBinder.drop(index, count);
        return this;
    }

    public MultiBinder permute(int... reorder) {
        this.invokeBinder = invokeBinder.permute(reorder);
        this.guardBinder = guardBinder.permute(reorder);
        return this;
    }

    public MultiBinder collect(int index, Class type) {
        this.invokeBinder = invokeBinder.collect(index, type);
        this.guardBinder = guardBinder.collect(index, type);
        return this;
    }

    public MultiBinder convert(Class returnType, Class... argTypes) {
        this.invokeBinder = invokeBinder.convert(returnType, argTypes);
        this.guardBinder = guardBinder.convert(boolean.class, argTypes);
        return this;
    }

    public MultiBinder spread(Class... spreadTypes) {
        this.invokeBinder = invokeBinder.spread(spreadTypes);
        this.guardBinder = guardBinder.spread(spreadTypes);
        return this;
    }

    public MultiBinder varargs(int index, Class type) {
        this.invokeBinder = invokeBinder.varargs(index, type);
        this.guardBinder = guardBinder.varargs(index, type);
        return this;
    }

    public MultiBinder insert(int index, byte value) {
        this.invokeBinder = invokeBinder.insert(index, value);
        this.guardBinder = guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, Object... values) {
        this.invokeBinder = invokeBinder.insert(index, values);
        this.guardBinder = guardBinder.insert(index, values);
        return this;
    }

    public MultiBinder insert(int index, Class[] types, Object... values) {
        this.invokeBinder = invokeBinder.insert(index, types, values);
        this.guardBinder = guardBinder.insert(index, types, values);
        return this;
    }

    public MultiBinder insert(int index, boolean value) {
        this.invokeBinder = invokeBinder.insert(index, value);
        this.guardBinder = guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, long value) {
        this.invokeBinder = invokeBinder.insert(index, value);
        this.guardBinder = guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, float value) {
        this.invokeBinder = invokeBinder.insert(index, value);
        this.guardBinder = guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, double value) {
        this.invokeBinder = invokeBinder.insert(index, value);
        this.guardBinder = guardBinder.insert(index, value);
        return this;
    }


    public MultiBinder insert(int index, short value) {
        invokeBinder.insert(index, value);
        guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, char value) {
        invokeBinder.insert(index, value);
        guardBinder.insert(index, value);
        return this;
    }

    public MultiBinder insert(int index, int value) {
        invokeBinder.insert(index, value);
        guardBinder.insert(index, value);
        return this;
    }

}
