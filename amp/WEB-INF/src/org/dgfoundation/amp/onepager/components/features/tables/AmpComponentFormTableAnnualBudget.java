package org.dgfoundation.amp.onepager.components.features.tables;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpComponentField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.ProposedProjectCostUpdateEvent;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;


public class AmpComponentFormTableAnnualBudget extends
		AmpFundingFormTableFeaturePanel<AmpActivityVersion,AmpAnnualProjectBudget> {

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	
	
	public AmpComponentFormTableAnnualBudget(String id, String fmName,
			IModel<AmpActivityVersion> model) throws Exception {
		super(id, model, fmName);
		
		getTableId().add(new AttributeModifier("width", "620"));
		
		final IModel<Set<AmpAnnualProjectBudget>> setModel = new PropertyModel<Set<AmpAnnualProjectBudget>>(
				model, "annualProjectBudgets");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpAnnualProjectBudget>());
		setTitleHeaderColSpan(5);
		list = new ListEditor<AmpAnnualProjectBudget>("listMTEF",
				setModel) {
			@Override
			protected void onPopulateItem(
					final org.dgfoundation.amp.onepager.components.ListItem<AmpAnnualProjectBudget> item) {
				final MarkupContainer listParent=this.getParent();
               
				AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("amount",
						new PropertyModel<Double>(item.getModel(),"amount"), "Amount", false, false) {
					
					@Override
					protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
						
						onFundingDetailChanged(target);
					}
					
					public IConverter getInternalConverter(java.lang.Class<?> type) {
						DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
						NumberFormat formatter = FormatHelper.getDecimalFormat(true);
						
//						formatter.setMinimumFractionDigits(0);
						converter.setNumberFormat(getLocale(), formatter);
						return converter; 
					}
				};
				amount.getTextContainer().setRequired(true);
				amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("9")));
				
				
				item.add(amount);
				
				AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel("date", new PropertyModel<Date>(
						item.getModel(), "year"), "Projection Date", null, false, false);
				datetmp.getDate().setRequired(true);
				datetmp.getDate().add(new AttributeModifier("class", "inputx_date"));
				Component date = datetmp;
				
				item.add(date);
				
                
                AmpDeleteLinkField delOrgId = new AmpDeleteLinkField(
						"delMtef", "Delete Internal Id") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.add(listParent);
						list.remove(item);
					}
				};
				item.add(delOrgId);

				
			}
		};
		add(list);
	}
	
	/**
     * Method called when the amount field value has been changed
     * @param target
     */
	protected void onFundingDetailChanged(AjaxRequestTarget target) {
		System.out.println("test");
	}	

}
