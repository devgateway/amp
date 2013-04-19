/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
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
		final IModel<Set<AmpFundingMTEFProjection>> setModel = new PropertyModel<Set<AmpFundingMTEFProjection>>(
				model, "mtefProjections");
		
		AmpAjaxLinkField addMTEF=new AmpAjaxLinkField("addMTEF","Add Projection","Add Projection") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpFundingMTEFProjection projection= new AmpFundingMTEFProjection();
				projection.setAmpFunding(model.getObject());
				//projection.setAmount(0d);
//				projection.setProjectionDate(new Date(System.currentTimeMillis()));

                String currentFiscalYear = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENT_SYSTEM_YEAR);
                Calendar calendar = Calendar.getInstance();

                int currentYear;
                if (currentFiscalYear != null)
                    currentYear = Integer.parseInt(currentFiscalYear);
                else
                    currentYear = calendar.get(Calendar.YEAR);

				Set<AmpFundingMTEFProjection> mtefSet = setModel.getObject();
                if (mtefSet != null){
					Iterator<AmpFundingMTEFProjection> it = mtefSet.iterator();
					while (it.hasNext()) {
						AmpFundingMTEFProjection mtefItem = (AmpFundingMTEFProjection) it
								.next();
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
				parent.getFundingInfo().checkChoicesRequired(mtefTableFeature.getEditorList().getCount());
				target.add(parent.getFundingInfo());
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
				target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
			}
		};
		add(addMTEF);
		
	}

}
