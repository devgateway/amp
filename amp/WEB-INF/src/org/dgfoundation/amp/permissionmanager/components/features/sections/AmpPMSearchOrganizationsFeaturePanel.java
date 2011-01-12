/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;

/**
 * @author dan
 *
 */
public class AmpPMSearchOrganizationsFeaturePanel extends AmpFeaturePanel {

	protected ListView<AmpOrganisation> idsList;
	
	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSearchOrganizationsFeaturePanel(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSearchOrganizationsFeaturePanel(String id, IModel<User> model, String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMSearchOrganizationsFeaturePanel(String id, IModel<User> model, String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);

		add(new Label("searchOrganizations","search orgs..."));
		//TODO
		
//		final AbstractAmpAutoCompleteTextField<AmpOrganisation> autoComplete = new AbstractAmpAutoCompleteTextField<AmpOrganisation>(AmpOrganisationSearchModel.class) {
//
//			@Override
//			protected String getChoiceValue(AmpOrganisation choice)
//					throws Throwable {
//				return choice.getAcronymAndName();
//			}
//
//			@Override
//			public void onSelect(AjaxRequestTarget target,AmpOrganisation choice) {
//				AmpOrganisationContact ampOrgCont = new AmpOrganisationContact();
//				ampOrgCont.setOrganisation(choice);
//				ampOrgCont.setContact(model.getObject());
//				Set<AmpOrganisationContact> set = setModel.getObject();
//				set.add(ampOrgCont);
//				idsList.removeAll();
//				target.addComponent(idsList.getParent());
//			}
//
//			@Override
//			public Integer getChoiceLevel(AmpOrganisation choice) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
//		autoComplete.add(sizeModifier);
//		final AmpComboboxFieldPanel<User> searchContacts=new AmpComboboxFieldPanel<User>("searchUsers", "Search Users", autoComplete,true);
//		add(searchContacts);
		
		add(new Label("orgsListTable","orgsListTable..."));
		
	}

}
