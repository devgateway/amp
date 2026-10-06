package org.dgfoundation.amp.onepager.components;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.DbUtil;

import java.util.List;



public class AmpSearchOrganizationComponent<T> extends AmpComponentPanel<T>  implements IOnChangeListener{
    private static final long serialVersionUID = 1L;

    private AmpSelectFieldPanel<AmpOrgGroup> orgGroupPanel;
    private AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel;
    
    public AmpSearchOrganizationComponent(String id, IModel<T> model,
            String fmName,  final AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel, List<AmpOrgGroup> availableChoices){
        this(id, model, fmName, autocompletePanel, availableChoices, true);
    }
    
    public AmpSearchOrganizationComponent(String id, IModel<T> model,
            String fmName,  final AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel, List<AmpOrgGroup> availableChoices, boolean isLabelVisible ) {
        super(id, model, fmName);
        TrnLabel title = new TrnLabel("title", fmName);
        title.setVisible(isLabelVisible);
        add(title);
        
        ChoiceRenderer cr = new ChoiceRenderer(){
            @Override
            public Object getDisplayValue(Object object) {
                if(object == null)
                    return TranslatorUtil.getTranslatedText("All Groups");
                AmpOrgGroup orgGroup = (AmpOrgGroup)object;
                return orgGroup.getOrgGrpName();
            }
        };

        IModel<List<? extends AmpOrgGroup>> orgGroupsModel = Model.ofList(availableChoices == null ? DbUtil.getAllVisibleOrgGroups() : availableChoices);
        orgGroupPanel = new AmpSelectFieldPanel<AmpOrgGroup>("selectOrgType", new Model<AmpOrgGroup>(),  orgGroupsModel, "Select Organization Type", true, true, cr, true);
        orgGroupPanel.getChoiceContainer().setChoices(orgGroupsModel);
        orgGroupPanel.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AmpOrgGroup org_group = (AmpOrgGroup) orgGroupPanel.getChoiceContainer().getModelObject();
                if(org_group != null)                       
                    autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.GROUP_FILTER, org_group);
                else
                    autocompletePanel.getModelParams().remove(AmpOrganisationSearchModel.PARAM.GROUP_FILTER);
                target.add(autocompletePanel);
            }

        });
        orgGroupPanel.getChoiceContainer().add(new AttributeAppender("style", true, new Model<String>("width: 100px; height: 22px; margin-top: 1px;"), ";"));
        orgGroupPanel.getChoiceContainer().add(new AttributeModifier("title", new Model<String>(TranslatorUtil.getTranslatedText("Choose an organisation group to filter by"))));
        add(orgGroupPanel);
        
        
        orgGroupPanel.setOutputMarkupId(true);
        this.autocompletePanel = autocompletePanel;
        autocompletePanel.getTextField().add(new AttributeModifier("title", new Model<String>(TranslatorUtil.getTranslatedText("Type an organisation name to filter by"))));
        add(autocompletePanel);
    }
    
    @Override
    public void onSelectionChanged() {
        Long id =  ((AmpOrgGroup)orgGroupPanel.getChoiceContainer().getModelObject()).getAmpOrgGrpId();
        autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.GROUP_FILTER,id);
    }
    
    public void setDefaultOrgGroup (AmpOrgGroup value) {
        orgGroupPanel.getChoiceContainer().setDefaultModelObject(value);
        autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.GROUP_FILTER,value);
    }

    public void setTemplateFilter(AmpTemplatesVisibility template) {
        autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.TEMPLATE_FILTER, template);
    }
    
    /**
     * sets the OrgType which will be used for filtering the displayed orgs / orgGroups
     * @param orgType
     */
    public void setFilteringOrgType(AmpOrgType orgType){
        if (orgType != null)
            autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.TYPE_FILTER, orgType);
        else
            autocompletePanel.getModelParams().remove(AmpOrganisationSearchModel.PARAM.TYPE_FILTER);
    }

}
