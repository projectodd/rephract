package org.projectodd.rephract.java.array;

import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ArrayPropertySetLink extends SmartLink implements Guard {

    public ArrayPropertySetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName, Object value) {
        return receiver.getClass().isArray();
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ArrayPropertySetLink.class, "guard", methodType(boolean.class, Object.class, String.class, Object.class ))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .filter(1, lookup().findStatic(Integer.class, "parseInt", methodType(int.class, String.class)))
                .invoke(lookup().findStatic(Array.class, "set", methodType(void.class, Object.class, int.class, Object.class))).target();
    }
}
