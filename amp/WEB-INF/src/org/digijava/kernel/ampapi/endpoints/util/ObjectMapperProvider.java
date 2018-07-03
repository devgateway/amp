package org.digijava.kernel.ampapi.endpoints.util;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;

/**
 * Customized ObjectMapper provider
 * 
 * @author Nadejda Mandrescu
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper defaultMapper = new ObjectMapper();
    private ObjectMapper jsonBeanMapper = new ObjectMapper();

    public ObjectMapperProvider() {
        configure(defaultMapper);
        configure(jsonBeanMapper);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        if (type.equals(JsonBean.class)) {
            configureJsonBeanMapperPerCurrentRequest();
            return jsonBeanMapper;
        }
        return defaultMapper;
    }
    
    private void configureJsonBeanMapperPerCurrentRequest() {
        Map<String, Set<String>> jsonFiltersDef = EndpointUtils.getAndClearJsonFilters();
        
        // configure Json Filters
        SimpleFilterProvider sfp = new SimpleFilterProvider();
        if (jsonFiltersDef != null && !jsonFiltersDef.isEmpty()) {
            for (Entry<String, Set<String>> jsonFilterDef : jsonFiltersDef.entrySet()) {
                SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(jsonFilterDef.getValue());
                sfp.addFilter(jsonFilterDef.getKey(), filter);
            }
        }
        // if nothing to filter or invalid filter
        sfp.setFailOnUnknownId(false);
        jsonBeanMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonBeanMapper.setFilterProvider(sfp);
    }

    /**
     * Defaults for all Object Mappers.
     */
    private void configure(ObjectMapper objectMapper) {
        // will not force committing of http servlet response
        objectMapper.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, false);
    }
}
