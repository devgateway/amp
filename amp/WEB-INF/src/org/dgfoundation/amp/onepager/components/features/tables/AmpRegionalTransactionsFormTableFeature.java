/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionsSumComparatorValidatorField;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpRegionalTransactionsFormTableFeature extends
		AmpRegionalFormTableFeaturePanel {
	
	private boolean alertIfExpenditureBiggerDisbursment = false;  
	private boolean alertIfDisbursmentBiggerCommitments = false;
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param transactionType 
	 * @param cvLocationModel 
	 * @throws Exception
	 */
	public AmpRegionalTransactionsFormTableFeature(String id,
			final IModel<Set<AmpRegionalFunding>> model, String fmName, int transactionType, IModel<AmpCategoryValueLocations> cvLocationModel) throws Exception {
		super(id, model, fmName, transactionType, 6,cvLocationModel);

		 if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT)))
		 	alertIfExpenditureBiggerDisbursment = true;
		 if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS)))
		    alertIfDisbursmentBiggerCommitments = true;

		    
		 AbstractReadOnlyModel<List<AmpRegionalFunding>> disbursementsListModel = getSubsetModel(model, Constants.DISBURSEMENT);
		 AbstractReadOnlyModel<List<AmpRegionalFunding>> expenditureModel = getSubsetModel(model, Constants.EXPENDITURE);
		 AbstractReadOnlyModel<List<AmpRegionalFunding>> commitmentModel = getSubsetModel(model, Constants.COMMITMENT);
		 
	//	 if(transactionType == Constants.DISBURSEMENT)
		
		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		
		AbstractReadOnlyModel<List<AmpRegionalFunding>> firstModel = null;
		AbstractReadOnlyModel<List<AmpRegionalFunding>> secondModel = null;
		String errorMassage= null;
		String fieldFMName= null;
		boolean alertIfcurrentSumIsBigger=true;
		 if(transactionType == Constants.DISBURSEMENT)
		 {
			 firstModel = disbursementsListModel;
			 errorMassage = "AmpCommitmentsCollectionsSumComparatorValidator";
			 secondModel = commitmentModel;
			 alertIfcurrentSumIsBigger=true;
			 fieldFMName= "checkCommitmentSum";
		 }	 else if(transactionType == Constants.COMMITMENT)
		 {
			 firstModel = commitmentModel;
			 errorMassage = "AmpCommitmentsCollectionsSumComparatorValidator";
			 secondModel = disbursementsListModel;
			 fieldFMName= "checkCommitmentSum";
			 alertIfcurrentSumIsBigger=false;
		 } else if(transactionType == Constants.EXPENDITURE)
		 {
			 firstModel = expenditureModel;
			 errorMassage = "AmpExpendituresCollectionsSumComparatorValidator";
			 secondModel = disbursementsListModel;
			 fieldFMName= "checkExpenditureSum";
			 alertIfcurrentSumIsBigger=true;
		 }
		 
		final AmpCollectionsSumComparatorValidatorField amountSumComparator= 
				new AmpCollectionsSumComparatorValidatorField("regAmountSumComparator",firstModel,fieldFMName, errorMassage); 
		amountSumComparator.setIndicatorAppender(iValidator);
		amountSumComparator.setSecondCollectionModel(secondModel);
        amountSumComparator.setOutputMarkupId(true);
		amountSumComparator.setAlertIfCurrentModelAmountSumBig(alertIfcurrentSumIsBigger);		
		add(amountSumComparator);
		
		
		boolean visibleAllowed = false;
		 if(transactionType == Constants.DISBURSEMENT)
		 {
			 firstModel = disbursementsListModel;
			 errorMassage = "AmpExpendituresCollectionsSumComparatorValidator";
			 secondModel = expenditureModel;
			 alertIfcurrentSumIsBigger=false;
			 fieldFMName= "checkExpenditureSum";
			 visibleAllowed = true;
		 }	
		
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator1=
				new AmpCollectionsSumComparatorValidatorField("regAmountSumComparator1",firstModel,fieldFMName, errorMassage); 
		amountSumComparator1.setIndicatorAppender(iValidator);
		amountSumComparator1.setSecondCollectionModel(secondModel);
		amountSumComparator1.setAlertIfCurrentModelAmountSumBig(alertIfcurrentSumIsBigger);
        amountSumComparator1.setOutputMarkupId(true);
		add(amountSumComparator1);
		amountSumComparator1.setVisibilityAllowed(visibleAllowed);
		

		list = new ListEditor<AmpRegionalFunding>("listTransactions", setModel) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpRegionalFunding> item) {
				item.add(getAdjustmentTypeComponent(item.getModel()));
				AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
				
				 if(alertIfExpenditureBiggerDisbursment);
				 	amountComponent.setAmountValidator(amountSumComparator1);
				 if(alertIfDisbursmentBiggerCommitments);
					 	amountComponent.setAmountValidator(amountSumComparator); 	
					 	
				item.add(amountComponent);

				item.add(new ListEditorRemoveButton("delTransaction", "Delete Item"){
					protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpRegionalFundingItemFeaturePanel parent = this.findParent(AmpRegionalFundingItemFeaturePanel.class);
						super.onClick(target);
						amountSumComparator.reloadValidationField(target);
						
						
						parent.visitChildren(AmpCollectionValidatorField.class,
								new IVisitor<AmpCollectionValidatorField, Void>() {
									@Override
									public void component(AmpCollectionValidatorField component,
											IVisit<Void> visit) {
										component.reloadValidationField(target);
										target.add(component.getParent());
										visit.dontGoDeeper();
									}
								});
					};
				});
			}
		};
		add(list);
	}
	private AbstractReadOnlyModel<List<AmpRegionalFunding>> getSubsetModel( final IModel<Set<AmpRegionalFunding>> regFundsModel , final int transactionType)
	{
		
		
		return new AbstractReadOnlyModel<List<AmpRegionalFunding>>() {
			private static final long serialVersionUID = 371618487459839210L;

			@Override
			public List<AmpRegionalFunding> getObject() {
				List<AmpRegionalFunding> result = new ArrayList<AmpRegionalFunding>();
				Set<AmpRegionalFunding> allItems = regFundsModel.getObject();
				if (allItems != null){
					Iterator<AmpRegionalFunding> iterator = allItems.iterator();
					while (iterator.hasNext()) {
						AmpRegionalFunding item =  iterator.next();
						if (item.getTransactionType() == transactionType)
							//if (item.getRegionLocation() == componentModel.getObject().hashCode())
								result.add(item);
					}
				}
				
				return result;
			}
		};
	}
}
