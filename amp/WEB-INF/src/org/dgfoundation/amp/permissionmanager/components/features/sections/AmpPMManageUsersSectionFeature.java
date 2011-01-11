/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMUserSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageUsersTableFeaturePanel;
import org.digijava.kernel.user.User;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersSectionFeature extends AmpPMSectionFeaturePanel {

	protected ListView<User> idsList;
	
	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, String fmName, final IModel<Set<User>> usersModel)
			throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
		
		AmpPMManageUsersTableFeaturePanel usersTable = new AmpPMManageUsersTableFeaturePanel("users", usersModel, "Users");
		
		//OnePagerUtil.getReadOnlyListModelFromSetModel(usersModel);
		IModel<List<User>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(usersModel);//new Model(new ArrayList());
		//$users
		add(usersTable);
		add(new PagingNavigator("navigator", (PageableListView)usersTable.getList()));
		idsList = usersTable.getList();
		
		
		//$searchUsers
//		idsList = new ListView<User>("searchedUsers", listModel) {
//			
//			private static final long serialVersionUID = 7218457979728871528L;
//			@Override
//			protected void populateItem(final ListItem<User> item) {
//				item.add(new Label("foundUser", item.getModelObject().getName()));
//			}
//		};
//		idsList.setReuseItems(true);
//		add(idsList);
		
		final AbstractAmpAutoCompleteTextField<User> autoComplete = new AbstractAmpAutoCompleteTextField<User>(AmpPMUserSearchModel.class) {

			@Override
			protected String getChoiceValue(User choice) throws Throwable {
				return choice.getName() +" - "+ choice.getEmail();
			}
			
			@Override
			public void onSelect(AjaxRequestTarget target, User choice) {
				Set<User> set = usersModel.getObject();
				set.clear();
				set.add(choice);
				idsList.removeAll();
				target.addComponent(AmpPMManageUsersSectionFeature.this);
//				add(JavascriptPackageResource.getHeaderContribution(AmpSubsectionFeaturePanel.class, "subsectionSlideTogglePM.js"));
			//	target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
			}

			@Override
			public Integer getChoiceLevel(User choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<User> searchContacts=new AmpComboboxFieldPanel<User>("searchUsers", "Search Users", autoComplete,true);
		add(searchContacts);
		
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, IModel model,
			String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

}
