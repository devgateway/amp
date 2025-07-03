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
        beanConfig.setDescription(
                "# AMP REST API Documentation\n\n" +
                        "[AMP API User Guide](user-guide.html)\n\n" +
                        "## Authentication and Authorization\n\n" +
                        "### How to Authenticate\n\n" +
                        "To use the API from Swagger UI(hti browser you can rely on the authentication that the browser has established from the [AMP Login Page](/showLayout.do/?layout=login).\n" +
                        "\n\n"+
                        "Alternatively, to use the AMP API in another API client like **postman**, you need to authenticate using the `/security/user/` endpoint:\n\n" +
                        "- **Method**: POST\n" +
                        "- **Required parameters**:\n" +
                        "  - `username`: Your login name\n" +
                        "  - `password`: SHA-1 hash of your password (not plain text)\n" +
                        "  - `workspaceId`: ID of the workspace to activate after login\n\n" +
                        "Upon successful authentication, you'll receive a session token that will be used for subsequent API calls.\n\n" +
                        "### Authorization Rules\n\n" +
                        "Different endpoints require different authorization rules:\n\n" +
                        "- `AUTHENTICATED`: Requires an authenticated session\n" +
                        "- `IN_WORKSPACE`: Requires a selected workspace\n" +
                        "- `IN_ADMIN`: Requires admin login\n" +
                        "- `ADD_ACTIVITY`: Requires add activity permission\n" +
                        "- `EDIT_ACTIVITY`: Requires edit activity permission\n" +
                        "- `VIEW_ACTIVITY`: Requires view activity permission\n\n" +
                        "Each endpoint's documentation specifies which authorization rules apply to it."
        );
        beanConfig.setResourcePackage("org.digijava.kernel.ampapi.endpoints");
        beanConfig.setScan(true);
    }
}
