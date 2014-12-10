package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

public class AmpComponentFormTableAnnualBudget
		extends
		AmpFundingFormTableFeaturePanel<AmpActivityVersion, AmpAnnualProjectBudget> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 732018195505777380L;

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */

	public AmpComponentFormTableAnnualBudget(String id, String fmName,
			final IModel<AmpActivityVersion> model) throws Exception {
		super(id, model, fmName);

		getTableId().add(new AttributeModifier("width", "620"));
		
		final AbstractReadOnlyModel<List<AmpCurrency>> currencyList = new AbstractReadOnlyModel<List<AmpCurrency>>() {
			@Override
			public List<AmpCurrency> getObject() {
				return (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode();
			}
		};

		
		final IModel<Set<AmpAnnualProjectBudget>> setModel = new PropertyModel<Set<AmpAnnualProjectBudget>>(
				model, "annualProjectBudgets");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpAnnualProjectBudget>());
		setTitleHeaderColSpan(5);
		list = new ListEditor<AmpAnnualProjectBudget>("listAnnualBudget", setModel, new AmpAnnualProjectBudget.AmpAnnualProjectBudgerComparator()) {
			@Override
			protected void onPopulateItem(
					final org.dgfoundation.amp.onepager.components.ListItem<AmpAnnualProjectBudget> item) {
				final MarkupContainer listParent = this.getParent();

				
				AmpFundingAmountComponent<AmpAnnualProjectBudget> fundingAmount = new AmpFundingAmountComponent<AmpAnnualProjectBudget>(
						"fundingAmountAnnualBudget", item.getModel(), "Amount", "amount", "Currency", "ampCurrencyId",
						"Projection Date", "year", true) {
//					@Override
//					protected void onFundingDetailChanged(AjaxRequestTarget target) {
//						super.onFundingDetailChanged(target);
//						AmpComponentFormTableAnnualBudget.this.onFundingDetailChanged(target);
//					}

				};
				//fundingAmount.setOutputMarkupId(true);
				item.add(fundingAmount);
//				final AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>(
//						"amountAnnualBudget", new PropertyModel<Double>(item.getModel(),
//								"amount"), "Amount", false, false) {
//
//					@Override
//					protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
//
//						onFundingDetailChanged(target);
//					}
//
//					public IConverter getInternalConverter(
//							java.lang.Class<?> type) {
//						DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
//						NumberFormat formatter = FormatHelper
//								.getDecimalFormat(true);
//
//						converter.setNumberFormat(getLocale(), formatter);
//						return converter;
//					}
//				};
//				amount.getTextContainer().setRequired(true);
//				amount.setOutputMarkupId(true);
//				amount.getTextContainer().add(
//						new AttributeModifier("size", new Model<String>("9")));
//
//				item.add(amount);
//
//				AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel(
//						"dateAnnualBudget",
//						new PropertyModel<Date>(item.getModel(), "year"),
//						"Projection Date", null, false, false);
//				datetmp.getDate().setRequired(true);
//				datetmp.getDate().add(
//						new AttributeModifier("class", "inputx_date"));
//				Component date = datetmp;
//
//				item.add(date);
//
//				
//				AmpSelectFieldPanel currency = new AmpSelectFieldPanel<AmpCurrency>("currencyAnnualBudget",
//						new PropertyModel<AmpCurrency>(item.getModel(), "ampCurrencyId"),
//						currencyList, "Currency", false, false, null, false) {
//							private static final long serialVersionUID = -7416247154386264496L;
//
//					@Override
//					protected void onAjaxOnUpdate(AjaxRequestTarget target) {
//						onFundingDetailChanged(target);
//					}
//				};
//				currency.getChoiceContainer().setRequired(true);
//				item.add(currency);

				
				AmpDeleteLinkField delAnnualBudget = new AmpDeleteLinkField("delAnnualBudget",
						"Delete Internal Id") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.add(listParent);
						//boolean result = model.getObject().getAnnualProjectBudgets().removeAll(model.getObject().getAnnualProjectBudgets());
						//amount.getModel().setObject(- amount.getModel().getObject());
						
						int idx = item.getIndex();

						for (int i = idx + 1; i < item.getParent().size(); i++) {
							ListItem<?> listItem = (ListItem<?>) item.getParent().get(i);
							listItem.setIndex(listItem.getIndex() - 1);
						}
						 
						list.items.remove(item.getIndex());
						list.remove(item);
						onFundingDetailChanged(target);

					}
				};
				item.add(delAnnualBudget);

			}
		};
		add(list);
	}

	/**
	 * Method called when the amount field value has been changed
	 * 
	 * @param target
	 */
	protected void onFundingDetailChanged(AjaxRequestTarget target) {
		System.out.println("test");
	}

}