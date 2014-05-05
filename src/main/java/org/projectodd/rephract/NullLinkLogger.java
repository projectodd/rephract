package org.projectodd.rephract;

public class NullLinkLogger implements LinkLogger {

    @Override
    public void log(String message, Object... arguments) {
        // NO-OP
    }

}
