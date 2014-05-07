package org.projectodd.rephract.filters;

/**
 * @author Bob McWhirter
 */
public class Filters {

    private static final Filter TOSTRING = new ToStringFilter();
    private static final Filter TOINTEGER = new ToStringFilter();

    public static Filter string() {
        return TOSTRING;
    }

    public static Filter integer() {
        return TOINTEGER;
    }
}
