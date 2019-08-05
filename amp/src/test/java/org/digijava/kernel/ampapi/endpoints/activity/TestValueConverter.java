package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;

public class TestValueConverter extends ValueConverter {
    
    private static final Map<Class<?>, Map<Long, Object>> TEST_OBJECTS = ImmutableMap.of(
            ObjectImporterTest.PersonAttribute.class, ImmutableMap.of(
                    1L, new ObjectImporterTest.PersonAttribute(1L, "Color", "Yellow"),
                    2L, new ObjectImporterTest.PersonAttribute(2L, "Color", "Red"),
                    10L, new ObjectImporterTest.PersonAttribute(10L, "Height", "Short"),
                    45L, new ObjectImporterTest.PersonAttribute(45L, "Intelligence", "High")
            )
    );
    
    public Object getObjectById(Class<?> entityClass, Object id) {
        if (TEST_OBJECTS.containsKey(entityClass)) {
            Long longId = (Long) id;
            return TEST_OBJECTS.get(entityClass).get(longId);
        }
        
        return super.getObjectById(entityClass, id);
    }
}
