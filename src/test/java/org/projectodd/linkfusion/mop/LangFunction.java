package org.projectodd.linkfusion.mop;

import java.util.Arrays;

public class LangFunction {

    public Object call(LangContext context, Object self, Object... args) {
        if (context == null) {
            throw new IllegalArgumentException("context must be supplied");
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < args.length; ++i) {
            buf.append(args[i].toString());
            if (i < args.length - 1) {
                buf.append(",");
            }
        }
        
        return buf.toString();
    }

}
