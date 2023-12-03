/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

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
import org.digijava.kernel.user.User;

import java.util.List;
import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMVerifiedUsersTableFeaturePanel extends AmpFormTableFeaturePanel {

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMVerifiedUsersTableFeaturePanel(String id, IModel model, String fmName) throws Exception {
        super(id, model, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param usersSetModel
     * @param fmName
     * @param hideLeadingNewLine
     * @throws Exception
     */
    public AmpPMVerifiedUsersTableFeaturePanel(String id,final IModel<Set<User>> usersSetModel, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, usersSetModel, fmName, hideLeadingNewLine);

        final AbstractReadOnlyModel<List<User>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(usersSetModel);
        
        list = new PageableListView<User>("verifiedUsersList", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<User> item) {
                final MarkupContainer listParent=this.getParent();
                item.add(new Label("userLabel", item.getModelObject().getName()));
                item.add(new Label("userEmailLabel", item.getModelObject().getEmail()));
                
                final AmpDeleteLinkField propertyDeleteLink = new AmpDeleteLinkField("removeSelectedUser", "Remove Selected User Link") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        usersSetModel.getObject().remove(item.getModelObject());
                        list.removeAll();
                        target.add(AmpPMVerifiedUsersTableFeaturePanel.this.getParent());
                    }
                };
                item.add(propertyDeleteLink);
                
            }
        };
        list.setReuseItems(true);
        add(list);
        
    
    }

}
