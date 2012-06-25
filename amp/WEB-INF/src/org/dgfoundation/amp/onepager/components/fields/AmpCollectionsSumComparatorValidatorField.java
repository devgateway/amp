package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.List;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpCollectionsSumComparatorValidator;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

public class AmpCollectionsSumComparatorValidatorField<T> extends AmpCollectionValidatorField<T,Double> {

	AbstractReadOnlyModel<List<T>> secondAmountListModel;

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
			IModel<? extends Collection<T>> setModel,  String fmName, String messageKey) {
		super(id, setModel, fmName, new AmpCollectionsSumComparatorValidator(messageKey));
		hiddenContainer.setType(Double.class);
	
	}

	
	
	@Override
	public IModel getHiddenContainerModel(final IModel<? extends Collection<T>> setModel) {
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



	protected double calculateSum(Collection<T> collection) {
		double total =0;
		
		for( T item : collection) 
		{
			if(item instanceof AmpFundingDetail)
			{
			if(((AmpFundingDetail)item).getTransactionAmount() != null)
			   total+=((AmpFundingDetail)item).getTransactionAmount().doubleValue();
			}
			else if(item instanceof AmpComponentFunding)
			{
			if(((AmpComponentFunding)item).getTransactionAmount() != null)
			   total+=((AmpComponentFunding)item).getTransactionAmount().doubleValue();
			}else if(item instanceof AmpRegionalFunding)
			{
			if(((AmpRegionalFunding)item).getTransactionAmount() != null)
			   total+=((AmpRegionalFunding)item).getTransactionAmount().doubleValue();
			}
			
				
				
		}
		return total;
	}



	public void setSecondCollectionModel(
			AbstractReadOnlyModel<List<T>> secondAmountListModel) {
		this.secondAmountListModel=secondAmountListModel;
		
	}



	public void setAlertIfCurrentModelAmountSumBig(boolean currentModelAmountSumBig) {
		this.currentModelAmountSumBig=currentModelAmountSumBig;
		
	}
	
	
}
