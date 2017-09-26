/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.extensions.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.EnumeratedType;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMCheckBoxIconPanel;

/**
 * @author dan
 *
 */
public class AmpPMCheckBoxTree extends BaseTree {

    /**
     * @param id
     */
    public AmpPMCheckBoxTree(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     */
    public AmpPMCheckBoxTree(String id, IModel<TreeModel> model) {
        super(id, model);
        // TODO Auto-generated constructor stub
    }

    public AmpPMCheckBoxTree(String id, TreeModel model)
    {
        super(id, new Model((Serializable)model));
    }

    
    /**
     * @see org.apache.wicket.extensions.markup.html.tree.BaseTree#newNodeComponent(java.lang.String,
     *      org.apache.wicket.model.IModel)
     */
    protected Component newNodeComponent(String id, IModel model)
    {
        //Object object = model.getObject();
        return new AmpPMCheckBoxIconPanel(id, model, AmpPMCheckBoxTree.this)
        {
            private static final long serialVersionUID = 1L;

            protected void onNodeCheckUpdated(TreeNode node, AmpPMCheckBoxTree tree, AjaxRequestTarget target)
            {
                super.onNodeCheckUpdated(node, tree, target);
                AmpPMCheckBoxTree.this.onNodeCheckUpdated(node, tree, target);
            }
        };
    }


    /**
     * Method invoked after the node has been selected / unselected.
     * 
     * @param node
     * @param tree
     * @param target
     */
    protected void onNodeCheckUpdated(TreeNode node, BaseTree tree, AjaxRequestTarget target) {
        if (!tree.getTreeState().isNodeSelected(node)) {
            deselectTree( tree, node );
        }
        else{
            selectTree( tree, node );
        }
    }
    
    public void refreshTree(){
        invalidateAll();
    }
    
    protected void selectTree(BaseTree tree, TreeNode node) {
        Enumeration nodeEnum = node.children();
        while ( nodeEnum.hasMoreElements() ) {
            TreeNode child = (TreeNode)nodeEnum.nextElement();
            tree.getTreeState().selectNode( child, true );
            DefaultMutableTreeNode dmtn=(DefaultMutableTreeNode) child;
            AmpTreeVisibilityModelBean userObject = (AmpTreeVisibilityModelBean) dmtn.getUserObject();
            userObject.setChecked(true);
            selectTree( tree, child );
        }
        
    }
    
    private void deselectTree( BaseTree tree, TreeNode node ) {
        Enumeration nodeEnum = node.children();
        while ( nodeEnum.hasMoreElements() ) {
            TreeNode child = (TreeNode)nodeEnum.nextElement();
            tree.getTreeState().selectNode( child, false );
            DefaultMutableTreeNode dmtn=(DefaultMutableTreeNode) child;
            AmpTreeVisibilityModelBean userObject = (AmpTreeVisibilityModelBean) dmtn.getUserObject();
            userObject.setChecked(false);
            deselectTree( tree, child );
        }
    }
    
    /**
     * The type of junction checkboxs and node selection checkboxs.
     * <dl>
     * <dt>Regular checkbox</dt>
     * <dd>Non-ajax checkbox, always refreshes the whole page. Works with javascript disabled.</dd>
     * <dt>Ajax checkbox</dt>
     * <dd>Links that supports partial updates. Doesn't work with javascript disabled</dd>
     * <dt>Ajax fallback checkbox</dt>
     * <dd>Link that supports partial updates. With javascript disabled acts like regular checkbox. The
     * drawback is that generated url (thus the entire html) is larger then using the other two</dd>
     * </dl>
     */
    public static final class CheckType extends EnumeratedType
    {

        /** partial updates with no fallback. */
        public static final CheckType AJAX = new CheckType("AJAX");

        /**
         * partial updates that falls back to a regular checkbox in case the client does not support
         * javascript.
         */
        public static final CheckType AJAX_FALLBACK = new CheckType("AJAX_FALLBACK");

        /**
         * non-ajax version that always re-renders the whole page.
         */
        public static final CheckType REGULAR = new CheckType("REGULAR");

        private static final long serialVersionUID = 1L;

        /**
         * Construct.
         * 
         * @param name
         *            the name of the type of the checkbox
         */
        public CheckType(String name)
        {
            super(name);
        }
    }

    /**
     * Helper class for calling an action from a checkbox.
     * 
     */
    public interface ICheckCallback extends IClusterable
    {
        /**
         * Called when the click is executed.
         * 
         * @param target
         *            The ajax request target
         */
        void onUpdate(AjaxRequestTarget target);
    }

    /**
     * Creates a checkbox of type specified by current checkType. When the checkbox is clicked it calls the
     * specified callback.
     * 
     * @param id
     *            The component id
     * @param callback
     *            The checkbox call back
     * @return The checkbox component
     */
    public MarkupContainer newCheckBox(String id, IModel model, final ICheckCallback callback)
    {
        //Object o = model.getObject();
        if (getCheckType() == CheckType.REGULAR)
        {
            return new CheckBox(id, model)
            {
                private static final long serialVersionUID = 1L;

                /**
                 * @see org.apache.wicket.markup.html.form.CheckBox#onSelectionChanged()
                 */
                public void onSelectionChanged(Object newValue)
                {
                    callback.onUpdate(null);
                }
                
                public boolean wantOnSelectionChangedNotifications() {
                    return true;
                }
            };
        }
        else if (getCheckType() == CheckType.AJAX)
        {
            return new AjaxCheckBox(id, model)
            {
                private static final long serialVersionUID = 1L;

                /**
                 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
                 */
                public void onUpdate(AjaxRequestTarget target)
                {
                    callback.onUpdate(target);
                }
                
                public boolean wantOnSelectionChangedNotifications() {
                    return false;
                }
            };
        }
        else
        {
            return new AjaxCheckBox(id, model)
            {
                private static final long serialVersionUID = 1L;
                

                /**
                 * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
                 */
                public void onUpdate(AjaxRequestTarget target)
                {
                    callback.onUpdate(target);
                }
                
                public boolean wantOnSelectionChangedNotifications() {
                    return false;
                }
            };
        }
    }
    
    public IModel newCheckBoxModel( final TreeNode tnode ) {
        return new IModel() {

            public Object getObject() {
                return getTreeState().isNodeSelected(tnode);
            }

            public void setObject(Object object) {          
            }

            public void detach() {
            }
            
        };
    }

    /**
     * Returns the current type of checkboxs on tree items.
     * 
     * @return The checkbox type
     */
    public CheckType getCheckType()
    {
        return checkType;
    }

    /**
     * Sets the type of checkboxs on tree items. After the checkbox type is changed, the whole tree is
     * rebuild and re-rendered.
     * 
     * @param checkType
     *            type of checkboxs
     */
    public void setCheckType(CheckType checkType)
    {
        if (this.checkType != checkType)
        {
            this.checkType = checkType;
            invalidateAll();
        }
    }
    
    private CheckType checkType = CheckType.AJAX;
}

