package org.projectodd.linkfusion;

import java.util.List;

class StrategyChainImpl implements StrategyChain {

    private InvocationRequest request;
    private List<LinkStrategy> strategies;

    StrategyChainImpl(InvocationRequest request, List<LinkStrategy> strategies) {
        this.request = request;
        this.strategies = strategies;
    }

    @Override
    public InvocationRequest getRequest() {
        return this.request;
    }

    @Override
    public StrategicLink nextStrategy(InvocationRequest request) {

        if (strategies.size() == 1) {
            return null;
        }

        StrategyChainImpl chain = new StrategyChainImpl(request, this.strategies.subList(1, this.strategies.size()));

        return chain.linkCurrent();
    }

    public StrategicLink nextStrategy() {
        return nextStrategy(this.request);
    }

    public StrategicLink linkCurrent() {
        System.err.println("link with: " + strategies.get(0));
        return strategies.get(0).link(this.request, this);
    }

}
