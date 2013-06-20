package org.dgfoundation.amp.onepager.yui.contacts;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpContactSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

public abstract class AmpContactAutocompleteFieldPanel extends
		AmpAutocompleteFieldPanel<AmpContact> {
	private static final long serialVersionUID = 1L;

	public AmpContactAutocompleteFieldPanel(
			String id,
			String fmName,
			boolean hideLabel,
			Class<? extends AbstractAmpAutoCompleteModel<AmpContact>> objectListModelClass,
			boolean useCache, boolean applyLocalFilter) {
		super(id, fmName, hideLabel, objectListModelClass,
				AmpContactAutocompleteFieldPanel.class,
				"AmpContactAutocompleteFieldPanel.js",
				"WicketContactAutoComplete");
	}

	@Override
	protected String[][] getChoiceValues(String input) {
		Collection<AmpContact> choices = getChoices(input);
		List<String[]> choiceValues = new ArrayList<String[]>();
		for (AmpContact choice : choices) {
			Integer choiceLevel = getChoiceLevel(choice);
			String details = getAdditionalDetails(choice);
			String styleClass = getStyleClass(choice);
			choiceValues.add(new String[] { getChoiceValue(choice),
					choiceLevel != null ? choiceLevel.toString() : "0",
					details, styleClass });
		}

		return choiceValues.toArray(new String[0][0]);
	}
	@Override
	protected AmpContact getSelectedChoice(String input) {
		try {
			AmpAuthWebSession session = (AmpAuthWebSession) this.getSession();
			String language=session.getLocale().getLanguage();
			
			AmpContactSearchModel newInstance = new AmpContactSearchModel(input, language, modelParams);
			newInstance.getParams().put(AbstractAmpAutoCompleteModel.PARAM.EXACT_MATCH, true);
			Collection<AmpContact> choices = newInstance.getObject();
			if(choices==null || choices.size()==0) throw new RuntimeException("Cannot find selection object!");
			return choices.iterator().next();
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} 
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
		if (contact.getId() == null)
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
						&& property.getValueAsFormatedPhoneNum().length() > 0) {

					phones += property.getValueAsFormatedPhoneNum() + "<br/>";
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