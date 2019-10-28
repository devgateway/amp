package org.digijava.module.aim.action;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.versioning.ActivityComparisonContext;
import org.digijava.module.aim.util.versioning.ActivityComparisonResult;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CompareActivityVersions extends DispatchAction {

    private static Logger logger = Logger.getLogger(EditActivity.class);


    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        return compare(mapping, form, request, response);
    }

    public ActionForward compare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        Session session = PersistenceManager.getRequestDBSession();
        setAdvancemode(vForm, request);



        if (request.getParameter("action") != null && request.getParameter("action").equals("setVersion")
                && request.getParameter("activityCurrentVersion") != null) {

            Long activityId = Long.parseLong(request.getParameter("activityCurrentVersion"));
            AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);
            AmpActivityGroup group = activity.getAmpActivityGroup();

            AmpActivityVersion prevVer = group.getAmpActivityLastVersion();

            //Why send the activity to the top of the list? It's confusing for users
            //thus we don't set the modifiedDate
            //activity.setModifiedDate(Calendar.getInstance().getTime());
            group.setAmpActivityLastVersion(activity);
            session.update(group);

            Site site = RequestUtils.getSite(request);
            Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
            java.util.Locale locale = new java.util.Locale(navigationLanguage.getCode());
            LuceneUtil.addUpdateActivity(request.getSession().getServletContext().getRealPath("/"), true, site, locale,
                    activity, prevVer);

            return new ActionForward(mapping.findForward("reload").getPath() + "?ampActivityId=" + activityId, true);
        }

        vForm.setOutputCollection(new ArrayList<CompareOutput>());
        ActivityComparisonContext context = new ActivityComparisonContext(TLSUtils.getSite().getId(),
                TLSUtils.getSite().getName(), TLSUtils.getEffectiveLangCode());

        vForm.setOutputCollectionGrouped(ActivityVersionUtil.compareActivities(vForm.getActivityOneId(),
                vForm.getActivityTwoId(), context));


        return mapping.findForward("forward");
    }

    private void modifyFundingOutputs(Map<String, List<CompareOutput>> outputGroupped) {


    }


    public ActionForward enableMerge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        List<CompareOutput> filteredList = new ArrayList<CompareOutput>();
        Iterator<CompareOutput> iter = vForm.getOutputCollection().iterator();
        while (iter.hasNext()) {
            CompareOutput auxCompare = iter.next();
            if (!auxCompare.getBlockSingleChangeOutput()) {
                filteredList.add(auxCompare);
            }
        }
        vForm.setOutputCollection(filteredList);
        vForm.setMergedValues(new String[vForm.getOutputCollection().size()]);
        return mapping.findForward("forward");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        return mapping.findForward("reload");
    }

    public ActionForward saveNewActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        // Get data from jsp.
        ActionErrors errors = new ActionErrors();
        boolean hasErrors = false;

        setAdvancemode(vForm, request);

        List<CompareOutput> auxData = new ArrayList<CompareOutput>();
        for (int i = 0; i < vForm.getMergedValues().length; i++) {
            CompareOutput auxOutput = vForm.getOutputCollection().get(i);
            CompareOutput newOutput = new CompareOutput();
            newOutput.setFieldOutput(auxOutput.getFieldOutput());
            newOutput.setMandatoryForSingleChangeOutput(auxOutput.getMandatoryForSingleChangeOutput());
            if (vForm.getMergedValues()[i].equals("L")) {
                newOutput.setOriginalValueOutput(new Object[] { auxOutput.getOriginalValueOutput()[1],
                        auxOutput.getOriginalValueOutput()[0] }); //add the left value, remove the one from the right
            } else if (vForm.getMergedValues()[i].equals("R")) {
                newOutput.setOriginalValueOutput(new Object[] { null, null }); //value is in place
            } else {
                // The user didn't select a value, then is empty (this is
                // important because the merged activity is a copy of one of the
                // compared versions).
                newOutput.setOriginalValueOutput(new Object[] { null, auxOutput.getOriginalValueOutput()[0] });
                //remove the one from right

                // Raise error if mandatory fields have no values.
                if (newOutput.getMandatoryForSingleChangeOutput()) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                            "error.aim.versioning.missingMandatoryValue", auxOutput.getDescriptionOutput()));
                    hasErrors = true;
                }
            }
            auxData.add(newOutput);
        }
        if (hasErrors) {
            saveErrors(request, errors);
            vForm.setShowMergeColumn(true);
            vForm.setMethod("enableMerge");
            return mapping.findForward("forward");
        }


        // The main idea is: once we have the collection with fields selected by
        // the user we need to COPY one of the AmpActivity objects and call the
        // getters from these fields and push those values into the matching
        // fields
        // in the new activity. Some values will have to be auto calculated like
        // some ids, dates, etc.
        // Others are more complex like collections or our objects that
        // implement Versionable, in those cases we need to call a specific
        // method to get the value of the field in a right format for inserting
        // into a new activity, ie: set the activityid property, etc.
        Session session = null;
        Transaction transaction = null;
        try {
            session = PersistenceManager.openNewSession();
            session.setFlushMode(FlushMode.COMMIT);
            transaction = session.beginTransaction();


            TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
            AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class, tm.getMemberId());

            AmpActivityVersion oldActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, vForm
                    .getOldActivity().getAmpActivityId());



            // Insert fields selected by user into AmpActity properties.
            Iterator<CompareOutput> iter = auxData.iterator();
            while (iter.hasNext()) {
                CompareOutput co = iter.next();
                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(co.getFieldOutput().getName(),
                        AmpActivityFields.class, "set");
                // Get value as object.
                Object addOriginalValueObject = co.getOriginalValueOutput()[0];
                Object remOriginalValueObject = co.getOriginalValueOutput()[1];
                // Check if implements Versionable and call prepareMerge.
                if (addOriginalValueObject != null) {
                    /* cloning of whole activity is done later
                    if (ActivityVersionUtil.implementsVersionable(addOriginalValueObject.getClass().getInterfaces())) {
                        Versionable auxVersionableValueObject = (Versionable) addOriginalValueObject;
                        addOriginalValueObject = auxVersionableValueObject.prepareMerge(auxActivity);
                    }
                    */
                    Class[] params = auxMethod.getParameterTypes();
                    if (params != null && params[0].getName().contains("java.util.Set")) {
                        Method auxGetMethod = ActivityVersionUtil.getMethodFromFieldName(co.getFieldOutput().getName(),
                                AmpActivityVersion.class, "get");
                        Set auxSet = (Set) auxGetMethod.invoke(oldActivity);
                        if (auxSet == null) {
                            auxSet = new HashSet();
                        }
                        auxSet.add(addOriginalValueObject);
                        auxMethod.invoke(oldActivity, auxSet);
                    } else {
                        auxMethod.invoke(oldActivity, addOriginalValueObject);
                    }
                    //session.update(auxActivity);
                }

                if (remOriginalValueObject != null) {
                    Class[] params = auxMethod.getParameterTypes();
                    if (params != null && params[0].getName().contains("java.util.Set")) {
                        Class clazz = remOriginalValueObject.getClass();
                        String idProperty = session.getSessionFactory().getClassMetadata(clazz)
                                .getIdentifierPropertyName();

                        Method method = clazz.getMethod("get" + Strings.capitalize(idProperty));
                        Long remId = (Long) method.invoke(remOriginalValueObject);

                        Method auxGetMethod = ActivityVersionUtil.getMethodFromFieldName(co.getFieldOutput().getName(),
                                AmpActivityVersion.class, "get");
                        Set auxSet = (Set) auxGetMethod.invoke(oldActivity);
                        if (auxSet != null) {
                            Iterator it = auxSet.iterator();
                            while (it.hasNext()) {
                                Object tmp = (Object) it.next();
                                Long tmpId = (Long) method.invoke(tmp);

                                if (tmpId.compareTo(remId) == 0) {
                                    it.remove();
                                    break;
                                }
                            }
                        }
                        auxMethod.invoke(oldActivity, auxSet);
                    } else {
                        if (addOriginalValueObject == null) {
                            // this is the case where no value was selected;
                            auxMethod.invoke(oldActivity, (Object) null);
                        }
                    }
                }
            }

            ContentTranslationUtil.cloneTranslations(oldActivity);
            AmpActivityVersion auxActivity = ActivityVersionUtil.cloneActivity(oldActivity, member);
            auxActivity.setAmpActivityId(null);

            session.evict(oldActivity);

            // Code related to versioning.
            AmpActivityGroup auxActivityGroup = (AmpActivityGroup) session.load(AmpActivityGroup.class, vForm
                    .getOldActivity().getAmpActivityGroup().getAmpActivityGroupId());
            AmpActivityVersion prevVersion      = auxActivityGroup.getAmpActivityLastVersion();
            auxActivityGroup.setAmpActivityLastVersion(auxActivity);
            session.save(auxActivityGroup);

            Date updatedTime = Calendar.getInstance().getTime();
            auxActivity.setAmpActivityGroup(auxActivityGroup);
            auxActivity.setModifiedDate(updatedTime);
            auxActivity.setUpdatedDate(updatedTime);
            auxActivity.setModifiedBy(member);

            auxActivity.setMergedActivity(true);
            auxActivity.setMergeSource1(vForm.getActivityOne());
            auxActivity.setMergeSource2(vForm.getActivityTwo());

            String ampId = ActivityUtil.generateAmpId(member.getUser(), prevVersion.getAmpActivityId(), session);
            auxActivity.setAmpId(ampId);

            AmpActivityContact actCont;
            Set<AmpActivityContact> contacts = new HashSet<AmpActivityContact>();
            Set<AmpActivityContact> activityContacts = auxActivity.getActivityContacts();
            if (activityContacts != null) {
                Iterator<AmpActivityContact> it = activityContacts.iterator();
                while (it.hasNext()) {
                    actCont = it.next();
                    actCont.setId(null);
                    actCont.setActivity(auxActivity);
                    session.save(actCont);
                    contacts.add(actCont);
                }
                auxActivity.setActivityContacts(contacts);
            }
            session.save(auxActivity);
            transaction.commit();

            logger.warn("Activity Saved.");

            Site site = RequestUtils.getSite(request);
            Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
            java.util.Locale locale = new java.util.Locale(navigationLanguage.getCode());
            LuceneUtil.addUpdateActivity(request.getSession().getServletContext().getRealPath("/"), true, site, locale,
                    auxActivity, prevVersion);
            AuditLoggerUtil.logObject(request, auxActivity, "add", "merged");
        } catch (Exception e) {
            logger.error("Can't save merged activity:", e);
            transaction.rollback();
        } finally {
            PersistenceManager.closeSession(session);
        }
        return mapping.findForward("index");
    }

    private void setAdvancemode(CompareActivityVersionsForm vForm, HttpServletRequest request) {
        boolean ispartofamanagetmentworkspace = false;
        boolean iscurrentworkspacemanager = false;

        TeamMember currentMember = (TeamMember) request.getSession().getAttribute("currentMember");
        AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());

        if (ampCurrentMember.getAmpMemberRole().getTeamHead()) {
            iscurrentworkspacemanager = true;
        }
        if (ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
            ispartofamanagetmentworkspace = true;
        }

        // If the current user is part of the management workspace or is not the
        // workspace manager of a workspace that's not management then hide.
        vForm.setAdvancemode(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
    }

    public ActionForward viewDifferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;

        ActivityComparisonResult result = ActivityVersionUtil.compareActivities(vForm.getActivityOneId());
        if (result != null) {
            vForm.setOutputCollectionGrouped(result.getCompareOutput());
            vForm.setActivityName(result.getName());
        } else {
            vForm.setOutputCollectionGrouped(null);
            vForm.setActivityName(null);
        }

        return mapping.findForward("forward");
    }

    public ActionForward compareAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        boolean isPermitted = AuditLoggerUtil.checkPermission(request);
        if (isPermitted) {
            vForm.populateEffectiveFilters();
            if (tm.getTeamHead()) {
                vForm.setEffectiveSelectedTeam(tm.getTeamName());
            }
            vForm.setActivityComparisonResultList(ActivityVersionUtil.getOutputCollectionGrouped(
                    vForm.getEffectiveSelectedUser(), vForm.getEffectiveSelectedTeam(), vForm.getEffectiveDateFrom(),
                    vForm.getEffectiveDateTo()));
        }
        return mapping.findForward("forward");
    }
    public ActionForward pdfExport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        Map<String, List<CompareOutput>> outputCollectionGrouped = vForm.getOutputCollectionGrouped();
        ByteArrayOutputStream baos;
        List<ActivityComparisonResult> comparisonResult;
        if (vForm.getActivityOneId() == 0) {
            comparisonResult = vForm.getActivityComparisonResultList();
        } else {
            AmpActivityVersion av = ActivityUtil.loadActivity(vForm.getActivityOneId());
            comparisonResult = new ArrayList(Arrays.asList(new ActivityComparisonResult(av.getAmpActivityId(),
                    av.getAmpId() + " " + av.getName(), outputCollectionGrouped)));
        }
        baos = AuditPDFexporter.getInstance().buildPDFexport(comparisonResult);

        response.setContentType("application/pdf; charset=UTF-8");
        response.setHeader("content-disposition", "attachment;filename=activity.pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        return null;
    }

    public ActionForward xlsExport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        List<ActivityComparisonResult> comparisonResult;
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=AuditLogger.xls");
        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        AuditExcelExporter auditExcelExporter = new AuditExcelExporter();
        if (vForm.getActivityOneId() == 0) {
            List<ActivityComparisonResult> outputCollection = vForm.getActivityComparisonResultList();
            HSSFWorkbook wb = auditExcelExporter.generateExcel(outputCollection);
            wb.write(response.getOutputStream());
        } else {
            Map<String, List<CompareOutput>> outputCollectionGrouped = vForm.getOutputCollectionGrouped();
            comparisonResult = new ArrayList(Arrays.asList(new ActivityComparisonResult(vForm.getActivityOneId(),
                    vForm.getActivityName(), outputCollectionGrouped)));
            HSSFWorkbook wb = auditExcelExporter.generateExcel(comparisonResult);
            wb.write(response.getOutputStream());
        }
        return null;
    }
}