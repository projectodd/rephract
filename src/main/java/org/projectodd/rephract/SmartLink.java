package org.projectodd.rephract;

import org.projectodd.rephract.builder.LinkBuilder;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public abstract class SmartLink extends Link {

    protected LinkBuilder builder;

    public SmartLink(LinkBuilder builder) {
        this.builder = builder;
    }
}
