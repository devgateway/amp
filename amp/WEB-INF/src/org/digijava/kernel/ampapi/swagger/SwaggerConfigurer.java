package org.digijava.kernel.ampapi.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import org.digijava.kernel.ampapi.swagger.converters.*;
import org.digijava.kernel.ampapi.swagger.types.*;

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

        ModelConverters.getInstance().addConverter(new JsonAnyGetterResolver());

        ModelConverters.getInstance().addConverter(new SwaggerMapWrapperResolver());

        ModelConverters.getInstance().read(FiltersPH.class);
        ModelConverters.getInstance().read(SettingsPH.class);
        ModelConverters.getInstance().read(PublicHeadersPH.class);
        ModelConverters.getInstance().read(PublicTopTotalsPH.class);
        ModelConverters.getInstance().read(PublicTopDataPH.class);

        SwaggerExtensions.getExtensions().add(new SwaggerAuthorization());

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setBasePath("/rest");
        beanConfig.setTitle("AMP REST API Documentation");
        beanConfig.setDescription("[AMP API User Guide](user-guide.html)");
        beanConfig.setResourcePackage("org.digijava.kernel.ampapi.endpoints");
        beanConfig.setScan(true);
    }
}
