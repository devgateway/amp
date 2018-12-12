package org.digijava.module.aim.annotations.activityversioning;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;

public class ActivityVersioningService {
	
	private static Logger logger = Logger.getLogger(ActivityVersioningService.class);


	public static Map<String, List<CompareOutput>> compareActivities(Long activityOneId, Long activityTwoId) throws Exception {
		// TODO Auto-generated method stub
		
		 Session session = PersistenceManager.getCurrentSession();
	        AmpActivityVersion ampActivityOne = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityOneId);
	        Hibernate.initialize(ampActivityOne);
	        ActivityVersionUtil.initializeActivity(ampActivityOne);
	        AmpActivityVersion ampActivityTwo = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityTwoId);
	        Hibernate.initialize(ampActivityTwo);
	        ActivityVersionUtil.initializeActivity(ampActivityTwo);
	        //This does not seem to be in use, check and remove
	        //vForm.setOldActivity(vForm.getActivityOne());
	         List<CompareOutput> outputCollection = new ArrayList<>();
	        ActivityHistory auditHistory1 = ActivityVersioningService.getAuditHistory(ampActivityOne);
	        ActivityHistory auditHistory2 = ActivityVersioningService.getAuditHistory(ampActivityTwo);
	         // Retrieve annotated for versioning fields.
	        Field[] fields = AmpActivityFields.class.getDeclaredFields();
	        for (int i = 0; i < fields.length; i++) {
	            //logger.info(fields[i]);
	            CompareOutput output = new CompareOutput();
	             if (fields[i].isAnnotationPresent(VersionableFieldSimple.class)) {
	                // Obtain "get" method from field.
	                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class, "get");
	                 // Compare values from 2 versions.
	                Object auxResult1 = auxMethod.invoke(ampActivityOne, null);
	                Object auxResult2 = auxMethod.invoke(ampActivityTwo, null);
	                 // Obtain annotation object.
	                VersionableFieldSimple auxAnnotation = fields[i].getAnnotation(VersionableFieldSimple.class);
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
	                    auxResult1 = ActivityUtil.getModifiedByUserName(ampActivityOne, auditHistory1);
	                    auxResult2 = ActivityUtil.getModifiedByUserName(ampActivityTwo, auditHistory2);
	                } else if (StringUtils.equals(fields[i].getName(), "updatedDate")) {
	                    auxResult1 = ActivityUtil.getModifiedByDate(ampActivityOne, auditHistory1);
	                    auxResult2 = ActivityUtil.getModifiedByDate(ampActivityTwo, auditHistory2);
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
	                             String aux1String = ActivityVersioningService.getStringOrEmpty(auxResult1);
	                            String aux2String = ActivityVersioningService.getStringOrEmpty(auxResult2);
	                             output.setStringOutput(new String[]{aux1String, aux2String});
	                            output.setOriginalValueOutput(new Object[]{auxResult1, auxResult2});
	                        } else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
	                            Versionable auxVersionable1 = Versionable.class.cast(auxMethod.invoke(ampActivityOne,
	                                    null));
	                            Versionable auxVersionable2 = Versionable.class.cast(auxMethod.invoke(ampActivityTwo,
	                                    null));
	                             String output1 = (auxVersionable1 != null) ? ActivityVersionUtil.generateFormattedOutput(
	                                    auxVersionable1.getOutput()) : null;
	                            String output2 = (auxVersionable2 != null) ?
	                                    ActivityVersionUtil.generateFormattedOutput(auxVersionable2.getOutput()) : null;
	                            output.setStringOutput(new String[]{output1, output2});
	                            output.setOriginalValueOutput(new Object[]{auxResult1, auxResult2});
	                        } else {
	                            if ("modifiedBy".equals(fields[i].getName())) {
	                                // this field cannot be used in merging
	                                output.setBlockSingleChangeOutput(true);
	                            }
	                             output.setStringOutput(new String[]{getStringOrEmpty(auxResult1), getStringOrEmpty(auxResult2)});
	                            output.setOriginalValueOutput(new Object[]{auxResult1, auxResult2});
	                        }
	                         outputCollection.add(output);
	                    }
	                }
	            }
	            if (fields[i].isAnnotationPresent(VersionableFieldTextEditor.class)) {
	                // Obtain "get" method from field.
	                Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
	                        "get");
	                 // Compare values from 2 versions.
	                String auxResult1 = (String) auxMethod.invoke(ampActivityOne, null);
	                String auxResult2 = (String) auxMethod.invoke(ampActivityTwo, null);
	                 // Obtain annotation object.
	                VersionableFieldTextEditor auxAnnotation = fields[i].getAnnotation(VersionableFieldTextEditor.class);
	                 // Compare values, if both are null then they are considered
	                // equal.
	                if (!(auxResult1 == null && auxResult2 == null)) {
	                    if ((auxResult1 != null && auxResult2 == null) || (auxResult1 == null && auxResult2 != null)
	                            || (!auxResult1.equals(auxResult2))) {
	                        output.setFieldOutput(fields[i]);
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
	                            output.setStringOutput(new String[]{auxBody1, auxBody2});
	                            output.setOriginalValueOutput(new String[]{auxResult1, auxResult2});
	                            outputCollection.add(output);
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
	                                     if (auxVersionable1.getClass().getName().
	                                            equals("org.digijava.module.aim.dbentity.AmpFunding") &&
	                                            auxVersionable2.getClass().getName().
	                                                    equals("org.digijava.module.aim.dbentity.AmpFunding")
	                                    ) {
	                                        auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
	                                                new String[]{
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable1.getOutput(),
	                                                                auxVersionable2.getOutput()),
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable2.getOutput(),
	                                                                auxVersionable1.getOutput())}, fields[i], new Object[]{
	                                                auxObject1, auxObject2}, false, false);
	                                     } else {
	                                        auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
	                                                new String[]{
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable1.getOutput()),
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable2.getOutput())}, fields[i], new Object[]{
	                                                auxObject1, auxObject2}, false, false);
	                                    }
	                                    outputCollection.add(auxOutput);
	                                    auxList.add(auxVersionable1);
	                                    auxList.add(auxVersionable2);
	                                }
	                            }
	                        }
	                        if (coincidence == 0) {
	                            CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[]{
	                                    ActivityVersionUtil.generateFormattedOutput(auxVersionable1.getOutput()),
	                                    ""}, fields[i], new Object[]{auxObject1, null}, false, false);
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
	                                                new String[]{
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable1.getOutput()),
	                                                        ActivityVersionUtil.generateFormattedOutput(
	                                                                auxVersionable2.getOutput())}, fields[i],
	                                                new Object[]{auxObject1, auxObject2}, false, false);
	                                        outputCollection.add(auxOutput);
	                                    }
	                                }
	                            }
	                        }
	                        if (coincidence == 0) {
	                            // Check if the object was added in the
	                            // previous iteration.
	                            if (!auxList.contains(auxVersionable2)) {
	                                CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[]{
	                                        "",
	                                        ActivityVersionUtil.generateFormattedOutput(auxVersionable2
	                                                .getOutput())}, fields[i], new Object[]{null, auxObject2}, false,
	                                        false);
	                                outputCollection.add(auxOutput);
	                            }
	                        }
	                    }
	                } else {
	                    output.setStringOutput(new String[]{auxResult1 != null ? auxResult1.toString() : "",
	                            auxResult2 != null ? auxResult2.toString() : ""});
	                    output.setDescriptionOutput(auxAnnotation.fieldTitle());
	                    outputCollection.add(output);
	                }
	            }
	        }
	         return ActivityVersioningService.groupOutputCollection(outputCollection);
	    }
	     private static void addAsDifferentIfNnoPresent(List<CompareOutput> outputCollection, Field[] fields, int i, VersionableCollection auxAnnotation, Collection auxCollection2, Iterator iter1) {
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
	         if (activity.getModifiedBy() == null || (activity.getUpdatedDate() == null && activity.getModifiedDate() == null)) {
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
		