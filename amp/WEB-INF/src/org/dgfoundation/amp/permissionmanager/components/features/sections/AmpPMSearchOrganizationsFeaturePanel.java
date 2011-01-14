/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMOrganizationsUsersTableFeaturePanel;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;

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
	public AmpPMSearchOrganizationsFeaturePanel(String id,final IModel<User> model, String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);

		final AmpPMOrganizationsUsersTableFeaturePanel orgsTable = new AmpPMOrganizationsUsersTableFeaturePanel("orgsListTable", model, "Verified Organizations", false);
		orgsTable.setTableWidth(510);
		add(orgsTable);
		//add(new PagingNavigator("workspacesNavigator", (PageableListView)workspacesTable.getList()));
		idsList = orgsTable.getList();
		
		
		final AbstractAmpAutoCompleteTextField<AmpOrganisation> autoComplete = new AbstractAmpAutoCompleteTextField<AmpOrganisation>(AmpOrganisationSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpOrganisation choice)
					throws Throwable {
				return choice.getAcronymAndName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,AmpOrganisation choice) {
//				AmpOrganisation ampOrg = new AmpOrganisation();
//				ampOrgCont.setOrganisation(choice);
//				ampOrgCont.setContact(model.getObject());
				Set<AmpOrganisation> set = model.getObject().getAssignedOrgs();
				set.add(choice);
				idsList.removeAll();
				target.addComponent(idsList.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpOrganisation> searchOrgs=new AmpComboboxFieldPanel<AmpOrganisation>("searchOrganizations", "Search Organizations", autoComplete);
		add(searchOrgs);

//		AmpButtonField addbutton = new AmpButtonField("saveButton", "Save Verified Organizations") {
//			@Override
//			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
////				AmpCo<>mponent comp = new AmpComponent();
////				setModel.getObject().add(comp);
//				try {
//					DbUtil.updateUser(model.getObject());
//				} catch (UMException e) {
//					e.printStackTrace();
//				}
//				target.addComponent(this.getParent());
////				target.appendJavascript(OnePagerConst.slideToggle);
//				
//			}
//		};
//		add(addbutton);

		add(new Link("saveButton"){

			@Override
			public void onClick() {
				try {
					DbUtil.updateUser(model.getObject());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}

}
