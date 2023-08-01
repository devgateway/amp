package org.digijava.module.aim.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.*;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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
            //session.update(activity);

            Site site = RequestUtils.getSite(request);
            Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
            java.util.Locale locale = new java.util.Locale(navigationLanguage.getCode());
            LuceneUtil.addUpdateActivity(request.getSession().getServletContext().getRealPath("/"), true, site, locale, activity, prevVer);
            
            return new ActionForward(mapping.findForward("reload").getPath() + "?ampActivityId=" + activityId,true);
        }

        vForm.setOutputCollection(new ArrayList<CompareOutput>());
        // Load the activities.
        vForm.setActivityOne((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityOneId()));
        Hibernate.initialize(vForm.getActivityOne());
        ActivityVersionUtil.initializeActivity(vForm.getActivityOne());
        vForm.setActivityTwo((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityTwoId()));
        Hibernate.initialize(vForm.getActivityTwo());
        ActivityVersionUtil.initializeActivity(vForm.getActivityTwo());

        vForm.setOldActivity(vForm.getActivityOne());
        
        
        ActivityHistory auditHistory1 = getAuditHistory(vForm.getActivityOne());
        ActivityHistory auditHistory2 = getAuditHistory(vForm.getActivityTwo());
        
        // Retrieve annotated for versioning fields.
        Field[] fields = AmpActivityFields.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //logger.info(fields[i]);
            CompareOutput output = new CompareOutput();

            if (fields[i].isAnnotationPresent(VersionableFieldSimple.class)) {
                // Obtain "get" method from field.
                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class, "get");

                // Compare values from 2 versions.
                Object auxResult1 = auxMethod.invoke(vForm.getActivityOne(), null);
                Object auxResult2 = auxMethod.invoke(vForm.getActivityTwo(), null);
                
                // Obtain annotation object.
                VersionableFieldSimple auxAnnotation = (VersionableFieldSimple) fields[i]
                        .getAnnotation(VersionableFieldSimple.class);

                // Sanitize String values.
                if (auxMethod.getReturnType().getName().equals("java.lang.String")) {
                    if (auxResult1 != null && auxResult1.toString().trim().equals("")) {
                        auxResult1 = null;
                    }
                    if (auxResult2 != null && auxResult2.toString().trim().equals("")) {
                        auxResult2 = null;
                    }
                }
                
                // AMP-25074 - retrieve information about who and when modified the activity 
                // from amp_audit_logger (if it is empty in the activity object)
                if (StringUtils.equals(fields[i].getName(), "modifiedBy")) {
                    auxResult1 = ActivityUtil.getModifiedByUserName(vForm.getActivityOne(), auditHistory1);
                    auxResult2 = ActivityUtil.getModifiedByUserName(vForm.getActivityTwo(), auditHistory2);
                } else if (StringUtils.equals(fields[i].getName(), "updatedDate")) {
                    auxResult1 = ActivityUtil.getModifiedByDate(vForm.getActivityOne(), auditHistory1);
                    auxResult2 = ActivityUtil.getModifiedByDate(vForm.getActivityTwo(), auditHistory2);
                }

                // Compare values, if both are null then they are considered equal.
                if (!(auxResult1 == null && auxResult2 == null)) {
                    if ((auxResult1 != null && auxResult2 == null)
                            || (auxResult1 == null && auxResult2 != null)
                            || (!auxResult1.equals(auxResult2))) {

                        logger.warn(fields[i].getName() + ": " + auxResult1 + "-" + auxResult2);
                        output.setDescriptionOutput(auxAnnotation.fieldTitle());
                        output.setFieldOutput(fields[i]);

                        // Identity "read-value" and "mandatory" fields.
                        // TODO: If needed do the same for
                        // @VersionableFieldTextEditor and
                        // @VersionableCollection.
                        output.setBlockSingleChangeOutput(auxAnnotation.blockSingleChange());
                        output.setMandatoryForSingleChangeOutput(auxAnnotation.mandatoryForSingleChange());

                        // Differentiate Wrappers from Classes that implements Versionable.
                        Class auxReturnType = auxMethod.getReturnType();

                        if (auxReturnType.getName().equals("java.util.Date")
                                || auxReturnType.getName().equals("java.sql.Date")
                                || auxReturnType.getName().equals("java.lang.String")
                                || auxReturnType.getName().equals("java.lang.Double")
                                || auxReturnType.getName().equals("java.lang.Integer")
                                || auxReturnType.getName().equals("java.lang.Long")
                                || auxReturnType.getName().equals("java.lang.Short")
                                || auxReturnType.getName().equals("java.lang.Float")
                                || auxReturnType.getName().equals("java.lang.Boolean")
                                || auxReturnType.getName().equals("java.math.BigDecimal")) {


                            String aux1String = getStringOrEmpty(auxResult1);
                            String aux2String = getStringOrEmpty(auxResult2);
                           
                            output.setStringOutput(new String[] {aux1String, aux2String});
                            output.setOriginalValueOutput(new Object[] {auxResult1, auxResult2});
                        } else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
                            Versionable auxVersionable1 = Versionable.class.cast(auxMethod.invoke(vForm
                                    .getActivityOne(), null));
                            Versionable auxVersionable2 = Versionable.class.cast(auxMethod.invoke(vForm
                                    .getActivityTwo(), null));

                            String output1 = (auxVersionable1 != null) ? ActivityVersionUtil.generateFormattedOutput(
                                    request, auxVersionable1.getOutput()) : null;
                            String output2 = (auxVersionable2 != null) ? ActivityVersionUtil.generateFormattedOutput(
                                    request, auxVersionable2.getOutput()) : null;
                            output.setStringOutput(new String[] { output1, output2 });
                            output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
                        } else {
                            if ("modifiedBy".equals(fields[i].getName())) {
                                // this field cannot be used in merging
                                output.setBlockSingleChangeOutput(true);
                            }
                            
                            output.setStringOutput(new String[] {getStringOrEmpty(auxResult1), getStringOrEmpty(auxResult2)});
                            output.setOriginalValueOutput(new Object[] {auxResult1, auxResult2});
                        }
                        
                        
                        vForm.getOutputCollection().add(output);
                    }
                }
            }
            if (fields[i].isAnnotationPresent(VersionableFieldTextEditor.class)) {
                // Obtain "get" method from field.
                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
                        "get");

                // Compare values from 2 versions.
                String auxResult1 = (String) auxMethod.invoke(vForm.getActivityOne(), null);
                String auxResult2 = (String) auxMethod.invoke(vForm.getActivityTwo(), null);

                // Obtain annotation object.
                VersionableFieldTextEditor auxAnnotation = (VersionableFieldTextEditor) fields[i]
                        .getAnnotation(VersionableFieldTextEditor.class);

                // Compare values, if both are null then they are considered
                // equal.
                if (!(auxResult1 == null && auxResult2 == null)) {
                    if ((auxResult1 != null && auxResult2 == null) || (auxResult1 == null && auxResult2 != null)
                            || (!auxResult1.equals(auxResult2))) {
                        output.setFieldOutput(fields[i]);
                        output.setBlockSingleChangeOutput(false);
                        output.setMandatoryForSingleChangeOutput(false);

                        output.setDescriptionOutput(auxAnnotation.fieldTitle());
                        Site site = RequestUtils.getSite(request);
                        String lang = RequestUtils.getNavigationLanguage(request).getCode();
                        String auxBody1 = DbUtil.getEditorBody(site, auxResult1, lang);
                        String auxBody2 = DbUtil.getEditorBody(site, auxResult2, lang);
                        auxBody1 = auxBody1 != null ? auxBody1 : "";
                        auxBody2 = auxBody2 != null ? auxBody2 : "";
                        if (!auxBody1.trim().equals(auxBody2.trim())) {
                            output.setStringOutput(new String[] { auxBody1, auxBody2 });
                            output.setOriginalValueOutput(new String[] { auxResult1, auxResult2 });
                            vForm.getOutputCollection().add(output);
                        }
                    }
                }
            }
            if (fields[i].isAnnotationPresent(VersionableCollection.class)) {
                // Obtain "get" method from field.
                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
                        "get");
                // Get values from 2 versions.
                // Sometimes I have a problem with lazy collections and
                // apparently closed session.
                // Delete these lines if there are problems when saving: two
                // sessions problem.
                session = PersistenceManager.getRequestDBSession();
                Object auxResult1 = auxMethod.invoke(vForm.getActivityOne(), null);
                Hibernate.initialize(auxResult1);
                Object auxResult2 = auxMethod.invoke(vForm.getActivityTwo(), null);
                Hibernate.initialize(auxResult2);
                // Obtain annotation object.
                VersionableCollection auxAnnotation = (VersionableCollection) fields[i]
                        .getAnnotation(VersionableCollection.class);
                // Create list of differences.
                List<CompareOutput> differences = new ArrayList<CompareOutput>();
                Collection auxCollection1 = (Collection) auxResult1;
                Collection auxCollection2 = (Collection) auxResult2;

                // Evaluate the type of objects stored in the collection.
                // TODO: Assuming everything is a Collection, later to implement
                // other types.
                if ((auxCollection1 == null && auxCollection2 == null)
                        || (auxCollection1 == null && auxCollection2.size() == 0)
                        || (auxCollection1.size() == 0 && auxCollection2 == null)
                        || (auxCollection1.size() == 0 && auxCollection2.size() == 0)) {
                    // Collections are equal.
                    continue;
                }
                Class auxReturnType = null;
                if (auxCollection1 != null && auxCollection1.size() != 0) {
                    auxReturnType = auxCollection1.toArray()[0].getClass();
                } else {
                    auxReturnType = auxCollection2.toArray()[0].getClass();
                }
                if (auxReturnType.getName().equals("java.util.Date") || auxReturnType.getName().equals("java.sql.Date")
                        || auxReturnType.getName().equals("java.lang.String")
                        || auxReturnType.getName().equals("java.lang.Double")
                        || auxReturnType.getName().equals("java.lang.Integer")
                        || auxReturnType.getName().equals("java.lang.Long")
                        || auxReturnType.getName().equals("java.lang.Short")
                        || auxReturnType.getName().equals("java.lang.Float")
                        || auxReturnType.getName().equals("java.lang.Boolean")
                        || auxReturnType.getName().equals("java.math.BigDecimal")) {

                    // Wrappers don't have IDs then we can't detect "updates".
                    // Iterate each collection and show as different the values
                    // not present in the other.
                    Iterator iter1 = auxCollection1.iterator();
                    while (iter1.hasNext()) {
                        int coincidence = 0;
                        Object auxObject1 = iter1.next();
                        String auxValue1 = auxObject1.toString();
                        Iterator iter2 = auxCollection2.iterator();
                        while (iter2.hasNext()) {
                            if (auxValue1.equals((String) iter2.next())) {
                                coincidence++;
                            }
                        }
                        if (coincidence == 0) {
                            CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
                                    auxValue1, "" }, fields[i], new Object[] { auxObject1, null }, false, false);
                            vForm.getOutputCollection().add(auxOutput);
                        }
                    }
                    iter1 = auxCollection2.iterator();
                    while (iter1.hasNext()) {
                        int coincidence = 0;
                        Object auxObject2 = iter1.next();
                        String auxValue2 = auxObject2.toString();
                        Iterator iter2 = auxCollection1.iterator();
                        while (iter2.hasNext()) {
                            if (auxValue2.equals((String) iter2.next())) {
                                coincidence++;
                            }
                        }
                        if (coincidence == 0) {
                            CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] { "",
                                    auxValue2 }, fields[i], new Object[] { null, auxObject2 }, false, false);
                            vForm.getOutputCollection().add(auxOutput);
                        }
                    }
                } else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
                    List<Versionable> auxList = new ArrayList<Versionable>();
                    Iterator iter1 = auxCollection1.iterator();
                    while (iter1.hasNext()) {
                        int coincidence = 0;
                        Object auxObject1 = iter1.next();
                        Versionable auxVersionable1 = (Versionable) auxObject1;
                        Iterator iter2 = auxCollection2.iterator();
                        while (iter2.hasNext()) {
                            Object auxObject2 = iter2.next();
                            Versionable auxVersionable2 = (Versionable) auxObject2;
                            if (auxVersionable1.equalsForVersioning(auxVersionable2)) {
                                coincidence++;
                                Object auxValue1 = auxVersionable1.getValue() != null ? auxVersionable1.getValue() : "";
                                Object auxValue2 = auxVersionable2.getValue() != null ? auxVersionable2.getValue() : "";
                                if (!auxValue1.equals(auxValue2)) {
                                    CompareOutput auxOutput = null;

                                    if (auxVersionable1.getClass().getName().
                                            equals("org.digijava.module.aim.dbentity.AmpFunding") &&
                                            auxVersionable2.getClass().getName().
                                            equals("org.digijava.module.aim.dbentity.AmpFunding")
                                            ) {
                                            auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
                                                    new String[] {
                                                            ActivityVersionUtil.generateFormattedOutput(request,
                                                                    auxVersionable1.getOutput(),
                                                                    auxVersionable2.getOutput()),
                                                            ActivityVersionUtil.generateFormattedOutput(request,
                                                                    auxVersionable2.getOutput(),
                                                                    auxVersionable1.getOutput()) }, fields[i], new Object[] {
                                                    auxObject1, auxObject2 }, false, false);

                                    } else {
                                        auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
                                        new String[] {
                                                ActivityVersionUtil.generateFormattedOutput(request,
                                                        auxVersionable1.getOutput()),
                                                ActivityVersionUtil.generateFormattedOutput(request,
                                                        auxVersionable2.getOutput()) }, fields[i], new Object[] {
                                                auxObject1, auxObject2 }, false, false);
                                    }
                                    vForm.getOutputCollection().add(auxOutput);
                                    auxList.add(auxVersionable1);
                                    auxList.add(auxVersionable2);
                                }
                            }
                        }
                        if (coincidence == 0) {
                            CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
                                    ActivityVersionUtil.generateFormattedOutput(request, auxVersionable1.getOutput()),
                                    "" }, fields[i], new Object[] { auxObject1, null }, false, false);
                            auxOutput.setBlockSingleChangeOutput(auxVersionable1.getOutput().hasDeletedValues());
                            vForm.getOutputCollection().add(auxOutput);
                            auxList.add(auxVersionable1);
                        }
                    }
                    iter1 = auxCollection2.iterator();
                    while (iter1.hasNext()) {
                        int coincidence = 0;
                        Object auxObject2 = iter1.next();
                        Versionable auxVersionable2 = (Versionable) auxObject2;
                        Iterator iter2 = auxCollection1.iterator();
                        while (iter2.hasNext()) {
                            Object auxObject1 = iter2.next();
                            Versionable auxVersionable1 = (Versionable) auxObject1;
                            if (auxVersionable2.equalsForVersioning(auxVersionable1)) {
                                coincidence++;
                                Object auxValue1 = auxVersionable1.getValue() != null ? auxVersionable1.getValue() : "";
                                Object auxValue2 = auxVersionable2.getValue() != null ? auxVersionable2.getValue() : "";
                                if (!auxValue2.equals(auxValue1)) {
                                    // Check if the object was added in the
                                    // previous iteration.
                                    if (!auxList.contains(auxVersionable2)) {
                                        CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
                                                new String[] {
                                                        ActivityVersionUtil.generateFormattedOutput(request,
                                                                auxVersionable1.getOutput()),
                                                        ActivityVersionUtil.generateFormattedOutput(request,
                                                                auxVersionable2.getOutput()) }, fields[i],
                                                new Object[] { auxObject1, auxObject2 }, false, false);
                                        vForm.getOutputCollection().add(auxOutput);
                                    }
                                }
                            }
                        }
                        if (coincidence == 0) {
                            // Check if the object was added in the
                            // previous iteration.
                            if (!auxList.contains(auxVersionable2)) {
                                CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
                                        "",
                                        ActivityVersionUtil.generateFormattedOutput(request, auxVersionable2
                                                .getOutput()) }, fields[i], new Object[] { null, auxObject2 }, false,
                                        false);
                                auxOutput.setBlockSingleChangeOutput(auxVersionable2.getOutput().hasDeletedValues());
                                vForm.getOutputCollection().add(auxOutput);
                            }
                        }
                    }
                } else {
                    output.setStringOutput(new String[] { auxResult1 != null ? auxResult1.toString() : "",
                            auxResult2 != null ? auxResult2.toString() : "" });
                    output.setDescriptionOutput(auxAnnotation.fieldTitle());
                    vForm.getOutputCollection().add(output);
                }
            }
        }

        Map<String, List<CompareOutput>> outputGroupped = groupOutputCollection (vForm.getOutputCollection());
        modifyFundingOutputs (outputGroupped);
        vForm.setOutputCollectionGrouped(outputGroupped);
    
        return mapping.findForward("forward");
    }

    private void modifyFundingOutputs (Map<String, List<CompareOutput>> outputGroupped) {


    }

    private Map<String, List<CompareOutput>> groupOutputCollection (List<CompareOutput> outputCollection) {
        Map<String, List<CompareOutput>> retVal = new HashMap<String, List<CompareOutput>>();
        int idx = 0;
        for (CompareOutput obj: outputCollection) {
            if (!obj.getBlockSingleChangeOutput()) {
                obj.setIndex(idx);
                idx ++;
            } else {
                obj.setIndex(-1); // skip from merge process
            }
            if (!retVal.containsKey(obj.getDescriptionOutput())) {
                retVal.put(obj.getDescriptionOutput(), new ArrayList<CompareOutput>());
            }
            retVal.get(obj.getDescriptionOutput()).add(obj);

        }

        return retVal;
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
                newOutput.setOriginalValueOutput(new Object[] { null, auxOutput.getOriginalValueOutput()[0] }); //remove the one from right 

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
    
    
        TeamMember currentTeamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(currentTeamMember.getMemberId());
        
        saveActivity(member, vForm, auxData, request);
        
        return mapping.findForward("index");
    }
    
    private void saveActivity(AmpTeamMember member, CompareActivityVersionsForm vForm,
                              List<CompareOutput> auxData, HttpServletRequest request) {
    
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
        
                if (remOriginalValueObject != null){
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
                        
                                if (tmpId.compareTo(remId) == 0){
                                    it.remove();
                                    break;
                                }
                            }
                        }
                        auxMethod.invoke(oldActivity, auxSet);
                    }
                    else{
                        if (addOriginalValueObject == null){
                            // this is the case where no value was selected;
                            auxMethod.invoke(oldActivity, (Object)null);
                        }
                    }
                }
            }
    
            ContentTranslationUtil.cloneTranslations(oldActivity);
            AmpActivityVersion auxActivity = ActivityVersionUtil.cloneActivity(oldActivity);
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
    
            auxActivity.setMergedActivity(true);
            auxActivity.setMergeSource1(vForm.getActivityOne());
            auxActivity.setMergeSource2(vForm.getActivityTwo());
    
            String ampId = ActivityUtil.generateAmpId(member.getUser(), prevVersion.getAmpActivityId(), session);
            auxActivity.setAmpId(ampId);
    
            AmpActivityContact actCont;
            Set<AmpActivityContact> contacts = new HashSet<AmpActivityContact>();
            Set<AmpActivityContact> activityContacts = auxActivity.getActivityContacts();
            if (activityContacts != null){
                Iterator<AmpActivityContact> it = activityContacts.iterator();
                while(it.hasNext()){
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
            LuceneUtil.addUpdateActivity(request.getSession().getServletContext().getRealPath("/"), true, site,
                    locale, auxActivity, prevVersion);
            AuditLoggerUtil.logObject(request, auxActivity, "add", "merged");
        } catch (Exception e) {
            logger.error("Can't save merged activity:", e);
            transaction.rollback();
        } finally {
            PersistenceManager.closeSession(session);
        }
    }

    private void setAdvancemode(CompareActivityVersionsForm vForm, HttpServletRequest request){
        boolean ispartofamanagetmentworkspace = false;
        boolean iscurrentworkspacemanager = false;
        
        TeamMember currentMember = (TeamMember)request.getSession().getAttribute("currentMember");
        AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
        
        if (ampCurrentMember.getAmpMemberRole().getTeamHead())
            iscurrentworkspacemanager = true;
        if (ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT))
            ispartofamanagetmentworkspace = true;
        
        //If the current user is part of the management workspace or is not the workspace manager of a workspace that's not management then hide.
        vForm.setAdvancemode(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
    }
    
    private String getStringOrEmpty(Object o) {
        if (o != null) {
            if (o instanceof Date || o instanceof java.sql.Date) {
                return FormatHelper.formatDate((Date) o);
            }
            
            return o.toString();
        }
        
        return "";
    }
    
    private ActivityHistory getAuditHistory(AmpActivityVersion activity) {
        ActivityHistory auditHistory = null;
        
        if (activity.getModifiedBy() == null || (activity.getUpdatedDate() == null && activity.getModifiedDate() == null)) {
            auditHistory = ActivityUtil.getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
        }
        
        return auditHistory;
    }
}
