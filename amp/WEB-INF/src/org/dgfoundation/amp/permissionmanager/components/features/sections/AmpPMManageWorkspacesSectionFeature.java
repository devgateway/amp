/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageWorkspacesTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesSectionFeature extends AmpPMSectionFeaturePanel {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesSectionFeature(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesSectionFeature(String id, IModel model, String fmName) throws Exception {
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
	public AmpPMManageWorkspacesSectionFeature(String id, IModel<Set<AmpTeam>> workspacesModel,	String fmName, boolean hideLabel) throws Exception {
		super(id, workspacesModel, fmName, hideLabel);
		
		AmpPMManageWorkspacesTableFeaturePanel workspacesTable = new AmpPMManageWorkspacesTableFeaturePanel("workspaces", workspacesModel, "Workspaces", false);
//		
//		//OnePagerUtil.getReadOnlyListModelFromSetModel(usersModel);
//		IModel<List<User>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(usersModel);//new Model(new ArrayList());
//		//$users
//		add(usersTable);
//		add(new PagingNavigator("navigator", (PageableListView)usersTable.getList()));
//		idsList = usersTable.getList();
		
		add(workspacesTable);
		add(new PagingNavigator("workspacesNavigator", (PageableListView)workspacesTable.getList()));
		
		final AbstractAmpAutoCompleteTextField<AmpTeam> autoComplete = new AbstractAmpAutoCompleteTextField<AmpTeam>(org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMWorkspaceSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpTeam choice) throws Throwable {
				return choice.getName();
			}
			
			@Override
			public void onSelect(AjaxRequestTarget target, AmpTeam choice) {
			//	Set<User> set = usersModel.getObject();
			//	set.clear();
			//	set.add(choice);
				//idsList.removeAll();
				target.addComponent(AmpPMManageWorkspacesSectionFeature.this);
//				add(JavascriptPackageResource.getHeaderContribution(AmpSubsectionFeaturePanel.class, "subsectionSlideTogglePM.js"));
			//	target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
			}

			@Override
			public Integer getChoiceLevel(AmpTeam choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpTeam> searchContacts=new AmpComboboxFieldPanel<AmpTeam>("searchWorkspaces", "Search Workspaces", autoComplete,true);
		add(searchContacts);		
		
		
	}

}
