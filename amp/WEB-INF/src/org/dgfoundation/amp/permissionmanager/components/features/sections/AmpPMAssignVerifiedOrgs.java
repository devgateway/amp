/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMUserSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMVerifiedOrganizationsTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMVerifiedUsersTableFeaturePanel;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author dan
 *
 */
public class AmpPMAssignVerifiedOrgs extends AmpFeaturePanel {
	
	protected ListView<AmpOrganisation> idOrgsList, idUsersList;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMAssignVerifiedOrgs(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMAssignVerifiedOrgs(String id, IModel<Set<AmpOrganisation>> model, String fmName)
			throws Exception {
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
	public AmpPMAssignVerifiedOrgs(String id, final IModel<Set<AmpOrganisation>> orgsModel, final IModel<Set<User>> usersModel, String fmName, boolean hideLabel) throws Exception {
		super(id, orgsModel, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		AmpPMVerifiedOrganizationsTableFeaturePanel searchVerifiedOrgs = new AmpPMVerifiedOrganizationsTableFeaturePanel("verifiedOrgs", orgsModel, "Verified Organizations", true);
		searchVerifiedOrgs.setTableWidth(480);
		add(searchVerifiedOrgs);
		add(new PagingNavigator("verifiedOrgsNavigator", (PageableListView)searchVerifiedOrgs.getList()));
		idOrgsList = searchVerifiedOrgs.getList();

		
		final AbstractAmpAutoCompleteTextField<AmpOrganisation> autoComplete = new AbstractAmpAutoCompleteTextField<AmpOrganisation>(AmpOrganisationSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpOrganisation choice)
					throws Throwable {
				return choice.getAcronymAndName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,AmpOrganisation choice) {
				Set<AmpOrganisation> set = orgsModel.getObject();
				set.add(choice);
				idOrgsList.removeAll();
				target.addComponent(idOrgsList.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpPMAssignVerifiedOrgs.this));
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpOrganisation> searchOrgs=new AmpComboboxFieldPanel<AmpOrganisation>("searchVerifiedOrgs", "Search Verified Organizations", autoComplete);
		searchOrgs.getTitleLabel().add(new AttributeModifier("class",new Model("perm_search")));
		add(searchOrgs);


		AmpPMVerifiedUsersTableFeaturePanel searchVerifiedUsers = new AmpPMVerifiedUsersTableFeaturePanel("verifiedUsers", usersModel, "Verified Organizations", true);
		searchVerifiedUsers.setTableWidth(480);
		add(searchVerifiedUsers);
		idUsersList = searchVerifiedUsers.getList();
		add(new PagingNavigator("verifiedUsersNavigator", (PageableListView)searchVerifiedOrgs.getList()));
		
		final AbstractAmpAutoCompleteTextField<User> autoCompleteUser = new AbstractAmpAutoCompleteTextField<User>(AmpPMUserSearchModel.class) {

			@Override
			protected String getChoiceValue(User choice) throws Throwable {
				return choice.getName() +" - "+ choice.getEmail();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,User choice) {
				Set<User> set = usersModel.getObject();
				set.add(choice);
				idUsersList.removeAll();
				target.addComponent(idUsersList.getParent());
			}

			@Override
			public Integer getChoiceLevel(User choice) {
				return null;
			}
		};
		AttributeModifier sizeModifierUser = new AttributeModifier("size",new Model(25));
		autoCompleteUser.add(sizeModifierUser);
		final AmpComboboxFieldPanel<User> searchUsers=new AmpComboboxFieldPanel<User>("searchVerifiedUsers", "Search Users", autoCompleteUser);
		searchUsers.getTitleLabel().add(new AttributeModifier("class",new Model("perm_search")));
		add(searchUsers);
		
		
		add(new Link("saveOrgsToUsersButton"){
			@Override
			public void onClick() {
				try {
					for (User user : usersModel.getObject()) {
						user.getAssignedOrgs().clear();
						user.getAssignedOrgs().addAll(orgsModel.getObject());
						DbUtil.updateUser(user);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		add(new Link("resetOrgsToUsersButton"){
			@Override
			public void onClick() {
				usersModel.getObject().clear();
				orgsModel.getObject().clear();
				idUsersList.removeAll();
				idOrgsList.removeAll();
			}
		});

		add(new Link("cancelOrgsToUsersButton"){
			@Override
			public void onClick() {
			}
			
		});

		
	}

}
