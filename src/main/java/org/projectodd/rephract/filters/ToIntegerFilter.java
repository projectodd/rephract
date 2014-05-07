package org.projectodd.rephract.filters;

/**
 * @author Bob McWhirter
 */
public class ToIntegerFilter extends SimpleStatelessFilter {

    public static int filter(Object arg) {
        //if ( arg instanceof Number ) {
            //return ((Number) arg).intValue();
        //}
        return Integer.parseInt(arg.toString());
    }

}
