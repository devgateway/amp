package org.digijava.kernel.ampapi.endpoints.contact.dto;

import io.swagger.annotations.ApiModel;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.dto.SwaggerMapWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used only for documentation in Swagger
 *
 * @author Nadejda Mandrescu
 */
@ApiModel(description = "A contact JSON that follows fields structure according to contact fields definition.\n"
        + "See [/contact/fields](#/contact/getAvailableFields) EP for the full list of fields and rules.\n"
        + "Export an existing contact as an example from "
        + "[/contact/{id}](#/contact/getContact) EP.")
public class SwaggerContact extends SwaggerMapWrapper<String, Object> {

    public SwaggerContact() {
    }

    public SwaggerContact(Map<String, Object> map) {
        super(map);
    }

    public static Map<String, Object> getExample() {
        Map<String, Object> c = new LinkedHashMap<>();
        c.put(ContactEPConstants.ID, 15);
        c.put(ContactEPConstants.NAME, "John");
        c.put(ContactEPConstants.LAST_NAME, "Smith");
        c.put(ContactEPConstants.EMAIL, "john.smith@amp.org");
        c.put("...", new Object());
        return c;
    }

}
