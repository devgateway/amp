package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.List;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.onepager.validators.AmpCollectionsSumComparatorValidator;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
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
	public IModel<Double> getHiddenContainerModel(final IModel<? extends Collection<T>> setModel) {
		Model<Double> model=new Model<Double>() {

			private static final long serialVersionUID = 3202118320593721972L;

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
			AmpCurrency currency = null;
			java.sql.Date currencyDate = null;
			double exchangeRate = 1;
			double amount = 0;
			boolean fixedRate = false;
			if(item instanceof AmpFundingDetail)
			{
				AmpFundingDetail fundItem =(AmpFundingDetail)item;
			    if(fundItem.getTransactionAmount() != null)
			    	amount=fundItem.getTransactionAmount();
			    else
			    	continue;
			    
			    if(fundItem.getFixedExchangeRate()!=null)
			    {
			    	fixedRate=true;
			    	exchangeRate=fundItem.getFixedExchangeRate();
			    }
			    else
			    {
			    	currency = 	fundItem.getAmpCurrencyId();
			    	if(fundItem.getTransactionDate()!=null)
			    	currencyDate = new java.sql.Date(fundItem.getTransactionDate().getTime());
			    }
			}
			else if(item instanceof AmpComponentFunding)
			{
				AmpComponentFunding compFundItem =(AmpComponentFunding)item;
			   if(compFundItem.getTransactionAmount() != null)
				   amount=compFundItem.getTransactionAmount();
			    else
			    	continue;
			   
			   if(compFundItem.getExchangeRate()!=null)
			    {
			    	fixedRate=true;
			    	exchangeRate=compFundItem.getExchangeRate();
			    }
			    else
			    {
			    	currency = 	compFundItem.getCurrency();
			    	if(compFundItem.getTransactionDate()!=null)
			    	  currencyDate = new java.sql.Date(compFundItem.getTransactionDate().getTime());
			    }
			   
			   
			}
			else if(item instanceof AmpRegionalFunding)
			{
				AmpRegionalFunding regFundItem =(AmpRegionalFunding)item;
				   if(regFundItem.getTransactionAmount() != null)
					   amount=regFundItem.getTransactionAmount();
				    else
				    	continue;
				   

				  currency = 	regFundItem.getCurrency();
				  if(regFundItem.getTransactionDate()!=null)
				  currencyDate = new java.sql.Date(regFundItem.getTransactionDate().getTime());

			}
			
		 	
		 if(!fixedRate )	
		    exchangeRate =	Util.getExchange(currency.getCurrencyCode(), currencyDate);	
		  
		  total += amount/exchangeRate;
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
