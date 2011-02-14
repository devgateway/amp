/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;

/**
 * @author dan
 *
 */
public abstract class AmpPMBaseTreePanel extends AmpComponentPanel {
	
	
	/**
	 * @param id
	 * @param treeModel
	 * @param fmName
	 */
	public AmpPMBaseTreePanel(String id, IModel<TreeModel> treeModel, String fmName) {
		super(id, treeModel, fmName);
		
		add(new AjaxLink("expandAll")
	        {
	            @Override
	            public void onClick(AjaxRequestTarget target)
	            {
	                getTree().getTreeState().expandAll();
	                getTree().updateTree(target);
	            }
	        });

	        add(new AjaxLink("collapseAll")
	        {
	            @Override
	            public void onClick(AjaxRequestTarget target)
	            {
	                getTree().getTreeState().collapseAll();
	                getTree().updateTree(target);
	            }
	        });
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMBaseTreePanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}
	
	
    protected abstract AbstractTree getTree();

    
}
