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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * @author aartimon@dginternational.org
 * @since Jan 11, 2012
 */
public class AmpProposedProjectCost extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {

	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;
	private List <FormComponent<?>> requiredFormComponents = new ArrayList <FormComponent<?>> ();

	public AmpProposedProjectCost(String id, String fmName, IModel<AmpActivityVersion> am) {
		super(id, fmName);
		
		final AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("proposedAmount",
				new PropertyModel<Double>(am, CategoryConstants.PROPOSE_PRJC_AMOUNT_KEY), CategoryConstants.PROPOSE_PRJC_AMOUNT_NAME,false) {
				public IConverter getInternalConverter(java.lang.Class<?> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat formatter = FormatHelper.getDecimalFormat(true);
				converter.setNumberFormat(getLocale(), formatter);
				return converter; 
			}

				@Override
				protected void onAjaxOnUpdate(AjaxRequestTarget target) {
					super.onAjaxOnUpdate(target);
					if (!this.getTextContainer().isRequired()) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(false);
						String js = String.format("$('#%s').change();",((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					}
					else if (this.getTextContainer().isRequired() && this.getTextContainer().getModel().getObject()!= null) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(true);
						String js = String.format("$('#%s').change();",((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					}
				
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
        final PropertyModel<Date> funDateModel = new PropertyModel<Date>(am, CategoryConstants.PROPOSE_PRJC_DATE_KEY);
        final AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("proposedDate", funDateModel, null, CategoryConstants.PROPOSE_PRJC_DATE_NAME);
        add(date);
      
        //validator for poposed project cost amount AMP-17254
        add(new AmpComponentPanel("proposedAmountRequired", "Required Validator for " + CategoryConstants.PROPOSE_PRJC_AMOUNT_NAME) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()){
                	amount.getTextContainer().setRequired(true);
                	//date.getDate().setRequired(true);
                	requiredFormComponents.add(amount.getTextContainer());
                }
            }
        });		
	}

	/**
	 * Return a list of FormComponent that are marked as required for this panel
	 * @return List<FormComponent<?>> with the FormComponent
	 */
	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}
