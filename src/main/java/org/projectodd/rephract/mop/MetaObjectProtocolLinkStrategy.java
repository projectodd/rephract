package org.projectodd.rephract.mop;

import org.projectodd.rephract.LinkStrategy;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;

import com.headius.invokebinder.Binder;

public interface MetaObjectProtocolLinkStrategy extends LinkStrategy {
    
    StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException;
    StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException;
    StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException;
    StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException;
    StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException, IllegalAccessException;

}
