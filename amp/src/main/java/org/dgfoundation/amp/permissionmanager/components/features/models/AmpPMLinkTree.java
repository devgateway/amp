/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.BaseTree;
import org.apache.wicket.extensions.markup.html.tree.LinkIconPanel;
import org.apache.wicket.extensions.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMTreeField;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * @author dan
 *
 */
public class AmpPMLinkTree extends LinkTree {

    /**
     * @param id
     */
    public AmpPMLinkTree(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     */
    public AmpPMLinkTree(String id, IModel<TreeModel> model) {
        super(id, model);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     */
    public AmpPMLinkTree(String id, TreeModel model) {
        super(id, model);
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    protected Component newNodeComponent(String id, IModel<Object> model)
    {
        return new LinkIconPanel(id, model, AmpPMLinkTree.this)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
            {
                //super.onNodeLinkClicked(node, tree, target);
                //AmpPMLinkTree.this.onNodeLinkClicked(node, tree, target);
                //target.add(AmpPMLinkTree.this);
                //System.out.println("aaa");
            }

            @Override
            protected Component newContentComponent(String componentId,
                    BaseTree tree, IModel<?> model) {
                DefaultMutableTreeNode o = (DefaultMutableTreeNode)model.getObject();
                //return new Label(componentId, getNodeTextModel(model));
                IModel<?> nodeTextModel = getNodeTextModel(model);
                return new AmpPMTreeField(componentId, nodeTextModel,componentId);
            }
                        
            @Override
            protected void addComponents(final IModel<Object> model, final BaseTree tree)
            {
                BaseTree.ILinkCallback callback = new BaseTree.ILinkCallback()
                {
                    private static final long serialVersionUID = 1L;

                    public void onClick(AjaxRequestTarget target)
                    {
                        //System.out.println("aaa");//onNodeLinkClicked(model.getObject(), tree, target);
                    }
                };

                MarkupContainer link = tree.newLink("iconLink", callback);
                add(link);
                link.add(newImageComponent("icon", tree, model));

                link = tree.newLink("contentLink", callback);
                add(link);
                link.add(newContentComponent("content", tree, model));
            }
            
        };
    }
    
    
    @Override
    protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
    {
        //tree.getTreeState().selectNode(node, !tree.getTreeState().isNodeSelected(node));
        //tree.updateTree(target);
    }
    

}
