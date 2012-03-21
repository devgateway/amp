/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpActivityBudgetExtrasPanel extends AmpFieldPanel {

	public AmpActivityBudgetExtrasPanel(String id, IModel model, String fmName) {
		super(id, model, fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		add(new AmpTextFieldPanel<String>("fy", new PropertyModel(model, "FY"), "FY", false, false));
		
		final AmpTextFieldPanel<String> vote = new AmpTextFieldPanel<String>("vote", new PropertyModel(model, "vote"), "Vote", false, false); 
		final AmpTextFieldPanel<String> subVote = new AmpTextFieldPanel<String>("subVote", new PropertyModel(model, "subVote"), "Sub-Vote", false, false); 
		final AmpTextFieldPanel<String> subProgram = new AmpTextFieldPanel<String>("subProgram", new PropertyModel(model, "subProgram"), "Sub-Program", false, false); 
		final AmpTextFieldPanel<String> projectCode = new AmpTextFieldPanel<String>("projectCode", new PropertyModel(model, "projectCode"), "Project Code", false, false); 
		add(new AmpComponentPanel("requiredField", "Validator Required Fields") {
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				if (this.isVisible()){
					vote.getTextContainer().setRequired(true);
					subVote.getTextContainer().setRequired(true);
					subProgram.getTextContainer().setRequired(true);
					projectCode.getTextContainer().setRequired(true);
				}
			}
		});
		vote.setTextContainerDefaultMaxSize();
		add(vote);
		subVote.setTextContainerDefaultMaxSize();
		add(subVote);
		subProgram.setTextContainerDefaultMaxSize();
		add(subProgram);
		projectCode.setTextContainerDefaultMaxSize();
		add(projectCode);
	}
}
