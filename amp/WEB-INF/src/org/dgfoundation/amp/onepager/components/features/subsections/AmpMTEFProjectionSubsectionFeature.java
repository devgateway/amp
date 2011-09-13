/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMTEFProjectionFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class AmpMTEFProjectionSubsectionFeature extends
		AmpSubsectionFeaturePanel<AmpFunding> {

	protected AmpMTEFProjectionFormTableFeature mtefTableFeature;

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpMTEFProjectionSubsectionFeature(String id, 
			final IModel<AmpFunding> model,String fmName) throws Exception {
		super(id, fmName, model);
		mtefTableFeature = new AmpMTEFProjectionFormTableFeature("mtefTableFeature", "MTEF Projections Table", model);
		add(mtefTableFeature);
		
		AmpAjaxLinkField addMTEF=new AmpAjaxLinkField("addMTEF","Add Projection","Add Projection") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpFundingMTEFProjection projection= new AmpFundingMTEFProjection();
				projection.setAmpFunding(model.getObject());
				projection.setAmount(0d);
				projection.setProjectionDate(new Date(System.currentTimeMillis()));
				projection.setReportingDate(new Date(System.currentTimeMillis()));
				projection.setAmpCurrency(CurrencyUtil.getCurrencyByCode(FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY )));
				model.getObject().getMtefProjections().add(projection);
				mtefTableFeature.getList().removeAll();
				target.addComponent(mtefTableFeature);
				AmpFundingItemFeaturePanel parent=(AmpFundingItemFeaturePanel) this.getParent().getParent();
				target.addComponent(parent.getFundingInfo());
			}
		};
		add(addMTEF);
		
	}

}
