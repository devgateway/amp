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
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMSearchOrganizationsFeaturePanel;
import org.digijava.kernel.user.User;

import com.visural.wicket.behavior.beautytips.BeautyTipBehavior;
import com.visural.wicket.behavior.beautytips.TipPosition;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersTableFeaturePanel extends AmpFormTableFeaturePanel {



	public AmpPMManageUsersTableFeaturePanel(String id, IModel<Set<User>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, model, fmName, hideLeadingNewLine);
		// TODO Auto-generated constructor stub
	}

	public AmpPMManageUsersTableFeaturePanel(String id, IModel<Set<User>> model, String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
		
		//final List<User> users= org.digijava.module.um.util.DbUtil.getList(User.class.getName(),"firstNames");
		//final IModel<Set<User>> usersModel = new AmpPMUserModel();
		AbstractReadOnlyModel<List<User>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
		//IModel usersModel = new Model((Serializable)users);
		
		list = new PageableListView<User>("usersList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<User> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new Label("userLabel", item.getModelObject().getName()));
				item.add(new Label("userEmailLabel", item.getModelObject().getEmail()));
			//	item.add(new Label("editUser", "editMe"));
//				String tooltipText = "info text de test";
//				if (tooltipText != null) {
//					BeautyTipBehavior toolTip = new BeautyTipBehavior(tooltipText);
//					toolTip.setPositionPreference(TipPosition.right);
//					item.add(toolTip);
//				}
			}
		};
		list.setReuseItems(true);
		add(list);
		
		add(new AmpPMSearchOrganizationsFeaturePanel("assignedOrgs", model, "Assigning Organizations", false));
		
	}

	

}
