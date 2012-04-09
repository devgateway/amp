package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpCollectionsSumComparatorValidator;
import org.dgfoundation.amp.onepager.validators.AmpPercentageCollectionValidator;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

public class AmpCollectionsSumComparatorValidatorField<T> extends AmpCollectionValidatorField<AmpFundingDetail,Double> {

	AbstractReadOnlyModel<List<AmpFundingDetail>> secondAmountListModel;

	private boolean currentModelAmountSumBig;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param setModel
	 * @param fmName
	 */
	public AmpCollectionsSumComparatorValidatorField(String id,
			IModel<? extends Collection<AmpFundingDetail>> setModel,  String fmName, String messageKey) {
		super(id, setModel, fmName, new AmpCollectionsSumComparatorValidator(messageKey));
		 
		hiddenContainer.setType(Double.class);
	
	}

	
	
	@Override
	public IModel getHiddenContainerModel(final IModel<? extends Collection<AmpFundingDetail>> setModel) {
		Model<Double> model=new Model<Double>() {
			@Override
			public void setObject(Double object) {
			}
			
			@Override
			public Double getObject() {		
				double sum1 =calculateSum(setModel.getObject());
				double sum2 =calculateSum(secondAmountListModel.getObject());
				if(currentModelAmountSumBig)
					return sum2-sum1;					
				else
					return sum1-sum2;
				
			}
		};
		return model;
	}



	protected double calculateSum(Collection<AmpFundingDetail> collection) {
		double total =0;
		for( AmpFundingDetail item : collection) 
		{
			if(item.getTransactionAmount() != null)
			   total+=item.getTransactionAmount().doubleValue();
				
		}
		return total;
	}



	public void setSecondCollectionModel(
			AbstractReadOnlyModel<List<AmpFundingDetail>> secondAmountListModel) {
		this.secondAmountListModel=secondAmountListModel;
		
	}



	public void setAlertIfCurrentModelAmountSumBig(boolean currentModelAmountSumBig) {
		this.currentModelAmountSumBig=currentModelAmountSumBig;
		
	}
	
	
}
