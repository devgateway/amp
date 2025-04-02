/**
* Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpAidEffectivenessFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpBudgetStructureFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpGPINiFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLineMinistryObservationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.OnepagerSection;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePager extends AmpHeaderFooter {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(OnePager.class);
    //for test purposes, it will be removed !!
    private final static boolean DEBUG_ACTIVITY_LOCK = false;
    public static final String DONOR_FUNDING_SECTION_NAME = "Funding";
    public static final String REGIONAL_FUNDING_SECTION_NAME = "Regional Funding";
    public static final String COMPONENTS_SECTION_NAME = "Components";

    protected AmpActivityModel am;
//  protected AmpActivityModel activityModelForSave;

    static OnepagerSection[] staticOnepagerSectionList = {
        new OnepagerSection("Identification",AmpIdentificationFormSectionFeature.class.getName(), 1, false),
        new OnepagerSection("Activity Internal IDs", AmpInternalIdsFormSectionFeature.class.getName(), 2, false),
        new OnepagerSection("Planning", AmpPlanningFormSectionFeature.class.getName(), 3, false),
        new OnepagerSection("Location",AmpLocationFormSectionFeature.class.getName(), 4, false, true, AmpRegionalFundingFormSectionFeature.class.getName()),
        new OnepagerSection("Program", AmpProgramFormSectionFeature.class.getName(), 5, false),
        new OnepagerSection("Cross Cutting Issues", AmpCrossCuttingIssuesFormSectionFeature.class.getName(), 6, false),
        new OnepagerSection("Sectors", AmpSectorsFormSectionFeature.class.getName(), 7, false),
        new OnepagerSection(DONOR_FUNDING_SECTION_NAME, AmpDonorFundingFormSectionFeature.class.getName(), 9, false),
        new OnepagerSection("Organizations", AmpRelatedOrganizationsFormSectionFeature.class.getName(), 8, false,true, AmpDonorFundingFormSectionFeature.class.getName()),
        new OnepagerSection(REGIONAL_FUNDING_SECTION_NAME, AmpRegionalFundingFormSectionFeature.class.getName(), 10, false),
        new OnepagerSection(COMPONENTS_SECTION_NAME, AmpComponentsFormSectionFeature.class.getName(), 11, false),
        new OnepagerSection("Structures", AmpStructuresFormSectionFeature.class.getName(), 12, false),
        new OnepagerSection("Issues Section", AmpIssuesFormSectionFeature.class.getName(), 13, false),
        new OnepagerSection("Regional Observations", AmpRegionalObservationsFormSectionFeature.class.getName(), 14, false),
        new OnepagerSection("Contacts", AmpContactsFormSectionFeature.class.getName(), 15, false),
        new OnepagerSection("Contracts", AmpContractingFormSectionFeature.class.getName(), 16, false),
        new OnepagerSection("M&E", AmpMEFormSectionFeature.class.getName(), 17, false),
        new OnepagerSection("Paris Indicators", AmpPIFormSectionFeature.class.getName(), 18, false),
        new OnepagerSection("Related Documents", AmpResourcesFormSectionFeature.class.getName(), 19, false),
        new OnepagerSection("Line Ministry Observations", AmpLineMinistryObservationsFormSectionFeature.class.getName(), 20, false),
        new OnepagerSection("Budget Structure", AmpBudgetStructureFormSectionFeature.class.getName(), 21, false),
        new OnepagerSection("GPI", AmpGPIFormSectionFeature.class.getName(), 22, false),
        new OnepagerSection("GPI 2017", AmpGPINiFormSectionFeature.class.getName(), 23, false),
        new OnepagerSection("Aid Effectivenes", AmpAidEffectivenessFormSectionFeature.class.getName(), 24, false)

        };
    public static final AtomicBoolean savedSections = new AtomicBoolean(false);
    public static final List<OnepagerSection> sectionsList = Collections.synchronizedList(loadPositions());
    protected AbstractReadOnlyModel<List<AmpComponentPanel>> listModel;
    private Component editLockRefresher;
    private AbstractAjaxTimerBehavior timer;
    public Component getEditLockRefresher() {
        return editLockRefresher;
    }


    public AbstractAjaxTimerBehavior getTimer() {
        return timer;
    }


    public static OnepagerSection findByName(String name){
        Iterator<OnepagerSection> it = sectionsList.iterator();
        while (it.hasNext()) {
            OnepagerSection os = it.next();
            if (os.getClassName().compareTo(name) == 0)
                return os;
        }
        return null;
    }

    public static OnepagerSection findByPosition(int pos){
        Iterator<OnepagerSection> it = sectionsList.iterator();
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
        if (activityParam.toString() == null){
            activityParam = parameters.get(OnePagerConst.ONEPAGER_URL_PARAMETER_SSC);
            if (activityParam.toString() != null)
                session.setFormType(ActivityUtil.ACTIVITY_TYPE_SSC);
        }

        if (activityParam.toString() == null)
            throw new AssertionError("unsuported form type");

        String activityId = activityParam.toString();
        final ValueWrapper<Boolean>newActivity= new ValueWrapper<Boolean>(false);

        long currentUserId = ((AmpAuthWebSession) getSession()).getCurrentMember().getMemberId();

        if ((activityId == null) || (activityId.compareTo("new") == 0)) {
            am = new AmpActivityModel();

            newActivity.value = true;

            PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, session.getCurrentMember());
            PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.ACTIVITY, am.getObject());


            am.getObject().setActivityCreator(session.getAmpCurrentMember());
            am.getObject().setTeam(session.getAmpCurrentMember().getAmpTeam());
            am.getObject().setActivityType(session.getFormType());

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
            String key = ActivityGatekeeper.lockActivity(activityId, currentUserId);
            if (key == null){ //lock not acquired
                //redirect page
                throw new RedirectToUrlException(ActivityGatekeeper.buildRedirectLink(activityId, currentUserId));
                //return;
            }
            if (!ActivityGatekeeper.allowedToEditActivity(activityId))
                throw new RedirectToUrlException(ActivityGatekeeper.buildPermissionRedirectLink(activityId));

            am = new AmpActivityModel(Long.valueOf(activityId), key);

            //check the permissions
            PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, session.getCurrentMember());
            PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.ACTIVITY, am.getObject());

            // -----> TODO-CONSTANTIN: comment lines below if you want to disable Permissions checking   <-----
            boolean canDo = am.getObject().canDo(GatePermConst.Actions.EDIT, PermissionUtil.getScope(session.getHttpSession()));
            if (!canDo) {
                throw new RedirectToUrlException(ActivityGatekeeper.buildPermissionRedirectLink(activityId));
            }
        }

        if (!am.getObject().getActivityType().equals(session.getFormType())) {
            throw new AssertionError("Form type is not compatible with activity type!");
        }

        try {
            initializeFormComponents(am);
            AmpActivityFormFeature formFeature= new AmpActivityFormFeature("activityFormFeature", am, "Activity Form", newActivity.value, listModel);
            add(formFeature);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if (DEBUG_ACTIVITY_LOCK) {
            editLockRefresher = new Label("editLockRefresher", "Locked [" + am.getEditingKey() + "] at:"
                    + System.currentTimeMillis());
        } else {
            editLockRefresher = new WebMarkupContainer("editLockRefresher");
        }
        timer = new AbstractAjaxTimerBehavior(ActivityGatekeeper.getRefreshInterval()) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean canCallListenerInterface(Component component, Method method) {
                return true;
            }

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (!editLockRefresher.isEnabled()) {
                    this.stop(target);
                }
                if (!newActivity.value) {
                    // we only refresh the lock if its not a new activity
                    long currentUserId = ((AmpAuthWebSession) getSession()).getCurrentMember().getMemberId();
                    Integer refreshStatus = ActivityGatekeeper.refreshLock(String.valueOf(am.getId()),
                            am.getEditingKey(), currentUserId);
                    if (editLockRefresher.isEnabled() && refreshStatus.equals(ActivityGatekeeper.REFRESH_LOCK_LOCKED))
                        throw new RedirectToUrlException(ActivityGatekeeper.buildRedirectLink(
                                String.valueOf(am.getId()), currentUserId));

                    if (DEBUG_ACTIVITY_LOCK) {
                        if (refreshStatus.equals(ActivityGatekeeper.REFRESH_LOCK_LOCKED))
                            editLockRefresher.setDefaultModelObject("FAILED to refresh lock!");
                        else
                            editLockRefresher.setDefaultModelObject("Locked [" + am.getEditingKey() + "] at:"
                                    + System.currentTimeMillis());
                        target.add(editLockRefresher);
                    }
                }
                // AMP-19698
                // keep alive jdbc connection
                AmpActivityModel.getHibernateSession().doWork(new Work() {
                    @Override
                    public void execute(Connection connection) throws SQLException {
                        try {
                            java.sql.Statement stm = connection.createStatement();
                            java.sql.ResultSet rs = stm.executeQuery("select 1");
                            if (rs.next())
                                ;
                            rs.close();
                            stm.close();
                        } catch (Exception e) {
                            //this is a ajax triger and the connectionas has already been closed. so its good to
                            //ignore the exception
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
            }
        };
        editLockRefresher.add(timer);
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
        AmpCategoryValueLocations ampLoc = LocationUtil.getAmpLocationByGeoCode(geoId);
        // This check is necessary to avoid an exception if the location doesn't have geoCode
        if (ampLoc != null) {
            activity.setName(activityName);
            actLoc.setLatitude(latitude);
            actLoc.setLongitude(longitude);
            actLoc.setLocationPercentage(100f);
            actLoc.setLocation(ampLoc);
            activity.getLocations().add(actLoc);
        }

        //we set the default value for activity budget if configured as a global setting
        Integer defaultActivityBudgetId = FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.DEFAULT_VALUE_FOR_ACTIVITY_BUDGET);
        if (defaultActivityBudgetId != null && defaultActivityBudgetId!=-1) {
            AmpCategoryValue defaultAmpCategoryValue = CategoryManagerUtil
                    .getAmpCategoryValueFromDb(defaultActivityBudgetId.longValue());
            if (defaultAmpCategoryValue != null) {
                if (activity.getCategories() == null) {
                    am.getObject().setCategories(new HashSet<AmpCategoryValue>());
                    activity.getCategories().add(defaultAmpCategoryValue);
                }
            }
        }

    }

    public void initializeFormComponents(final IModel<AmpActivityVersion> am) throws Exception {
        listModel = new AbstractReadOnlyModel<List<AmpComponentPanel>>() {
            private static final long serialVersionUID = 1L;
            private List<AmpComponentPanel> list = null;
            private AmpComponentPanel initObject(OnepagerSection os, List<OnepagerSection> features, HashMap<String, AmpComponentPanel> temp){
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
                        constructor = clazz.getConstructor(String.class, String.class, IModel.class,
                                AmpComponentPanel.class);
                    else
                        constructor = clazz.getConstructor(String.class, String.class, IModel.class);

                    AmpComponentPanel feature = null;
                    if (os.getDependent()) {
                        feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am, dep);
                    } else {
                        feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am);
                    }
                    if (AmpFormSectionFeaturePanel.class.isAssignableFrom(feature.getClass())) {
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

            public List<AmpComponentPanel> initObjects() {
                Iterator<OnepagerSection> it = sectionsList.iterator();
                LinkedList<AmpComponentPanel> ret = new LinkedList<AmpComponentPanel>();
                HashMap<String, AmpComponentPanel> temp = new HashMap<String, AmpComponentPanel>();
                while (it.hasNext()) {
                    OnepagerSection os = it
                            .next();
                    AmpComponentPanel fet = initObject(os, sectionsList, temp);
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

    private static void saveOnce(Session session, List<OnepagerSection> list) {
        //Only update the whole collection one time at startup in order to save new sections
        if (savedSections.compareAndSet(false, true)){
            for (OnepagerSection os : list)
                session.saveOrUpdate(os);
        }
    }

    private static void checkOrder(List<OnepagerSection> list) {
        TreeSet<Integer> positions = new TreeSet<Integer>();
        for (OnepagerSection os : list){
            if (positions.contains(os.getPosition())){
                Integer newPosition = positions.last() + 1;
                positions.add(newPosition);
                os.setPosition(newPosition);
            }
            else
                positions.add(os.getPosition());
        }
    }

    public static void sortSections(List<OnepagerSection> list){
        Collections.sort(list, new Comparator<OnepagerSection>() {
            @Override
            public int compare(OnepagerSection o1, OnepagerSection o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
    }

    private static List<OnepagerSection> loadPositions() {
        Session session = null;
        try {
            session = PersistenceManager.openNewSession();
            session.beginTransaction();
            Criteria c = session.createCriteria(OnepagerSection.class);
            c.setCacheable(true);
            List<OnepagerSection> results = c.list();
            List<OnepagerSection> returnList = new LinkedList<OnepagerSection>();
            for (OnepagerSection localOs : staticOnepagerSectionList) {
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

            checkOrder(returnList);
            saveOnce(session, returnList);
            sortSections(returnList);
            return returnList;
        } catch (Exception e) {
            logger.error("Can't load onepager section positions:", e);
            session.getTransaction().rollback();
            return null;
        } finally {
           PersistenceManager.closeSession(session);
        }
    }

    public AmpActivityModel getActivityModel() {
        return am;
    }
}
