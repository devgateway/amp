/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesTableFeaturePanel extends
		AmpFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel model,
			String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLeadingNewLine
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel<Set<AmpTeam>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, model, fmName, hideLeadingNewLine);

		AbstractReadOnlyModel<List<AmpTeam>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
		list = new PageableListView<AmpTeam>("usersList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpTeam> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new Label("workspaceName", item.getModelObject().getName()));
				item.add(new Label("workspaceMembers", item.getModelObject().getName()));
			//	item.add(new Label("editUser", "editMe"));
//				String tooltipText = "info text de test";
//				if (tooltipText != null) {
//					BeautyTipBehavior toolTip = new BeautyTipBehavior(tooltipText);
//					toolTip.setPositionPreference(TipPosition.right);
//					item.add(toolTip);
//				}
//				try {
//					item.add(new AmpPMSearchOrganizationsFeaturePanel("assignedOrgsPerUser", item.getModel(), "Assigning Organizations", true));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
			}
		};
		list.setReuseItems(true);
		add(list);
		
	}
		
}

