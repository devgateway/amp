/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.TreeModel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMObjectVisibilitySearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * @author dan
 *
 */
public class AmpPMAssignFieldPermissionComponentPanel extends AmpComponentPanel {

    private Boolean showWorkspace=true;

    
    /**
     * @param id
     * @param model
     * @param fmName
     * @param teamsModel 
     * @param ampTreeVisibilityModel2 
     */
    public AmpPMAssignFieldPermissionComponentPanel(String id,final IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityBeanModel, String fmName, IModel<Set<AmpTeam>> teamsModel, final IModel<AmpTreeVisibility> ampTreeVisibilityModel) {
        super(id, ampTreeVisibilityBeanModel, fmName, AmpFMTypes.MODULE);
        // TODO Auto-generated constructor stub
        
        final Form form = new Form("ampFieldPMForm")
        {
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                //System.out.println("ampFieldPMForm submitted");
            }
        };
        form.setOutputMarkupId(true);
        
        this.showWorkspace=true;
        final TransparentWebMarkupContainer workspaces = new TransparentWebMarkupContainer("workspaces");
        workspaces.setOutputMarkupId(true);
        form.add(workspaces);
        
        List<String> permissionPriorityList = PMUtil.getPermissionPriority();
        final IModel<String> permissionChoiceModel = new Model(permissionPriorityList.get(0));
        
        //permission priority radiobutton
        final RadioChoice permissionPriorityChoices = new RadioChoice("permissionPriorityChoices", permissionChoiceModel,   permissionPriorityList, new ChoiceRenderer(){           
            @Override
            public Object getDisplayValue(Object object) {
                return TranslatorUtil.getTranslation(object.toString());
            }
        });
        //permissionPriorityChoices = new RadioChoice("permissionPriorityChoices", );
        permissionPriorityChoices.setSuffix("");
        form.add(permissionPriorityChoices);
        
        final AmpLabelFieldPanel<String> permPriorityLabel = new AmpLabelFieldPanel<String>("permPriorityLabel", new Model("Permission Priority"), "Permission Priority", true);
        form.add(permPriorityLabel);
        
        //show workspace checkbox
        AjaxCheckBox showWorkspaceCheckBox =    new AjaxCheckBox("showWorkspace", new Model(this.showWorkspace)){
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // TODO Auto-generated method stub
                setShowWorkspaces(!getShowWorkspaces());
                workspaces.setVisible(getShowWorkspaces());
                permPriorityLabel.setVisible(getShowWorkspaces());
                permissionPriorityChoices.setVisible(getShowWorkspaces());
                target.add(AmpPMAssignFieldPermissionComponentPanel.this);
            }
            
        };
        showWorkspaceCheckBox.setOutputMarkupId(true);
        form.add(showWorkspaceCheckBox);

        //FM tree
        TreeModel treeModel = PMUtil.createTreeModel(ampTreeVisibilityBeanModel);
        final IModel<TreeModel> iTreeModel = new Model((Serializable) treeModel);
        final AmpPMTreeVisibilityFieldPermission tree = new AmpPMTreeVisibilityFieldPermission("fmFieldsPanel", iTreeModel, "FM Fields Panel");
        tree.setOutputMarkupId(true);
        form.add(tree);
        
        //search text box
        final AmpAutocompleteFieldPanel<AmpObjectVisibility> autoComplete = new AmpAutocompleteFieldPanel<AmpObjectVisibility>("searchFields", "Search Fields", AmpPMObjectVisibilitySearchModel.class) {
            @Override
            protected String getChoiceValue(AmpObjectVisibility choice) {
                return DbUtil.filter(choice.getName());
            }
            @Override
            public void onSelect(AjaxRequestTarget target, AmpObjectVisibility choice) {
                if(choice != null) {
                    ampTreeVisibilityBeanModel.setObject(PMUtil.buildTreeObjectFMPermissions(choice));
                    iTreeModel.setObject(PMUtil.createTreeModel(ampTreeVisibilityBeanModel));
                    tree.refreshTree(iTreeModel);
                    target.add(AmpPMAssignFieldPermissionComponentPanel.this);
                }
            }
            @Override
            public Integer getChoiceLevel(AmpObjectVisibility choice) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        AttributeModifier sizeModifier = new AttributeModifier("size",new Model(43));
        autoComplete.add(sizeModifier);
        //final AmpComboboxFieldPanel<AmpObjectVisibility> searchFields=new AmpComboboxFieldPanel<AmpObjectVisibility>("searchFields", "Search Fields", autoComplete,false,true);
        form.add(autoComplete);
        
        
        PermissionMap permMap = PMUtil.createPermissionMap(AmpFieldsVisibility.class, true);
        IModel<PermissionMap> permMapModel = new Model(permMap);
        TreeSet<AmpPMReadEditWrapper> gatesSet = new TreeSet<AmpPMReadEditWrapper>();
        PMUtil.generateGatesList((CompositePermission)permMapModel.getObject().getPermission(),gatesSet);
        final IModel<Set<AmpPMReadEditWrapper>> gatesSetModel = new Model((Serializable) gatesSet);
        final AmpPMAddPermFormTableFeaturePanel permGatesFieldsFormTable = new AmpPMAddPermFormTableFeaturePanel("permGatesFieldsForm", gatesSetModel, "Gate Form Table", true);
        permGatesFieldsFormTable.setTableWidth(470);
        permGatesFieldsFormTable.setIgnorePermissions(true);
        permGatesFieldsFormTable.setEnabled(true);
        permGatesFieldsFormTable.setOutputMarkupId(true);
        form.add(permGatesFieldsFormTable);
        
        TreeSet<AmpPMReadEditWrapper> workspacesSet = new TreeSet<AmpPMReadEditWrapper>();
        PMUtil.generateWorkspacesList(teamsModel, workspacesSet);
        final IModel<Set<AmpPMReadEditWrapper>> workspacesSetModel = new Model((Serializable) workspacesSet);
        final AmpPMAddPermFormTableFeaturePanel permWorkspacesFieldsFormTable = new AmpPMAddPermFormTableFeaturePanel("permWorkspacesFieldsForm", workspacesSetModel, "Workspaces Form Table", true);
        permWorkspacesFieldsFormTable.setTableWidth(470);
        permWorkspacesFieldsFormTable.setIgnorePermissions(true);
        permWorkspacesFieldsFormTable.setOutputMarkupId(true);
        permWorkspacesFieldsFormTable.setEnabled(false); 
        form.add(permWorkspacesFieldsFormTable);
        
        PMUtil.setPermissionPriorityVisibility(permissionChoiceModel, permGatesFieldsFormTable, permWorkspacesFieldsFormTable);
        
        
        permissionPriorityChoices.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
              PMUtil.setPermissionPriorityVisibility(permissionChoiceModel, permGatesFieldsFormTable, permWorkspacesFieldsFormTable);
              target.add(AmpPMAssignFieldPermissionComponentPanel.this);
            }

          }); 
        AmpAjaxLinkField resetFieldPermBtn = new AmpAjaxLinkField("resetFieldPermissionButton","Reset Field Permission Button","Reset"){
            //@Override
            public void onClick(AjaxRequestTarget target) {
                form.clearInput();
                target.add(AmpPMAssignFieldPermissionComponentPanel.this);
            }
        };
        resetFieldPermBtn.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        form.add(resetFieldPermBtn);
        
        
        AmpButtonField saveAndSubmit = new AmpButtonField("saveFieldPermissionButton", "Save Field Permission Button","Save", true, true){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                    //System.out.println("saveFieldPermissionButton  submit pressed");
                    if(PMUtil.ROLE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0)
                        PMUtil.assignFieldsPermission(iTreeModel, gatesSetModel, null);
                    if(PMUtil.CUMMULATIVE.compareTo(permissionChoiceModel.getObject()) == 0)
                        PMUtil.assignFieldsPermission(iTreeModel, gatesSetModel, workspacesSetModel);
                    if(PMUtil.WORKSPACE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0)
                        PMUtil.assignFieldsPermission(iTreeModel, null, workspacesSetModel);
                    
                    //System.out.println("PM field permission assigned");
            }
        };
        saveAndSubmit.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        form.add(saveAndSubmit);
        
        add(form);
    }

    public Boolean getShowWorkspaces(){
        return this.showWorkspace;
    }
    
    
    public void setShowWorkspaces(Boolean workspace){
        this.showWorkspace=workspace;
    }
    
    /**
     * @param id
     * @param fmName
     * @param fmType
     */
    public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName,AmpFMTypes fmType) {
        super(id, fmName, fmType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param fmBehavior
     */
    public AmpPMAssignFieldPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
        // TODO Auto-generated constructor stub
    }


}
