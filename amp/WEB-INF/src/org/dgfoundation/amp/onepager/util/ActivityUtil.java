/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.util;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Bytes;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.upload.FileItemEx;
import org.dgfoundation.amp.onepager.helper.*;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.*;
import org.digijava.module.contentrepository.exception.JCRSessionException;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.message.triggers.ActivityValidationWorkflowTrigger;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.util.ProjectUtil;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.digijava.module.aim.auth.AmpPostLoginAction.doActualTruBudgetLogin;
import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;

/**
 * Util class used to manipulate an activity
 *
 * @author aartimon@dginternational.org
 * @since Jun 17, 2011
 */
public class ActivityUtil {
    private static final Logger logger = Logger.getLogger(ActivityUtil.class);

    /**
     * types for {@link org.digijava.module.aim.dbentity.AmpActivityFields#activityType}
     */
    public static final Long ACTIVITY_TYPE_PROJECT = 0L;
    public static final Long ACTIVITY_TYPE_SSC = 1L;

    /**
     * Method used to save an Activity/ActivityVersion depending
     * on activation of versioning option
     *
     * @param am
     */
    public static void saveActivity(AmpActivityModel am, boolean draft, boolean rejected) {

        AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
        if (!wicketSession.getLocale().getLanguage().equals(TLSUtils.getLangCode())) {
            logger.error("WRONG LANGUAGE: TLSUtils(" + TLSUtils.getLangCode() + ") vs Wicket(" + wicketSession.getLocale().getLanguage() + ")");
        }


        AmpTeamMember ampCurrentMember = wicketSession.getAmpCurrentMember();

        ServletContext sc = wicketSession.getHttpSession().getServletContext();

        AmpActivityVersion oldA = am.getObject();

        AmpActivityVersion newA = saveActivity(oldA, am.getTranslationHashMap().values(), ampCurrentMember, wicketSession.getSite(), wicketSession.getLocale(), sc.getRealPath("/"), draft, SaveContext.activityForm(rejected));

        am.setObject(newA);

        ActivityGatekeeper.unlockActivity(String.valueOf(am.getId()), am.getEditingKey());
        AmpActivityModel.endConversation();
    }

    /**
     * Method used to save an Activity/ActivityVersion depending
     * on activation of versioning option
     *
     * @param oldA
     * @param values
     * @param ampCurrentMember
     * @param site
     * @param locale
     * @param rootRealPath
     * @param draft
     */
    public static AmpActivityVersion saveActivity(AmpActivityVersion oldA, Collection<AmpContentTranslation> values, AmpTeamMember ampCurrentMember, Site site, Locale locale, String rootRealPath, boolean draft, SaveContext saveContext) {
        Session session;
        EditorStore editorStore;
        if (saveContext.getSource() == ActivitySource.ACTIVITY_FORM) {
            session = AmpActivityModel.getHibernateSession();

            editorStore = getSessionEditorStore();
        } else {
            session = PersistenceManager.getSession();

            editorStore = new EditorStore();
        }

        boolean newActivity = oldA.getAmpActivityId() == null;
        AmpActivityVersion a = null;
        List<AmpContentTranslation> cumulativeValues = new ArrayList<>();
        try {
            a = saveActivityNewVersion(oldA, values, cumulativeValues, ampCurrentMember, draft, session, saveContext,
                    editorStore, site);
        } catch (Exception exception) {
            logger.error("Error saving activity:", exception); // Log the exception
            throw new RuntimeException("Can't save activity:", exception);
        }

        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(a.getApprovalStatus())) {
            new ActivityValidationWorkflowTrigger(a);
        }

        LuceneUtil.addUpdateActivity(rootRealPath, !newActivity, site, locale, a, oldA, cumulativeValues);

        return a;
    }

    public static void prepareToSave(AmpActivityVersion a, AmpActivityVersion oldA,
                                     AmpTeamMember ampCurrentMember, boolean draft, SaveContext context) {
        boolean newActivity = isNewActivity(oldA == null ? a : oldA);

        updateModifyCreateInfo(a, ampCurrentMember, newActivity);

        if (newActivity) {
            a.setTeam(a.getActivityCreator().getAmpTeam());
        }

        if (context.isUpdateActivityStatus()) {
            setActivityStatus(ampCurrentMember, draft, a, oldA, newActivity, context.isRejected());
        }
    }

    public static AmpActivityVersion saveActivityNewVersion(AmpActivityVersion a,
                                                            Collection<AmpContentTranslation> translations, List<AmpContentTranslation> cumulativeTranslations,
                                                            AmpTeamMember ampCurrentMember, boolean draft,
                                                            Session session, SaveContext context, EditorStore editorStore, Site site) throws Exception {

        boolean draftChange = detectDraftChange(a, draft);
        return saveActivityNewVersion(a, translations, cumulativeTranslations, ampCurrentMember, draft,
                draftChange, session, context, editorStore, site);
    }

    /**
     * saves a new version of an activity
     * returns newActivity
     */
    public static AmpActivityVersion saveActivityNewVersion(AmpActivityVersion a,
                                                            Collection<AmpContentTranslation> translations, List<AmpContentTranslation> cumulativeTranslations,
                                                            AmpTeamMember ampCurrentMember, boolean draft,
                                                            boolean draftChange, Session session, SaveContext context,
                                                            EditorStore editorStore, Site site) throws Exception {

        AmpActivityVersion oldA = a;
        boolean newActivity = isNewActivity(a);
        logger.info("Project old name: "+a.getName());
        String name = a.getName();

        a.setDraft(draft);
        a.setDeleted(false);
        //we will check what is comming in funding
        Set<AmpFunding> af = a.getFunding();

        //check if we have funding items with null in ammount
        //this is not a valid use case but a possible due to the flexibility of the configurations in FM mode
        if (af != null && Hibernate.isInitialized(af)) {
            updateFundingDetails(af);
            updateFundingProjectResults(af);
        }

        if (ContentTranslationUtil.multilingualIsEnabled())
            ContentTranslationUtil.cloneTranslations(a, translations, cumulativeTranslations);

        //is versioning activated?
        boolean createNewVersion = (draft == draftChange) && ActivityVersionUtil.isVersioningEnabled();
        boolean isActivityForm = context.getSource() == ActivitySource.ACTIVITY_FORM;
        if (oldA.getAmpActivityId() != null)
            session.evict(oldA);
//        Set<AmpComponent> components = a.getComponents();
//        Set<AmpFunding> funding = a.getFunding();
//        Set<AmpOrgRole> orgRoles = a.getOrgrole();
        if (createNewVersion) {
            try {
                AmpActivityGroup tmpGroup = a.getAmpActivityGroup();

                a = ActivityVersionUtil.cloneActivity(a);
//                if (a.getAmpActivityId()!=null) {//do this only on update
//                    a.setComponents(components);
//                    a.setFunding(funding);
//                    a.setOrgrole(orgRoles);
//                }
                //keeping session.clear() only for acitivity form as it was before
                if (isActivityForm)
                    session.clear();
                a.setMember(new HashSet<>());
                if (tmpGroup == null) {
                    //we need to create a group for this activity
                    tmpGroup = new AmpActivityGroup();
                    tmpGroup.setAmpActivityLastVersion(a);

                    //TODO this is a temporary status for the case when we have a new activity and we want to create a new version
                    a.setApprovalStatus(ApprovalStatus.created);
                    Long id = (Long) session.save(tmpGroup);
                    tmpGroup.setAmpActivityGroupId(id);
                    a.setAmpActivityGroup(tmpGroup);
                    if (a.getAmpActivityId() == null)
                        session.save(a);
                    else
                        session.merge(a);

                }
//                session.flush();

                session.flush();
//                session.clear();

            } catch (CloneNotSupportedException e) {
                logger.error("Can't clone current Activity: ", e);
            }
        }
        logger.info("Object before prepare :" + a);

        if (context.isPrepareToSave()) {
            prepareToSave(a, oldA, ampCurrentMember, draft, context);
        }
        logger.info("Object after prepare :" + a);

        if (a.getAmpActivityGroup() == null) {
            //we need to create a group for this activity
            AmpActivityGroup tmpGroup = new AmpActivityGroup();
            tmpGroup.setAmpActivityLastVersion(a);
            session.save(tmpGroup);
//            tmpGroup.setAmpActivityGroupId(id);
            a.setAmpActivityGroup(tmpGroup);

        }
//        session.flush();

        setCreationTimeOnStructureImages(a);

        AmpActivityGroup group = a.getAmpActivityGroup();
        if (group.getAmpActivityGroupId() == null) {
            throw new RuntimeException("Non-existent group should have been added by now!");
        }

        if (!newActivity) {
            session.clear();
            //existing activity
            //previousVersion for current activity
            if (group.getAmpActivityLastVersion().getAmpActivityId().equals(a.getAmpActivityId())) {
                forceVersionIncrement(session, group);
            }
            group.setAmpActivityLastVersion(a);
            session.merge(group);

        }
//        for (AmpOrgRole ampOrgRole: a.getOrgrole()){
//            session.saveOrUpdate(ampOrgRole);
//        }

        a.setAmpActivityGroup(group);
        updateMultiStakeholderField(a);
        if (isActivityForm) {
            saveActivityResources(a, session);
            saveActivityGPINiResources(a, session);
            saveComments(a, session, draft);
        } else {
            updateFiscalYears(a);
        }

        saveEditors(session, createNewVersion, editorStore, site);

        saveAgreements(a, session, isActivityForm);
        saveContacts(a, session, (draft != draftChange), ampCurrentMember);

        updateComponentFunding(a);
        saveAnnualProjectBudgets(a, session);
        saveProjectCosts(a, session);
        saveStructures(a, session);
        if (createNewVersion) {
            if (a.getAmpActivityId() == null)
                session.save(a);
            else {
               cleanObjectFromSession(session,AmpActivityVersion.class, a.getAmpActivityId());
                session.saveOrUpdate(a);
            }
        } else {
//            session.saveOrUpdate(a);
            session.merge(a);
        }
        session.flush();

        updatePerformanceRules(oldA, a);

        if (newActivity) {
            a.setAmpId(org.digijava.module.aim.util.ActivityUtil.generateAmpId(ampCurrentMember.getUser(), a.getAmpActivityId(), session));
            session.merge(a);
        }

        updateIndirectPrograms(a, session);

        logAudit(ampCurrentMember, a, newActivity);
//        updateComponents(a,session);
        session.flush();
//        session.clear();
//        session.getTransaction().commit();
//        session.refresh(a);

//        a.setComponents(new HashSet<>(query.list()));
        if (getSettingValue(getGlobalSettingsBySection("trubudget"),"isEnabled").equalsIgnoreCase("true")&&TeamUtil.getCurrentUser().getTruBudgetEnabled()) {
            // TODO: 9/12/23 check if project is already existing
            Query<AmpComponent> query = session.createQuery("FROM "+AmpComponent.class.getName()+" ac  WHERE ac.activity=:activity AND ac.activity IS NOT NULL", AmpComponent.class).setCacheable(true);
            query.setParameter("activity", a.getAmpActivityId(), LongType.INSTANCE);
            ProjectUtil.init();
            TruBudgetActivity truBudgetActivity = ProjectUtil.activityAlreadyInTrubudget(a.getAmpActivityId());
            logger.info("TrubudgetActivity found "+truBudgetActivity);
            if (truBudgetActivity==null) {
                ProjectUtil.createProject(a,query.list(),name);
            }
            else
            {
                //update project
                logger.info("Project name: "+a.getName());
                ProjectUtil.updateProject(truBudgetActivity.getTruBudgetId(),a,query.list(),name);
//                session.flush();
            }
            new Thread(ProjectUtil::end).start();

        }



        return a;
    }

    private static  void updateComponents(AmpActivityVersion ampActivityVersion, Session session)
    {
        Query<AmpComponent> query = session.createQuery("FROM "+AmpComponent.class.getName()+" ac  WHERE ac.activity=:activity AND ac.activity IS NOT NULL", AmpComponent.class);
        query.setParameter("activity", ampActivityVersion.getAmpActivityId(), LongType.INSTANCE);
        //            z.setActivity(null);//we can also delete this component permanently
//        query.stream().filter(x->!ampActivityVersion.getComponents().stream().map(AmpComponent::getAmpComponentId).collect(Collectors.toList()).contains(x.getAmpComponentId()) && x.getAmpComponentId()!=null).forEach(y->y.setActivity(null));
        for (AmpComponent ampComponent: query.list()){
            if (ampActivityVersion.getComponents().contains(ampComponent))
            {
                ampComponent.setActivity(null);
                session.update(ampComponent);
            }
        }
    }
    private static <T> void cleanObjectFromSession(Session session, Class<T> objectClass, Long id)
    {
        T object = session.get(objectClass, id);
        if (!isNull(object))
        {
            session.evict(object);
        }
    }

    private static void updateMultiStakeholderField(AmpActivityVersion a) {
        if (!Boolean.TRUE.equals(a.getMultiStakeholderPartnership())) {
            a.setMultiStakeholderPartners(null);
        }
    }

    private static void updateIndirectPrograms(AmpActivityVersion a, Session session) {
        new IndirectProgramUpdater().updateIndirectPrograms(a, session);
    }

    public static boolean detectDraftChange(AmpActivityVersion a, boolean draft) {
        return Boolean.TRUE.equals(a.getDraft()) != draft;
    }

    public static <T extends AmpActivityFields> boolean isNewActivity(T a) {
        // it would be nicer to rely upon AMP ID, but some old activities may lack it
        return a.getAmpActivityId() == null;
    }

    /**
     * To be used every time when the activity is saved/updated
     *
     * @param activity
     * @param teamMember
     */
    public static void updateModifyCreateInfo(AmpActivityVersion activity, AmpTeamMember teamMember,
                                              boolean newActivity) {
        Date updateDate = Calendar.getInstance().getTime();

        activity.setUpdatedDate(updateDate);
        activity.setModifiedDate(updateDate);

        if (teamMember == null) {
            throw new RuntimeException("Modified team member cannot be null");
        }

        activity.setModifiedBy(teamMember);

        if (newActivity) {
            activity.setCreatedDate(updateDate);
            // when activity is imported from AMP Offline, creator will be set and can be different from modifier
            if (activity.getActivityCreator() == null) {
                activity.setActivityCreator(teamMember);
            }
        }
    }

    private static void logAudit(AmpTeamMember teamMember, AmpActivityVersion activity, boolean newActivity) {
        String additionalDetails = determineDetails(teamMember, activity, newActivity);
        TeamMember tm = teamMember.toTeamMember();
        if (!newActivity) {
            AuditLoggerUtil.logActivityUpdate(tm, activity, Collections.singletonList(additionalDetails));
        } else {
            try {
                AuditLoggerUtil.logObject(tm, activity, "add", additionalDetails);
            } catch (DgException e) {
                e.printStackTrace();
            }
        }
    }

    private static String determineDetails(AmpTeamMember teamMember, AmpActivityVersion activity, boolean newActivity) {
        String additionalDetails = "approved";

        Long teamId = teamMember.getAmpTeam().getAmpTeamId();
        String validation = org.digijava.module.aim.util.DbUtil.getValidationFromTeamAppSettings(teamId);

        if (activity.getDraft() != null) {
            if (!activity.getDraft() && !(Constants.PROJECT_VALIDATION_OFF.equals(validation))) {
                if (!isApproved(activity)
                        && (Constants.PROJECT_VALIDATION_FOR_ALL_EDITS.equals(validation) || newActivity)) {
                    additionalDetails = "pending approval";
                }
            } else if (activity.getDraft()) {
                additionalDetails = "draft";
            }
        }

        return additionalDetails;
    }

    public static boolean isApproved(AmpActivityVersion activity) {
        ApprovalStatus approvalStatus = activity.getApprovalStatus();
        return ApprovalStatus.approved.equals(approvalStatus)
                || ApprovalStatus.startedapproved.equals(approvalStatus);
    }

    private static void updatePerformanceRules(AmpActivityVersion oldA, AmpActivityVersion a) {
        PerformanceRuleManager ruleManager = PerformanceRuleManager.getInstance();

        Set<AmpPerformanceRule> matchedRules = new HashSet<>();

        if (ruleManager.canActivityContainPerformanceIssues(a)) {
            matchedRules = ruleManager.getPerformanceRulesFromIssues(ruleManager.findPerformanceIssues(a));
        }

        ruleManager.deleteActivityPerformanceRule(PersistenceManager.getSession(), oldA.getAmpActivityId());
        ruleManager.updateActivityPerformanceRules(a.getAmpActivityId(), matchedRules);
    }

    private static void updateFiscalYears(AmpActivityVersion a) {
        Set<Long> actFiscalYears = a.getFiscalYears();

        if (actFiscalYears != null) {
            List<Long> fiscalYears = new ArrayList<>(actFiscalYears);
            fiscalYears.sort(Comparator.naturalOrder());
            a.setFY(StringUtils.join(fiscalYears, ","));
        }
    }

    /**
     * Since none of the AmpActivityGroup properties are changed hibernate does not automatically increment the
     * version. Yet activity can change and AmpActivityGroup would remains the same, thus forcing version
     * increment explicitly.
     */
    private static void forceVersionIncrement(Session session, AmpActivityGroup group) {
        session.buildLockRequest(new LockOptions(LockMode.OPTIMISTIC_FORCE_INCREMENT)).lock(group);
    }

    /**
     * Remove funding items with null amount (that means that the form is missconfigured)
     * Set updateDate for modified records. Set the parent funding for funding details
     *
     * @param fundings
     */
    private static void updateFundingDetails(Set<AmpFunding> fundings) {
        Date updatedDate = new Date();
        for (AmpFunding ampFunding : fundings) {
            if (ampFunding.getFundingDetails() != null && Hibernate.isInitialized(ampFunding.getFundingDetails())) {
                ampFunding.getFundingDetails().removeIf(afd -> afd.getTransactionAmount() == null);
                for (AmpFundingDetail afd : ampFunding.getFundingDetails()) {
                    if (calculateFundingDetailCheckSum(afd).equals(afd.getCheckSum())) {
                        afd.setUpdatedDate(updatedDate);
                    }
                    afd.setAmpFundingId(ampFunding);
                }
            }

            if (ampFunding.getMtefProjections() != null && Hibernate.isInitialized(ampFunding.getMtefProjections())) {
                ampFunding.getMtefProjections().removeIf(afm -> afm.getTransactionAmount() == null);
                for (AmpFundingMTEFProjection afm : ampFunding.getMtefProjections()) {
                    if (calculateFundingDetailCheckSum(afm).equals(afm.getCheckSum())) {
                        afm.setUpdatedDate(updatedDate);
                    }
                    afm.setAmpFunding(ampFunding);
                }
            }
        }
    }

    private static void updateFundingProjectResults(Set<AmpFunding> fundings) {
        for (AmpFunding funding : fundings) {
            if (!Boolean.TRUE.equals(funding.getProjectResultsAvailable())) {
                funding.setProjectResultsLink(null);
            }
        }
    }

    private static void setCreationTimeOnStructureImages(AmpActivityVersion activity) {
        if (activity.getStructures() != null) {
            for (AmpStructure str : activity.getStructures()) {
                if (str.getImages() != null) {
                    for (AmpStructureImg img : str.getImages()) {
                        img.setStructure(str);
                        img.setCreationTime(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    private static void setActivityStatus(AmpTeamMember ampCurrentMember, boolean savedAsDraft, AmpActivityFields a,
                                          AmpActivityVersion oldA, boolean newActivity, boolean rejected) {
        boolean teamLeadFlag = isApprover(ampCurrentMember);
        logger.info("Teamlead? " + teamLeadFlag);
        Boolean crossTeamValidation = ampCurrentMember.getAmpTeam().getCrossteamvalidation();
        boolean isSameWorkspace = ampCurrentMember.getAmpTeam().getAmpTeamId().equals(a.getTeam().getAmpTeamId());

        // Check if validation is ON in GS and APP Settings
        String validation = getValidationSetting(ampCurrentMember);
        if (isProjectValidationOn(validation)) {
            if (teamLeadFlag) {
                if (savedAsDraft) {
                    if (rejected) {
                        a.setApprovalStatus(ApprovalStatus.rejected);
                    } else {
                        if (newActivity) {
                            a.setApprovalStatus(ApprovalStatus.started);
                        } else {
                            if (oldA.getApprovalStatus() != null
                                    && ApprovalStatus.started.equals(oldA.getApprovalStatus()))
                                a.setApprovalStatus(ApprovalStatus.started);
                            else
                                a.setApprovalStatus(ApprovalStatus.edited);
                        }
                    }
                } else {
                    // If activity belongs to the same workspace where TL/AP is
                    // logged set it validated
                    if (isSameWorkspace) {
                        a.setApprovalStatus(ApprovalStatus.approved);
                        a.setApprovedBy(ampCurrentMember);
                        a.setApprovalDate(Calendar.getInstance().getTime());
                    } else {
                        /*
                         * If activity doesn't belongs to the same workspace
                         * where TL/AP is logged but cross team validation is on
                         * set it validated
                         */
                        if (crossTeamValidation) {
                            a.setApprovalStatus(ApprovalStatus.approved);
                            a.setApprovedBy(ampCurrentMember);
                            a.setApprovalDate(Calendar.getInstance().getTime());
                        } else {
                            if (ApprovalStatus.started.equals(oldA.getApprovalStatus())) {
                                a.setApprovalStatus(ApprovalStatus.started);
                            } else {
                                a.setApprovalStatus(ApprovalStatus.edited);
                            }
                        }
                    }
                }
            } else {
                if (Constants.PROJECT_VALIDATION_FOR_NEW_ONLY.equals(validation)) {
                    if (newActivity) {
                        // all the new activities will have the started status
                        a.setApprovalStatus(ApprovalStatus.started);
                    } else {
                        // if we edit an existing not validated status it will
                        // keep the old status - started
                        if (oldA.getApprovalStatus() != null
                                && ApprovalStatus.started.equals(oldA.getApprovalStatus()))
                            a.setApprovalStatus(ApprovalStatus.started);
                            // if we edit an existing activity that is validated or
                            // startedvalidated or edited
                        else
                            a.setApprovalStatus(ApprovalStatus.approved);
                    }
                } else {
                    if (Constants.PROJECT_VALIDATION_FOR_ALL_EDITS.equals(validation)) {
                        if (newActivity) {
                            a.setApprovalStatus(ApprovalStatus.started);
                        } else {
                            if (oldA.getApprovalStatus() != null
                                    && ApprovalStatus.started.equals(oldA.getApprovalStatus()))
                                a.setApprovalStatus(ApprovalStatus.started);
                            else
                                a.setApprovalStatus(ApprovalStatus.edited);
                        }
                    }
                }

            }
        } else {
            // Validation is OF in GS activity approved
            if (newActivity) {
                a.setApprovalStatus(ApprovalStatus.startedapproved);
            } else {
                a.setApprovalStatus(ApprovalStatus.approved);
            }
            a.setApprovedBy(ampCurrentMember);
            a.setApprovalDate(Calendar.getInstance().getTime());
        }
        logger.info("Project status: "+a.getApprovalStatus());
        if (a.getApprovalStatus().equals(ApprovalStatus.approved)|| a.getApprovalStatus().equals(ApprovalStatus.rejected))
        {
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

            User user=a.getActivityCreator().getUser();
            logger.info("Activity creator: "+user.getEmail());

            if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true")&&user.getTruBudgetEnabled()) {

                TruBudgetActivity truBudgetActivity  = PersistenceManager.getRequestDBSession().createQuery("FROM "+TruBudgetActivity.class.getName()+" ta WHERE ta.ampActivityId="+a.getAmpActivityId(), TruBudgetActivity.class).stream().findAny().orElse(null);
            if (truBudgetActivity!=null) {

                try {

                    doActualTruBudgetLogin(user);
                    String token = ProjectUtil.getTrubudgetToken();
                    new Thread(()-> {
                        logger.info("Started background task for closing project.");
                    try {
                        Session session = PersistenceManager.openNewSession();
                        Transaction transaction = session.beginTransaction();
                        ProjectUtil.closeProject(truBudgetActivity.getTruBudgetId(), settings, token, session);
                        session.flush();
                        transaction.commit();
                    } catch (Exception e) {
                        logger.info("Error during project close", e);
                    }
                    }).start();

                } catch (Exception e) {
                    logger.info("An error during project close: ", e);
                }
            }
            }
        }
    }

    /**
     * Verifies if the team member can approve an activity from the specified team
     * See {@link #setActivityStatus(AmpTeamMember, boolean, AmpActivityFields, AmpActivityVersion, boolean, boolean)}
     *
     * @param atm               the team member to check
     * @param activityTeamId    the team id that activity belongs to that the TM can have the approval right
     * @param oldApprovalStatus the old approval status
     * @return true if the user is allowed to approve the activity
     */
    public static boolean canApprove(AmpTeamMember atm, Long activityTeamId, ApprovalStatus oldApprovalStatus) {
        String validation = getValidationSetting(atm);
        if (isProjectValidationOn(validation)) {
            if (isApprover(atm)) {
                boolean isSameWorkspace = atm.getAmpTeam().getAmpTeamId().equals(activityTeamId);
                return isSameWorkspace || atm.getAmpTeam().getCrossteamvalidation();
            } else if (Constants.PROJECT_VALIDATION_FOR_NEW_ONLY.equals(validation)) {
                return oldApprovalStatus != null && !oldApprovalStatus.equals(ApprovalStatus.started);
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * An activity can be rejected only if:
     * 1. the activity is not new
     * 2. the activity is not draft
     * 3. the validation settings is set to on
     * 4. the user is approver of the workspace or is the teamlead of the ws
     *
     * @param atm
     * @param isDraft
     * @param isNewActivity
     * @return
     */
    public static boolean canReject(AmpTeamMember atm, Boolean isDraft, Boolean isNewActivity) {
        return BooleanUtils.isFalse(isNewActivity) && BooleanUtils.isFalse(isDraft)
                && isProjectValidationOn(getValidationSetting(atm)) && isApprover(atm);
    }

    /**
     * Detect if the teammember is approver of the workspace or is the teamlead of the ws
     *
     * @param atm team member
     * @return
     */
    private static boolean isApprover(AmpTeamMember atm) {
        AmpTeamMemberRoles role = atm.getAmpMemberRole();
        return role != null && (role.getTeamHead() || role.isApprover());
    }

    public static boolean canApproveWith(ApprovalStatus approvalStatus, AmpTeamMember atm, boolean isNewActivity,
                                         Boolean isDraft) {
        if (atm == null) {
            return false;
        }
        String validation = getValidationSetting(atm);
        if (isProjectValidationOn(validation)) {
            return Boolean.FALSE.equals(isDraft) && ApprovalStatus.approved.equals(approvalStatus);
        }
        ApprovalStatus allowed = isNewActivity ? ApprovalStatus.startedapproved : ApprovalStatus.approved;
        return allowed.equals(approvalStatus);
    }

    private static String getValidationSetting(AmpTeamMember atm) {
        Long teamId = atm.getAmpTeam().getAmpTeamId();
        if (!isProjectValidationOn()) {
            return Constants.PROJECT_VALIDATION_OFF;
        }
        return org.digijava.module.aim.util.DbUtil.getValidationFromTeamAppSettings(teamId);
    }

    private static boolean isProjectValidationOn(String validation) {
        logger.info("Validation: " + validation);
        return !Constants.PROJECT_VALIDATION_OFF.equalsIgnoreCase(validation);
    }

    public static boolean isProjectValidationOn() {
        String gsValidationOnOff = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION);
        return Constants.PROJECT_VALIDATION_ON.equals(gsValidationOnOff);
    }

    public static boolean isProjectValidationForNewOnly(AmpTeamMember atm) {
        String validation = getValidationSetting(atm);
        return Constants.PROJECT_VALIDATION_FOR_NEW_ONLY.equals(validation);
    }

    /**
     * Method used to load the last version of an object
     *
     * @param am
     * @param id
     * @return activity loaded
     */
    public static AmpActivityVersion load(AmpActivityModel am, Long id) {
        if (id == null) {
            return new AmpActivityVersion();
        }

        Session session = AmpActivityModel.getHibernateSession();//am.getSession();


        //am.setTransaction(session.beginTransaction());

        /**
         * try to use optimistic locking
         */
        AmpActivityVersion act = session.get(AmpActivityVersion.class, id);

        //check the activity group for the last version of an activity
        AmpActivityGroup group = act.getAmpActivityGroup();
        if (group == null) { //Activity created previous to the versioning system?
            //we need to create a group for this activity
            group = new AmpActivityGroup();
            group.setAmpActivityLastVersion(act);

            session.save(group);
        }

        if (act.getDraft() == null)
            act.setDraft(false);
        act.setAmpActivityGroup(group);

        if (act.getComponents() != null)
            act.getComponents().size();
        if (act.getMember() != null)
            act.getMember().size();
        if (act.getContracts() != null)
            act.getContracts().size();
        if (act.getIndicators() != null)
            act.getIndicators().size();


        return act;
    }


    private static void updateComponentFunding(AmpActivityVersion a) {
        Set<AmpComponent> components = a.getComponents();

        if (components == null) {
            return;
        }

        for (AmpComponent ampComponent : components) {
            if (Hibernate.isInitialized(ampComponent.getFundings())) {
                if (ampComponent.getFundings() != null) {

                    ampComponent.getFundings().forEach(acf -> {
                        if (acf.getTransactionAmount() == null)
                        {
                            ampComponent.getFundings().remove(acf);
                        }
                        if (!acf.getComponentFundingStatus().getValue().equalsIgnoreCase("rejected")){
                           acf.setComponentRejectReason("");
                        }
                        saveComponentFundingResources(acf);
                    });
                }
            }

        }

    }

    private static void saveComments(AmpActivityVersion a, Session session, boolean draft) {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();


        HashSet<AmpComments> newComm = s.getMetaData(OnePagerConst.COMMENTS_ITEMS);
        HashSet<AmpComments> delComm = s.getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS);

        if (delComm != null) {
            for (AmpComments tComm : delComm) {
                session.delete(tComm);
            }
        }

        if (newComm != null) {
            for (AmpComments tComm : newComm) {
                if (ActivityVersionUtil.isVersioningEnabled() && !draft) {
                    try {
                        tComm = (AmpComments) tComm.prepareMerge(a);
                    } catch (CloneNotSupportedException e) {
                        logger.error("can't clone: ", e);
                    }
                }

                if (tComm.getMemberId() == null)
                    tComm.setMemberId(((AmpAuthWebSession) org.apache.wicket.Session.get()).getAmpCurrentMember());
                if (tComm.getAmpActivityId() == null)
                    tComm.setAmpActivityId(a);
                session.saveOrUpdate(tComm);
            }
        }
        session.flush();
    }

    private static EditorStore getSessionEditorStore() {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
        return s.getMetaData(OnePagerConst.EDITOR_ITEMS);
    }

    private static void saveEditors(Session session, boolean createNewVersion, EditorStore editorStore, Site site) {

        Map<String, Map<String, String>> editors = editorStore.getValues();

        //String currentLanguage = TLSUtils.getLangCode();
        if (editors == null) {
            return;
        }
        for (String key : editors.keySet()) {
            String oldKey = editorStore.getOldKey().get(key);
            Map<String, String> values = editors.get(key);
            Set<String> locales = values.keySet();

            for (String locale : locales) {
                String value = values.get(locale);

                if (value == null || value.trim().length() == 0)
                    continue; //we don't save empty editors

                try {
                    boolean editorFound = false;
                    List<Editor> edList = DbUtil.getEditorList(oldKey, site);
                    for (Editor editor : edList) {
                        if (editor.getLanguage().equals(locale)) {
                            //editor.setBody(value);
                            editorFound = true;

                            //create a new editor and copy the old values
                            Editor toSaveEditor = new Editor();
                            toSaveEditor.setBody(value);
                            toSaveEditor.setSiteId(editor.getSiteId());
                            toSaveEditor.setLanguage(editor.getLanguage());
                            toSaveEditor.setEditorKey(key);
                            session.save(toSaveEditor);

                            if (!createNewVersion) {
                                //we need to delete the old editor since this is not a new activity version
                                session.delete(editor);
                            }

                            break;
                        }
                    }

                    if (!editorFound) {
                        //add new editor
                        Editor editor = new Editor();
                        editor.setBody(value);
                        editor.setSite(site);
                        editor.setLanguage(locale);
                        editor.setEditorKey(key);
                        session.saveOrUpdate(editor);
                    }

                } catch (EditorException e) {
                    logger.error("Can't get editor:", e);
                }
            }


        }
    }

    /**
     * Method to save/update agreements into hibernate session
     *
     * @param a              the AmpActivityVersion object
     * @param session        the Hibernate Session
     * @param isActivityForm the parameter used to decide the source of the agreements (wicket session, activity object)
     */
    private static void saveAgreements(AmpActivityVersion a, Session session, boolean isActivityForm) {
        Set<AmpAgreement> agreements = isActivityForm ? getAgreementsFromActivityForm() : getAgreementsFromActivity(a);

        for (AmpAgreement agg : agreements) {
            if (agg.getId() == null || agg.getId() < 0L) {
                agg.setId(null);
                session.save(agg);
            } else {
                session.merge(agg);
            }
        }
    }

    /**
     * Get the agreements from the Wicket session
     *
     * @return Set<AmpAgreement>
     */
    private static Set<AmpAgreement> getAgreementsFromActivityForm() {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Set<AmpAgreement> agreements = s.getMetaData(OnePagerConst.AGREEMENT_ITEMS);

        return agreements == null ? new HashSet<>() : agreements;
    }

    /**
     * get Agreements from the activity object.
     * Usually this method will process activities created/updated via Activity API endpoints
     *
     * @param a the AmpActivityVersion object
     * @return Set<AmpAgreement>
     */
    private static Set<AmpAgreement> getAgreementsFromActivity(AmpActivityVersion a) {
        Set<AmpAgreement> agreements = new HashSet<>();

        Set<AmpFunding> af = a.getFunding();
        if (af != null && Hibernate.isInitialized(af)) {
            agreements = af.stream()
                    .filter(f -> f.getAgreement() != null && Hibernate.isInitialized(f.getAgreement()))
                    .map(AmpFunding::getAgreement)
                    .collect(Collectors.toSet());
        }

        return agreements;
    }

    private static void saveActivityResources(AmpActivityVersion a, Session session) {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();

        if (a.getActivityDocuments() == null) {
            a.setActivityDocuments(new HashSet<>());
        }

        HashSet<TemporaryActivityDocument> newResources = s.getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
        HashSet<AmpActivityDocument> deletedResources = s.getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
        HashSet<TemporaryActivityDocument> existingTitles = s.getMetaData(OnePagerConst.RESOURCES_EXISTING_ITEM_TITLES);

        // update titles when multilingual is enabled
        if (ContentTranslationUtil.multilingualIsEnabled()) {
            updateResourcesTitles(newResources, deletedResources, existingTitles);
        }

        // remove old resources
        deleteResources(a, deletedResources);

        // insert new resources in the system
        insertResources(a, newResources);
    }

    public static void saveComponentFundingResources(AmpComponentFunding a) {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();

        if (a.getComponentFundingDocuments() == null) {
            a.getComponentFundingDocuments().addAll(new HashSet<>());
        }

        HashSet<TemporaryComponentFundingDocument> newResources = s.getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(a.getJustAnId());
        HashSet<AmpComponentFundingDocument> deletedResources = s.getMetaData(OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS).get(a.getJustAnId());
        HashSet<TemporaryComponentFundingDocument> existingTitles = s.getMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES).get(a.getJustAnId());
        newResources= newResources!=null?newResources:new HashSet<>();
        deletedResources= deletedResources!=null?deletedResources:new HashSet<>();
        existingTitles= existingTitles!=null?existingTitles:new HashSet<>();

        // update titles when multilingual is enabled
        if (ContentTranslationUtil.multilingualIsEnabled()) {
            updateComponentFundingResourcesTitles(newResources, deletedResources, existingTitles);
        }

        // remove old resources
        deleteComponentFundingResources(a, deletedResources);

        // insert new resources in the system
        insertComponentFundingResources(a, newResources);
        if (!(getSettingValue(getGlobalSettingsBySection("trubudget"),"isEnabled").equalsIgnoreCase("true")&&TeamUtil.getCurrentUser().getTruBudgetEnabled()) || a.getTransactionType() != 1) {
            newResources.clear();
        }
    }

    private static void insertComponentFundingResources(AmpComponentFunding a, HashSet<TemporaryComponentFundingDocument> newResources) {
        if (newResources != null) {
            for (TemporaryComponentFundingDocument temp : newResources) {
                TemporaryDocumentData tdd = new TemporaryDocumentData();
                tdd.setTitle(temp.getTitle());
                tdd.setName(temp.getFileName());
                tdd.setDescription(temp.getDescription());
                tdd.setNotes(temp.getNote());
                if (temp.getTranslatedTitleList() != null) {
                    Map<String, String> translatedTitleMap = new HashMap<String, String>();
                    for (ResourceTranslation titleTranslation : temp.getTranslatedTitleList()) {
                        translatedTitleMap.put(titleTranslation.getLocale(), titleTranslation.getTranslation());
                    }
                    tdd.setTranslatedTitles(translatedTitleMap);
                }

                if (temp.getTranslatedDescriptionList() != null) {
                    Map<String, String> translatedDescMap = new HashMap<String, String>();
                    for (ResourceTranslation descTranslation : temp.getTranslatedDescriptionList()) {
                        translatedDescMap.put(descTranslation.getLocale(), descTranslation.getTranslation());
                    }
                    tdd.setTranslatedDescriptions(translatedDescMap);
                }

                if (temp.getTranslatedNoteList() != null) {
                    Map<String, String> translatedNoteMap = new HashMap<String, String>();
                    for (ResourceTranslation noteTranslation : temp.getTranslatedDescriptionList()) {
                        translatedNoteMap.put(noteTranslation.getLocale(), noteTranslation.getTranslation());
                    }
                    tdd.setTranslatedNotes(translatedNoteMap);
                }

//                if (temp.getType() != null) {
//                    tdd.setCmDocTypeId(temp.getType().getId());
//                }
                if (temp.getDate() != null) {
                    tdd.setDate(temp.getDate().getTime());
                }
                if (temp.getYear() != null) {
                    tdd.setYearofPublication(temp.getYear());
                }
                if (temp.getWebLink() == null || temp.getWebLink().length() == 0) {
                    if (temp.getFile() != null) {
                        tdd.setFileSize(temp.getFile().getSize());
                        tdd.setFormFile(generateFormFile(temp.getFile()));
                    }
                }

                tdd.setWebLink(temp.getWebLink());

                try {
                    NodeWrapper node = tdd.saveToRepository(SessionUtil.getCurrentServletRequest());

                    AmpComponentFundingDocument aad = new AmpComponentFundingDocument();
                    aad.setAmpComponentFunding(a);
//                    aad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
                    if (node != null) {
                        aad.setUuid(node.getUuid());
                    } else {
                        aad.setUuid(temp.getExistingDocument().getUuid());
                    }
                    a.getComponentFundingDocuments().add(aad);
                } catch (JCRSessionException ex) {
                    // we catch the exception and show a warning, but allow the activity to be saved
                    logger.warn("The JCR Session couldn't be opened. " + "The document " + tdd.getName()
                            + " will not be saved.", ex);
                }
            }
        }
    }

    private static void deleteComponentFundingResources(AmpComponentFunding a, HashSet<AmpComponentFundingDocument> deletedResources) {
        Session session = PersistenceManager.getRequestDBSession();
//        Transaction transaction=session.getTransaction();

        if (deletedResources != null) {
            for (AmpComponentFundingDocument tmpDoc : deletedResources) {
//                tmpDoc.setAmpComponentFunding(null);
                if (tmpDoc!=null){
                    tmpDoc.setAmpComponentFunding(null);
                    a.getComponentFundingDocuments().remove(tmpDoc);

                    if (tmpDoc.getId()!=null) {
                        session.update(tmpDoc);
                    }

                }


            }
        }
//        session.flush();

//        transaction.commit();
//        session.close();


    }

    private static void updateComponentFundingResourcesTitles(HashSet<TemporaryComponentFundingDocument> newResources,
                                              HashSet<AmpComponentFundingDocument> deletedResources, HashSet<TemporaryComponentFundingDocument> existingTitles) {
        if (existingTitles != null) {
            HttpServletRequest req = SessionUtil.getCurrentServletRequest();

            for (TemporaryComponentFundingDocument d : existingTitles) {
                Node node = DocumentManagerUtil.getWriteNode(d.getExistingDocument().getUuid(), req);
                if (node != null && d != null) {
                    NodeWrapper nw = new NodeWrapper(node);

                    //NodeWrapper's title will be null if the document is multilingual
                    //and it was saved in ONLY one language. Then language was changed
                    // and jackrabbit tries to retrieve the title for the other language.
                    //The call to -> getTranslatedTitleByLang(TLSUtils.getLangCode());
                    //returns null.
                    //In that scenario we act as if we were changing the document name
                    boolean onlyOneLanguageSaved = nw.getTitle() == null;
                    if (onlyOneLanguageSaved || !nw.getTitle().equals(d.getTitle())) {
                        logger.warn("lang " + TLSUtils.getLangCode());
                        if (onlyOneLanguageSaved) {
                            populateAmpComponentFundingDocsTranslatedTitles(d, nw);
                        }

                        if (d.getWebLink() != null && d.getWebLink().trim().length() > 0 &&
                                (d.getFileName() == null || d.getFileName().trim().length() == 0)) {
                            d.setFileName(d.getWebLink());
                        }

                        if (!deletedResources.contains(d.getExistingDocument())) {
                            String contentType = nw.getContentType();
                            String fileName = nw.getName();
                            Bytes fileSize = null;
                            InputStream fileData = null;
                            try {
                                if (nw.getNode().hasProperty(CrConstants.PROPERTY_DATA))
                                    fileData = nw.getNode().getProperty(CrConstants.PROPERTY_DATA).getBinary().getStream();
                                //                                  .getBinary().getStream();
                                if (nw.getNode().hasProperty(CrConstants.PROPERTY_FILE_SIZE))
                                    fileSize = Bytes.bytes(nw.getNode().getProperty(CrConstants.PROPERTY_FILE_SIZE).getLong());
                                DocumentManagerUtil.logoutJcrSessions(req);
                            } catch (RepositoryException e) {
                                logger.error("Error while getting data stream from JCR:", e);
                            }

                            FileUpload file = new FileUpload(new FileItemEx(fileName, contentType, fileData, fileSize));
                            d.setFile(file);
                            newResources.add(d);
                            deletedResources.add(d.getExistingDocument());
                        }
                    }
                }
            }
        }
    }

    private static void populateAmpComponentFundingDocsTranslatedTitles(TemporaryComponentFundingDocument d, NodeWrapper nw) {
        List<ResourceTranslation> translatedTitles = d.getTranslatedTitleList();
        if (translatedTitles == null) {
            translatedTitles = new ArrayList<>();
        }
        List<String> languages = TranslatorUtil.getLocaleCache();
        for (String locale : languages) {
            String translation = nw.getTranslatedTitleByLang(locale);
            if (translation != null && !Objects.equals(locale, TLSUtils.getLangCode())) {
                ResourceTranslation resource = new ResourceTranslation(d.getExistingDocument()
                        .getUuid(), translation, locale);
                translatedTitles.add(resource);
            }
        }

        translatedTitles.add(new ResourceTranslation(d.getExistingDocument().getUuid(), d
                .getTitle(), TLSUtils.getLangCode()));
        d.setTranslatedTitleList(translatedTitles);
    }

    /**
     * @param a
     * @param newResources
     */
    private static void insertResources(AmpActivityVersion a, HashSet<TemporaryActivityDocument> newResources) {
        if (newResources != null) {
            for (TemporaryActivityDocument temp : newResources) {
                TemporaryDocumentData tdd = new TemporaryDocumentData();
                tdd.setTitle(temp.getTitle());
                tdd.setName(temp.getFileName());
                tdd.setDescription(temp.getDescription());
                tdd.setNotes(temp.getNote());
                if (temp.getTranslatedTitleList() != null) {
                    Map<String, String> translatedTitleMap = new HashMap<String, String>();
                    for (ResourceTranslation titleTranslation : temp.getTranslatedTitleList()) {
                        translatedTitleMap.put(titleTranslation.getLocale(), titleTranslation.getTranslation());
                    }
                    tdd.setTranslatedTitles(translatedTitleMap);
                }

                if (temp.getTranslatedDescriptionList() != null) {
                    Map<String, String> translatedDescMap = new HashMap<String, String>();
                    for (ResourceTranslation descTranslation : temp.getTranslatedDescriptionList()) {
                        translatedDescMap.put(descTranslation.getLocale(), descTranslation.getTranslation());
                    }
                    tdd.setTranslatedDescriptions(translatedDescMap);
                }

                if (temp.getTranslatedNoteList() != null) {
                    Map<String, String> translatedNoteMap = new HashMap<String, String>();
                    for (ResourceTranslation noteTranslation : temp.getTranslatedDescriptionList()) {
                        translatedNoteMap.put(noteTranslation.getLocale(), noteTranslation.getTranslation());
                    }
                    tdd.setTranslatedNotes(translatedNoteMap);
                }

                if (temp.getType() != null) {
                    tdd.setCmDocTypeId(temp.getType().getId());
                }
                if (temp.getDate() != null) {
                    tdd.setDate(temp.getDate().getTime());
                }
                if (temp.getYear() != null) {
                    tdd.setYearofPublication(temp.getYear());
                }
                if (temp.getWebLink() == null || temp.getWebLink().length() == 0) {
                    if (temp.getFile() != null) {
                        tdd.setFileSize(temp.getFile().getSize());
                        tdd.setFormFile(generateFormFile(temp.getFile()));
                    }
                }

                tdd.setWebLink(temp.getWebLink());

                try {
                    NodeWrapper node = tdd.saveToRepository(SessionUtil.getCurrentServletRequest());

                    AmpActivityDocument aad = new AmpActivityDocument();
                    aad.setAmpActivity(a);
                    aad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
                    if (node != null) {
                        aad.setUuid(node.getUuid());
                    } else {
                        aad.setUuid(temp.getExistingDocument().getUuid());
                    }
                    a.getActivityDocuments().add(aad);
                } catch (JCRSessionException ex) {
                    // we catch the exception and show a warning, but allow the activity to be saved
                    logger.warn("The JCR Session couldn't be opened. " + "The document " + tdd.getName()
                            + " will not be saved.", ex);
                }
            }
        }
    }

    /**
     * For Document Manager compatibility purposes
     *
     * @param file
     */
    private static FormFile generateFormFile(FileUpload file) {

        return new FormFile() {

            @Override
            public void setFileSize(int arg0) {
            }

            @Override
            public void setFileName(String arg0) {
            }

            @Override
            public void setContentType(String arg0) {
            }

            @Override
            public InputStream getInputStream() throws FileNotFoundException, IOException {
                return file.getInputStream();
            }

            @Override
            public int getFileSize() {
                return (int) file.getSize();
            }

            @Override
            public String getFileName() {
                return file.getClientFileName();
            }

            @Override
            public byte[] getFileData() throws FileNotFoundException, IOException {
                return file.getBytes();
            }

            @Override
            public String getContentType() {
                return file.getContentType();
            }

            @Override
            public void destroy() {
            }
        };
    }

    /**
     * @param a
     * @param deletedResources
     */
    private static void deleteResources(AmpActivityVersion a, HashSet<AmpActivityDocument> deletedResources) {
        if (deletedResources != null) {
            for (AmpActivityDocument tmpDoc : deletedResources) {
                if (tmpDoc!=null) {
                    a.getActivityDocuments().remove(tmpDoc);
                }
            }
        }
    }

    /**
     * @param newResources
     * @param deletedResources
     * @param existingTitles
     */
    private static void updateResourcesTitles(HashSet<TemporaryActivityDocument> newResources,
                                              HashSet<AmpActivityDocument> deletedResources, HashSet<TemporaryActivityDocument> existingTitles) {
        if (existingTitles != null) {
            HttpServletRequest req = SessionUtil.getCurrentServletRequest();

            for (TemporaryActivityDocument d : existingTitles) {
                Node node = DocumentManagerUtil.getWriteNode(d.getExistingDocument().getUuid(), req);
                if (node != null && d != null) {
                    NodeWrapper nw = new NodeWrapper(node);

                    //NodeWrapper's title will be null if the document is multilingual
                    //and it was saved in ONLY one language. Then language was changed
                    // and jackrabbit tries to retrieve the title for the other language.
                    //The call to -> getTranslatedTitleByLang(TLSUtils.getLangCode());
                    //returns null.
                    //In that scenario we act as if we were changing the document name
                    boolean onlyOneLanguageSaved = nw.getTitle() == null;
                    if (onlyOneLanguageSaved || !nw.getTitle().equals(d.getTitle())) {
                        logger.warn("lang " + TLSUtils.getLangCode());
                        if (onlyOneLanguageSaved) {
                            populateTranslatedTitles(d, nw);
                        }

                        if (d.getWebLink() != null && d.getWebLink().trim().length() > 0 &&
                                (d.getFileName() == null || d.getFileName().trim().length() == 0)) {
                            d.setFileName(d.getWebLink());
                        }

                        if (!deletedResources.contains(d.getExistingDocument())) {
                            String contentType = nw.getContentType();
                            String fileName = nw.getName();
                            Bytes fileSize = null;
                            InputStream fileData = null;
                            try {
                                if (nw.getNode().hasProperty(CrConstants.PROPERTY_DATA))
                                    fileData = nw.getNode().getProperty(CrConstants.PROPERTY_DATA).getBinary().getStream();
                                //                                  .getBinary().getStream();
                                if (nw.getNode().hasProperty(CrConstants.PROPERTY_FILE_SIZE))
                                    fileSize = Bytes.bytes(nw.getNode().getProperty(CrConstants.PROPERTY_FILE_SIZE).getLong());
                                DocumentManagerUtil.logoutJcrSessions(req);
                            } catch (RepositoryException e) {
                                logger.error("Error while getting data stream from JCR:", e);
                            }

                            FileUpload file = new FileUpload(new FileItemEx(fileName, contentType, fileData, fileSize));
                            d.setFile(file);
                            newResources.add(d);
                            deletedResources.add(d.getExistingDocument());
                        }
                    }
                }
            }
        }
    }

    private static void populateTranslatedTitles(TemporaryActivityDocument d, NodeWrapper nw) {
        List<ResourceTranslation> translatedTitles = d.getTranslatedTitleList();
        if (translatedTitles == null) {
            translatedTitles = new ArrayList<ResourceTranslation>();
        }
        List<String> languages = TranslatorUtil.getLocaleCache();
        for (String locale : languages) {
            String translation = nw.getTranslatedTitleByLang(locale);
            if (translation != null && locale != TLSUtils.getLangCode()) {
                ResourceTranslation resource = new ResourceTranslation(d.getExistingDocument()
                        .getUuid(), translation, locale);
                translatedTitles.add(resource);
            }
        }

        translatedTitles.add(new ResourceTranslation(d.getExistingDocument().getUuid(), d
                .getTitle(), TLSUtils.getLangCode()));
        d.setTranslatedTitleList(translatedTitles);
    }

    private static void saveActivityGPINiResources(AmpActivityVersion a, Session session) {
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();

        HashSet<TemporaryGPINiDocument> newResources = s.getMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS);
        HashSet<AmpGPINiSurveyResponseDocument> deletedResources = s.getMetaData(OnePagerConst
                .GPI_RESOURCES_DELETED_ITEMS);

        // remove old resources
        deleteGPINiResources(a, deletedResources);

        // insert new resources in the system
        insertGPINiResources(a, newResources);
        session.flush();
    }

    /**
     * @param deletedResources
     */
    private static void deleteGPINiResources(AmpActivityVersion a, HashSet<AmpGPINiSurveyResponseDocument>
            deletedResources) {
        if (deletedResources != null) {
            for (AmpGPINiSurveyResponseDocument tmpDoc : deletedResources) {
                AmpGPINiSurveyResponse surveyResponse = tmpDoc.getSurveyResponse();

                for (AmpOrgRole tempOrgRole : a.getOrgrole()) {
                    if (tempOrgRole.getGpiNiSurvey() != null) {
                        for (AmpGPINiSurveyResponse tempGPINiSurveyResponse : tempOrgRole.getGpiNiSurvey()
                                .getResponses()) {
                            if (Objects.equals(tempGPINiSurveyResponse.getOldKey(), surveyResponse
                                    .getAmpGPINiSurveyResponseId())) {

                                Set<AmpGPINiSurveyResponseDocument> docsToBeRemoved = tempGPINiSurveyResponse
                                        .getSupportingDocuments().stream()
                                        .filter(d -> d.getUuid().equals(tmpDoc.getUuid()))
                                        .collect(Collectors.toSet());

                                tempGPINiSurveyResponse.getSupportingDocuments().removeAll(docsToBeRemoved);

                            }

                        }

                    }
                }
            }
        }
    }

    /**
     * @param a
     * @param newResources
     */
    private static void insertGPINiResources(AmpActivityVersion a, HashSet<TemporaryGPINiDocument> newResources) {
        if (newResources != null) {

            for (TemporaryGPINiDocument temp : newResources) {
                AmpGPINiSurveyResponse surveyResponse = temp.getSurveyResponse();

                TemporaryDocumentData tdd = new TemporaryDocumentData();
                tdd.setTitle(temp.getTitle());
                tdd.setName(temp.getFileName());

                if (temp.getDate() != null) {
                    tdd.setDate(temp.getDate().getTime());
                }
                if (temp.getWebLink() == null || temp.getWebLink().length() == 0) {
                    if (temp.getFile() != null) {
                        tdd.setFileSize(temp.getFile().getSize());
                        tdd.setFormFile(generateFormFile(temp.getFile()));
                    }
                }

                tdd.setWebLink(temp.getWebLink());

                try {
                    NodeWrapper node = tdd.saveToRepository(SessionUtil.getCurrentServletRequest());

                    AmpGPINiSurveyResponseDocument responseDocument = new AmpGPINiSurveyResponseDocument();

                    if (node != null) {
                        responseDocument.setUuid(node.getUuid());
                    } else {
                        responseDocument.setUuid(temp.getExistingDocument().getUuid());
                    }

                    for (AmpOrgRole tempOrgRole : a.getOrgrole()) {
                        if (tempOrgRole.getGpiNiSurvey() != null) {
                            for (AmpGPINiSurveyResponse tempGPINiSurveyResponse : tempOrgRole.getGpiNiSurvey()
                                    .getResponses()) {
                                if (shouldResponseToBeUpdated(surveyResponse, tempGPINiSurveyResponse)
                                ) {
                                    responseDocument.setSurveyResponse(tempGPINiSurveyResponse);

                                    if (tempGPINiSurveyResponse.getSupportingDocuments() == null) {
                                        tempGPINiSurveyResponse.setSupportingDocuments(new
                                                HashSet<AmpGPINiSurveyResponseDocument>());
                                    }

                                    tempGPINiSurveyResponse.getSupportingDocuments().add(responseDocument);
                                }

                            }

                        }
                    }

                } catch (JCRSessionException ex) {
                    // we catch the exception and show a warning, but allow the activity to be saved
                    logger.warn("The JCR Session couldn't be opened. " + "The document " + tdd.getName()
                            + " will not be saved.", ex);
                }

            }
        }
    }

    private static boolean shouldResponseToBeUpdated(AmpGPINiSurveyResponse surveyResponse, AmpGPINiSurveyResponse
            tempGPINiSurveyResponse) {
        return (Objects.equals(tempGPINiSurveyResponse.getAmpGPINiSurvey().getAmpOrgRole().getOrganisation().getAmpOrgId(), surveyResponse.getAmpGPINiSurvey().getAmpOrgRole().getOrganisation().getAmpOrgId())
                && tempGPINiSurveyResponse.getAmpGPINiQuestion().getCode()
                .equals(surveyResponse.getAmpGPINiQuestion().getCode()));
    }

    public static void saveContacts(AmpActivityVersion a, Session session, boolean checkForContactsRemoval,
                                    AmpTeamMember teamMember) throws Exception {
        Set<AmpActivityContact> activityContacts = a.getActivityContacts();
        // if activity contains contact,which is not in contact list, we should remove it
        Long oldActivityId = a.getAmpActivityId();
        if (oldActivityId != null) {
            if (checkForContactsRemoval || !ActivityVersionUtil.isVersioningEnabled()) {
                //List<AmpActivityContact> activityDbContacts=ContactInfoUtil.getActivityContacts(oldActivityId);
                List<Long> activityDbContactsIds = ContactInfoUtil.getActivityContactIds(oldActivityId);
                if (activityDbContactsIds != null && activityDbContactsIds.size() > 0) {
                    for (Long actContactId : activityDbContactsIds) {
                        int count = 0;
                        if (activityContacts != null) {
                            for (AmpActivityContact activityContact : activityContacts) {
                                if (activityContact.getId() != null && activityContact.getId().equals(actContactId)) {
                                    count++;
                                    break;
                                }
                            }
                        }
                        if (count == 0) { //if activity contains contact,which is not in contact list, we should remove it
                            Query qry = session.createQuery("delete from " + AmpActivityContact.class.getName() + " a where a.id=" + actContactId);
                            qry.executeUpdate();
                        }
                    }
                }

            }
        }

        boolean newActivity = a.getAmpActivityId() == null;

        //to avoid saving the same contact twice on the same session, we keep track of the
        //already saved ones.
        Map<Long, Boolean> savedContacts = new HashMap<Long, Boolean>();
        AmpTeamMember creator = teamMember;
        if (creator == null) {
            creator = TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest());
        }

        //add or edit activity contact and amp contact
        if (activityContacts != null && activityContacts.size() > 0) {
            for (AmpActivityContact activityContact : activityContacts) {
                Long contactId = activityContact.getContact().getId();
                // if the contact already exists on the DB, and was not saved
                // already
                if (contactId != null && savedContacts.get(contactId) == null) {
                    savedContacts.put(activityContact.getContact().getId(), false);
                }
                // save the contact first, if the contact is new or if it is not
                // new but has not been saved already.
                if (contactId == null || (newActivity && !savedContacts.get(contactId))) {
                    activityContact.getContact().setCreator(creator);
                    session.saveOrUpdate(activityContact.getContact());
                    savedContacts.put(activityContact.getContact().getId(), true);
                }
                if (activityContact.getId() == null) {
                    session.saveOrUpdate(activityContact);
                    if (!newActivity) {
                        session.merge(activityContact.getContact());
                    }
                }
            }
        }
    }

    private static void saveAnnualProjectBudgets(AmpActivityVersion a,
                                                 Session session) {
        if (a.getAmpActivityId() != null) {
            for (AmpAnnualProjectBudget annualBudget : a.getAnnualProjectBudgets()) {
                annualBudget.setActivity(a);
                if (annualBudget.getAmpAnnualProjectBudgetId() == null) {
                    session.saveOrUpdate(annualBudget);
                } else {
                    session.merge(annualBudget);
                }
            }
        }
    }

    private static void saveStructures(AmpActivityVersion a, Session session) {
        if (a.getAmpActivityId() != null) {
            for (AmpStructure structure : a.getStructures()) {
                structure.setActivity(a);
                AmpStructureCoordinate ampStructureCoordinate = new AmpStructureCoordinate();
                ampStructureCoordinate.setStructure(structure);
                ampStructureCoordinate.setLatitude(structure.getLatitude());
                ampStructureCoordinate.setLongitude(structure.getLongitude());
                if (!structure.getCoordinates().contains(ampStructureCoordinate)) {
                    structure.getCoordinates().add(ampStructureCoordinate);
                }
                if (structure.getAmpStructureId() == null) {
                    session.save(structure);
                } else {
                    session.merge(structure);
                }
            }
        }
    }

    private static void saveProjectCosts(AmpActivityVersion a, Session session) {
        if (a.getCostAmounts() != null) {
            for (AmpFundingAmount afa : a.getCostAmounts()) {
                if (afa.getAmpFundingAmountId() == null) {
                    String hql = "FROM "+ AmpFundingAmount.class.getName()+ " e WHERE e.activity= :activityValue";
                    List<AmpFundingAmount> results = session.createQuery(hql, AmpFundingAmount.class)
                            .setParameter("activityValue", afa.getActivity())
                            .list();
                    if (results!=null && !results.isEmpty()) {
                        results.forEach(fundingAmount -> {
                            if (fundingAmount.getFunType().equals(afa.getFunType())) {

                                fundingAmount.setActivity(null);
                                Query qry = session.createQuery("update " + AmpFundingAmount.class.getName() + " a set a.activity= :activityValue where a.ampFundingAmountId= :fundingAmountId");
                                qry.setParameter("activityValue", null).setParameter("fundingAmountId",fundingAmount.getAmpFundingAmountId(), LongType.INSTANCE).executeUpdate();
                            }

                        });
                    }

                    afa.setActivity(a);
                    session.saveOrUpdate(afa);
                } else {
                    cleanObjectFromSession(session, AmpFundingAmount.class,afa.getAmpFundingAmountId());
                    session.update(afa);
                }
            }
        }


    }

    /**
     * Determine if in a activity a related organization is attached to a funding or not
     *
     * @param activity
     * @param orgRole
     * @return
     */
    public static boolean hasOrgRoleFundingsInActivity(AmpActivityVersion activity, AmpOrgRole orgRole) {
        Set<AmpFunding> fundings = activity.getFunding();
        AmpOrganisation org = orgRole.getOrganisation();
        AmpRole role = orgRole.getRole();

        for (AmpFunding ampFunding : fundings) {
            if (ampFunding.getAmpDonorOrgId().getAmpOrgId().equals(org.getAmpOrgId())
                    && ((ampFunding.getSourceRole() == null && role.getRoleCode().equals(Constants.FUNDING_AGENCY))
                    || (ampFunding.getSourceRole() != null
                    && ampFunding.getSourceRole().getRoleCode().equals(role.getRoleCode())))) {

                return true;
            }
        }


        return false;
    }

    /**
     * Determine if in a activity a related organization is attached to a component funding or not
     *
     * @param activity
     * @param org
     * @return
     */
    public static boolean hasOrgComponentFundingsInActivity(AmpActivityVersion activity, AmpOrganisation org) {
        Long ampOrgId = org.getAmpOrgId();
        Set<AmpComponentFunding> componentFundings = activity.getComponents().stream()
                .flatMap(c -> c.getFundings().stream())
                .filter(c -> c.getReportingOrganization() != null
                        || c.getComponentSecondResponsibleOrganization() != null)
                .collect(Collectors.toSet());

        for (AmpComponentFunding acf : componentFundings) {
            if ((acf.getReportingOrganization() != null
                    && acf.getReportingOrganization().getAmpOrgId().equals(ampOrgId))
                    || (acf.getComponentSecondResponsibleOrganization() != null
                    && acf.getComponentSecondResponsibleOrganization().getAmpOrgId().equals(ampOrgId))) {

                return true;
            }
        }

        return false;
    }

    /**
     * Get the range list of fiscal years (FY field from budget extras component, identification section in AF)
     *
     * @return
     */
    public static List<String> getFiscalYearsRange() {
        int rangeStartYear = FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.YEAR_RANGE_START);
        int rangeNumber = FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);

        return Stream.iterate(rangeStartYear, i -> i + 1)
                .limit(rangeNumber).map(Object::toString)
                .collect(Collectors.toList());
    }

    public static boolean isFiscalYearInRange(int year) {
        int rangeStartYear = FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.YEAR_RANGE_START);
        int rangeNumber = FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
        return year >= rangeStartYear && year < rangeStartYear + rangeNumber;
    }

    /**
     * Calculate object checksum based on HashCode of not null values to
     * determine if the object has been changed
     *
     * @param item
     */
    public static Long calculateFundingDetailCheckSum(FundingInformationItem item) {
        long checkSum = 0L;
        checkSum += checkSum + (item.getTransactionAmount() != null ? item.getTransactionAmount().hashCode() : 0L);
        checkSum += checkSum
                + (item.getAbsoluteTransactionAmount() != null ? item.getAbsoluteTransactionAmount().hashCode() : 0L);
        checkSum += checkSum + (item.getAmpCurrencyId() != null ? item.getAmpCurrencyId().hashCode() : 0L);
        checkSum += checkSum + (item.getTransactionDate() != null ? item.getTransactionDate().hashCode() : 0L);
        checkSum += checkSum + (item.getRecipientOrg() != null ? item.getRecipientOrg().hashCode() : 0L);
        checkSum += checkSum + (item.getRecipientRole() != null ? item.getRecipientRole().hashCode() : 0L);
        checkSum += checkSum + (item.getTransactionType() != null ? item.getTransactionType().hashCode() : 0L);
        checkSum += checkSum + (item.getAdjustmentType() != null ? item.getAdjustmentType().hashCode() : 0L);
        checkSum += checkSum + (item.getDisbOrderId() != null ? item.getDisbOrderId().hashCode() : 0L);
        checkSum += checkSum + (item.getFixedExchangeRate() != null ? item.getFixedExchangeRate().hashCode() : 0L);
        checkSum += checkSum + (item.getDisbOrderId() != null ? item.getDisbOrderId().hashCode() : 0L);
        checkSum += checkSum + (item.getFixedExchangeRate() != null ? item.getFixedExchangeRate().hashCode() : 0L);
        checkSum += checkSum + (item.getContract() != null ? item.getContract().hashCode() : 0L);
        checkSum += checkSum + (item.getExpCategory() != null ? item.getExpCategory().hashCode() : 0L);
        checkSum += checkSum + (item.getPledgeid() != null ? item.getPledgeid().hashCode() : 0L);
        checkSum += checkSum + (item.getDisasterResponse() != null ? item.getDisasterResponse().hashCode() : 0L);
        checkSum += checkSum + (item.getExpenditureClass() != null ? item.getExpenditureClass().hashCode() : 0L);
        return checkSum;
    }
}
