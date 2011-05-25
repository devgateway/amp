/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePagerWrapper extends AmpHeaderFooter {
	protected Form<AmpActivityVersion> activityForm;
	
	//for test purposes, it will be removed !!
	 
	protected IModel<AmpActivityVersion> am;
//	protected AmpActivityModel activityModelForSave;
	
	public OnePagerWrapper(PageParameters parameters) {
		super();
		
		String activityId = (String) parameters.get("activity");
		
		if ((activityId == null) || (activityId.compareTo("new") == 0)){
			am = new AmpActivityModel();
		}
		else{
			am = new AmpActivityModel(Long.valueOf(activityId));
		}
		
		
		
		activityForm=new Form<AmpActivityVersion>("activityForm") ;
		activityForm.setOutputMarkupId(true);
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		
        //int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        //feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
		
		//add ajax submit button
		AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.FEATURE) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				info("Activity saved successfully");
				target.addComponent(feedbackPanel);
			}
		};
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		activityForm.add(saveAndSubmit);
	}
}
