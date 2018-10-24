package org.dgfoundation.amp.metamodel.type;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.digijava.module.aim.dbentity.AuditedEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object in domain model.
 * <p>Has a list of mutable attributes.</p>
 * <p>Objects inside collections are matched by using {@link AuditedEntity#getId()}
 * and {@link AuditedEntity#getOriginalObjectId()}.</p>
 *
 * TODO in diff, for providing context, we display all attributes of ValueType, however is somewhat verbose
 *
 * @author Octavian Ciubotaru
 */
public final class ObjectType implements Type, Iterable<Attribute> {

    private Map<String, Attribute> attributes;

    public ObjectType(List<Attribute> attributes) {
        this.attributes = attributes.stream()
                .collect(Collectors.toMap(Attribute::getName, a -> a, throwingMerger(), LinkedHashMap::new));
    }

    /**
     * TODO copied from {@link Collectors}, move to some util class
     */
    private static <T> BinaryOperator<T> throwingMerger() {
        return (var0, var1) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", var0));
        };
    }

    /**
     * Return attribute by its name.
     * @param name name of the attribute
     * @return the attribute or null
     */
    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    @NotNull
    @Override
    public Iterator<Attribute> iterator() {
        return attributes.values().iterator();
    }
}
