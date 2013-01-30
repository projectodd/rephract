package org.projectodd.rephract;

import java.util.List;

class StrategyChainImpl implements StrategyChain {

    private InvocationRequest request;
    private List<LinkStrategy> strategies;
    private LinkLogger logger;

    StrategyChainImpl(LinkLogger logger, InvocationRequest request, List<LinkStrategy> strategies) {
        this.logger = logger;
        this.request = request;
        this.strategies = strategies;
    }

    @Override
    public InvocationRequest getRequest() {
        return this.request;
    }

    @Override
    public StrategicLink nextStrategy(InvocationRequest request) throws NoSuchMethodException, IllegalAccessException {
        if (strategies.size() == 1) {
            return null;
        }

        StrategyChainImpl chain = new StrategyChainImpl(this.logger, request, this.strategies.subList(1, this.strategies.size()));

        return chain.linkCurrent();
    }

    public StrategicLink nextStrategy() throws NoSuchMethodException, IllegalAccessException {
        return nextStrategy(this.request);
    }

    public StrategicLink linkCurrent() throws NoSuchMethodException, IllegalAccessException {
        return strategies.get(0).link(this.request, this);
    }

}
