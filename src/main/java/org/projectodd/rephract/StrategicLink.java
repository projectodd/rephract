package org.projectodd.rephract;

import com.headius.invokebinder.Binder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

public class StrategicLink {
    private static boolean debug = ( System.getProperty( "rephract.debug" ) != null );

    private MethodHandle target;
    private MethodHandle guard;

    private String className;
    private String methodName;
    private int lineNumber;

    public StrategicLink(MethodHandle target, MethodHandle guard) {
        this.target = target;
        this.guard = guard;
        if (debug) {
            StackTraceElement head = Thread.currentThread().getStackTrace()[2];
            this.className = head.getClassName();
            this.methodName = head.getMethodName();
            this.lineNumber = head.getLineNumber();
        }
    }

    public MethodHandle getTarget() {
        if (debug) {
            try {
                MethodHandle debugMh = MethodHandles.lookup().findStatic(StrategicLink.class, "debug", MethodType.methodType(Object[].class, StrategicLink.class, String.class, Object[].class));

                debugMh = Binder.from(Object[].class, Object[].class)
                        .insert(0, this)
                        .insert(1, "target")
                        .invoke(debugMh);

                Class<?>[] spreadTypes = this.target.type().parameterArray();

                return Binder.from(this.target.type())
                        .convert(this.target.type().erase())
                        .collect(0, Object[].class)
                        .filter(0, debugMh)
                        .spread(spreadTypes)
                        .invoke(this.target);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return this.target;
        } else {
            return this.target;
        }
    }

    public MethodHandle getGuard() {
        if (debug) {
            try {
                MethodHandle debugMh = MethodHandles.lookup().findStatic(StrategicLink.class, "debug", MethodType.methodType(Object[].class, StrategicLink.class, String.class, Object[].class));

                debugMh = Binder.from(Object[].class, Object[].class)
                        .insert(0, this)
                        .insert(1, "guard")
                        .invoke(debugMh);

                Class<?>[] spreadTypes = this.guard.type().parameterArray();

                return Binder.from(this.guard.type())
                        .convert(this.guard.type().erase())
                        .collect(0, Object[].class)
                        .filter(0, debugMh)
                        .spread(spreadTypes)
                        .invoke(this.guard);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return this.guard;
        } else {
            return this.guard;
        }
    }

    public static Object[] debug(StrategicLink link, String debugType, Object[] params) {
        StringBuffer buf = new StringBuffer();
        buf.append(link.className).append(".").append(link.methodName).append(":").append(link.lineNumber).append(": ");
        if (debugType.equals("guard")) {
            buf.append(link.guard.toString());
        } else {
            buf.append(link.target.toString());
        }
        buf.append("\n  called with:\n");

        for (int i = 0; i < params.length; ++i) {
            buf.append( "    " + i + ": " );
            if (params[i].getClass().isArray()) {
                buf.append(Arrays.asList((Object[]) params[i]));
            } else {
                buf.append(params[i]);
            }
            buf.append( "\n" );
        }

        System.err.println("DEBUG-" + debugType + ": " + buf);

        return params;
    }

    public String toString() {
        return "[StrategicLink: target=" + target + "; guard=" + guard + "]";
    }

}
