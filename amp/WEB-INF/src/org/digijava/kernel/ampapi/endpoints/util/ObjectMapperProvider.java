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
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;

/**
 * Customized ObjectMapper provider
 * 
 * @author Nadejda Mandrescu
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    public ObjectMapperProvider() {
        // will not force committing of http servlet response
        mapper.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, false);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Hibernate4Module hibernateModule = new Hibernate4Module();
        hibernateModule.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);

        mapper.registerModule(hibernateModule);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        configureMapperPerCurrentRequest();
        return mapper;
    }
    
    private void configureMapperPerCurrentRequest() {
        Map<String, Set<String>> jsonFiltersDef = EndpointUtils.getAndClearJsonFilters();
        
        // configure request filters
        SimpleFilterProvider sfp = new SimpleFilterProvider();
        if (jsonFiltersDef != null && !jsonFiltersDef.isEmpty()) {
            for (Entry<String, Set<String>> jsonFilterDef : jsonFiltersDef.entrySet()) {
                SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(jsonFilterDef.getValue());
                sfp.addFilter(jsonFilterDef.getKey(), filter);
            }
        }
        // if nothing to filter or invalid filter
        sfp.setFailOnUnknownId(false);
        mapper.setFilterProvider(sfp);
    }
}
