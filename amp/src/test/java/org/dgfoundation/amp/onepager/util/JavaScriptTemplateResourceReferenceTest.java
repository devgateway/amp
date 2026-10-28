package org.dgfoundation.amp.onepager.util;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.resource.IResource;
import org.dgfoundation.amp.onepager.components.upload.FileUploadBehavior;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.onepager.yui.contacts.AmpContactAutocompleteFieldPanel;
import org.digijava.kernel.web.SecurityHeadersFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("activity-form-resources")
public class JavaScriptTemplateResourceReferenceTest {
    private Application previousApplication;
    private WebApplication application;

    @BeforeEach
    public void setUpApplication() {
        previousApplication = ThreadContext.getApplication();
        application = new WebApplication() {
            @Override
            public Class<? extends Page> getHomePage() {
                return WebPage.class;
            }
        };
        application.setName("resource-test-" + UUID.randomUUID());
        application.setConfigurationType(RuntimeConfigurationType.DEPLOYMENT);
        application.setServletContext(new MockServletContext(application, null));
        ThreadContext.setApplication(application);
        application.initApplication();
    }

    @AfterEach
    public void destroyApplication() {
        try {
            application.internalDestroy();
        } finally {
            ThreadContext.setApplication(previousApplication);
        }
    }

    @Test
    public void autocompleteTemplatesAreServedAsJavaScript() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("noResults", "No matching results");

        assertScript(AmpAutocompleteFieldPanel.class, "AmpAutocompleteFieldPanel.js", variables,
                "YAHOO.widget.WicketAutoComplete", "No matching results");
        assertScript(AmpContactAutocompleteFieldPanel.class, "AmpContactAutocompleteFieldPanel.js", variables,
                "YAHOO.widget.WicketContactAutoComplete", "No matching results");
    }

    @Test
    public void uploadTemplateIsServedAsJavaScript() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("componentMarkupId", "upload");
        variables.put("url", "/upload");
        variables.put("paramName", "FILE-UPLOAD");
        variables.put("uploadFailedMsg", "Upload failed");
        variables.put("uploadStartedMsg", "Upload started");
        variables.put("uploadFailedTooBigMsg", "File too large");
        variables.put("uploadMaxFileSize", "1048576");
        variables.put("uploadNoFileLabel", "No file chosen \u00e9");

        assertScript(FileUploadBehavior.class, "FileUploadBehavior.js", variables,
                "function setupFileUpload", "No file chosen \u00e9");
    }

    private void assertScript(Class<?> scope, String file, Map<String, Object> variables,
            String function, String interpolatedValue) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(application,
                new MockHttpSession(application.getServletContext()), application.getServletContext());
        MockHttpServletResponse response = new MockHttpServletResponse(request);
        ServletWebRequest wicketRequest = new ServletWebRequest(request, "/wicket");
        ServletWebResponse wicketResponse = new ServletWebResponse(wicketRequest, response);
        JavaScriptTemplateResourceReference reference =
                new JavaScriptTemplateResourceReference(scope, file, Model.ofMap(variables));
        new SecurityHeadersFilter().doFilter(request, response,
                (servletRequest, servletResponse) ->
                        reference.getResource().respond(new IResource.Attributes(wicketRequest, wicketResponse)));

        assertNull(response.getErrorMessage());
        assertEquals(200, response.getStatus());
        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("text/javascript", response.getContentType().split(";")[0]);
        String body = new String(response.getBinaryContent(), StandardCharsets.UTF_8);
        assertTrue(body.contains(function));
        assertTrue(body.contains(interpolatedValue));
        assertFalse(body.contains("${"));
    }
}
