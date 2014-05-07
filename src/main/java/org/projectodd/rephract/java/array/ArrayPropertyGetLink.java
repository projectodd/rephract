package org.projectodd.rephract.java.array;

import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.filters.Filters;
import org.projectodd.rephract.guards.Guard;
import org.projectodd.rephract.java.AbstractResolvingLink;
import org.projectodd.rephract.java.reflect.Resolver;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ArrayPropertyGetLink extends SmartLink implements Guard {

    private Boolean isLength;

    public ArrayPropertyGetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, String propertyName) {

        if (!receiver.getClass().isArray()) {
            return false;
        }

        if (this.isLength != null) {
            if (propertyName.equals("length") != isLength) {
                return false;
            }
        }

        this.isLength = propertyName.equals("length");

        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ArrayPropertyGetLink.class, "guard", methodType(boolean.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        if (this.isLength) {
            return this.builder
                    .drop(1)
                    .invoke(lookup().findStatic(Array.class, "getLength", methodType(int.class, Object.class))).target();
        }

        return this.builder
                .filter(1, lookup().findStatic(Integer.class, "parseInt", methodType(int.class, String.class)))
                .invoke(lookup().findStatic(Array.class, "get", methodType(Object.class, Object.class, int.class))).target();
    }
}
