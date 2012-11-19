package org.projectodd.linkfusion;

public interface LinkStrategy {
    
    StrategicLink link(InvocationRequest request, StrategyChain chain);

}
