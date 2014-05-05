package org.projectodd.rephract;

/**
 * @author Bob McWhirter
 */
public interface ContextualConstructable<V> {

    public V construct(Object context, Object... args) throws Throwable;
}
