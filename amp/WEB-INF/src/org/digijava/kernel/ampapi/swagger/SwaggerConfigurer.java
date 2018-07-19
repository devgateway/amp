package org.digijava.kernel.ampapi.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import org.digijava.kernel.ampapi.swagger.converters.AmpOfflineVersionResolver;
import org.digijava.kernel.ampapi.swagger.converters.GeneratedReportResolver;
import org.digijava.kernel.ampapi.swagger.converters.JsonSerializeUsingResolver;
import org.digijava.kernel.ampapi.swagger.converters.JAXBElementUnwrapper;
import org.digijava.kernel.ampapi.swagger.converters.ReportTypesResolver;
import org.digijava.kernel.ampapi.swagger.types.FiltersPH;
import org.digijava.kernel.ampapi.swagger.types.SettingsPH;

/**
 * @author Octavian Ciubotaru
 */
public class SwaggerConfigurer {

    public void configure() {
        ModelConverters.getInstance().addConverter(new AmpOfflineVersionResolver());

        ModelConverters.getInstance().addConverter(new JAXBElementUnwrapper());

        ModelConverters.getInstance().addConverter(new JsonSerializeUsingResolver());

        ModelConverters.getInstance().addConverter(new GeneratedReportResolver());

        ModelConverters.getInstance().addConverter(new ReportTypesResolver());

        ModelConverters.getInstance().read(FiltersPH.class);

        ModelConverters.getInstance().read(SettingsPH.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setBasePath("/rest");
        beanConfig.setTitle("AMP REST API Documentation");
        beanConfig.setResourcePackage("org.digijava.kernel.ampapi.endpoints");
        beanConfig.setScan(true);
    }
}
