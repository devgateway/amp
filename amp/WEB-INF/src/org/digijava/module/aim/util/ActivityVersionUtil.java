package org.digijava.module.aim.util;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.version.exception.CannotGetLastVersionForVersionException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

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

    public static String generateFormattedOutput(HttpServletRequest request, Output out) throws WorkerException {
        return generateFormattedOutput(request, out, null);
    }

    public static String generateFormattedOutput(HttpServletRequest request, Output out, Output out1) throws WorkerException {
        Site site = RequestUtils.getSite(request);
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();

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
                        ret.append(DbUtil.filter(text));
                        if (!existsInOtherVersion) ret.append("</font>");
                    }
                    // }
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
                            Object value = auxOutput2.getValue()[i];
                            if (value instanceof Timestamp) {
                                ret.append(DateConversion.convertDateToString(new Date(((Timestamp) value).getTime())));
                            } else if (value instanceof BigDecimal
                                    || value instanceof Double
                                    || value instanceof Float) {
                                NumberFormat formatter = FormatHelper.getDecimalFormat();
                                formatter.setMaximumFractionDigits(0);
                                ret.append(formatter.format(value));
                            } else {
                                String text = value != null ? value.toString() : "";
                                if (auxOutput2.getTranslateValue())
                                    text = TranslatorWorker.translateText(text, langCode, site.getId());                                                                 
                                ret.append(DbUtil.filter(text));                                                               
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
                aux = Integer.parseInt(gsValue);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
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
            query.setParameter("oldActivityId", oldActivity, LongType.INSTANCE);
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
     * @return
     * @throws CloneNotSupportedException
     */
    public static AmpActivityVersion cloneActivity(AmpActivityVersion in) throws CloneNotSupportedException {
        AmpActivityVersion out = SerializationUtils.clone(in);
        
        Class clazz = AmpActivityFields.class;
        
        Field[] fields = clazz.getDeclaredFields();//clazz.getFields();
        for (Field field : fields) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                logger.debug("Init set: " + field.getName());
                initSet(out, field);
            }
        }


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
}
