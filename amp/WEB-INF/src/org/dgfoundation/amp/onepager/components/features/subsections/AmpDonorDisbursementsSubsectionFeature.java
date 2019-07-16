/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorDisbursementsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * Displays the funding disbursements subsection
 * @author mpostelnicu@dgateway.org
 * since Nov 8, 2010
 */
public class AmpDonorDisbursementsSubsectionFeature extends
        AmpSubsectionFeatureFundingPanel<AmpFunding> {

    private IModel<AmpOrganisation> fundingOrgModel; 
    
    protected AmpDonorDisbursementsFormTableFeature disbursementsTableFeature;
    
    public AmpDonorDisbursementsFormTableFeature getDisbursementsTableFeature() {
        return disbursementsTableFeature;
    }
    
    
    @Override
    protected void onConfigure() {
        super.onConfigure();
    }


    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpDonorDisbursementsSubsectionFeature(String id,
            final IModel<AmpFunding> model, int transactionType) throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionType), model, transactionType);
        disbursementsTableFeature = new AmpDonorDisbursementsFormTableFeature("disbursementsTableFeature", model, "Disbursements Table", transactionType);
        add(disbursementsTableFeature);
        fundingOrgModel = new PropertyModel<AmpOrganisation>(model, "ampDonorOrgId");              
        AmpAjaxLinkField addDisbursement=new AmpAjaxLinkField("addDisbursement","Add Disbursement","Add Disbursement") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpFundingDetail fd= new AmpFundingDetail();
                fd.setAmpFundingId(model.getObject());
                //fd.setTransactionAmount(0d);
                fd.setReportingDate(new Date(System.currentTimeMillis()));
                fd.setUpdatedDate(new Date(System.currentTimeMillis()));
//              fd.setTransactionDate(new Date(System.currentTimeMillis()));
                fd.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                fd.setTransactionType(Constants.DISBURSEMENT);
                disbursementsTableFeature.getEditorList().addItem(fd);
                disbursementsTableFeature.getEditorList().updateModel();
                target.add(disbursementsTableFeature);
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
        addDisbursement.setAffectedByFreezing(false);
        add(addDisbursement);
    }

}
