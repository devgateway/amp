/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author dan
 *
 */
public class AmpPMOrganizationsUsersTableFeaturePanel extends AmpFormTableFeaturePanel {



    public AmpPMOrganizationsUsersTableFeaturePanel(String id, final IModel<User> userModel, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, userModel, fmName, hideLeadingNewLine);
        final PropertyModel<Set<AmpOrganisation>> setModel=new PropertyModel<Set<AmpOrganisation>>(userModel,"assignedOrgs");
        
        final AbstractReadOnlyModel<List<AmpOrganisation>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);
        
        list = new PageableListView<AmpOrganisation>("orgsList", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpOrganisation> item) {
                final MarkupContainer listParent=this.getParent();
                item.add(new Label("orgName", item.getModelObject().getName()));
                item.add(new Label("orgAcronym", item.getModelObject().getAcronym()));
                
                final AmpDeleteLinkField propertyDeleteLink = new AmpDeleteLinkField("removeVerifiedOrganization", "Remove Verified Organization Link") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setModel.getObject().remove(item.getModelObject());
                        try {
                            DbUtil.updateUser(userModel.getObject());
                        } catch (AdminException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        list.removeAll();
                        target.add(AmpPMOrganizationsUsersTableFeaturePanel.this.getParent());
                    }
                };
                item.add(propertyDeleteLink);
                
                
            }
        };
        list.setReuseItems(true);
        add(list);
    }

    public AmpPMOrganizationsUsersTableFeaturePanel(String id, IModel<User> model, String fmName) throws Exception {
        super(id, model, fmName);
        
    }

    

}
