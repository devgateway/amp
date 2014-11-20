package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.tables.AmpOverallFundingTotalsTable;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpOverviewSection extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {
	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();

	public AmpOverviewSection(String id, String fmName, IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName);
		
		//create a propose project cost section
		
		
		
		final WebMarkupContainer wmc = new WebMarkupContainer("fundingOverviewContainter");
		wmc.setOutputMarkupId(true);
		add(wmc);
		
		if (FeaturesUtil
				.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN)) {
					wmc.add(new AttributeAppender("class", new Model<String>("fundingOverviewDiv"), ""));
		}else{
			wmc.add(new AttributeAppender("class", new Model<String>("fundingOverviewDiv fundingOverviewDivNoTabs"), ""));
			
		}
        AmpProposedProjectCost propProjCost = new AmpProposedProjectCost(
				"propProjCost", "Proposed Project Cost", am);
        propProjCost.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
		wmc.add(propProjCost);
		getRequiredFormComponents().addAll(
				propProjCost.getRequiredFormComponents());
		
		RangeValidator<Integer> rangeValidator = new RangeValidator<Integer>(1,
				10);
		AttributeModifier attributeModifier = new AttributeModifier("size",
				new Model(1));
		AmpTextFieldPanel<Integer> fundingSourcesNumberPanel = new AmpTextFieldPanel<Integer>(
				"fundingSourcesNumber", new PropertyModel<Integer>(am,
						"fundingSourcesNumber"),
				CategoryConstants.FUNDING_SOURCES_NUMBER_NAME,
				AmpFMTypes.MODULE);
		fundingSourcesNumberPanel.getTextContainer().add(rangeValidator);
		fundingSourcesNumberPanel.getTextContainer().add(attributeModifier);
		wmc.add(fundingSourcesNumberPanel);

		AmpCategorySelectFieldPanel typeOfCooperation = new AmpCategorySelectFieldPanel(
				"typeOfCooperation", CategoryConstants.TYPE_OF_COOPERATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_COOPERATION_KEY),
				CategoryConstants.TYPE_OF_COOPERATION_NAME, true, false, null,
				AmpFMTypes.MODULE);
		wmc.add(typeOfCooperation);

		AmpCategorySelectFieldPanel typeOfImplementation = new AmpCategorySelectFieldPanel(
				"typeOfImplementation",
				CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
				CategoryConstants.TYPE_OF_IMPLEMENTATION_NAME, true, false,
				null, AmpFMTypes.MODULE);
		wmc.add(typeOfImplementation);

		AmpCategorySelectFieldPanel modalities = new AmpCategorySelectFieldPanel(
				"modalities",
				CategoryConstants.MODALITIES_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"), CategoryConstants.MODALITIES_KEY),
				CategoryConstants.MODALITIES_NAME, true, false, null,
				AmpFMTypes.MODULE);
		wmc.add(modalities);				
		
		
		
		AmpOverallFundingTotalsTable overallFunding = new AmpOverallFundingTotalsTable(
				"overallFunding", "Overall Funding Totals", new PropertyModel<Set<AmpFunding>>(am, "funding"));
		overallFunding.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));

		wmc.add(overallFunding);
	}
	 
	/**
	 * Return a list of FormComponent that are marked as required for this panel
	 * 
	 * @return List<FormComponent<?>> with the FormComponent
	 */
	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}


