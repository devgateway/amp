package org.digijava.kernel.ampapi.filters;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.metadata.ClassMetadata;

import javax.persistence.metamodel.EntityType;

/**
 * @author Octavian Ciubotaru
 */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.EntityPersister;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HibernateEntityResolver implements Function<ObjectIdGenerator.IdKey, Object> {

    private final Map<Class<?>, EntityPersister> entityPersisters;

    public HibernateEntityResolver(Session session) {
        entityPersisters = new HashMap<>();
        SessionFactory sessionFactory = session.getSessionFactory();
//        entityPersisters = sessionFactory.getMetamodel().getEntities().stream()
//                .collect(Collectors.toMap(EntityType::getJavaType, session.getE));
        sessionFactory.getMetamodel().getEntities().forEach(entityType -> {
            MetamodelImplementor metamodel = (MetamodelImplementor) sessionFactory.getMetamodel();

            EntityPersister entityPersister = metamodel.entityPersister(entityType.getJavaType());
            entityPersisters.put(entityType.getJavaType(), entityPersister);

        });
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
        EntityPersister entityPersister = entityPersisters.get(idKey.scope);
        return entityPersister != null && keyMatchesEntityIdentifier(idKey, entityPersister);
    }

    private boolean keyMatchesEntityIdentifier(ObjectIdGenerator.IdKey idKey, EntityPersister entityPersister) {
        return entityPersister.getIdentifierType().getReturnedClass().isAssignableFrom(idKey.key.getClass());
    }
}