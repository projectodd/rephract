package org.projectodd.linkfusion;

public interface StrategyChain {
    
    InvocationRequest getRequest();
    StrategicLink nextStrategy();
    StrategicLink nextStrategy(InvocationRequest request);

}
