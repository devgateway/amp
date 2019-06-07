package org.digijava.kernel.ampapi.swagger;

import org.digijava.kernel.ampapi.swagger.converters.AmpOfflineVersionResolver;
import org.digijava.kernel.ampapi.swagger.converters.GeneratedReportResolver;
import org.digijava.kernel.ampapi.swagger.converters.JAXBElementUnwrapper;
import org.digijava.kernel.ampapi.swagger.converters.JsonAnyGetterResolver;
import org.digijava.kernel.ampapi.swagger.converters.JsonSerializeUsingResolver;
import org.digijava.kernel.ampapi.swagger.converters.ReportTypesResolver;
import org.digijava.kernel.ampapi.swagger.converters.SwaggerMapWrapperResolver;
import org.digijava.kernel.ampapi.swagger.types.FiltersPH;
import org.digijava.kernel.ampapi.swagger.types.PublicHeadersPH;
import org.digijava.kernel.ampapi.swagger.types.PublicTopDataPH;
import org.digijava.kernel.ampapi.swagger.types.PublicTopTotalsPH;
import org.digijava.kernel.ampapi.swagger.types.SettingsPH;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.ext.SwaggerExtensions;

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
