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
import org.digijava.module.gateperm.core.Permission;

/**
 * @author dan
 *
 */
public class AmpPMViewGlobalPermsTableFeaturePanel extends AmpFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMViewGlobalPermsTableFeaturePanel(String id, IModel model, String fmName) throws Exception {
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
	public AmpPMViewGlobalPermsTableFeaturePanel(String id, IModel<Set<Permission>> permissionsModel, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, permissionsModel, fmName, hideLeadingNewLine);
		// TODO Auto-generated constructor stub
		AbstractReadOnlyModel<List<Permission>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(permissionsModel);
		list = new PageableListView<Permission>("globalPermsList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<Permission> item) {
				item.add(new Label("globalPermName", item.getModelObject().getName()));
			}
		};
		list.setReuseItems(true);
		add(list);
		
	}

}
