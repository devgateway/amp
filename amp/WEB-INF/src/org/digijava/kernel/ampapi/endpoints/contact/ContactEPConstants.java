package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;

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
    public static final String ORGANISATION_CONTACTS = "organisation_contacts";
    public static final String PRIMARY_CONTACT = "primary_contact";

    public static final String CREATED_BY = InterchangeUtils.underscorify(ContactFieldsConstants.CREATED_BY);
}
