/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * @author aartimon@dginternational.org
 * @since Jan 11, 2012
 */
public class AmpProposedProjectCost extends AmpComponentPanel<Void> {

	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;

	public AmpProposedProjectCost(String id, String fmName, IModel<AmpActivityVersion> am) {
		super(id, fmName);
		
		AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("proposedAmount",
				new PropertyModel<Double>(am, "funAmount"), "Amount",false) {
			public IConverter getInternalConverter(java.lang.Class<?> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat formatter = FormatHelper.getDecimalFormat(true);
				converter.setNumberFormat(getLocale(), formatter);
				return converter; 
			}
		};
		amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
		add(amount);
		
		AbstractReadOnlyModel<List<String>> currencyList = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				List<AmpCurrency> tmp = (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode();
				ArrayList<String> ret = new ArrayList<String>(); 
				
				Iterator<AmpCurrency> it = tmp.iterator();
				while (it.hasNext()) {
					AmpCurrency c = (AmpCurrency) it.next();
					ret.add(c.getCurrencyCode());
				}
				return ret;
			}
		};
		
		PropertyModel<String> currencyModel = new PropertyModel<String>(am, "currencyCode");
		
		if (currencyModel.getObject() == null){
			currencyModel.setObject(CurrencyUtil.getWicketWorkspaceCurrency().getCurrencyCode());
		}
		
		AmpSelectFieldPanel<String> currency = new AmpSelectFieldPanel<String>("proposedCurrency",
				currencyModel, currencyList,
				"Currency", false, false);
		add(currency);
        final PropertyModel<Date> funDateModel = new PropertyModel<Date>(
                am, "funDate");
        AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("proposedDate", funDateModel, null, "Signature Date");
		add(date);
	}
}
