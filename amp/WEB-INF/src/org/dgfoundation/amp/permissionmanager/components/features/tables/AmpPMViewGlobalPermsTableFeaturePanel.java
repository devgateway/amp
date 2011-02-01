/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpEditLinkField;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class AmpPMViewGlobalPermsTableFeaturePanel extends AmpFormTableFeaturePanel {

	//boolean editPermVisible = false;
	
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
	public AmpPMViewGlobalPermsTableFeaturePanel(String id,final IModel<Set<Permission>> permissionsSetModel, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, permissionsSetModel, fmName, hideLeadingNewLine);
		// TODO Auto-generated constructor stub
		AbstractReadOnlyModel<List<Permission>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(permissionsSetModel);
		
		list = new PageableListView<Permission>("globalPermsList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<Permission> item) {
				
				final MarkupContainer listParent=this.getParent();
				
				item.add(new Label("globalPermName", item.getModelObject().getName()));
				
				final Label editGlobalPerm = new Label("editGlobalPerm","un teasta sdgsad gag aga dsgsdga sdhgadshahasd asdfha hdfshadfhadfhdfahafdhfdhdh");
				editGlobalPerm.setVisible(false);
				editGlobalPerm.setOutputMarkupId(true);
				item.add(editGlobalPerm);
				
				AmpEditLinkField editLink = new AmpEditLinkField("editGlobalPermLink", "Edit Global Permission Link", true, true) {
					
					@Override
					protected void onClick(AjaxRequestTarget target) {
						editGlobalPerm.setVisible(!editGlobalPerm.isVisible());
						target.addComponent(listParent);
					}
				};
				item.add(editLink);
				
				AmpDeleteLinkField deleteLink = new AmpDeleteLinkField("deleteGlobalPermLink", "Delete Global Permission Link") {
					
					@Override
					protected void onClick(AjaxRequestTarget target) {
						permissionsSetModel.getObject().remove(item.getModelObject());
						try {
							if(item.getModelObject() instanceof GatePermission)
								PermissionUtil.deletePermission(item.getModelObject().getId());
							else if(item.getModelObject() instanceof CompositePermission)
								{
									for (Permission perm : ((CompositePermission)item.getModelObject()).getPermissions()) {
										PermissionUtil.deletePermission(perm.getId());
									}
									PermissionUtil.deletePermission(item.getModelObject().getId());
								}
						} catch (HibernateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DgException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						target.addComponent(AmpPMViewGlobalPermsTableFeaturePanel.this.getParent());
						list.removeAll();
					}
				};
				item.add(deleteLink);
				
				
				
			}
		};
		list.setOutputMarkupId(true);
		list.setReuseItems(true);
		add(list);
		
	}

}
