/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractBasicSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractDetailsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractFundingAllocationSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractOrganizationsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;
import org.dgfoundation.amp.onepager.models.AmpMultiValueDropDownChoiceModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEItemFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {
	

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpMEItemFeaturePanel(String id, String fmName,
			final IModel<IndicatorActivity> indicatorModel){
		super(id, indicatorModel, fmName, true);
		
		final IndicatorActivity ia = indicatorModel.getObject();

		if (ia.getValues() == null)
			ia.setValues(new HashSet<AmpIndicatorValue>());
		
		final Label indicatorNameLabel = new Label("indicatorName", new PropertyModel<String>(indicatorModel.getObject().getIndicator(), "name"));
		add(indicatorNameLabel);

		String indCodeString = "";
		if (indicatorModel.getObject().getIndicator().getCode() != null && indicatorModel.getObject().getIndicator().getCode().trim().compareTo("") != 0)
			indCodeString = " - " + indicatorModel.getObject().getIndicator().getCode();
		
		final Label indicatorCodeLabel = new Label("indicatorCode", new Model<String>(indCodeString));
		add(indicatorCodeLabel);

		final IModel<AmpCategoryValue> logFrameModel = new PersistentObjectModel<AmpCategoryValue>();
		try {
			AmpCategorySelectFieldPanel logframe = new AmpCategorySelectFieldPanel("logframe", CategoryConstants.LOGFRAME_KEY, logFrameModel, "Logframe Category", true, true);
			add(logframe);
		} catch (Exception e) {
			logger.error(e);
		}

		final IModel<AmpIndicatorValue> baseVal = new PersistentObjectModel<AmpIndicatorValue>();
		final IModel<AmpIndicatorValue> targetVal = new PersistentObjectModel<AmpIndicatorValue>();
		final IModel<AmpIndicatorValue> revisedVal = new PersistentObjectModel<AmpIndicatorValue>();
		final IModel<AmpIndicatorValue> currentVal = new PersistentObjectModel<AmpIndicatorValue>();
		
		final Model<Boolean> valuesSet = new Model<Boolean>(false);

		Iterator<AmpIndicatorValue> iterator = ia.getValues().iterator();
		while (iterator.hasNext()) {
			AmpIndicatorValue val = (AmpIndicatorValue) iterator
					.next();
			
			switch (val.getValueType()) {
			case AmpIndicatorValue.BASE:
				baseVal.setObject(val);
				break;
			case AmpIndicatorValue.TARGET:
				targetVal.setObject(val);
				break;
			case AmpIndicatorValue.REVISED:
				revisedVal.setObject(val);
				break;
			case AmpIndicatorValue.ACTUAL:
				currentVal.setObject(val);
				break;
			default:
				break;
			}
		}

		if (baseVal.getObject() == null)
			baseVal.setObject(new AmpIndicatorValue(AmpIndicatorValue.BASE));
		if (targetVal.getObject() == null){
			targetVal.setObject(new AmpIndicatorValue(AmpIndicatorValue.TARGET));
			valuesSet.setObject(false);
		}
		else
			valuesSet.setObject(true);
			
		if (revisedVal.getObject() == null)
			revisedVal.setObject(new AmpIndicatorValue(AmpIndicatorValue.REVISED));
		if (currentVal.getObject() == null)
			currentVal.setObject(new AmpIndicatorValue(AmpIndicatorValue.ACTUAL));
		
		final AmpIndicatorGroupField base = new AmpIndicatorGroupField("base", baseVal, "Base Value", "Base");
		add(base);
		
		final AmpIndicatorGroupField target = new AmpIndicatorGroupField("target", targetVal, "Target Value", "Target");
		if (valuesSet.getObject())
			target.setEnabled(false);
		else
			target.setEnabled(true);
		target.setOutputMarkupId(true);
		add(target);

		final AmpIndicatorGroupField revised = new AmpIndicatorGroupField("revised", revisedVal, "Revised Value", "Revised");
		if (valuesSet.getObject())
			revised.setVisible(true);
		else
			revised.setVisible(false);
		revised.setOutputMarkupId(true);
		add(revised);

		final AmpIndicatorGroupField current = new AmpIndicatorGroupField("current", currentVal, "Current Value", "Current");
		add(current);
		
		final IModel<AmpIndicatorRiskRatings> riskModel = new PersistentObjectModel<AmpIndicatorRiskRatings>();

		AbstractSingleSelectChoice<AmpIndicatorRiskRatings> risk = new DropDownChoice<AmpIndicatorRiskRatings>(
				"risk",
				riskModel, new LoadableDetachableModel<List<AmpIndicatorRiskRatings>>() {
					@Override
					protected List<AmpIndicatorRiskRatings> load() {
						return (List<AmpIndicatorRiskRatings>) MEIndicatorsUtil.getAllIndicatorRisks();
					}
				}).setNullValid(true);
		risk.setOutputMarkupId(true);
		add(risk);

		AjaxButton setValue = new AjaxButton("setValues", new Model<String>("Set Value")) {
			@Override
			protected void onSubmit(AjaxRequestTarget art, Form<?> arg1) {
				AmpCategoryValue logFrame = logFrameModel.getObject();
				AmpIndicatorRiskRatings riskVal = riskModel.getObject();
				
				Set<AmpIndicatorValue> vals = indicatorModel.getObject().getValues();
				vals.clear();

				AmpIndicatorValue tmp = baseVal.getObject();
				tmp.setLogFrame(logFrame);
				tmp.setRisk(riskVal);
				vals.add(tmp);
				baseVal.setObject(tmp.clone());
				
				tmp = revisedVal.getObject(); 
				tmp.setLogFrame(logFrame);
				tmp.setRisk(riskVal);
				vals.add(tmp);
				revisedVal.setObject(tmp.clone());
				
				tmp = targetVal.getObject(); 
				tmp.setLogFrame(logFrame);
				tmp.setRisk(riskVal);
				vals.add(tmp);
				targetVal.setObject(tmp.clone());

				tmp = currentVal.getObject(); 
				tmp.setLogFrame(logFrame);
				tmp.setRisk(riskVal);
				vals.add(tmp);
				currentVal.setObject(tmp.clone());

				
				if (!valuesSet.getObject()){
					target.setEnabled(false);
					revised.setVisible(true);
					valuesSet.setObject(true);
				}

				art.addComponent(base);
				art.addComponent(target);
				art.addComponent(revised);
				art.addComponent(current);
			}
		};
		add(setValue);
	}

}
