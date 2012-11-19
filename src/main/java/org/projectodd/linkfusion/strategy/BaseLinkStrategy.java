package org.projectodd.linkfusion.strategy;

import java.util.List;

import org.projectodd.linkfusion.InvocationRequest;
import org.projectodd.linkfusion.LinkStrategy;
import org.projectodd.linkfusion.Operation;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;

public class BaseLinkStrategy implements LinkStrategy {

    @Override
    public StrategicLink link(InvocationRequest request, StrategyChain chain) {

        if (request.isFusionRequest()) {
            List<Operation> ops = request.getOperations();

            StrategicLink link = null;

            for (Operation each : ops) {
                switch (each.getType()) {
                case GET_PROPERTY:
                    link = linkGetProperty(chain, each);
                    break;
                case SET_PROPERTY:
                    link = linkSetProperty(chain, each);
                    break;
                }

                if (link != null) {
                    return link;
                }
            }
        }

        return null;
    }

    protected StrategicLink linkGetProperty(StrategyChain chain, Operation op) {
        Object receiver = chain.getRequest().receiver();
        String propName = op.getParameter();
        if (propName == null) {
            propName = chain.getRequest().arguments()[1].toString();
        }
        return linkGetProperty(chain, receiver, propName);
    }

    protected StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName) {
        return chain.nextStrategy();
    }

    protected StrategicLink linkSetProperty(StrategyChain chain, Operation op) {
        Object receiver = chain.getRequest().receiver();
        String propName = op.getParameter();
        Object value = null;
        if (propName == null) {
            propName = chain.getRequest().arguments()[1].toString();
            value = chain.getRequest().arguments()[2];
        } else {
            value = chain.getRequest().arguments()[1];
        }
        return linkSetProperty(chain, receiver, propName, value);
    }

    protected StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String name, Object value) {
        return chain.nextStrategy();
    }

}
