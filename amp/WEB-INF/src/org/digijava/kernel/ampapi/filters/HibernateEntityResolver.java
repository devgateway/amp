package org.digijava.kernel.ampapi.filters;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
            Session session = PersistenceManager.getSession();

            // Get the CriteriaBuilder
            CriteriaBuilder builder = session.getCriteriaBuilder();

            // Create the CriteriaQuery and specify the result type (AmpCurrency in this case)
            CriteriaQuery<AmpCurrency> query = builder.createQuery(AmpCurrency.class);

            // Specify the root entity (AmpCurrency in this case)
            Root<AmpCurrency> root = query.from(AmpCurrency.class);

            // Add the condition for 'currencyCode' field equals 'idKey'
            query.select(root).where(builder.equal(root.get("currencyCode"), idKey));

            // Execute the query and get the single result or null if not found
            Query<AmpCurrency> typedQuery = session.createQuery(query);
            return typedQuery.uniqueResult();

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