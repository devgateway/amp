package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;

public class TestValueConverter extends ValueConverter {
    
    public static final Map<Class<?>, Map<Object, Object>> TEST_OBJECTS = ImmutableMap.of(
            ObjectImporterTest.PersonAttribute.class, ImmutableMap.of(
                    "10", new ObjectImporterTest.PersonAttribute("10", "Height", "Small")
            )
    );
    
    public Object getObjectById(Class<?> entityClass, Object id) {
        if (TEST_OBJECTS.containsKey(entityClass)) {
            return TEST_OBJECTS.get(entityClass).get(id);
        }
        
        return super.getObjectById(entityClass, id);
    }
}
