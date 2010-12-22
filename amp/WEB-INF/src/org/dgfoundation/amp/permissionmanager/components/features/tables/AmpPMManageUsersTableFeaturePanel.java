/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.digijava.kernel.user.User;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersTableFeaturePanel extends AmpFormTableFeaturePanel {



	public AmpPMManageUsersTableFeaturePanel(String id, IModel model, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, model, fmName, hideLeadingNewLine);
		// TODO Auto-generated constructor stub
	}

	public AmpPMManageUsersTableFeaturePanel(String id, IModel model, String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
		
		final List<User> users= org.digijava.module.um.util.DbUtil.getList(User.class.getName(),"firstNames");
		
		IModel usersModel = new Model((Serializable)users);
		list = new ListView<User>("usersList", usersModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<User> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new Label("userLabel", item.getModelObject().getName()+" - " + item.getModelObject().getEmail()));
				item.add(new Label("editUser", "editMe"));
			}
		};
		list.setReuseItems(true);
		add(list);
		
		
	}

	

}
