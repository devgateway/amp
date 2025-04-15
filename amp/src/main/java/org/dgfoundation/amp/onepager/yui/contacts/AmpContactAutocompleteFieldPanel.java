package org.dgfoundation.amp.onepager.yui.contacts;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactPhoneProperty;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

import java.util.*;

public abstract class AmpContactAutocompleteFieldPanel extends
        AmpAutocompleteFieldPanel<AmpContact> {
    private static final long serialVersionUID = 1L;
    private static Map <Long,AmpContact>  reusableObjects = new HashMap <Long,AmpContact> ();


    public AmpContactAutocompleteFieldPanel(
            String id,
            String fmName,
            boolean hideLabel,
            Class<? extends AbstractAmpAutoCompleteModel<AmpContact>> objectListModelClass,
            String aditionalTooltipKey, boolean showTooltipIfLabelHidden) {
        super(id, fmName, hideLabel, objectListModelClass,
                AmpContactAutocompleteFieldPanel.class,
                "AmpContactAutocompleteFieldPanel.js",
                "WicketContactAutoComplete",aditionalTooltipKey,showTooltipIfLabelHidden);
    }

    @Override
    protected String[][] getChoiceValues(String input) {
        Collection<AmpContact> choices = getChoices(input);
        List<String[]> choiceValues = new ArrayList<String[]>();
        for (AmpContact choice : choices) {
            String choiceId = getChoiceId(choice);
            Integer choiceLevel = getChoiceLevel(choice);
            String details = getAdditionalDetails(choice);
            String styleClass = getStyleClass(choice);
            choiceValues.add(new String[] { getChoiceValue(choice),
                    choiceLevel != null ? choiceLevel.toString() : "0",
                    details, styleClass, choiceId });
        }

        return choiceValues.toArray(new String[0][0]);
    }

    /**
     * Get the object coresponding to the user's selection
     * @param objId
     * @return
     */
    @Override
    protected AmpContact getSelectedChoice(Long objId) {
        if (objId.equals(-1L)){
            AmpContact newContact = new AmpContact();
            //newContact.setName(TranslatorUtil.getTranslation("Change Name"));
            //newContact.setLastname(TranslatorUtil.getTranslation("Change Lastname"));
            newContact.setTemporaryId("_"+new Date().getTime());

            return newContact;
        }
        AmpContact selectedContact = null;
        if (reuseObjects) {
            selectedContact = reusableObjects.get(objId);
        }
        if (selectedContact == null) {
            selectedContact = super.getSelectedChoice(objId);
            reusableObjects.put(objId, selectedContact);
        }
        return selectedContact;
    }

    @Override
    protected String getChoiceValue(AmpContact choice) {
        if (choice.getId() == null) {
            return TranslatorUtil.getTranslation("add new contact");
        }
        String ret = choice.getFullname();
        if (ret == null)
            ret = "";
        return ret;
    }

    @Override
    public Integer getChoiceLevel(AmpContact choice) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getStyleClass(AmpContact choice) {
        if (choice.getId() == null) {
            return "l_mid_b";
        }
        return "";
    }

    public String getAdditionalDetails(AmpContact contact) {
        if (contact.getId() == null || contact.getId().equals(-1L))
            return "";
        StringBuilder details = new StringBuilder();
        String emails = "";
        String orgs = "";
        String phones = "";
        String faxes = "";
        if (contact.getTitle() != null) {
            details.append(contact.getTitle().getValue());
        }
        details.append(" ");
        details.append(DbUtil.filter(contact.getNameAndLastName()));
        details.append("<br/>");
        if (contact.getProperties() != null) {
            for (AmpContactProperty property : contact.getProperties()) {
                if (property.getName().equals(
                        Constants.CONTACT_PROPERTY_NAME_EMAIL)
                        && property.getValue().length() > 0) {
                    emails += property.getValue() + "<br/>";
                } else if (property.getName().equals(
                        Constants.CONTACT_PROPERTY_NAME_PHONE)
                        && ((AmpContactPhoneProperty) property).getValueAsFormatedPhoneNum().length() > 0) {

                    phones += ((AmpContactPhoneProperty) property).getValueAsFormatedPhoneNum() + "<br/>";
                } else if (property.getName().equals(
                        Constants.CONTACT_PROPERTY_NAME_FAX)
                        && property.getValue().length() > 0) {
                    faxes += property.getValue() + "<br/>";
                }
            }
        }
        details.append("<br/>");
        details.append("Emails: ");
        details.append(emails);
        details.append("<br/>");

        details.append("<br/>");
        details.append("Phones: ");
        details.append(phones);
        details.append("<br/>");

        details.append("<br/>");
        details.append("Faxes: ");
        details.append(faxes);
        details.append("<br/>");

        if ((contact.getOrganizationContacts() != null && contact
                .getOrganizationContacts().size() > 0)
                || (contact.getOrganisationName() != null && contact
                        .getOrganisationName().length() > 0)) {
            if (contact.getOrganisationName() != null
                    && contact.getOrganisationName().length() > 0) {
                orgs += DbUtil.filter(contact.getOrganisationName()) + "<br/>";
            }
            for (AmpOrganisationContact contOrg : contact
                    .getOrganizationContacts()) {
                orgs += DbUtil.filter(contOrg.getOrganisation().getName()) + "<br/>";
            }
        }
        details.append("<br/>");
        details.append("Organizations: ");
        details.append(orgs);
        details.append("<br/>");
        return DbUtil.filter(details.toString(), true);
    }


}
