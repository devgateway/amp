/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.HashSet;
import java.util.Set;

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
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.util.LocationUtil;

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
			
			
			if(parameters.get("lat") != null && parameters.get("lat") != null && parameters.get("geoId") != null && parameters.get("name") != null){
				String activityName = (String)parameters.get("name");
				String latitude = (String)parameters.get("lat");
				String longitude = (String)parameters.get("lon");
				String geoId = (String)parameters.get("geoId");
				
				initializeActivity(am.getObject(), activityName, latitude, longitude, geoId);
			}
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

	/**
	 * Method used to initialize an Activity/ActivityVersion with
	 * preset location information for the GIS module.
	 * This allows to add activities by clicking on a map
	 *
	 * @param activity the activity
	 * @param activityName the new activity name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param geoId the geoId/geoCode for the location
	 */

	private void initializeActivity(AmpActivityVersion activity,
			String activityName, String latitude, String longitude, String geoId) {

		AmpActivityLocation actLoc = new AmpActivityLocation();
		AmpLocation ampLoc = LocationUtil.getAmpLocationByGeoCode(geoId);
		// This check is necessary to avoid an exception if the location doesn't
		// have geoCode
		if (ampLoc != null) {
			Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
			activity.setName(activityName);
			actLoc.setLatitude(latitude);
			actLoc.setLongitude(longitude);
			actLoc.setLocationPercentage(100f);
			actLoc.setLocation(ampLoc);
			locations.add(actLoc);
			activity.setLocations(locations);
		}

	}

}
