/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePager extends AmpHeaderFooter {
	
	private static Logger logger = Logger.getLogger(OnePager.class);
	//for test purposes, it will be removed !!
	 
	protected IModel<AmpActivityVersion> am;
//	protected AmpActivityModel activityModelForSave;
	
	public OnePager(PageParameters parameters) {
		super();
		
		String activityId = (String) parameters.get("activity");
		boolean newActivity = false;
		if ((activityId == null) || (activityId.compareTo("new") == 0)){
			am = new AmpActivityModel();
			newActivity = true;
		}
		else{
			am = new AmpActivityModel(Long.valueOf(activityId));
		}
		
		
		try {
			
			AmpActivityFormFeature formFeature= new AmpActivityFormFeature("activityFormFeature", am, "Activity Form", newActivity);
			add(formFeature);

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		
		
		
	}
}
