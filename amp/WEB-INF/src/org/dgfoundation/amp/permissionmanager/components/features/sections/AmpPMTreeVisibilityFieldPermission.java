/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Enumeration;

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
	public AmpPMTreeVisibilityFieldPermission(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param ampTreeVisibilityModel
	 * @param string
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityModel, String fmName) {
		super(id, ampTreeVisibilityModel, fmName);

		tree = new AmpPMCheckBoxTree("tree", createTreeModel(ampTreeVisibilityModel)){
			@Override
			protected void onNodeCheckUpdated(TreeNode node, BaseTree tree, AjaxRequestTarget target) {
				if (!tree.getTreeState().isNodeSelected(node)) {
					deselectTree( tree, node );
				}
				else{
					selectTree( tree, node );
				}
			}
		};
        tree.getTreeState().setAllowSelectMultiple(true);
        add(tree);
        tree.getTreeState().collapseAll();
	}

	protected void selectTree(BaseTree tree, TreeNode node) {
		Enumeration nodeEnum = node.children();
		while ( nodeEnum.hasMoreElements() ) {
			TreeNode child = (TreeNode)nodeEnum.nextElement();
			tree.getTreeState().selectNode( child, true );
			selectTree( tree, child );
		}
		
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMBaseTreePanel#getTree()
	 */
	@Override
	protected AbstractTree getTree() {
		// TODO Auto-generated method stub
		return tree;
	}
	
	private void deselectTree( BaseTree tree, TreeNode node ) {
		Enumeration nodeEnum = node.children();
		while ( nodeEnum.hasMoreElements() ) {
			TreeNode child = (TreeNode)nodeEnum.nextElement();
			tree.getTreeState().selectNode( child, false );
			deselectTree( tree, child );
		}
	}
	

}
