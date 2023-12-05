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
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMWorkspaceSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageWorkspacesTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesSectionFeature extends AmpPMSectionFeaturePanel {

    protected ListView<AmpTeam> idsList;
    
    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageWorkspacesSectionFeature(String id, String fmName) throws Exception {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageWorkspacesSectionFeature(String id, IModel model, String fmName) throws Exception {
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
    public AmpPMManageWorkspacesSectionFeature(String id, final IModel<Set<AmpTeam>> workspacesModel,   String fmName, boolean hideLabel) throws Exception {
        super(id, workspacesModel, fmName, hideLabel);
        
        final AmpPMManageWorkspacesTableFeaturePanel workspacesTable = new AmpPMManageWorkspacesTableFeaturePanel("workspaces", workspacesModel, "Workspaces", false);
        add(workspacesTable);
        
        PageableListView<AmpTeam> pageList= (PageableListView)workspacesTable.getList();
        pageList.setItemsPerPage(10);
        AmpPMAjaxPagingNavigator pager = new AmpPMAjaxPagingNavigator("workspacesNavigator", pageList);
        add(pager);
        
        idsList = workspacesTable.getList();
        final AmpAutocompleteFieldPanel<AmpTeam> autoComplete = new AmpAutocompleteFieldPanel<AmpTeam>("searchWorkspaces", "Search Workspaces",AmpPMWorkspaceSearchModel.class) {

            /**
             * 
             */
            private static final long serialVersionUID = -4113089893479844312L;

            @Override
            protected String getChoiceValue(AmpTeam choice){
                return choice.getName();
            }
            
            @Override
            public void onSelect(AjaxRequestTarget target, AmpTeam choice) {
                Set<AmpTeam> set = workspacesModel.getObject();
                set.clear();
                set.add(choice);
                idsList.removeAll();
                //workspacesTable.getSliders().clear();
                target.add(AmpPMManageWorkspacesSectionFeature.this);
                target.appendJavaScript(OnePagerUtil.getToggleJS(AmpPMManageWorkspacesSectionFeature.this.getSliderPM()));
            }

            @Override
            public Integer getChoiceLevel(AmpTeam choice) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
        autoComplete.add(sizeModifier);
        //final AmpComboboxFieldPanel<AmpTeam> searchContacts=new AmpComboboxFieldPanel<AmpTeam>("searchWorkspaces", "Search Workspaces", autoComplete,false,true);
        add(autoComplete);      
        
    }
    
}
