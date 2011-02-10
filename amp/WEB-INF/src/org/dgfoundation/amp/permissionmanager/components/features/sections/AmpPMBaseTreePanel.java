/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

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
    	
//        List<Object> l1 = new ArrayList<Object>();
//        l1.add("test 1.1");
//        l1.add("test 1.2");
//        l1.add("test 1.3");
//        List<Object> l2 = new ArrayList<Object>();
//        l2.add("test 2.1");
//        l2.add("test 2.2");
//        l2.add("test 2.3");
//        List<Object> l3 = new ArrayList<Object>();
//        l3.add("test 3.1");
//        l3.add("test 3.2");
//        l3.add("test 3.3");
//
//        l2.add(l3);
//
//        l2.add("test 2.4");
//        l2.add("test 2.5");
//        l2.add("test 2.6");
//
//        l3 = new ArrayList<Object>();
//        l3.add("test 3.1");
//        l3.add("test 3.2");
//        l3.add("test 3.3");
//        l2.add(l3);
//
//        l1.add(l2);
//
//        l2 = new ArrayList<Object>();
//        l2.add("test 2.1");
//        l2.add("test 2.2");
//        l2.add("test 2.3");
//
//        l1.add(l2);
//
//        l1.add("test 1.3");
//        l1.add("test 1.4");
//        l1.add("test 1.5");

        return convertToTreeModel(tree.getItems());
    }

    private TreeModel convertToTreeModel(List<Object> list)
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
//            if (o instanceof List)
//            {
//                DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean("subtree..."));
//                parent.add(child);
//                add(child, (List<Object>)o);
//            }
//            else
//            {
//                DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.toString()));
//                parent.add(child);
//            }
        }
    }


    
    
}
