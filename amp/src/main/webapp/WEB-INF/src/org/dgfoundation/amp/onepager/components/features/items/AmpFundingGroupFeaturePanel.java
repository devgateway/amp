/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author aartimon@dginternational.org since Jul 4, 2012
 */
public class AmpFundingGroupFeaturePanel extends AmpFeaturePanel<AmpOrganisation> {
    private static final long serialVersionUID = 1L;
    private ListEditor<AmpFunding> list;
    private IModel<AmpOrganisation> fundingOrgModel;
    private IModel<AmpRole> fundingRoleModel;
    private Integer tabIndex;

    
    public ListEditor<AmpFunding> getList() {
        return list;
    }
    
    public Integer getMaxFundingItemIndexFromList() {
        Integer max = null;
        for (AmpFunding af : list.items) {
            if (max == null)
                max = af.getIndex();
            if (max < af.getIndex()) 
                max = af.getIndex();
        }
        return max;
    }


    @Override
    protected void onConfigure() {
        super.onConfigure();

    }

    public AmpFundingGroupFeaturePanel(String id, String fmName, final IModel<AmpRole> role, 
            IModel<Set<AmpFunding>> fundsModel, final IModel<AmpOrganisation> model,final IModel<AmpActivityVersion> am, final AmpDonorFundingFormSectionFeature parent) {
        super(id, model, fmName, true);
        fundingOrgModel = model;
        fundingRoleModel = role;
        String translatedRole = TranslatorWorker.translateText(role.getObject().getName());
        String suffix = role.getObject() == null ? "" : " (" + translatedRole + ")";
        add(new Label("donorOrg", model.getObject().getName() + suffix));
        AbstractMixedSetModel<AmpFunding> setModel = new AbstractMixedSetModel<AmpFunding>(fundsModel) {
            @Override
            public boolean condition(AmpFunding item) {
                return item.getAmpDonorOrgId().getAmpOrgId().equals(model.getObject().getAmpOrgId())
                        && item.getSourceRole().getAmpRoleId().equals(role.getObject().getAmpRoleId());
            }
        };
        
        list = new ListEditor<AmpFunding>("listFunding", setModel) {
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpFunding> item) {
                AmpFundingItemFeaturePanel fundingItemFeature;
                try {
                    
                    fundingItemFeature = new AmpFundingItemFeaturePanel(
                            "fundingItem", "Funding Item",
                            item.getModel(), am, parent,item.getIndex());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(fundingItemFeature);
            }
        };
        add(list);
        
        final Boolean isTabView=FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN);
        
        AmpAjaxLinkField addNewFunding= new AmpAjaxLinkField("addAnotherFunding","New Funding Item","New Funding Item") {           
            private static final long serialVersionUID = 1L;

            @Override
            protected void onClick(AjaxRequestTarget target) {
                if (fundsModel.getObject().size() > 0) {
                    AmpFunding funding = new AmpFunding();
                    funding.setAmpDonorOrgId(model.getObject());
                    funding.setSourceRole(role.getObject());
                    
                    parent.addFundingItem(funding);
                    target.add(parent);
                    target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
                    if (isTabView) {
                        int index = parent.calculateTabIndex(funding.getAmpDonorOrgId(),
                                funding.getSourceRole());
                        
                        target.appendJavaScript("switchTabs("+ index +");");
                    }
                }
            }
        };
        
        add(addNewFunding);
    }
    
    public IModel<AmpRole> getRole() {
        return fundingRoleModel;
    }

    public void setTabIndex(Integer index) {
        this.tabIndex = index;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }
    
}
