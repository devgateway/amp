package org.digijava.kernel.ampapi.swagger;

import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.Operation;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Nadejda Mandrescu
 *
 */
public class SwaggerAuthorization extends AbstractSwaggerExtension {
    private static final Set<AuthRule> IGNORE_RULES = new TreeSet<>(Arrays.asList(
            AuthRule.AMP_OFFLINE,
            AuthRule.AMP_OFFLINE_OPTIONAL,
            AuthRule.PUBLIC_VIEW_ACTIVITY));

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<SwaggerExtension> chain) {
        ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
        if (apiMethod != null && apiMethod.authTypes() != null) {
            for (AuthRule authRule : apiMethod.authTypes()) {
                if (!IGNORE_RULES.contains(authRule)) {
                    operation.addSecurity(authRule.name(), null);
                }
            }
        }
        super.decorateOperation(operation, method, chain);
    }
}
