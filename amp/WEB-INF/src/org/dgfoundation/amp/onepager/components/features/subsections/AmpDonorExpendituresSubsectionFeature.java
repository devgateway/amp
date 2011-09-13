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
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorCommitmentsFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorExpendituresFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
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
		AmpSubsectionFeaturePanel<AmpFunding> {

	protected AmpDonorExpendituresFormTableFeature expTableFeature;
	
	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpDonorExpendituresSubsectionFeature(String id,
			final IModel<AmpFunding> model, String fmName, int transactionType) throws Exception {
		super(id, fmName, model);
		expTableFeature = new AmpDonorExpendituresFormTableFeature("expTableFeature", model, "Expenditures Table");
		add(expTableFeature);
		
		AmpAjaxLinkField addCommit=new AmpAjaxLinkField("addExp","Add Expenditure","Add Expenditure") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpFundingDetail fd= new AmpFundingDetail();
				fd.setAmpFundingId(model.getObject());
				fd.setTransactionAmount(0d);
				fd.setReportingDate(new Date(System.currentTimeMillis()));
				fd.setAdjustmentType(Constants.ACTUAL);
				fd.setTransactionDate(new Date(System.currentTimeMillis()));
				fd.setTransactionType(Constants.EXPENDITURE);
				fd.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY )));
				model.getObject().getFundingDetails().add(fd);
				expTableFeature.getList().removeAll();
				target.addComponent(expTableFeature);
				AmpFundingItemFeaturePanel parent=(AmpFundingItemFeaturePanel) this.getParent().getParent();
				target.addComponent(parent.getFundingInfo());
			}
		};
		add(addCommit);
	}

}
