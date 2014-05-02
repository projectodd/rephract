package org.projectodd.rephract.filters;

/**
 * @author Bob McWhirter
 */
public class Filters {

    private static final Filter TOSTRING = new ToStringFilter();

    public static Filter string() {
        return TOSTRING;
    }
}
