package org.projectodd.rephract;

public interface LinkStrategy {
    
    StrategicLink link(InvocationRequest request, StrategyChain chain) throws NoSuchMethodException, IllegalAccessException;

}
