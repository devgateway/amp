package org.digijava.kernel.ampapi.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.digijava.kernel.ampapi.swagger.converters.AmpOfflineVersionResolver;
import org.digijava.kernel.ampapi.swagger.converters.JsonSerializeUsingResolver;
import org.digijava.kernel.ampapi.swagger.converters.JAXBElementUnwrapper;

/**
 * @author Octavian Ciubotaru
 */
public class SwaggerConfigurer {

    public void configure() {

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setBasePath("/rest");
        beanConfig.setTitle("AMP REST API Documentation");
        beanConfig.setResourcePackage("org.digijava.kernel.ampapi.endpoints");
        beanConfig.setScan(true);

        ModelConverters.getInstance().addConverter(new AmpOfflineVersionResolver());

        ModelConverters.getInstance().addConverter(new JAXBElementUnwrapper());

        ModelConverters.getInstance().addConverter(new JsonSerializeUsingResolver());

        ModelConverters.getInstance().addClassToSkip(GeneratedReport.class.getName());
    }
}
