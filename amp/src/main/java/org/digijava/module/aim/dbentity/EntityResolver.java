package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>Delegates resolving of objects to an external resolver. Delegation can only be realized by calling
 * {@link EntityResolver#doWithResolver} and is bound to current thread. Children threads will inherit the resolver
 * from parent thread.
 *
 * <p>This resolver maintains a local cache. Resolving same {@link ObjectIdGenerator.IdKey key} multiple times will
 * result in only one delegated call and subsequent calls will return the cached object.
 *
 * @author Octavian Ciubotaru
 */
public final class EntityResolver implements ObjectIdResolver {

    private static final ThreadLocal<Function<ObjectIdGenerator.IdKey, Object>> TL_RESOLVER =
            new InheritableThreadLocal<>();

    private Map<ObjectIdGenerator.IdKey, Object> items = new HashMap<ObjectIdGenerator.IdKey, Object>();

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        if (items.containsKey(id)) {
            throw new IllegalStateException(
                    "Already had POJO for id (" + id.key.getClass().getName() + ") [" + id + "]");
        }
        items.put(id, pojo);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        return items.computeIfAbsent(id, this::loadFromContext);
    }

    private Object loadFromContext(ObjectIdGenerator.IdKey id) {
        Function<ObjectIdGenerator.IdKey, Object> resolver = TL_RESOLVER.get();
        if (resolver == null) {
            throw new IllegalStateException("No contextual resolver was provided");
        }
        return resolver.apply(id);
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new EntityResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == getClass();
    }

    public static void doWithResolver(Function<ObjectIdGenerator.IdKey, Object> resolver, Runnable runnable) {
        Function<ObjectIdGenerator.IdKey, Object> oldResolver = TL_RESOLVER.get();
        try {
            TL_RESOLVER.set(resolver);
            runnable.run();
        } finally {
            TL_RESOLVER.set(oldResolver);
        }
    }
}
