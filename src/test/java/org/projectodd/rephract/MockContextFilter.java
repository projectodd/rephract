package org.projectodd.rephract;

import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.filters.SimpleStatelessFilter;

/**
* @author Bob McWhirter
*/
public class MockContextFilter extends SimpleStatelessFilter {

    public static final Filter INSTANCE = new MockContextFilter();

    public static Object filter(Object arg) {
        if ( arg instanceof MockContext ) {
            return arg;
        }
        return new MockContext();
    }
}
