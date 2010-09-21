/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * @author mihai
 *
 */
public class AmpFundingAmountComponent<T> extends AmpComponentPanel<T> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3842513519009818922L;

	public AmpFundingAmountComponent(String id, String fmName) {
		super(id, fmName);

		TextField<Double> amount=new TextField<Double>("thousandsTransactionAmount");
		amount.add(new MinimumValidator<Double>(0d));
		add(amount);
		
		DateTextField date=new DateTextField("transactionDate");
		date.add(new DatePicker());
		add(date);
		
		
		
		ArrayList<AmpCurrency> currencies=new ArrayList<AmpCurrency>();
		
		AmpCurrency usd=new AmpCurrency();
		usd.setCurrencyCode("USD");
		currencies.add(usd);
		
		AmpCurrency eur=new AmpCurrency();
		eur.setCurrencyCode("EUR");
		currencies.add(eur);
		
		DropDownChoice<AmpCurrency> currency=new DropDownChoice<AmpCurrency>("ampCurrencyId", currencies);	
		add(currency);
	}
	
}
