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
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorArrearsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author acartaleanu@dgateway.org
 * since Apr 10, 2016
 */
public class AmpDonorArrearsSubsectionFeature extends AmpSubsectionFeatureFundingPanel<AmpFunding> {

    protected AmpDonorArrearsFormTableFeature arrearsTableFeature;
    
    /**
     * @param id
     * @param fmName
     * @param model
     * @param ampFundingItemFeaturePanel 
     * @throws Exception
     */
    public AmpDonorArrearsSubsectionFeature(String id,
            final IModel<AmpFunding> model, int transactionType) throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionType), model, transactionType);
        arrearsTableFeature = new AmpDonorArrearsFormTableFeature("arrearsTableFeature", model, "Arrears Table", transactionType);
        add(arrearsTableFeature);
        AmpAjaxLinkField addArrears = new AmpAjaxLinkField("addArrears","Add Arrears Transaction","Add Arrears Transaction") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpFundingDetail fd= new AmpFundingDetail();
                fd.setReportingDate(new Date(System.currentTimeMillis()));
                fd.setUpdatedDate(new Date(System.currentTimeMillis())); 
                fd.setAmpFundingId(model.getObject());
                fd.setTransactionType(Constants.ARREARS);
                fd.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                
                arrearsTableFeature.getEditorList().addItem(fd);
                target.add(arrearsTableFeature);
                
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
        addArrears.setAffectedByFreezing(false);
        add(addArrears);
    }

}
