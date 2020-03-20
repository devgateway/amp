package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactEPConstants {

    private ContactEPConstants() {
    }

    public static final String CONTACT = "contact";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LAST_NAME = "last-name";

    public static final String PHONE = "phone";
    public static final String FAX = "fax";
    public static final String EMAIL = "email";
    
    public static final int CONTACT_PROPERTY_MAX_SIZE = 3;

    public static final String CREATED_BY = FieldMap.underscorify(ContactFieldsConstants.CREATED_BY);
}
