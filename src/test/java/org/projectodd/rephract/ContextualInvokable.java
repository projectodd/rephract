package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public interface ContextualInvokable<V> {

    public V invoke(Object context, Object self, Object... args) throws Throwable;

}
