/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.gateperm.core.CompositePermission;

/**
 * @author dan
 *
 */
public class AmpPMAddPermissionFormFeaturePanel extends AmpComponentPanel {

    /**
     * @param id
     * @param fmName
     * @param fmType
     */
    public AmpPMAddPermissionFormFeaturePanel(String id, String fmName, AmpFMTypes fmType) {
        super(id, fmName, fmType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpPMAddPermissionFormFeaturePanel(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
        
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param fmBehavior
     */
    public AmpPMAddPermissionFormFeaturePanel(String id, IModel model,  String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception 
     */
    public AmpPMAddPermissionFormFeaturePanel(String id, final IModel<CompositePermission> cpModel, String fmName, String permissionActionTitle, boolean isEdit) {
        super(id, cpModel, fmName);
        
        final Form addPermForm = new Form ("addPermissionForm")
        {
            protected void onSubmit() {
                //System.out.println("addPermissionForm submitted");
            }
        };
        addPermForm.setOutputMarkupId(true);
        
        addPermForm.add(new Label("permissionActionTitle", permissionActionTitle));
        
        AmpTextFieldPanel permName = new AmpTextFieldPanel("permissionName", new PropertyModel(cpModel,"name"), "Permission Name", false, true);
        addPermForm.add(permName);
    
        AmpAjaxLinkField resetPermBtn = new AmpAjaxLinkField("resetPermissionButton","Reset Permission Button","Reset Permission"){
            //@Override
            public void onClick(AjaxRequestTarget target) {
                cpModel.setObject(null);
                addPermForm.clearInput();
                target.add(AmpPMAddPermissionFormFeaturePanel.this);
            }
        };
        addPermForm.add(resetPermBtn);
        resetPermBtn.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        
        AmpButtonField  saveAndSubmit = new AmpButtonField("savePermissionButton", "Save Permission Button", true, true) {
            
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                // TODO Auto-generated method stub
                //System.out.println("savePermissionButton  submit pressed");
                cpModel.setObject(null);
                addPermForm.clearInput();
                //System.out.println("PM new permission created");
            }
        };
        saveAndSubmit.getButton().add(new AttributeModifier("class", new Model("buttonx")));
        addPermForm.add(saveAndSubmit);
        
        //adding the form
        add(addPermForm);
    }

}
