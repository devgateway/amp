package org.digijava.module.aim.util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.version.exception.CannotGetLastVersionForVersionException;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class ActivityVersionUtil {

    private static Logger logger = Logger.getLogger(ActivityVersionUtil.class);


    public static Method getMethodFromFieldName(String fieldName, Class auxClass, String prefix) throws Exception {
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        methodName = prefix + methodName;
        if (prefix.equals("set")) {
            for (int i = 0; i < auxClass.getDeclaredMethods().length; i++) {
                if (auxClass.getDeclaredMethods()[i].toString().contains(methodName)) {
                    return auxClass.getDeclaredMethods()[i];
                }
            }
        }
        try {
            return auxClass.getMethod(methodName, null);
        }
        catch(Exception e) {
            if (prefix.equals("get")) {
                // boolean fields might have "is" as a prefix
                return getMethodFromFieldName(fieldName, auxClass, "is");
            }
            throw e;
        }
    }

    public static boolean implementsVersionable(Class[] array) {
        boolean ret = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i].getName().equals("org.digijava.module.aim.dbentity.Versionable")) {
                ret = true;
            }
        }
        return ret;
    }

    public static String generateFormattedOutput(Output out) throws WorkerException {
        // TODO Auto-generated method stub
        return generateFormattedOutput(out, null);
    }

    public static String generateFormattedOutput(Output out, Output out1) throws WorkerException {
    

        
            Site site = TLSUtils.getSite();
            String langCode = TLSUtils.getEffectiveLangCode();
        
        
        StringBuilder ret = new StringBuilder();
        if (out.getOutputs() != null) {
            // First level.
            Iterator<Output> iter = out.getOutputs().iterator();
            int iterIdx = 0;
            while (iter.hasNext()) {
                Output auxOutput = iter.next();
                boolean existsInOtherVersion = true;

                for (int i = 0; i < auxOutput.getTitle().length; i++) {
                    String title = auxOutput.getTitle()[i];

                    if (out1 != null) {
                        existsInOtherVersion = out1.getOutputByTitle(title) != null;
                    }
                    if (!title.trim().isEmpty()) {
                        if (!existsInOtherVersion) ret.append("<font color='red'>");
                        ret.append("<br/><b>").append(TranslatorWorker.translateText(auxOutput.getTitle()[i], langCode, site.getId())).
                                append(":</b>&nbsp;");
                        if (!existsInOtherVersion) ret.append("</font>");
                    }
                }
                for (int i = 0; i < auxOutput.getValue().length; i++) {
                    /*
                     * if (auxOutput.getValue()[i] instanceof Date) { String
                     * date = DateConversion.ConvertDateToString((Date)
                     * auxOutput.getValue()[i]); ret += date; } else {
                     */
                    if (auxOutput.getValue()[i]!=null){
                        String text = auxOutput.getValue()[i].toString();
                        if (auxOutput.getTranslateValue())
                            text = TranslatorWorker.translateText(text, langCode, site.getId());
                        if (!existsInOtherVersion) ret.append("<font color='red'>");
                        ret.append(org.digijava.module.aim.util.DbUtil.filter(text));
                        if (!existsInOtherVersion) ret.append("</font>");
                    }
                    
                }
                if (auxOutput.getOutputs() != null) {

                    Output output2 = null;
                    if (out1 != null) {
                        output2 = out1.getOutputByTitle(auxOutput.getTitle()[0]);
                    }


                    // Second level.
                    String tabs = "<br/> &nbsp; &nbsp; &nbsp;";
                    Iterator<Output> iter2 = auxOutput.getOutputs().iterator();
                    while (iter2.hasNext()) {
                        Output auxOutput2 = iter2.next();

                        Output output3 = null;
                        if (output2 != null) {
                            output3 = output2.getOutputByValues(auxOutput2.getValue());
                        }

                        ret.append(tabs);
                        for (int i = 0; i < auxOutput2.getTitle().length; i++) {
                            ret.append("<b>").
                                    append(TranslatorWorker.translateText(auxOutput2.getTitle()[i], langCode, site.getId())).
                                    append("</b>");
                        }
                        for (int i = 0; i < auxOutput2.getValue().length; i++) {
                            boolean markAsDifferent = false;
                            if (output2 != null && output3 == null) {
                                markAsDifferent = true;
                                ret.append("<font color='red'>");
                            }
                            
                            if (auxOutput2.getValue()[i] instanceof Timestamp) {
                                String date = DateConversion.convertDateToString(new Date(((Timestamp) auxOutput2.getValue()[i]).getTime()));
                                ret.append(date);
                            } else if (auxOutput2.getValue()[i] instanceof BigDecimal 
                                    || auxOutput2.getValue()[i] instanceof Double 
                                    || auxOutput2.getValue()[i] instanceof Float) {
                                NumberFormat formatter = FormatHelper.getDecimalFormat();
                                formatter.setMaximumFractionDigits(0);
                                ret.append(formatter.format(auxOutput2.getValue()[i]));
                            } else {
                                String text = auxOutput2.getValue()[i].toString();
                                if (auxOutput2.getTranslateValue())
                                    text = TranslatorWorker.translateText(text, langCode, site.getId());
                                ret.append(org.digijava.module.aim.util.DbUtil.filter(text));
                            }
                            if (markAsDifferent)
                                ret.append("</font>");
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < out.getTitle().length; i++) {
                ret.append(out.getTitle()[i]);
            }
            for (int i = 0; i < out.getValue().length; i++) {
                ret.append(out.getValue()[i]);
            }
        }
        return ret.toString();
    }

    public static int numberOfVersions() {
        //AMP-17263: no good reason to have this feature, so effectively disabling it.
        //per issue AMP-18412 i've reverted the changes of AMP-17263.
        int aux = 999999; //if the queue size doesn't exists we show all.
        String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.VERSION_QUEUE_SIZE);
        if (gsValue != null) {
            try {
                aux = Integer.valueOf(gsValue).intValue();
            } catch (NumberFormatException e) {
                logger.error(e);
            }
        }
        return aux;
    }
    
    public static boolean isVersioningEnabled(){
        return (numberOfVersions() > 0);
    }
    
    public static Long getLastVersionForVersion(Long oldActivity) throws CannotGetLastVersionForVersionException {
        try {
            Session session = PersistenceManager.getSession();
            String queryStr = "SELECT v.ampActivityGroup.ampActivityLastVersion.ampActivityId  FROM " +AmpActivityVersion.class.getName() +
                    " v  WHERE v.ampActivityId=:oldActivityId";
            Query query     = session.createQuery(queryStr);
            query.setLong("oldActivityId", oldActivity);
            Long id     = (Long)query.uniqueResult();
            return id;
        } catch (Exception e) {
            logger.error(e.getMessage() );
            e.printStackTrace();
            throw new CannotGetLastVersionForVersionException(e);
        }
    }
    
    
        /**
     * Create a copy of the {@link AmpActivityVersion} with all Collections linked with
     * it and ready to save.
     * 
     * @param in
     * @param member
     * @return
     * @throws CloneNotSupportedException
     */
    public static AmpActivityVersion cloneActivity(AmpActivityVersion in, AmpTeamMember member) throws CloneNotSupportedException {
        AmpActivityVersion out = (AmpActivityVersion) in.clone();
        
        Class clazz = AmpActivityFields.class;
        
        Field[] fields = clazz.getDeclaredFields();//clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (Collection.class.isAssignableFrom(field.getType())){
                logger.debug("Init set: " + field.getName());
                initSet(out, field);
            }
        }

        out.setAmpActivityGroup(null);
        out.setModifiedBy(member);
        return out;
    }
    
    /**
     * for the definition of this function, please see JavaDoc for {@link #initSet(AmpActivityVersion, Field)}
     * @param obj
     * @param newActivity
     * @return
     */
    private static Object prepareMerge(Object obj, AmpActivityVersion newActivity) throws Exception {
        if (obj instanceof Versionable)
            return ((Versionable) obj).prepareMerge(newActivity);
        else
            return obj;
    }
    
    /**
     * out.field = new Set[prepareMerge(out.field.each)]
     * f(X) = ((Versionable) X.prepareMerge()), if X instanceof Versionable.
     * f(X) = X, otherwise 
     * @param out
     * @param field
     */
    private static void initSet(AmpActivityVersion out, Field field){
        String setName = Strings.capitalize(field.getName());
        Class<?> clazz = out.getClass();
        try {
            Method method = clazz.getMethod("get" + setName);
            Set<Object> returnSet = null;
            Set<?> set = (Set<?>) method.invoke(out);
            if (set != null){
                returnSet = new HashSet<>();
                for(Object obj:set)
                    returnSet.add(prepareMerge(obj, out));
                }           
            if (Set.class.isAssignableFrom(field.getType()))
                method = clazz.getMethod("set" + setName, Set.class);
            else
                method = clazz.getMethod("set" + setName, Collection.class);
            method.invoke(out, returnSet);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Can't init set '"+ setName +"':", e);
        }
    }

    /**
     * Initialize all collections (up to level 2) for an {@link AmpActivityVersion}
     * object. Use it to prevent exceptions with lazy collections.
     * 
     * @param act
     * @return
     * @throws DgException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static AmpActivityVersion initializeActivity(AmpActivityVersion act) throws DgException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Session session = PersistenceManager.getRequestDBSession();
        Method[] methods = AmpActivityVersion.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().contains("get") && methods[i].getReturnType().getName().contains("java.util.Set")) {
                Object methodValue = methods[i].invoke(act, null);
                Collection auxColl = (Collection) methodValue;
                if (auxColl != null) {
                    auxColl.size();
                    Iterator iInner = auxColl.iterator();
                    while (iInner.hasNext()) {
                        Object auxInnerObject = iInner.next();
                        Method[] innerMethods = auxInnerObject.getClass().getDeclaredMethods();
                        for (int j = 0; j < innerMethods.length; j++) {
                            if (innerMethods[j].getName().contains("get")
                                    && innerMethods[j].getReturnType().getName().contains("java.util.Set")) {
                                Object innerMethodValue = innerMethods[j].invoke(auxInnerObject, null);
                                Collection auxInnerColl = (Collection) innerMethodValue;
                                if (auxInnerColl != null) {
                                    auxInnerColl.size();
                                }
                            }
                        }
                    }
                }
            }
        }
        return act;
    }
    
    
    public static Map<String, List<CompareOutput>> compareActivities(Long activityOneId) throws Exception {

        Session session = PersistenceManager.getCurrentSession();
        AmpActivityVersion ampActivityOne = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityOneId);
        AmpActivityVersion ampActivityTwo = ActivityUtil.getPreviousVersion(ampActivityOne);
        // Since ampActivityTwo is a ref. variable of type AmpActivityVersion,
        // we can't use equals() to compare references.
        // Instead we use == operator to compare references, while equals() is to compare the object content.
        return (ampActivityTwo == null) ? null
                : compareActivities(activityOneId, ampActivityTwo.getAmpActivityId());

    }
        
       
    public static Map<String, List<CompareOutput>> compareActivities(Long activityOneId, Long activityTwoId)
            throws Exception {
        Session session = PersistenceManager.getCurrentSession();
        AmpActivityVersion ampActivityOne = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityOneId);

        Hibernate.initialize(ampActivityOne);
        ActivityVersionUtil.initializeActivity(ampActivityOne);

        AmpActivityVersion ampActivityTwo = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityTwoId);

        Hibernate.initialize(ampActivityTwo);
        ActivityVersionUtil.initializeActivity(ampActivityTwo);
        // This does not seem to be in use, check and remove
        // vForm.setOldActivity(vForm.getActivityOne());
        List<CompareOutput> outputCollection = new ArrayList<>();
        ActivityHistory auditHistory1 = ActivityVersionUtil.getAuditHistory(ampActivityOne);
        ActivityHistory auditHistory2 = ActivityVersionUtil.getAuditHistory(ampActivityTwo);
        // Retrieve annotated for versioning fields.
        Field[] fields = AmpActivityFields.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // logger.info(fields[i]);
            CompareOutput output = new CompareOutput();
            if (fields[i].isAnnotationPresent(VersionableFieldSimple.class)) {
                processVersionableSimple(ampActivityOne, ampActivityTwo, outputCollection,
                        auditHistory1, auditHistory2, fields[i], output);
            }
            if (fields[i].isAnnotationPresent(VersionableFieldTextEditor.class)) {
                processVersionableTextEditor(ampActivityOne, ampActivityTwo, outputCollection, fields[i], output);
            }
            if (fields[i].isAnnotationPresent(VersionableCollection.class)) {
                processVersionableCollection(ampActivityOne, ampActivityTwo, outputCollection, fields, i, output);
            }
        }
        return ActivityVersionUtil.groupOutputCollection(outputCollection);
    }

    private static void processVersionableCollection(AmpActivityVersion ampActivityOne,
                                                     AmpActivityVersion ampActivityTwo,
                                                     List<CompareOutput> outputCollection, Field[] fields,
                                                     int i, CompareOutput output) throws Exception {
        Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(),
                AmpActivityVersion.class, "get");
        // Get values from 2 versions.
        // Sometimes I have a problem with lazy collections and
        // apparently closed session.
        // Delete these lines if there are problems when saving: two
        // sessions problem.
        Object auxResult1 = auxMethod.invoke(ampActivityOne, null);
        Hibernate.initialize(auxResult1);
        Object auxResult2 = auxMethod.invoke(ampActivityTwo, null);
        Hibernate.initialize(auxResult2);
        // Obtain annotation object.
        VersionableCollection auxAnnotation = fields[i].getAnnotation(VersionableCollection.class);
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
            return;
        }
        Class auxReturnType = null;
        if (auxCollection1 != null && auxCollection1.size() != 0) {
            auxReturnType = auxCollection1.toArray()[0].getClass();
        } else {
            auxReturnType = auxCollection2.toArray()[0].getClass();
        }
        if (isOfSimpleType(auxReturnType)) {
            // Wrappers don't have IDs then we can't detect "updates".
            // Iterate each collection and show as different the values
            // not present in the other.
            addAsDifferentIfNnoPresent(outputCollection, fields, i, auxAnnotation, auxCollection2,
                    auxCollection1.iterator());
            addAsDifferentIfNnoPresent(outputCollection, fields, i, auxAnnotation, auxCollection2,
                    auxCollection2.iterator());
        } else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
            List<Versionable> auxList = new ArrayList<>();
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
                            if (auxVersionable1.getClass().getName()
                                    .equals("org.digijava.module.aim.dbentity.AmpFunding")
                                    && auxVersionable2.getClass().getName()
                                            .equals("org.digijava.module.aim.dbentity.AmpFunding")) {
                                auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
                                        ActivityVersionUtil.generateFormattedOutput(auxVersionable1.getOutput(),
                                                auxVersionable2.getOutput()),
                                        ActivityVersionUtil.generateFormattedOutput(auxVersionable2.getOutput(),
                                                auxVersionable1.getOutput()) },
                                        fields[i], new Object[] { auxObject1, auxObject2 }, false, false);
                            } else {
                                auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
                                        new String[] {
                                                ActivityVersionUtil
                                                        .generateFormattedOutput(auxVersionable1.getOutput()),
                                                ActivityVersionUtil
                                                        .generateFormattedOutput(auxVersionable2.getOutput()) },
                                        fields[i], new Object[] { auxObject1, auxObject2 }, false, false);
                            }
                            outputCollection.add(auxOutput);
                            auxList.add(auxVersionable1);
                            auxList.add(auxVersionable2);
                        }
                    }
                }
                if (coincidence == 0) {
                    CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
                            ActivityVersionUtil.generateFormattedOutput(auxVersionable1.getOutput()), "" },
                            fields[i], new Object[] { auxObject1, null }, false, false);
                    outputCollection.add(auxOutput);
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
                                                ActivityVersionUtil
                                                        .generateFormattedOutput(auxVersionable1.getOutput()),
                                                ActivityVersionUtil
                                                        .generateFormattedOutput(auxVersionable2.getOutput()) },
                                        fields[i], new Object[] { auxObject1, auxObject2 }, false, false);
                                outputCollection.add(auxOutput);
                            }
                        }
                    }
                }
                if (coincidence == 0) {
                    // Check if the object was added in the
                    // previous iteration.
                    if (!auxList.contains(auxVersionable2)) {
                        CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
                                new String[] { "",
                                        ActivityVersionUtil
                                                .generateFormattedOutput(auxVersionable2.getOutput()) },
                                fields[i], new Object[] { null, auxObject2 }, false, false);
                        outputCollection.add(auxOutput);
                    }
                }
            }
        } else {
            output.setStringOutput(new String[] { auxResult1 != null ? auxResult1.toString() : "",
                    auxResult2 != null ? auxResult2.toString() : "" });
            output.setDescriptionOutput(auxAnnotation.fieldTitle());
            outputCollection.add(output);
        }
    }

    private static boolean isOfSimpleType(Class auxReturnType) {
        return auxReturnType.getName().equals("java.util.Date") || auxReturnType.getName().equals("java.sql.Date")
                || auxReturnType.getName().equals("java.lang.String")
                || auxReturnType.getName().equals("java.lang.Double")
                || auxReturnType.getName().equals("java.lang.Integer")
                || auxReturnType.getName().equals("java.lang.Long")
                || auxReturnType.getName().equals("java.lang.Short")
                || auxReturnType.getName().equals("java.lang.Float")
                || auxReturnType.getName().equals("java.lang.Boolean")
                || auxReturnType.getName().equals("java.math.BigDecimal");
    }

    private static void processVersionableTextEditor(AmpActivityVersion ampActivityOne,
                                                     AmpActivityVersion ampActivityTwo,
                                                     List<CompareOutput> outputCollection, Field field,
                                                     CompareOutput output) throws Exception {
        // Obtain "get" method from field.
        Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(field.getName(),
                AmpActivityVersion.class, "get");
        // Compare values from 2 versions.
        String auxResult1 = (String) auxMethod.invoke(ampActivityOne, null);
        String auxResult2 = (String) auxMethod.invoke(ampActivityTwo, null);
        // Obtain annotation object.
        VersionableFieldTextEditor auxAnnotation = field.getAnnotation(VersionableFieldTextEditor.class);
        // Compare values, if both are null then they are considered
        // equal.
        if (!(auxResult1 == null && auxResult2 == null)) {
            if ((auxResult1 != null && auxResult2 == null) || (auxResult1 == null && auxResult2 != null)
                    || (!auxResult1.equals(auxResult2))) {
                output.setFieldOutput(field);
                output.setBlockSingleChangeOutput(false);
                output.setMandatoryForSingleChangeOutput(false);
                output.setDescriptionOutput(auxAnnotation.fieldTitle());
                Site site = TLSUtils.getSite();
                String lang = TLSUtils.getEffectiveLangCode();
                String auxBody1 = DbUtil.getEditorBody(site, auxResult1, lang);
                String auxBody2 = DbUtil.getEditorBody(site, auxResult2, lang);
                auxBody1 = auxBody1 != null ? auxBody1 : "";
                auxBody2 = auxBody2 != null ? auxBody2 : "";
                if (!auxBody1.trim().equals(auxBody2.trim())) {
                    output.setStringOutput(new String[] { auxBody1, auxBody2 });
                    output.setOriginalValueOutput(new String[] { auxResult1, auxResult2 });
                    outputCollection.add(output);
                }
            }
        }
    }

    private static void processVersionableSimple(AmpActivityVersion ampActivityOne,
                                                 AmpActivityVersion ampActivityTwo,
                                                 List<CompareOutput> outputCollection, ActivityHistory auditHistory1,
                                                 ActivityHistory auditHistory2, Field field, CompareOutput output)
            throws Exception {
        // Obtain "get" method from field.
        Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(field.getName(),
                AmpActivityVersion.class, "get");
        // Compare values from 2 versions.
        Object auxResult1 = auxMethod.invoke(ampActivityOne, null);
        Object auxResult2 = auxMethod.invoke(ampActivityTwo, null);
        // Obtain annotation object.
        VersionableFieldSimple auxAnnotation = field.getAnnotation(VersionableFieldSimple.class);
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
        if (StringUtils.equals(field.getName(), "modifiedBy")) {
            auxResult1 = ActivityUtil.getModifiedByUserName(ampActivityOne, auditHistory1);
            auxResult2 = ActivityUtil.getModifiedByUserName(ampActivityTwo, auditHistory2);
        } else if (StringUtils.equals(field.getName(), "updatedDate")) {
            auxResult1 = ActivityUtil.getModifiedByDate(ampActivityOne, auditHistory1);
            auxResult2 = ActivityUtil.getModifiedByDate(ampActivityTwo, auditHistory2);
        }
        // Compare values, if both are null then they are considered equal.
        if (!(auxResult1 == null && auxResult2 == null)) {
            if ((auxResult1 != null && auxResult2 == null) || (auxResult1 == null && auxResult2 != null)
                    || (!auxResult1.equals(auxResult2))) {
                logger.warn(field.getName() + ": " + auxResult1 + "-" + auxResult2);
                output.setDescriptionOutput(auxAnnotation.fieldTitle());
                output.setFieldOutput(field);
                // Identity "read-value" and "mandatory" fields.
                // TODO: If needed do the same for
                // @VersionableFieldTextEditor and
                // @VersionableCollection.
                output.setBlockSingleChangeOutput(auxAnnotation.blockSingleChange());
                output.setMandatoryForSingleChangeOutput(auxAnnotation.mandatoryForSingleChange());
                // Differentiate Wrappers from Classes that implements Versionable.
                Class auxReturnType = auxMethod.getReturnType();
                if (isOfSimpleType(auxReturnType)) {
                    String aux1String = ActivityVersionUtil.getStringOrEmpty(auxResult1);
                    String aux2String = ActivityVersionUtil.getStringOrEmpty(auxResult2);
                    output.setStringOutput(new String[] { aux1String, aux2String });
                    output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
                } else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
                    Versionable auxVersionable1 = Versionable.class
                            .cast(auxMethod.invoke(ampActivityOne, null));
                    Versionable auxVersionable2 = Versionable.class
                            .cast(auxMethod.invoke(ampActivityTwo, null));
                    String output1 = (auxVersionable1 != null)
                            ? ActivityVersionUtil.generateFormattedOutput(auxVersionable1.getOutput())
                            : null;
                    String output2 = (auxVersionable2 != null)
                            ? ActivityVersionUtil.generateFormattedOutput(auxVersionable2.getOutput())
                            : null;
                    output.setStringOutput(new String[] { output1, output2 });
                    output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
                } else {
                    if ("modifiedBy".equals(field.getName())) {
                        // this field cannot be used in merging
                        output.setBlockSingleChangeOutput(true);
                    }
                    output.setStringOutput(
                            new String[] { getStringOrEmpty(auxResult1), getStringOrEmpty(auxResult2) });
                    output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
                }
                outputCollection.add(output);
            }
        }
    }

    public static Map<Long, Map<String, List<CompareOutput>>> compareActivities(List<Long> activitiesId)
            throws Exception {

        Map<Long, Map<String, List<CompareOutput>>> listOfActivities = new HashMap<>();

        for (Long activityId : activitiesId) {
            Map<String, List<CompareOutput>> activityComparedToPreviousVersion = compareActivities(activityId);
            if (activityComparedToPreviousVersion != null) {
                listOfActivities.put(activityId, activityComparedToPreviousVersion);
            }
        }

        return listOfActivities;
    }
     
    private static void addAsDifferentIfNnoPresent(List<CompareOutput> outputCollection, Field[] fields, int i,
            VersionableCollection auxAnnotation, Collection auxCollection2, Iterator iter1) {
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
            CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[]{
                    auxValue1, ""}, fields[i], new Object[]{auxObject1, null}, false, false);
            outputCollection.add(auxOutput);
        }
    }
}  
    
    private static ActivityHistory getAuditHistory(AmpActivityVersion activity) {
        ActivityHistory auditHistory = null;
        if (activity.getModifiedBy() == null
                || (activity.getUpdatedDate() == null && activity.getModifiedDate() == null)) {
            auditHistory = ActivityUtil.getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
        }
         return auditHistory;
    }
     private static String getStringOrEmpty(Object o) {
        if (o != null) {
            if (o instanceof Date || o instanceof java.sql.Date) {
                return FormatHelper.formatDate((Date) o);
            }
             return o.toString();
        }
         return "";
    }
     private static Map<String, List<CompareOutput>> groupOutputCollection(List<CompareOutput> outputCollection) {
        Map<String, List<CompareOutput>> retVal = new HashMap<String, List<CompareOutput>>();
        int idx = 0;
        for (CompareOutput obj : outputCollection) {
            if (!obj.getBlockSingleChangeOutput()) {
                obj.setIndex(idx);
                idx++;
            } else {
                obj.setIndex(-1); // skip from merge process
            }
            if (!retVal.containsKey(obj.getDescriptionOutput())) {
                retVal.put(obj.getDescriptionOutput(), new ArrayList<>());
            }
            retVal.get(obj.getDescriptionOutput()).add(obj);
         }
         return retVal;
    }
     
    
    
}
