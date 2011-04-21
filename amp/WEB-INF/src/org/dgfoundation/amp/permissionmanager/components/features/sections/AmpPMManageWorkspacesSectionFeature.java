/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMWorkspaceSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageWorkspacesTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesSectionFeature extends AmpPMSectionFeaturePanel {

	protected ListView<AmpTeam> idsList;
	
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
	public AmpPMManageWorkspacesSectionFeature(String id, final IModel<Set<AmpTeam>> workspacesModel,	String fmName, boolean hideLabel) throws Exception {
		super(id, workspacesModel, fmName, hideLabel);
		
		final AmpPMManageWorkspacesTableFeaturePanel workspacesTable = new AmpPMManageWorkspacesTableFeaturePanel("workspaces", workspacesModel, "Workspaces", false);
		
		add(workspacesTable);
//		AjaxPagingNavigator pager = new AjaxPagingNavigator("workspacesNavigator", (PageableListView)workspacesTable.getList()) {
//			@Override
//			protected void onAjaxEvent(AjaxRequestTarget target) {
//				target.addComponent(AmpPMManageWorkspacesSectionFeature.this);
//				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpPMManageWorkspacesSectionFeature.this));
//			}
//		};
		AmpPMAjaxPagingNavigator pager = new AmpPMAjaxPagingNavigator("workspacesNavigator", (PageableListView)workspacesTable.getList());
		add(pager);
		idsList = workspacesTable.getList();
		
		final AbstractAmpAutoCompleteTextField<AmpTeam> autoComplete = new AbstractAmpAutoCompleteTextField<AmpTeam>(AmpPMWorkspaceSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpTeam choice) throws Throwable {
				return choice.getName();
			}
			
			@Override
			public void onSelect(AjaxRequestTarget target, AmpTeam choice) {
				Set<AmpTeam> set = workspacesModel.getObject();
				set.clear();
				set.add(choice);
				idsList.removeAll();
				workspacesTable.getSliders().clear();
				target.addComponent(AmpPMManageWorkspacesSectionFeature.this);
				target.appendJavascript(OnePagerConst.getToggleJS(AmpPMManageWorkspacesSectionFeature.this.getSliderPM()));
			}

			@Override
			public Integer getChoiceLevel(AmpTeam choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpTeam> searchContacts=new AmpComboboxFieldPanel<AmpTeam>("searchWorkspaces", "Search Workspaces", autoComplete,false,true);
		add(searchContacts);		
		
	}
	
}
