/**
* Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.digijava.module.aim.dbentity.OnepagerSection;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePager extends AmpHeaderFooter {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OnePager.class);
	//for test purposes, it will be removed !!
	private final static boolean DEBUG_ACTIVITY_LOCK = false;
    public static final String DONOR_FUNDING_SECTION_NAME = "Donor Funding";
    public static final String REGIONAL_FUNDING_SECTION_NAME = "Regional Funding";
    public static final String COMPONENTS_SECTION_NAME = "Components";

    protected AmpActivityModel am;
//	protected AmpActivityModel activityModelForSave;

	static OnepagerSection[] test = {
		new OnepagerSection("Identification", "org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature", 1, false),
		new OnepagerSection("Activity Internal IDs", "org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature", 2, false),
		new OnepagerSection("Planning", "org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature", 3, false),
		new OnepagerSection("Location", "org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature", 4, false, true, "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature"),
		new OnepagerSection("Program", "org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature", 5, false),
		new OnepagerSection("Cross Cutting Issues", "org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature", 6, false),
		new OnepagerSection("Sectors", "org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature", 7, false),
		new OnepagerSection(DONOR_FUNDING_SECTION_NAME, "org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature", 8, false),
		new OnepagerSection(REGIONAL_FUNDING_SECTION_NAME, "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature", 9, false),
		new OnepagerSection("Related Organizations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature", 10, false),
		new OnepagerSection(COMPONENTS_SECTION_NAME, "org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature", 11, false),
		new OnepagerSection("Structures", "org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature", 12, false),
		new OnepagerSection("Issues Section", "org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature", 13, false),
		new OnepagerSection("Regional Observations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature", 14, false),
		new OnepagerSection("Contacts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature", 15, false),
	    new OnepagerSection("Contracts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature", 16, false),
		new OnepagerSection("M&E", "org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature", 17, false),
		new OnepagerSection("Paris Indicators", "org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature", 18, false),
		new OnepagerSection("Related Documents", "org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature", 19, false),
		new OnepagerSection("Line Ministry Observations", "org.dgfoundation.amp.onepager.components.features.sections.AmpLineMinistryObservationsFormSectionFeature", 20, false)
		};
	public static final LinkedList<OnepagerSection> fList = new LinkedList<OnepagerSection>(Arrays.asList(test));
    public static final AtomicBoolean savedSections = new AtomicBoolean(false);
	protected AbstractReadOnlyModel<List<AmpComponentPanel>> listModel;
	private Component editLockRefresher;

	public Component getEditLockRefresher() {
		return editLockRefresher;
	}

	public static OnepagerSection findByName(String name){
		Iterator<OnepagerSection> it = fList.iterator();
		while (it.hasNext()) {
			OnepagerSection os = it.next();
			if (os.getClassName().compareTo(name) == 0)
				return os;
		}
		return null;
	}
	
	public static OnepagerSection findByPosition(int pos){
		Iterator<OnepagerSection> it = fList.iterator();
		while (it.hasNext()) {
			OnepagerSection os = it.next();
			if (os.getPosition() == pos)
				return os;
		}
		return null;
	}
	
	public OnePager() {
		super();
		PageParameters parameters = decodePageParameters(getRequest());
		AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
		session.reset();
		StringValue activityParam = parameters.get(OnePagerConst.ONEPAGER_URL_PARAMETER_ACTIVITY);
		String activityId = (activityParam==null ? null : activityParam.toString());
		boolean newActivity = false;
		if ((activityId == null) || (activityId.compareTo("new") == 0)){
			am = new AmpActivityModel();
			newActivity = true;
			
			am.getObject().setActivityCreator(session.getAmpCurrentMember());
			am.getObject().setTeam(session.getAmpCurrentMember().getAmpTeam());
			
			if(parameters.get("lat") != null && parameters.get("lat") != null && parameters.get("geoId") != null && parameters.get("name") != null){
				String activityName = parameters.get("name").toString();
				String latitude = parameters.get("lat").toString();
				String longitude = parameters.get("lon").toString();
				String geoId = parameters.get("geoId").toString();
				
				initializeActivity(am.getObject(), activityName, latitude, longitude, geoId);
			}
		}
		else{
			//try to acquire lock for activity editing
			String key = ActivityGatekeeper.lockActivity(activityId, ((AmpAuthWebSession)getSession()).getCurrentMember().getMemberId());
			if (key == null){ //lock not acquired
				//redirect page
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(ActivityGatekeeper.buildRedirectLink(activityId)));
				//return;
			}
			
			am = new AmpActivityModel(Long.valueOf(activityId), key);
		}
		
		
		PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, session.getCurrentMember());
		PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.ACTIVITY, am.getObject());

		try {
			initializeFormComponents(am);
			AmpActivityFormFeature formFeature= new AmpActivityFormFeature("activityFormFeature", am, "Activity Form", newActivity, listModel);
			add(formFeature);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		
		if (DEBUG_ACTIVITY_LOCK)
			editLockRefresher = new Label("editLockRefresher", "Locked [" + am.getEditingKey() + "] at:" + System.currentTimeMillis());
		else
			editLockRefresher = new WebMarkupContainer("editLockRefresher");
		if (!newActivity){
			editLockRefresher.add(new AbstractAjaxTimerBehavior(ActivityGatekeeper.getRefreshInterval()){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean canCallListenerInterface(Component component,
						Method method) {
					return true;
				}
				
				@Override
				protected void onTimer(AjaxRequestTarget target) {
					Integer refreshStatus = ActivityGatekeeper.refreshLock(String.valueOf(am.getId()), am.getEditingKey(), ((AmpAuthWebSession)getSession()).getCurrentMember().getMemberId());
					if (editLockRefresher.isEnabled() && refreshStatus.equals(ActivityGatekeeper.REFRESH_LOCK_LOCKED))
						getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(ActivityGatekeeper.buildRedirectLink(String.valueOf(am.getId()))));
					
					if (DEBUG_ACTIVITY_LOCK){
						if (refreshStatus.equals(ActivityGatekeeper.REFRESH_LOCK_LOCKED))
							editLockRefresher.setDefaultModelObject("FAILED to refresh lock!");
						else
							editLockRefresher.setDefaultModelObject("Locked [" + am.getEditingKey() + "] at:" + System.currentTimeMillis());
						target.add(editLockRefresher);
					}
				}
			});
		}
		else
			if (DEBUG_ACTIVITY_LOCK)
				editLockRefresher.setDefaultModelObject("");
		add(editLockRefresher);
	}
	
	/**
     * Decodes a URL in the form:
     * 
     * /mountpoint/paramName1/paramValue1/paramName2/paramValue2 
     * 
     * (i.e. a URL using the pre wicket 1.5 Hybrid URL strategy)
     */
    public PageParameters decodePageParameters(Request request)
    {
        PageParameters parameters = new PageParameters();

        Iterator<String> segment = request.getUrl().getSegments().iterator();
        if (segment.hasNext())
        	segment.next();
        
        while (segment.hasNext()){
        	String key = segment.next();
        	String value = null;
        	if (segment.hasNext())
        		value = segment.next();
        	if (value != null)
        		parameters.add(key, value);
        }
        
        return parameters.isEmpty() ? null : parameters;
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
	
	public void initializeFormComponents(final IModel<AmpActivityVersion> am) throws Exception {
        loadPositions(fList);
        checkOrder(fList);
        saveOnce(fList);
		Collections.sort(fList, new Comparator<OnepagerSection>() {
			@Override
			public int compare(OnepagerSection o1, OnepagerSection o2) {
				return o1.getPosition() - o2.getPosition();
			}
		});
		listModel = new AbstractReadOnlyModel<List<AmpComponentPanel>>() {
			private static final long serialVersionUID = 1L;
			private List<AmpComponentPanel> list = null;
			private AmpComponentPanel initObject(OnepagerSection os, LinkedList<OnepagerSection> features, HashMap<String, AmpComponentPanel> temp){
				AmpComponentPanel existing = temp.get(os.getClassName());
				if (existing != null)
					return existing;
				
				AmpComponentPanel dep = null;
				if (os.getDependent()){
					Iterator<OnepagerSection> it = features.iterator();
					OnepagerSection depOs = null;
					while (it.hasNext()) {
						OnepagerSection tmpos = it
								.next();
						if (tmpos.getClassName().compareTo(os.getDependentClassName()) == 0){
							depOs = tmpos;
							break;
						}
					}
					dep = initObject(depOs, features, temp);
				}
				
				Class clazz = null;
				try {
					clazz = Class.forName(os.getClassName());
				} catch (ClassNotFoundException e) {
					logger.error("Can't find class for section:" + os.getName(), e);
				}
				Constructor constructor = null;
				try {
					if (os.getDependent())
						constructor = clazz.getConstructor(String.class, String.class, IModel.class, AmpComponentPanel.class);
					else
						constructor = clazz.getConstructor(String.class, String.class, IModel.class);
					
					AmpComponentPanel feature = null;
					if (os.getDependent())
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am, dep);
					else
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am);
					
					if (AmpFormSectionFeaturePanel.class.isAssignableFrom(feature.getClass())){
						AmpFormSectionFeaturePanel fsfp = (AmpFormSectionFeaturePanel) feature;
						fsfp.setFolded(os.getFolded());
					}
					
					temp.put(os.getClassName(), feature);
					feature.setOutputMarkupId(true);
					return feature;
				} catch (Exception e) {
					logger.error("Can't init constructor for section:" + os.getName(), e);
					return null;
				}
				
			}
			
			public List<AmpComponentPanel> initObjects(){
				Iterator<OnepagerSection> it = fList.iterator();
				LinkedList<AmpComponentPanel> ret = new LinkedList<AmpComponentPanel>();
				HashMap<String, AmpComponentPanel> temp = new HashMap<String, AmpComponentPanel>();
				while (it.hasNext()) {
					OnepagerSection os = it
							.next();
					AmpComponentPanel fet = initObject(os, fList, temp);
					ret.add(fet);
				}
				return ret;
			}
			
			@Override
			public List<AmpComponentPanel> getObject() {
				if (list == null)
					list = initObjects();
				
				return list;
			}
		};
	}

    private void saveOnce(List<OnepagerSection> fList) {
        //Only update the whole collection one time at startup in order to save new sections
        if (savedSections.compareAndSet(false, true)){
            try {
                Session session = PersistenceManager.getRequestDBSession();
                for (OnepagerSection os : fList)
                    session.saveOrUpdate(os);
            } catch (DgException e) {
                logger.error("Can't save onepager sections: ", e);
            }
        }
    }

    private void checkOrder(List<OnepagerSection> fList) {
        TreeSet<Integer> positions = new TreeSet<Integer>();
        for (OnepagerSection os : fList){
            if (positions.contains(os.getPosition())){
                Integer newPosition = positions.last() + 1;
                positions.add(newPosition);
                os.setPosition(newPosition);
            }
            else
                positions.add(os.getPosition());
        }
    }

    private void loadPositions(LinkedList<OnepagerSection> fList) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
        } catch (DgException e) {
            logger.error("Can't load onepager section positions:", e);
            return;
        }
        Criteria c = session.createCriteria(OnepagerSection.class);
        c.setCacheable(true);
        List<OnepagerSection> results = c.list();
        List<OnepagerSection> returnList = new LinkedList<OnepagerSection>();
        for (OnepagerSection localOs : fList) {
            boolean found = false;
            for (OnepagerSection savedOs : results){
                if (localOs.getClassName().equals(savedOs.getClassName())){
                    //load transient fields
                    savedOs.load(localOs);
                    found = true;
                    returnList.add(savedOs);
                    break;
                }
            }
            if (!found)
                returnList.add(localOs);
        }
        fList.clear();
        fList.addAll(returnList);
    }
}
