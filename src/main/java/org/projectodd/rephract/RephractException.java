package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public class RephractException extends Exception {

    public RephractException(String message) {
        super( message );
    }

    public RephractException(Throwable cause) {
        super( cause );
    }

    public RephractException(String message, Throwable cause) {
        super( message, cause );
    }
}
