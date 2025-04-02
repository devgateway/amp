package org.digijava.kernel.ampapi.filters;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.metadata.ClassMetadata;

/**
 * @author Octavian Ciubotaru
 */
public class HibernateEntityResolver implements Function<ObjectIdGenerator.IdKey, Object> {

    private final Map<Class, ClassMetadata> entityClasses;

    public HibernateEntityResolver(Session session) {
        entityClasses = session.getSessionFactory().getAllClassMetadata().values().stream()
                .collect(Collectors.toMap(m -> m.getMappedClass(), m -> m));
    }

    @Override
    public Object apply(ObjectIdGenerator.IdKey idKey) {
        if (canResolveEntity(idKey)) {
            return PersistenceManager.getSession().load(idKey.scope, (Serializable) idKey.key);
        } else if (idKey.scope.equals(AmpCurrency.class) && idKey.key instanceof String) {
            return PersistenceManager.getSession().createCriteria(AmpCurrency.class)
                    .add(Property.forName("currencyCode").eq(idKey.key))
                    .uniqueResult();
        }
        return null;
    }

    private boolean canResolveEntity(ObjectIdGenerator.IdKey idKey) {
        ClassMetadata classMetadata = entityClasses.get(idKey.scope);
        return classMetadata != null && keyMatchesEntityIdentifier(idKey, classMetadata);
    }

    private boolean keyMatchesEntityIdentifier(ObjectIdGenerator.IdKey idKey, ClassMetadata classMetadata) {
        return classMetadata.getIdentifierType().getReturnedClass().isAssignableFrom(idKey.key.getClass());
    }
}
