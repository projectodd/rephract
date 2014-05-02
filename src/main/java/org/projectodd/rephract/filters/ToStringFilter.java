package org.projectodd.rephract.filters;

/**
 * @author Bob McWhirter
 */
public class ToStringFilter extends SimpleStatelessFilter {

    public static Object filter(Object arg) {
        return arg.toString();
    }

}
