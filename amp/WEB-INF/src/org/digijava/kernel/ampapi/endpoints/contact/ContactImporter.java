package org.digijava.kernel.ampapi.endpoints.contact;

import java.util.ArrayList;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
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
                AmpFieldsEnumerator.getEnumerator().getContactField());
    }

    public ContactImporter createContact(JsonBean newJson) {
        return importContact(null, newJson);
    }

    public ContactImporter updateContact(Long contactId, JsonBean newJson) {
        return importContact(contactId, newJson);
    }

    private ContactImporter importContact(Long contactId, JsonBean newJson) {
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

            validateAndImport(contact, newJson.any());

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
    public JsonBean getResult() {
        JsonBean result = new JsonBean();
        if (errors.size() == 0 && contact == null) {
            result.set(EPConstants.ERROR, ApiError.toError(ApiError.UNKOWN_ERROR).getErrors());
        } else if (errors.size() > 0) {
            result.set(EPConstants.ERROR, ApiError.toError(errors.values()).getErrors());
            result.set(ContactEPConstants.CONTACT, newJson);
        } else {
            result = new JsonBean();
            result.set(ContactEPConstants.ID, contact.getId());
            result.set(ContactEPConstants.NAME, contact.getName());
            result.set(ContactEPConstants.LAST_NAME, contact.getLastname());
        }
        if (!warnings.isEmpty()) {
            result.set(EPConstants.WARNINGS, ApiError.formatNoWrap(warnings.values()));
        }
        
        return result;
    }

}
