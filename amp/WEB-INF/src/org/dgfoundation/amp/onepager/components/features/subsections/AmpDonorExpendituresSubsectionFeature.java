/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorCommitmentsFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorExpendituresFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
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
public class AmpDonorExpendituresSubsectionFeature extends
        AmpSubsectionFeatureFundingPanel<AmpFunding> {

    protected AmpDonorExpendituresFormTableFeature expTableFeature;
    
    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpDonorExpendituresSubsectionFeature(String id,
            final IModel<AmpFunding> model, int transactionType) throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionType), model, transactionType);
        expTableFeature = new AmpDonorExpendituresFormTableFeature("expTableFeature", model, "Expenditures Table", transactionType);
        add(expTableFeature);

        AmpAjaxLinkField addExp=new AmpAjaxLinkField("addExp","Add Expenditure","Add Expenditure") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpFundingDetail fd= new AmpFundingDetail();
                fd.setAmpFundingId(model.getObject());
                fd.setReportingDate(new Date(System.currentTimeMillis()));
                fd.setUpdatedDate(new Date(System.currentTimeMillis()));
                fd.setTransactionType(Constants.EXPENDITURE);
                fd.setAmpCurrencyId(CurrencyUtil.getWicketWorkspaceCurrency());
                expTableFeature.getEditorList().addItem(fd);
                target.add(expTableFeature);
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
        addExp.setAffectedByFreezing(false);
        add(addExp);
    }

}
