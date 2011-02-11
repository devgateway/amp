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
	
	
	IModel<AmpTreeVisibilityModelBean> ampTreeVisibility;
	

	public IModel<AmpTreeVisibilityModelBean> getAmpTreeVisibility() {
		return ampTreeVisibility;
	}

	public void setAmpTreeVisibility(IModel<AmpTreeVisibilityModelBean> ampTreeVisibility) {
		this.ampTreeVisibility = ampTreeVisibility;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMBaseTreePanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMBaseTreePanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMBaseTreePanel(String id, IModel model, String fmName,
			AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMBaseTreePanel(String id, IModel<AmpTreeVisibilityModelBean> model, String fmName) {
		super(id, model, fmName);
		setAmpTreeVisibility(model);
		
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
	
	
    protected abstract AbstractTree getTree();

    protected TreeModel createTreeModel(IModel<AmpTreeVisibilityModelBean> treeModel)
    {
    	AmpTreeVisibilityModelBean tree = treeModel.getObject();
        return convertToTreeModel(tree,tree.getItems());
    }

    private TreeModel convertToTreeModel(AmpTreeVisibilityModelBean tree, List<Object> list)
    {
        TreeModel model = null;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean("ROOT",list));
        add(rootNode, list);
        model = new DefaultTreeModel(rootNode);
        return model;
    }

    private void add(DefaultMutableTreeNode parent, List<Object> sub)
    {
        for (Iterator<Object> i = sub.iterator(); i.hasNext();)
        {
        	AmpTreeVisibilityModelBean o = (AmpTreeVisibilityModelBean)i.next();
        	if(o.getItems().size()>0){
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.getName(),o.getItems()));
              parent.add(child);
              add(child, (List<Object>)o.getItems());
        	}
        	else{
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.toString()));
              parent.add(child);
        	}
        }
    }


    
    
}
