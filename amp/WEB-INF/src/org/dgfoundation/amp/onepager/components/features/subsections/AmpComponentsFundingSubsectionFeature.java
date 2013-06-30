/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentsFundingFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author aartimon@dginternational.org
 * since Nov 22, 2010
 */
public class AmpComponentsFundingSubsectionFeature extends
		AmpSubsectionFeaturePanel<AmpComponent> {

	protected AmpComponentsFundingFormTableFeature tableFeature;
	
	/**
	 * @param id
	 * @param fmName
	 * @param compFundsModel
	 * @throws Exception
	 */
	public AmpComponentsFundingSubsectionFeature(String id, 
			final IModel<AmpActivityVersion> activityModel, final IModel<AmpComponent> componentModel,
			final IModel<Set<AmpComponentFunding>> compFundsModel,
			String fmName, final int transactionType) throws Exception {
		super(id, fmName);

		String transactionTypeName;
		switch (transactionType) {
		case Constants.COMMITMENT:
			transactionTypeName = "Commitment";
			break;
		case Constants.DISBURSEMENT:
			transactionTypeName = "Disbursement";
			break;
		case Constants.EXPENDITURE:
			transactionTypeName = "Expenditure";
			break;
		default:
			throw new Exception("Unknown Transaction Type");
		}

		tableFeature = new AmpComponentsFundingFormTableFeature("tableFeature", componentModel, compFundsModel, activityModel, transactionTypeName + " Table", transactionType);
		add(tableFeature);
		
		AmpAjaxLinkField addButton=new AmpAjaxLinkField("add", "Add " + transactionTypeName, "Add " + transactionTypeName) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpComponentFunding cf = new AmpComponentFunding();
				cf.setActivity(activityModel.getObject());
				try {
					cf.setAdjustmentType(CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL));
				} catch (Exception e) {
					logger.error("", e);
				}
				cf.setComponent(componentModel.getObject());
				cf.setTransactionAmount(0d);
				cf.setCurrency(CurrencyUtil.getWicketWorkspaceCurrency());
				cf.setTransactionDate(null);
				cf.setTransactionType(transactionType);
				cf.setAmpComponentFundingId(null);


                tableFeature.getEditorList().addItem(cf);
				compFundsModel.getObject().add(cf);

				target.add(tableFeature);
			}
		};
		add(addButton);
	}

}
