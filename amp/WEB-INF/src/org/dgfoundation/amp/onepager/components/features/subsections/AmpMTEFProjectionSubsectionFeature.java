/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMTEFProjectionFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class AmpMTEFProjectionSubsectionFeature extends AmpSubsectionFeatureFundingPanel<AmpFunding> {

    protected AmpMTEFProjectionFormTableFeature mtefTableFeature;

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpMTEFProjectionSubsectionFeature(String id, final IModel<AmpFunding> model) throws Exception {
        super(id, AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(Constants.MTEFPROJECTION), model,
                Constants.MTEFPROJECTION);
        
        mtefTableFeature = new AmpMTEFProjectionFormTableFeature("mtefTableFeature", "MTEF Projections Table", model);
        add(mtefTableFeature);
        
        final IModel<Set<AmpFundingMTEFProjection>> setModel = new PropertyModel<Set<AmpFundingMTEFProjection>>(model, "mtefProjections");
        
        AmpAjaxLinkField addMTEF=new AmpAjaxLinkField("addMTEF","Add Projection","Add Projection") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpFundingMTEFProjection projection= new AmpFundingMTEFProjection();
                projection.setAmpFunding(model.getObject());
                //projection.setAmount(0d);
//              projection.setProjectionDate(new Date(System.currentTimeMillis()));

                Calendar calendar = Calendar.getInstance();

                int currentYear = Util.getCurrentFiscalYear();

                Set<AmpFundingMTEFProjection> mtefSet = setModel.getObject();
                if (mtefSet != null) {
                    for(AmpFundingMTEFProjection mtefItem : mtefSet) {
                        calendar.setTime(mtefItem.getProjectionDate());
                        int mtefItemYear = calendar.get(Calendar.YEAR);
                        if (mtefItemYear + 1 > currentYear)
                            currentYear = mtefItemYear + 1;
                    }
                }
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.YEAR, currentYear);
                projection.setProjectionDate(calendar.getTime());
                projection.setReportingDate(new Date(System.currentTimeMillis()));
                projection.setAmpCurrency(CurrencyUtil.getWicketWorkspaceCurrency());
                mtefTableFeature.getEditorList().addItem(projection);
                target.add(mtefTableFeature);
                AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                parent.getFundingInfo().configureRequiredFields();
                target.add(parent.getFundingInfo());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
        addMTEF.setAffectedByFreezing(false);
        add(addMTEF);
        
    }

}
