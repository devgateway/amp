/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionsSumComparatorValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoModel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoRenderer;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 25, 2010
 */
public class AmpComponentsFundingFormTableFeature extends
		AmpFormTableFeaturePanel {

	private boolean alertIfExpenditureBiggerDisbursment = false;  
	private boolean alertIfDisbursmentBiggerCommitments = false;
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpComponentsFundingFormTableFeature(String id,
			final IModel<AmpComponent> componentModel,
			final IModel<Set<AmpComponentFunding>> compFundsModel, 
			final IModel<AmpActivityVersion> activityModel, String fmName,
			final int transactionType) throws Exception {
		super(id, activityModel, fmName);
		setTitleHeaderColSpan(5);

		AbstractReadOnlyModel<List<AmpComponentFunding>> listModel = new AbstractReadOnlyModel<List<AmpComponentFunding>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpComponentFunding> getObject() {
				List<AmpComponentFunding> result = new ArrayList<AmpComponentFunding>();
				Set<AmpComponentFunding> allComp = compFundsModel.getObject();
				if (allComp != null){
					Iterator<AmpComponentFunding> iterator = allComp.iterator();
					while (iterator.hasNext()) {
						AmpComponentFunding comp = (AmpComponentFunding) iterator
						.next();
						if (comp.getTransactionType() == transactionType)
							if (comp.getComponent().hashCode() == componentModel.getObject().hashCode())
								result.add(comp);
					}
				}
				
				return result;
			}
		};

		
		 if( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT).equalsIgnoreCase("TRUE"));
		 	alertIfExpenditureBiggerDisbursment = true;
		 if( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS).equalsIgnoreCase("TRUE"));
		    alertIfDisbursmentBiggerCommitments = true;

		    
		 AbstractReadOnlyModel<List<AmpComponentFunding>> disbursementsListModel = getSubsetModel(compFundsModel, Constants.DISBURSEMENT);
		 AbstractReadOnlyModel<List<AmpComponentFunding>> expenditureModel = getSubsetModel(compFundsModel, Constants.EXPENDITURE);
		 AbstractReadOnlyModel<List<AmpComponentFunding>> commitmentModel = getSubsetModel(compFundsModel, Constants.COMMITMENT);
		 
	//	 if(transactionType == Constants.DISBURSEMENT)
		
		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		
		AbstractReadOnlyModel<List<AmpComponentFunding>> model = null;
		AbstractReadOnlyModel<List<AmpComponentFunding>> secondModel = null;
		String errorMassage= null;
		String fieldFMName= null;
		boolean alertIfcurrentSumIsBigger=true;
		 if(transactionType == Constants.DISBURSEMENT)
		 {
			 model = disbursementsListModel;
			 errorMassage = "AmpCommitmentsCollectionsSumComparatorValidator";
			 secondModel = commitmentModel;
			 alertIfcurrentSumIsBigger=true;
			 fieldFMName= "checkCommitmentSum";
		 }	 else if(transactionType == Constants.COMMITMENT)
		 {
			 model = commitmentModel;
			 errorMassage = "AmpCommitmentsCollectionsSumComparatorValidator";
			 secondModel = disbursementsListModel;
			 fieldFMName= "checkCommitmentSum";
			 alertIfcurrentSumIsBigger=false;
		 } else if(transactionType == Constants.EXPENDITURE)
		 {
			 model = expenditureModel;
			 errorMassage = "AmpExpemdituresCollectionsSumComparatorValidator";
			 secondModel = disbursementsListModel;
			 fieldFMName= "checkExpenditureSum";
			 alertIfcurrentSumIsBigger=true;
		 }
		 
		final AmpCollectionsSumComparatorValidatorField amountSumComparator= 
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator",model,fieldFMName, errorMassage); 
		amountSumComparator.setIndicatorAppender(iValidator);
		amountSumComparator.setSecondCollectionModel(secondModel);
		amountSumComparator.setAlertIfCurrentModelAmountSumBig(alertIfcurrentSumIsBigger);		
		add(amountSumComparator);
		
		
		boolean visibleAllowed = false;
		 if(transactionType == Constants.DISBURSEMENT)
		 {
			 model = disbursementsListModel;
			 errorMassage = "AmpExpemdituresCollectionsSumComparatorValidator";
			 secondModel = expenditureModel;
			 alertIfcurrentSumIsBigger=false;
			 fieldFMName= "checkExpenditureSum";
			 visibleAllowed = true;
		 }	
		
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator1=
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator1",model,fieldFMName, errorMassage); 
		amountSumComparator1.setIndicatorAppender(iValidator);
		amountSumComparator1.setSecondCollectionModel(secondModel);
		amountSumComparator1.setAlertIfCurrentModelAmountSumBig(alertIfcurrentSumIsBigger);
		add(amountSumComparator1);
		amountSumComparator1.setVisibilityAllowed(visibleAllowed);
		
		
		list = new ListView<AmpComponentFunding>("list", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpComponentFunding> item) {
				IModel<AmpComponentFunding> model = item.getModel();
					try{
						AmpCategoryGroupFieldPanel adjustmentTypes = new AmpCategoryGroupFieldPanel(
							"adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
									new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
									CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
									 false, false, true);
					adjustmentTypes.getChoiceContainer().setRequired(true);
					
					item.add(adjustmentTypes);
					}catch(Exception e)
					{
						logger.error("AmpCategoryGroupFieldPanel initialization failed");
					}
					
					AmpFundingAmountComponent amountComponent = new AmpFundingAmountComponent<AmpComponentFunding>("fundingAmount",
							model, "Amount", "transactionAmount", "Currency",
							"currency", "Transaction Date", "transactionDate");
					
					 if(alertIfExpenditureBiggerDisbursment);
					 	amountComponent.setAmountValidator(amountSumComparator1);
					 if(alertIfDisbursmentBiggerCommitments);
						 	amountComponent.setAmountValidator(amountSumComparator); 	
				
				item.add(amountComponent);
				 
				item.add(new AmpDeleteLinkField("delete", "Delete") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						compFundsModel.getObject().remove(item.getModelObject());
						target.add(AmpComponentsFundingFormTableFeature.this);
						list.removeAll();
					}
					});
			}
		};
		list.setReuseItems(true);
		add(list);

	}
	
	
	private AbstractReadOnlyModel<List<AmpComponentFunding>> getSubsetModel( final IModel<Set<AmpComponentFunding>> compFundsModel , final int transactionType)
	{
		
		
		return new AbstractReadOnlyModel<List<AmpComponentFunding>>() {
			private static final long serialVersionUID = 370618487459839210L;

			@Override
			public List<AmpComponentFunding> getObject() {
				List<AmpComponentFunding> result = new ArrayList<AmpComponentFunding>();
				Set<AmpComponentFunding> allComp = compFundsModel.getObject();
				if (allComp != null){
					Iterator<AmpComponentFunding> iterator = allComp.iterator();
					while (iterator.hasNext()) {
						AmpComponentFunding comp = (AmpComponentFunding) iterator
						.next();
						if (comp.getTransactionType() == transactionType)
							//if (comp.getComponent().hashCode() == componentModel.getObject().hashCode())
								result.add(comp);
					}
				}
				
				return result;
			}
		};
	}

}
