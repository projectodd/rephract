package org.projectodd.rephract.mop;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import org.projectodd.rephract.Operation;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.NonContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class MockFrontLinkStrategy extends NonContextualLinkStrategy {
    
    public static class WrappedObject {
        private Object object;

        public WrappedObject(Object object) {
            this.object = object;
        }
        
        public Object getObject() {
            return this.object;
        }
    }
    
    @Override
    protected StrategicLink linkCall(StrategyChain chain, Operation op) throws NoSuchMethodException, IllegalAccessException {
        StrategicLink link = super.linkCall(chain, op);
        
        if ( link != null ) {
            MethodHandle returnFilter = lookup().findStatic(MockFrontLinkStrategy.class, "filterReturn", MethodType.methodType(Object.class, Object.class));
            
            MethodHandle filtered = Binder.from( link.getTarget().type() )
                    .filterReturn( returnFilter )
                    .invoke( link.getTarget() );
            
            return new StrategicLink( filtered, link.getGuard() );
        }
        
        return null;
    }
    
    public static Object filterReturn(Object returnValue) {
        return new WrappedObject( returnValue );
    }
    
    

}
