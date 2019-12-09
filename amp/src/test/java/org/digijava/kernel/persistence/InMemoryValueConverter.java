package org.digijava.kernel.persistence;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporterTest;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Non-persistent implementation of {@code ValueConverter} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryValueConverter extends ValueConverter {
    
    public static final Map<Long, Object> PERSONE_ATTRIBUTE_TEST_OBJECTS = ImmutableMap.of(
            10L, new ObjectImporterTest.PersonAttribute(10L, "Height", "Small")
    );
    
    public static final Map<Class<?>, Function<Long, Object>> DAO_PROVIDER = ImmutableMap.of(
            ObjectImporterTest.PersonAttribute.class, (id) -> PERSONE_ATTRIBUTE_TEST_OBJECTS.get(id),
            AmpCategoryValue.class, (id) -> InMemoryCategoryValuesManager.getInstance().get(id),
            AmpTeamMember.class, (id) -> InMemoryTeamMemberManager.getInstance().get(id),
            AmpLocation.class, (id) -> InMemoryLocationManager.getInstance().get(id),
            AmpOrganisation.class, (id) -> InMemoryOrganisationManager.getInstance().get(id)
    );
    
    public Object getObjectById(Class<?> entityClass, Object id) {
        if (DAO_PROVIDER.containsKey(entityClass)) {
            return DAO_PROVIDER.get(entityClass).apply(Long.valueOf(id.toString()));
        }
        
        return super.getObjectById(entityClass, id);
    }
}
