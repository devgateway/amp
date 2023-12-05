/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMUserSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageUsersTableFeaturePanel;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersSectionFeature extends AmpPMSectionFeaturePanel {

    protected ListView<User> idsList;
    //protected boolean visible = true ;
    
    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageUsersSectionFeature(String id, String fmName, final IModel<Set<User>> usersModel)
            throws Exception {
        super(id, fmName);
        
        Set<AmpOrganisation> s = new TreeSet<AmpOrganisation>();
        final IModel<Set<AmpOrganisation>> allOrgsModel = new Model((Serializable)s);
        Set<User> t = new TreeSet<User>();
        final IModel<Set<User>> allUsersModel = new Model((Serializable)t);

        final AmpPMAssignVerifiedOrgs usersOrgs = new AmpPMAssignVerifiedOrgs("assignMultiUsersMultiOrgs",allOrgsModel, allUsersModel, "Assign Verified Organizations to Users", false);
        usersOrgs.getLabelContainer().add(new AttributeModifier("class",new Model("perm_h3")));
        usersOrgs.setIgnorePermissions(true);
        usersOrgs.setVisibilityAllowed(true);
        usersOrgs.setIgnoreFmVisibility(true);
        usersOrgs.setOutputMarkupId(true);
        usersOrgs.setVisible(false);
        add(usersOrgs);
        
        final AmpPMManageUsersTableFeaturePanel usersTable = new AmpPMManageUsersTableFeaturePanel("users", usersModel, "Users");
        usersTable.setVisibilityAllowed(true);
        usersTable.setIgnorePermissions(true);
        usersTable.setIgnoreFmVisibility(true);
        usersTable.setOutputMarkupId(true);
        usersTable.setVisible(true);
        add(usersTable);
        
        final AmpPMAjaxPagingNavigator paginator = new AmpPMAjaxPagingNavigator("navigator", (PageableListView)usersTable.getList());
        paginator.setVisibilityAllowed(true);
        paginator.setVisible(true);
        add(paginator);
        idsList = usersTable.getList();
        
        final AmpAutocompleteFieldPanel<User> searchUsers = new AmpAutocompleteFieldPanel<User>("searchUsers", "Search Users", AmpPMUserSearchModel.class) {
            @Override
            public void onSelect(AjaxRequestTarget target, User choice) {
                Set<User> set = usersModel.getObject();
                set.clear();
                set.add(choice);
                idsList.removeAll();
                usersTable.getSliders().clear();
                target.add(AmpPMManageUsersSectionFeature.this);
                target.appendJavaScript(OnePagerUtil.getToggleJS(AmpPMManageUsersSectionFeature.this.getSliderPM()));
            }
            
            @Override
            protected String getChoiceValue(User choice)  {
                return choice.getName()+" - "+ choice.getEmail();
            }
            
            @Override
            public Integer getChoiceLevel(User choice) {
                return null;
            }
        };
        AttributeModifier sizeModifier = new AttributeModifier("size",new Model(100));
        searchUsers.getTextField().add(sizeModifier);
        searchUsers.setVisibilityAllowed(true);
        searchUsers.setIgnorePermissions(true);
        searchUsers.setIgnoreFmVisibility(true);
        add(searchUsers);
        
        AmpAjaxLinkField addVerifiedOrgsBtn = new AmpAjaxLinkField("addOrgsToUsers","Add Verified Organizations Button","Add Verified Organizations"){

            @Override
            public void onClick(AjaxRequestTarget target) {
                //visible = !visible;
                usersTable.setVisible(!usersTable.isVisible());
                paginator.setVisible(!paginator.isVisible());
                searchUsers.setVisible(!searchUsers.isVisible());
                usersOrgs.setVisible(!usersOrgs.isVisible());
                target.add(AmpPMManageUsersSectionFeature.this);
                target.appendJavaScript(OnePagerUtil.getToggleJS(AmpPMManageUsersSectionFeature.this.getSliderPM()));
            }
            
        };
        addVerifiedOrgsBtn.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        addVerifiedOrgsBtn.setVisibilityAllowed(true);
        add(addVerifiedOrgsBtn);
        
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     * @throws Exception
     */
    public AmpPMManageUsersSectionFeature(String id, IModel model,
            String fmName, boolean hideLabel) throws Exception {
        super(id, model, fmName, hideLabel);
        // TODO Auto-generated constructor stub
    }

}
