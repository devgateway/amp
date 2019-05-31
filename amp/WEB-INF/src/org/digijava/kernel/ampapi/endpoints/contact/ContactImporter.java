package org.digijava.kernel.ampapi.endpoints.contact;

import java.util.LinkedHashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.CommonErrors;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ContactImporter extends ObjectImporter {

    private AmpContact contact;

    public ContactImporter() {
        super(new InputValidatorProcessor(InputValidatorProcessor.getContactFormatValidators()),
                new InputValidatorProcessor(InputValidatorProcessor.getContactBusinessRulesValidators()),
                AmpFieldsEnumerator.getEnumerator().getContactFields());
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
                    addError(ContactErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.ID));
                    return this;
                }
            } else {
                addError(ContactErrors.FIELD_READ_ONLY.withDetails(ContactEPConstants.ID));
                return this;
            }
        }

        Object createdById = newJson.get(ContactEPConstants.CREATED_BY);
        AmpTeamMember createdBy = TeamMemberUtil.getAmpTeamMember(getLongOrNull(createdById));
        if (createdById != null && createdBy == null) {
            addError(ContactErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.CREATED_BY));
            return this;
        }
        if (contactId == null && createdBy == null) {
            TeamMember teamMember = TeamMemberUtil.getLoggedInTeamMember();
            if (teamMember != null) {
                createdBy = TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
            } else {
                addError(ContactErrors.FIELD_REQUIRED.withDetails(ContactEPConstants.CREATED_BY));
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

    public AmpContact getContact() {
        return contact;
    }

    /**
     * Get the result of import/update contact in JsonBean format
     *
     * @return JsonBean the result of the import or update action
     */
    public JsonApiResponse getResult() {
        Map<String, Object> details = new LinkedHashMap<>();
        if (errors.size() == 0 && contact != null) {
            details.put(ContactEPConstants.ID, contact.getId());
            details.put(ContactEPConstants.NAME, contact.getName());
            details.put(ContactEPConstants.LAST_NAME, contact.getLastname());
        } else {
            details.put(ContactEPConstants.CONTACT, newJson);
            if (errors.isEmpty()) {
                addError(CommonErrors.UNKOWN_ERROR);
            }
        }
        return buildResponse(details, null);
    }

}
