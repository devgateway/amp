package org.digijava.kernel.ampapi.swagger;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.Operation;

/**
 * @author Nadejda Mandrescu
 *
 */
public class SwaggerAuthorization extends AbstractSwaggerExtension {

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<SwaggerExtension> chain) {
        ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
        if (apiMethod != null && apiMethod.authTypes() != null) {
            for (AuthRule authRule : apiMethod.authTypes()) {
                operation.addSecurity(authRule.name(), null);
            }
        }
        super.decorateOperation(operation, method, chain);
    }
}
