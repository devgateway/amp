/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMUserSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMVerifiedOrganizationsTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMVerifiedUsersTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMAssignVerifiedOrgs extends AmpFeaturePanel {
    
    protected ListView<AmpOrganisation> idOrgsList, idUsersList;

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpPMAssignVerifiedOrgs(String id, String fmName) throws Exception {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMAssignVerifiedOrgs(String id, IModel<Set<AmpOrganisation>> model, String fmName)
            throws Exception {
        super(id, model, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     * @throws Exception
     */
    public AmpPMAssignVerifiedOrgs(String id, final IModel<Set<AmpOrganisation>> orgsModel, final IModel<Set<User>> usersModel, String fmName, boolean hideLabel) throws Exception {
        super(id, orgsModel, fmName, hideLabel);
        // TODO Auto-generated constructor stub
        AmpPMVerifiedOrganizationsTableFeaturePanel searchVerifiedOrgs = new AmpPMVerifiedOrganizationsTableFeaturePanel("verifiedOrgs", orgsModel, "Verified Organizations", true);
        searchVerifiedOrgs.setTableWidth(480);
        searchVerifiedOrgs.setOutputMarkupId(true);
        searchVerifiedOrgs.setVisibilityAllowed(true);
        searchVerifiedOrgs.setIgnorePermissions(true);
        add(searchVerifiedOrgs);
        
        AmpPMAjaxPagingNavigator pager = new AmpPMAjaxPagingNavigator("verifiedOrgsNavigator", (PageableListView)searchVerifiedOrgs.getList());
        add(pager);
        idOrgsList = searchVerifiedOrgs.getList();


        final AmpAutocompleteFieldPanel<AmpOrganisation> autoComplete = new AmpAutocompleteFieldPanel<AmpOrganisation>("searchVerifiedOrgs", "Search Verified Organizations",AmpOrganisationSearchModel.class) {

            @Override
            protected String getChoiceValue(AmpOrganisation choice) {
                return org.digijava.module.aim.util.DbUtil.filter(choice.getName());
            }
            
            @Override
            protected boolean showAcronyms() {
                return true;
            }
            
            @Override
            protected String getAcronym(AmpOrganisation choice) {
                return choice.getAcronym();
            }

            @Override
            public void onSelect(AjaxRequestTarget target,AmpOrganisation choice) {
                Set<AmpOrganisation> set = orgsModel.getObject();
                set.add(choice);
                idOrgsList.removeAll();
                target.add(idOrgsList.getParent());
                target.add(AmpPMAssignVerifiedOrgs.this);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpPMAssignVerifiedOrgs.this));
            }

            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                return null;
            }
        };
        AttributeModifier sizeModifier = new AttributeModifier("size",new Model(30));
        autoComplete.add(sizeModifier);
        //final AmpComboboxFieldPanel<AmpOrganisation> searchOrgs=new AmpComboboxFieldPanel<AmpOrganisation>("searchVerifiedOrgs", "Search Verified Organizations", autoComplete);
        autoComplete.getTitleLabel().add(new AttributeModifier("class",new Model("perm_search")));
        add(autoComplete);


        AmpPMVerifiedUsersTableFeaturePanel searchVerifiedUsers = new AmpPMVerifiedUsersTableFeaturePanel("verifiedUsers", usersModel, "Users", true);
        searchVerifiedUsers.setTableWidth(480);
        add(searchVerifiedUsers);
        idUsersList = searchVerifiedUsers.getList();
        

        AmpPMAjaxPagingNavigator paginator = new AmpPMAjaxPagingNavigator("verifiedUsersNavigator", (PageableListView)searchVerifiedUsers.getList());
        add(paginator);
        final AmpAutocompleteFieldPanel<User> autoCompleteUser = new AmpAutocompleteFieldPanel<User>("searchVerifiedUsers", "Search Users", AmpPMUserSearchModel.class) {

            @Override
            protected String getChoiceValue(User choice) {
                return choice.getName() +" - "+ choice.getEmail();
            }

            @Override
            public void onSelect(AjaxRequestTarget target,User choice) {
                Set<User> set = usersModel.getObject();
                set.add(choice);
                idUsersList.removeAll();
                target.add(idUsersList.getParent());
                target.add(AmpPMAssignVerifiedOrgs.this);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpPMAssignVerifiedOrgs.this));
            }

            @Override
            public Integer getChoiceLevel(User choice) {
                return null;
            }
        };
        AttributeModifier sizeModifierUser = new AttributeModifier("size",new Model(30));
        autoCompleteUser.add(sizeModifierUser);
        //final AmpComboboxFieldPanel<User> searchUsers=new AmpComboboxFieldPanel<User>("searchVerifiedUsers", "Search Users", autoCompleteUser);
        autoCompleteUser.getTitleLabel().add(new AttributeModifier("class",new Model("perm_search")));
        add(autoCompleteUser);
        
        
        add(new Link("saveOrgsToUsersButton"){
            @Override
            public void onClick() {
                try {
                    for (User user : usersModel.getObject()) {
                        PMUtil.addOrganizationsToUser(user, orgsModel.getObject());
                        DbUtil.updateUser(user);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        add(new Link("resetOrgsToUsersButton"){
            @Override
            public void onClick() {
                usersModel.getObject().clear();
                orgsModel.getObject().clear();
                idUsersList.removeAll();
                idOrgsList.removeAll();
            }
        });


        
    }

}
