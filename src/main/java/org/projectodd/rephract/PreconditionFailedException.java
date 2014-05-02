package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public class PreconditionFailedException extends RephractException {

    public PreconditionFailedException() {
        super( "failed to meet precondition" );
    }
}
