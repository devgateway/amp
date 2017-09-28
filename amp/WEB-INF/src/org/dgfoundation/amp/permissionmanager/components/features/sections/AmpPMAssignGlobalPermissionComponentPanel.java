/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMPermissibleCategoryChoiceRenderer;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * @author dan
 *
 */
public class AmpPMAssignGlobalPermissionComponentPanel extends  AmpComponentPanel {


    public AmpPMAssignGlobalPermissionComponentPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, IModel<Set<AmpTeam>> tm, String fmName) {
        super(id, globalPermissionsModel, fmName, AmpFMTypes.MODULE);

        List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
        final IModel<Class> globalPermissibleClassModel=new Model(availablePermissibleCategories.get(0));
        
        final Form form = new Form("ampGlobalPMForm")
        {
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                //System.out.println("ampGlobalPMForm submitted");
            }
        };
        form.setOutputMarkupId(true);

        //displaying information about permission
        final IModel<String> infoGlobalPermModel = new Model<String>(" ");
        Label infoGlobalPermLabel = new Label("infoGlobalPerm",infoGlobalPermModel);
        infoGlobalPermLabel.setOutputMarkupId(true);
        form.add(infoGlobalPermLabel);
        
        //gates list 
        Set<AmpPMReadEditWrapper> gatesSet = null;
        gatesSet            = new TreeSet<AmpPMReadEditWrapper>();
        populateGatesSet(gatesSet, globalPermissibleClassModel, infoGlobalPermModel);
        
        final IModel<Set<AmpPMReadEditWrapper>> gatesSetModel = new Model((Serializable) gatesSet);
        final AmpPMAddPermFormTableFeaturePanel permGatesFormTable = new AmpPMAddPermFormTableFeaturePanel("gatePermForm", gatesSetModel, "Permission Form Table", true);
        permGatesFormTable.setTableWidth(300);
        permGatesFormTable.setOutputMarkupId(true);
        form.add(permGatesFormTable);
        
        //drop down with global permission categories
        DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissibleClassModel ,availablePermissibleCategories, new AmpPMPermissibleCategoryChoiceRenderer("simpleName"));
        dropDownPermCategories.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Set<AmpPMReadEditWrapper> gatesSetAux = new TreeSet<AmpPMReadEditWrapper>();
                populateGatesSet(gatesSetAux, globalPermissibleClassModel, infoGlobalPermModel);
                gatesSetModel.setObject(gatesSetAux);
                target.add(AmpPMAssignGlobalPermissionComponentPanel.this);
            }
        });
        form.add(dropDownPermCategories);

        AmpAjaxLinkField resetGlobalPerm = new AmpAjaxLinkField("resetGlobalPermissionButton","Reset Global Permission Button","Reset"){
            //@Override
            public void onClick(AjaxRequestTarget target) {
                form.clearInput();
                target.add(AmpPMAssignGlobalPermissionComponentPanel.this);
            }
        };
        resetGlobalPerm.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        form.add(resetGlobalPerm);

        
        TreeSet<AmpPMReadEditWrapper> workspacesSet = new TreeSet<AmpPMReadEditWrapper>();
        PMUtil.generateWorkspacesList(tm, workspacesSet);
        final IModel<Set<AmpPMReadEditWrapper>> workspacesSetModel = new Model((Serializable) workspacesSet);
        final AmpPMAddPermFormTableFeaturePanel permWorkspacesFieldsFormTable = new AmpPMAddPermFormTableFeaturePanel("permWorkspacesFieldsForm", workspacesSetModel, "Workspaces Form Table", true);
        permWorkspacesFieldsFormTable.setTableWidth(470);
        permWorkspacesFieldsFormTable.setIgnorePermissions(true);
        permWorkspacesFieldsFormTable.setOutputMarkupId(true);
        permWorkspacesFieldsFormTable.setEnabled(true); 
        form.add(permWorkspacesFieldsFormTable);
        
        AmpButtonField saveAndSubmit = new AmpButtonField("saveGlobalPermissionButton", "Save Global Permission", "Save", true, true){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                PMUtil.assignGlobalPermission(gatesSetModel.getObject(),workspacesSetModel.getObject(), globalPermissibleClassModel.getObject());
                infoGlobalPermModel.setObject("Permission saved");
            }
        };
        saveAndSubmit.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        form.add(saveAndSubmit);
        
        add(form);
    }

    private void populateGatesSet(Set<AmpPMReadEditWrapper> gatesSet, IModel<Class> globalPermissionMapForPermissibleClassModel, IModel<String> infoGlobalPermModel) {
        
        PermissionMap pm = PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject(), null);
        
        if(pm == null || pm.getPermission()==null){
            //there is no permission for current selected class model
            infoGlobalPermModel.setObject("There is no permission assigned to this category");
            PMUtil.generateDefaultGatesList(gatesSet);
        }
        else 
            if (!(pm.getPermission() instanceof CompositePermission)){
                //permission is not Composite type
                infoGlobalPermModel.setObject("Permission assigned can not be displayed in this form. Please use advanced Permission Manager to view it");
                PMUtil.generateDefaultGatesList(gatesSet);
            }
            else {
                infoGlobalPermModel.setObject(" ");
                PMUtil.generateGatesList((CompositePermission)pm.getPermission(),gatesSet);
            }
    }
/*
    private Set<AmpPMReadEditWrapper> populateGatesSet(final IModel<Class> globalPermissionMapForPermissibleClassModel,final IModel<PermissionMap> pmAuxModel, IModel<String> infoGlobalPermModel) throws Exception{
        Set<AmpPMReadEditWrapper> gatesSet = new TreeSet<AmpPMReadEditWrapper>();
        PermissionMap pmAux = null;
        pmAux   =   PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject(), null);
        //0 is ok, 1 permission map doesn't exist, 2 permission map contains a GatePermission or other type different to CompositePermission
        int flag = 0; 
        if(pmAux==null || pmAux.getPermission()==null){
            pmAux = PMUtil.createPermissionMap(globalPermissionMapForPermissibleClassModel.getObject(), true);
            flag =  1;
        }
        if(!(pmAux.getPermission() instanceof CompositePermission)){
            pmAux.setPermission(PMUtil.createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
                    "This permission was created using the PM UI by admin user",false));
            flag =  2;
        }
        switch (flag) {
        case 0: infoGlobalPermModel.setObject(" ");break;
        case 1: infoGlobalPermModel.setObject("There is no permission assigned to this category");break;
        case 2: infoGlobalPermModel.setObject("Permission assigned can not be displayed in this form. Please use advanced Permission Manager to view it");break;
        }
        pmAuxModel.setObject(pmAux);
        PMUtil.generateGatesList((CompositePermission)pmAuxModel.getObject().getPermission(),gatesSet);
        return gatesSet;
    }
*/
    public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName, AmpFMTypes fmType) {
        super(id, fmName, fmType);
    }

    public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName) {
        super(id, fmName);
    }

    public AmpPMAssignGlobalPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
    }

    
    

    


}
