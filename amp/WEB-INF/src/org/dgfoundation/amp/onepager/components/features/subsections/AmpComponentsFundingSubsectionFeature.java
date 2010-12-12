/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentsFundingFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.Constants;

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
			final IModel<AmpActivity> activityModel, final IModel<AmpComponent> componentModel,
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

		tableFeature = new AmpComponentsFundingFormTableFeature("tableFeature", compFundsModel, activityModel, transactionTypeName + " Table", transactionType);
		add(tableFeature);
		
		AmpButtonField addCommit=new AmpButtonField("add","Add " + transactionTypeName) {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpComponentFunding cf = new AmpComponentFunding();
				cf.setActivity(activityModel.getObject());
				cf.setAdjustmentType(Constants.ACTUAL);
				cf.setComponent(componentModel.getObject());
				cf.setTransactionAmount(0d);
				cf.setTransactionType(transactionType);
				cf.setAmpComponentFundingId(null);

				compFundsModel.getObject().add(cf);
				
				tableFeature.getList().removeAll();
				target.addComponent(tableFeature);
			}
		};
		add(addCommit);
	}

}
