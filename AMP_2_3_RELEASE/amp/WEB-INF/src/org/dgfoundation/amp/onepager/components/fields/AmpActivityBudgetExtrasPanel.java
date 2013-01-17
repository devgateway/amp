/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.models.ActivityFYModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpActivityBudgetExtrasPanel extends AmpFieldPanel<AmpActivityVersion> {
	private static final long serialVersionUID = 1L;

	public AmpActivityBudgetExtrasPanel(String id, IModel<AmpActivityVersion> model, String fmName) {
		super(id, model, fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		String startYear = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START);
		int rangeStartYear = Integer.parseInt(startYear);
		String numbYearsRange = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
		int rangeNumber = Integer.parseInt(numbYearsRange);
		List<String> years = new ArrayList<String>();
		for (int i = rangeStartYear; i < rangeStartYear + rangeNumber; i++) {
			years.add("" + i);
		}

        final AmpSelectFieldPanel fy = new AmpSelectFieldPanel("fy", new ActivityFYModel(new PropertyModel<String>(model, "FY")), years, "FY", false, true, false);
		fy.getChoiceContainer().setOutputMarkupId(true);
		fy.setOutputMarkupId(true);
		fy.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(fy);		
			}		
	      });
		// add(new AmpTextFieldPanel<String>("fy", new PropertyModel(model,
		// "FY"), "FY", false, false));
		add(fy);
		final AmpTextFieldPanel<String> projectCode = new AmpTextFieldPanel<String>("projectCode", new PropertyModel(model, "projectCode"), "Project Code", false, false); 
		projectCode.setTextContainerDefaultMaxSize();
		add(projectCode);
		
		final AmpTextFieldPanel<String> vote = new AmpTextFieldPanel<String>("vote", new PropertyModel(model, "vote"), "Vote", false, false); 
		final AmpTextFieldPanel<String> subVote = new AmpTextFieldPanel<String>("subVote", new PropertyModel(model, "subVote"), "Sub-Vote", false, false); 
		final AmpTextFieldPanel<String> subProgram = new AmpTextFieldPanel<String>("subProgram", new PropertyModel(model, "subProgram"), "Sub-Program", false, false); 
		final AmpTextFieldPanel<String> ministryCode = new AmpTextFieldPanel<String>("ministryCode", new PropertyModel(model, "ministryCode"), "Ministry Code", false, false);
		add(new AmpComponentPanel("requiredField", "Validator Required Fields") {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				if (this.isVisible()){
					vote.getTextContainer().setRequired(true);
					subVote.getTextContainer().setRequired(true);
					subProgram.getTextContainer().setRequired(true);
					ministryCode.getTextContainer().setRequired(true);
					//projectCode.getTextContainer().setRequired(true);
				}
			}
		});
		vote.setTextContainerDefaultMaxSize();
		add(vote);
		subVote.setTextContainerDefaultMaxSize();
		add(subVote);
		subProgram.setTextContainerDefaultMaxSize();
		add(subProgram);
		ministryCode.setTextContainerDefaultMaxSize();
		add(ministryCode);
		
	}
}
