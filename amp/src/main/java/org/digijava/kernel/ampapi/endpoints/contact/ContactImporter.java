package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class ContactImporter extends ObjectImporter<AmpContact> {

    private AmpContact contact;

    public ContactImporter() {
        super(new InputValidatorProcessor(InputValidatorProcessor.getFormatValidators()),
                AmpFieldsEnumerator.getEnumerator().getContactField(),
                TLSUtils.getSite());
    }

    public ContactImporter createContact(Map<String, Object> newJson) {
        return importContact(null, newJson);
    }

    public ContactImporter updateContact(Long contactId, Map<String, Object> newJson) {
        return importContact(contactId, newJson);
    }

    private ContactImporter importContact(Long contactId, Map<String, Object> newJson) {
        this.newJson = newJson;

        Object contactJsonId = newJson.get(ContactEPConstants.ID);

        if (contactJsonId != null) {
            if (contactId != null) {
                if (!contactId.equals(getLongOrNull(contactJsonId))) {
                    addError(ValidationErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.ID));
                    return this;
                }
            } else {
                addError(ValidationErrors.FIELD_READ_ONLY.withDetails(ContactEPConstants.ID));
                return this;
            }
        }

        Object createdById = newJson.get(ContactEPConstants.CREATED_BY);
        AmpTeamMember createdBy = TeamMemberUtil.getAmpTeamMember(getLongOrNull(createdById));
        if (createdById != null && createdBy == null) {
            addError(ValidationErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.CREATED_BY));
            return this;
        }
        if (contactId == null && createdBy == null) {
            TeamMember teamMember = TeamMemberUtil.getLoggedInTeamMember();
            if (teamMember != null) {
                createdBy = TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
            } else {
                addError(ValidationErrors.FIELD_REQUIRED.withDetails(ContactEPConstants.CREATED_BY));
                return this;
            }
        }

        try {
            if (contactId == null) {
                contact = new AmpContact();
            } else {
                contact = (AmpContact) PersistenceManager.getSession().get(AmpContact.class, contactId);

                if (contact == null) {
                    ApiErrorResponseService.reportResourceNotFound(ContactErrors.CONTACT_NOT_FOUND);
                }
            }

            validateAndImport(contact, newJson);

            if (errors.isEmpty()) {
                setupBeforeSave(contact, createdBy);
                PersistenceManager.getSession().saveOrUpdate(contact);
                PersistenceManager.flushAndCommit(PersistenceManager.getSession());
            } else {
                PersistenceManager.rollbackCurrentSessionTx();
            }
        } catch (RuntimeException e) {
            PersistenceManager.rollbackCurrentSessionTx();
            throw new RuntimeException("Failed to import contact", e);
        }

        return this;
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
    public AmpContact getImportResult() {
        return contact;
    }

    @Override
    protected String getInvalidInputFieldName() {
        return ContactEPConstants.CONTACT;
    }

}
