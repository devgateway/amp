/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorCommitmentsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author mpostelnicu@dgateway.org
 * since Nov 8, 2010
 */
public class AmpDonorCommitmentsSubsectionFeature extends AmpSubsectionFeatureFundingPanel<AmpFunding> {

    protected AmpDonorCommitmentsFormTableFeature commitsTableFeature;
    
    /**
     * @param id
     * @param fmName
     * @param model
     * @param AmpFundingItemFeaturePanel
     * @throws Exception
     */
    public AmpDonorCommitmentsSubsectionFeature(String id,
            final IModel<AmpFunding> model, int transactionType) throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionType), model, transactionType);
        commitsTableFeature = new AmpDonorCommitmentsFormTableFeature("commitsTableFeature", model, "Commitments Table", transactionType);
        add(commitsTableFeature);
        
        AmpAjaxLinkField addCommit=new AmpAjaxLinkField("addCommit","Add Commitment","Add Commitment") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpFundingDetail fd= new AmpFundingDetail();
//              fd.setTransactionAmount(0d);
                fd.setReportingDate(new Date(System.currentTimeMillis()));
                fd.setUpdatedDate(new Date(System.currentTimeMillis()));
                //fd.setAdjustmentType(Constants.ACTUAL);
//              fd.setTransactionDate(new Date(System.currentTimeMillis()));
                fd.setAmpFundingId(model.getObject());
                fd.setTransactionType(Constants.COMMITMENT);
                
                fd.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                
                //model.getObject().getFundingDetails().add(fd);
                //commitsTableFeature.getList().removeAll();
                commitsTableFeature.getEditorList().addItem(fd);
                target.add(commitsTableFeature);
                
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
        addCommit.setAffectedByFreezing(false);
        add(addCommit);
    }

}
