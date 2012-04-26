/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LabelIconPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMCheckBoxTree;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;

/**
 * @author dan
 *
 */
public class AmpPMCheckBoxIconPanel extends LabelIconPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the panel.
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model that is used to access the TreeNode
	 * @param tree
	 */
	public AmpPMCheckBoxIconPanel(String id, IModel model, AmpPMCheckBoxTree tree)
	{
		super(id, model, tree);
	}
	
	/**
	 * @see org.apache.wicket.markup.html.tree.LabelIconPanel#addComponents(org.apache.wicket.model.IModel,
	 *      org.apache.wicket.markup.html.tree.BaseTree)
	 */
	protected void addComponents(final IModel model, final BaseTree tree)
	{
		final AmpPMCheckBoxTree cbTree = (AmpPMCheckBoxTree)tree;
		AmpPMCheckBoxTree.ICheckCallback callback = new AmpPMCheckBoxTree.ICheckCallback()
		{
			private static final long serialVersionUID = 1L;

			public void onUpdate(AjaxRequestTarget target)
			{
				onNodeCheckUpdated((TreeNode)model.getObject(), cbTree, target);
			}
		};

		add(newImageComponent("icon", tree, model));
		
//		IModel dataModel = cbTree.newCheckBoxModel((TreeNode)model.getObject());
		MarkupContainer cb = null;
//		add( cb = cbTree.newCheckBox("checkbox", dataModel, callback) );
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) model.getObject();
		AmpTreeVisibilityModelBean ampTree = (AmpTreeVisibilityModelBean) dmtn.getUserObject();
		IModel dataModel = new PropertyModel(ampTree,"checked"); 
		add(cb = cbTree.newCheckBox("checkbox", dataModel, callback)); 
		add(newContentComponent("content", tree, model));
	}

	/**
	 * Handler invoked when the checkbox is clicked. By default makes the node selected
	 * 
	 * @param node
	 * @param tree
	 * @param target
	 */
	protected void onNodeCheckUpdated(TreeNode node, AmpPMCheckBoxTree tree, AjaxRequestTarget target)
	{
		tree.getTreeState().selectNode(node, !tree.getTreeState().isNodeSelected(node));
		tree.updateTree(target);
	}

}
