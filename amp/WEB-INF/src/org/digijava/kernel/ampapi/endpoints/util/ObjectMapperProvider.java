package org.digijava.kernel.ampapi.endpoints.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

        Hibernate5Module hibernateModule = new Hibernate5Module();
        hibernateModule.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);

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
