package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public interface Constructable<V> {

    public V construct(Object...args) throws Throwable;
}
