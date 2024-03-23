package org.digijava.kernel.ampapi.endpoints.util;

import com.fasterxml.jackson.jaxrs.cfg.JaxRSFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;

import javax.ws.rs.ext.Provider;

/**
 * {@link JacksonJsonProvider} without cache for endpoint readers and writers.
 * Disabled caching temporarily until conditional filtering for JsonBean is rewritten or removed.
 *
 * See {@link ObjectMapperProvider#configureJsonBeanMapperPerCurrentRequest()} and
 * {@link EndpointUtils#applyJsonFilter(java.lang.String, java.lang.String...)}.
 *
 * Jackson json provider for jax rs is registered automatically through SPI. If caching is to be enabled back then
 * this class has just to be removed.
 *
 * @author Octavian Ciubotaru
 */
@Provider
public class NoCacheJacksonJsonProvider extends JacksonJsonProvider {

    public NoCacheJacksonJsonProvider() {
        disable(JaxRSFeature.CACHE_ENDPOINT_READERS);
        disable(JaxRSFeature.CACHE_ENDPOINT_WRITERS);
    }
}
