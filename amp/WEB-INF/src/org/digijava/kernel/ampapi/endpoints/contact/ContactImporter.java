package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ContactImporter extends ObjectImporter {

    private AmpContact contact;

    public ContactImporter() {
        super(AmpContact.class, new InputValidatorProcessor(InputValidatorProcessor.getContactValidators()));
    }

    public List<ApiErrorMessage> createContact(JsonBean newJson) {
        return importContact(null, newJson);
    }

    public List<ApiErrorMessage> updateContact(Long contactId, JsonBean newJson) {
        return importContact(contactId, newJson);
    }

    private List<ApiErrorMessage> importContact(Long contactId, JsonBean newJson) {
        this.newJson = newJson;

        List<APIField> fieldsDef = AmpFieldsEnumerator.PRIVATE_ENUMERATOR.getContactFields();

        Object createdById = newJson.get(ContactEPConstants.CREATED_BY);
        AmpTeamMember createdBy = TeamMemberUtil.getAmpTeamMember(getLongOrNull(createdById));
        if (createdById != null && createdBy == null) {
            return singletonList(ContactErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.CREATED_BY));
        }
        if (contactId == null && createdBy == null) {
            TeamMember teamMember = TeamMemberUtil.getLoggedInTeamMember();
            if (teamMember != null) {
                createdBy = TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
            } else {
                return singletonList(ContactErrors.FIELD_REQUIRED.withDetails(ContactEPConstants.CREATED_BY));
            }
        }

        try {
            if (contactId == null) {
                contact = new AmpContact();
            } else {
                contact = (AmpContact) PersistenceManager.getSession().load(AmpContact.class, contactId);
                cleanImportableFields(fieldsDef, contact);
            }

            contact = (AmpContact) validateAndImport(contact, null, fieldsDef, newJson.any(), null, null);

            if (contact == null) {
                throw new ObjectConversionException();
            }

            setupBeforeSave(contact, createdBy);

            PersistenceManager.getSession().saveOrUpdate(contact);

            PersistenceManager.flushAndCommit(PersistenceManager.getSession());
        } catch (ObjectConversionException | RuntimeException e) {
            PersistenceManager.rollbackCurrentSessionTx();

            if (e instanceof RuntimeException) {
                throw new RuntimeException("Failed to import contact", e);
            }
        }

        return new ArrayList<>(errors.values());
    }

    private Long getLongOrNull(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            return null;
        }
    }

    private void setupBeforeSave(AmpContact contact, AmpTeamMember createdBy) {
        if (contact.getId() == null) {
            contact.setCreator(createdBy);
        }
        contact.getProperties().forEach(p -> p.setContact(contact));
        contact.getOrganizationContacts().forEach(o -> o.setContact(contact));
    }

    @Override
    protected void configureCustom(Object obj, APIField fieldDef) {
        super.configureCustom(obj, fieldDef);

        if (obj instanceof AmpContactProperty) {
            ((AmpContactProperty) obj).setName(fieldDef.getDiscriminator());
        }
    }

    public AmpContact getContact() {
        return contact;
    }
}
