package org.dgfoundation.amp.onepager.util;

import org.apache.wicket.model.IModel;
import org.apache.wicket.resource.TextTemplateResourceReference;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JavaScriptTemplateResourceReference extends TextTemplateResourceReference {
    private static final long serialVersionUID = 1L;

    public JavaScriptTemplateResourceReference(Class<?> scope, String fileName,
            IModel<Map<String, Object>> variablesModel) {
        super(scope, fileName, "text/javascript", StandardCharsets.UTF_8.name(), variablesModel);
    }
}
