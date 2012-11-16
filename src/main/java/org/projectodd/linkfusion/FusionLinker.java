package org.projectodd.linkfusion;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import com.headius.invokebinder.Binder;

public class FusionLinker {

    public FusionLinker() {

    }

    public MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        return Binder.from(CallSite.class, Lookup.class, String.class, MethodType.class)
                .insert(0, this)
                .invokeVirtual(lookup, "bootstrap");
    }

    public CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        System.err.println( "bootstrapping: " + name + ": " + type );
        MutableCallSite callSite = new MutableCallSite(type);
        
        RequestImpl request = new RequestImpl(callSite, lookup, name, type);
        
        MethodHandle relink = Binder.from( type )
                .printType()
                .varargs(0, Object[].class)
                .printType()
                .convert( Object.class, Object[].class )
                .insert(0, this)
                .printType()
                .insert(1, request )
                .printType()
                .invokeVirtual(MethodHandles.lookup(), "relink" );
        callSite.setTarget( relink );
        return callSite;
    }

    public CallSite bootstrap(String name, MethodType type) throws Throwable {
        return bootstrap(MethodHandles.lookup(), name, type);
    }

    public Object relink(RequestImpl request, Object[] args) {
        System.err.println( "relink: " + request );
        System.err.println( "args: " + Arrays.asList(args));
        
        try {
            MethodHandle target = Binder.from( request.type() )
                    .printType()
                    .drop(0, request.type().parameterCount() )
                    .printType()
                    .insert(0, this )
                    .printType()
                    .invokeVirtual(request.lookup(), "returnSomething" );
            
            System.err.println( "setting target: " + target );
            request.setTarget( target );
            return target.invokeWithArguments(args);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Object returnSomething() {
        System.err.println( "RETURN SOMETHING!" );
        return System.currentTimeMillis();
    }

}
