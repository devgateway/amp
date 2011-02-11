/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMCheckBoxTree;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;

/**
 * @author dan
 *
 */
public class AmpPMTreeVisibilityFieldPermission extends AmpPMBaseTreePanel {

	private BaseTree tree;
	

	/**
	 * @param id
	 * @param ampTreeVisibilityModel
	 * @param string
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityModel, String fmName) {
		super(id, ampTreeVisibilityModel, fmName);
		setAmpTreeVisibility(ampTreeVisibilityModel);
		
		tree = new AmpPMCheckBoxTree("tree", createTreeModel(ampTreeVisibilityModel));
        tree.getTreeState().setAllowSelectMultiple(true);
        add(tree);
        tree.getTreeState().expandAll();
	}



	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMBaseTreePanel#getTree()
	 */
	@Override
	protected AbstractTree getTree() {
		// TODO Auto-generated method stub
		return tree;
	}
	

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityModel, String fmName, AmpFMTypes fmBehavior) {
		super(id, ampTreeVisibilityModel, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
		setAmpTreeVisibility(ampTreeVisibilityModel);
	}


}
