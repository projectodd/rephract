package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public interface Invokable<V> {

    public V invoke(Object self, Object...args) throws Throwable;

}
