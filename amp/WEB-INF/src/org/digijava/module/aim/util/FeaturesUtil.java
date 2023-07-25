package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibilityAlphaOrderComparator;
import org.dgfoundation.amp.visibility.AmpTreeVisibilityAlphaTreeOrderComparator;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Flag;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.Logic;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;


/**
 * @author medea
 *
 */
public class FeaturesUtil {

    private static Logger logger = Logger.getLogger(FeaturesUtil.class);

    private static Map<String, AmpGlobalSettings> globalSettingsCache = null;

    public static String errorLog = "";
    
    public final static String AMP_TREE_VISIBILITY_ATTR = "ampTreeVisibility";
    
    public static void logGlobalSettingsCache() {
        String log = "";
        for (AmpGlobalSettings ampGlobalSetting:globalSettingsCache.values()) {
            log = log + ampGlobalSetting.getGlobalSettingsName() + ":" +
            ampGlobalSetting.getGlobalSettingsValue() + ";";
        }
        logger.info("GlobalSettingsCache is -> " + log);
    }

    public static synchronized Map<String, AmpGlobalSettings> getGlobalSettingsCache() {
        return globalSettingsCache;
    }
    
    public static synchronized void buildGlobalSettingsCache(List<AmpGlobalSettings> globalSettings) {
        globalSettingsCache = new HashMap<String, AmpGlobalSettings>();
        for (AmpGlobalSettings sett : globalSettings) {
            globalSettingsCache.put(sett.getGlobalSettingsName(), sett);
        }
    }

    public static boolean isDefault(Long templateId) {
        String s = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.FEATURE_TEMPLATE);
        return s != null && templateId == Long.parseLong(s);
    }

    public static List<String> getAssignedToTeams (Long templateId) {
        List<String> retVal = null;
        Session sess = PersistenceManager.getSession();
        StringBuilder qs = new StringBuilder("select t.name from ").
                append(AmpTeam.class.getName()).append(" t  where t.fmTemplate=:TEMPLATE_ID");
        Query q = sess.createQuery(qs.toString());
        q.setLong("TEMPLATE_ID", templateId);
        List<String> tmpVal = q.list();
        if (!tmpVal.isEmpty())
            retVal = tmpVal;

        return retVal;
    }

    public static void setUsedByTeamNames(Collection <AmpTemplatesVisibility> templates) {
        for (AmpTemplatesVisibility tv : templates) {
            tv.setUsedByTeamsNames(getAssignedToTeams(tv.getId()));
        }
    }   
    
    public static Double applyThousandsForVisibility(Double amount) 
    {
        if (amount == null)
            return null;
        return amount / AmountsUnits.getDefaultValue().divider;
    }

    public static Double applyThousandsForEntry(Double amount) 
    {
        if (amount == null)
            return null;
        return amount * AmountsUnits.getDefaultValue().divider;
    }
    
    public static Collection getAMPFeatures() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + AmpFeature.class.getName() + " f";
            qry = session.createQuery(qryStr);
            col = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPTemplates() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + FeatureTemplates.class.getName() + " f";
            qry = session.createQuery(qryStr);
            col = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    /**
     * Used to get the features which are currently active for AMP
     * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
     */
    public static Collection getActiveFeatures() {
        FeatureTemplates template = getTemplate(getGlobalSettingValue(
                GlobalSettingsConstants.FEATURE_TEMPLATE));
        return getTemplateFeatures(template.getTemplateId());

    }
    
    public static FeatureTemplates getActiveTemplate(){
        FeatureTemplates template = getTemplate(getGlobalSettingValue(GlobalSettingsConstants.FEATURE_TEMPLATE));
        return template;
    }

    public static AmpFeature toggleFeature(Integer featureId) {
        Session session = null;
        AmpFeature feature = null;
        
        session = PersistenceManager.getSession();
        feature = (AmpFeature) session.load(AmpFeature.class,featureId);
        feature.setActive(!feature.isActive());
        session.update(feature);
    
        return feature;
    }

    /**
     * @author dan
     */
    public static boolean existTemplate(String templateName) {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + FeatureTemplates.class.getName() + " f" +
            " where f.featureTemplateName = '" + templateName + "'";
            qry = session.createQuery(qryStr);
            col = qry.list();
            if (col == null)
                return false;
            if (col.size() == 0)
                return false;
            if (col.size() > 0)
                return true;
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static FeatureTemplates getTemplate(String templateId) {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + FeatureTemplates.class.getName() + " f" +
            " where f.templateId = '" + templateId + "'";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (!col.isEmpty()) {
            Iterator it = col.iterator();

            FeatureTemplates x = (FeatureTemplates) it.next();
            return x;
        }
        else
            return null;
    }

    /**
     * @author dan
     */
    public static boolean deleteTemplate(Long id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            FeatureTemplates ft = new FeatureTemplates();
            ft = (FeatureTemplates) session.load(FeatureTemplates.class, id);
            session.delete(ft);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static Collection getTemplateFeatures(Long id) {
        Session session = null;
        FeatureTemplates ft = new FeatureTemplates();
        try {
            session = PersistenceManager.getSession();
            ft = (FeatureTemplates) session.load(FeatureTemplates.class, id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ft.getFeatures();
    }

    /**
     * @author dan
     */
    public static String getTemplateName(Long id) {
        Session session = null;
        FeatureTemplates ft = new FeatureTemplates();
        try {
            session = PersistenceManager.getSession();
            ft = (FeatureTemplates) session.load(FeatureTemplates.class, id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ft.getFeatureTemplateName();
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void insertTemplateFeatures(Collection features,
            String template) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            FeatureTemplates ampTemplate = new FeatureTemplates();
            ampTemplate.setFeatureTemplateName(template);
            ampTemplate.setFeatures(new HashSet());
            for (Iterator it = features.iterator(); it.hasNext(); ) {
                AmpFeature ampFeature = (AmpFeature) it.next();
                ampTemplate.getFeatures().add(ampFeature);
            }
            session.save(ampTemplate);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void updateTemplateFeatures(Collection features,
            Long templateId,
            String templateName) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            FeatureTemplates ampTemplate = new FeatureTemplates();
            ampTemplate = (FeatureTemplates) session.load(FeatureTemplates.class,
                    templateId);
            ampTemplate.setFeatureTemplateName(templateName);
            ampTemplate.setFeatures(new HashSet());
            for (Iterator it = features.iterator(); it.hasNext(); ) {
                AmpFeature ampFeature = (AmpFeature) it.next();
                ampTemplate.getFeatures().add(ampFeature);
            }
            session.saveOrUpdate(ampTemplate);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    public static Collection getAllCountries() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select c.countryId,c.countryName from " + Country.class.getName() +
            " c order by c.countryName";
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                Object obj[] = (Object[]) itr.next();
                Long cId = (Long) obj[0];
                String cName = (String) obj[1];
                org.digijava.module.aim.helper.Country
                ctry = new org.digijava.module.aim.helper.Country();
                ctry.setId(cId);
                ctry.setName(cName);
                col.add(ctry);
            }

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }


    public static String getCurrentCountryName() {
        Session session = null;
        String qryStr = null;
        String countryName = null;
        Query qry = null;

        try {
            String defaultCountry = getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
            session = PersistenceManager.getSession();
            qryStr = "select c.countryId, c.countryName from " + Country.class.getName() +
            " c where c.iso = '"+defaultCountry+"'";
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                Object obj[] = (Object[]) itr.next();
                countryName = (String) obj[1];
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return countryName;
    }

    public static List<Country> getAllDgCountries() {
        String qryStr = "select c from " + Country.class.getName() + " c";
        return PersistenceManager.getSession().createQuery(qryStr).list();
    }

    public static void deleteThumbnail(int placeholder) {
        Session session = null;
        AmpHomeThumbnail thumbnail = getAmpHomeThumbnail(placeholder);
        try {
            session = PersistenceManager.getSession();
            session.delete(thumbnail);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public static AmpHomeThumbnail getAmpHomeThumbnail(int placeholder) {
        Session session = null;
        Query q = null;
        Collection c = null;
        AmpHomeThumbnail thumbnail = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select a from " + AmpHomeThumbnail.class.getName()
            + " a where (a.placeholder=:placeholder) ";
            q = session.createQuery(queryString);
            q.setParameter("placeholder", placeholder, IntegerType.INSTANCE);
            c = q.list();
            if(c.size()!=0)
                thumbnail=(AmpHomeThumbnail) c.iterator().next();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } 
        return thumbnail;
    }
    
    public static Collection getAllCountryFlags() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;
        String params = "";

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f.countryId,f.defaultFlag from " +
            AmpSiteFlag.class.getName() + " f";
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                Object obj[] = (Object[]) itr.next();
                Long cId = (Long) obj[0];
                Boolean defFlag = (Boolean) obj[1];
                Flag f = new Flag();
                if (params != null && params.trim().length() > 0) {
                    params += ",";
                }
                params += cId.longValue();

                f.setCntryId(cId);
                f.setDefaultFlag(defFlag.booleanValue());
                col.add(f);
            }

            if (params != null && params.trim().length() > 0) {
                qryStr = "select c.countryId,c.countryName from " +
                Country.class.getName() + " c" +
                " where c.countryId in (" + params + ")";

                qry = session.createQuery(qryStr);
                itr = qry.list().iterator();
                while (itr.hasNext()) {
                    Object obj[] = (Object[]) itr.next();
                    Long cId = (Long) obj[0];
                    String cName = (String) obj[1];
                    long temp = cId.longValue();

                    Iterator itr1 = col.iterator();
                    while (itr1.hasNext()) {
                        Flag f = (Flag) itr1.next();
                        if (f.getCntryId().longValue() == temp) {
                            f.setCntryName(cName);
                        }
                    }

                }
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return col;
    }

    public static AmpSiteFlag getAmpSiteFlag(Long id) {
        Session session = null;
        AmpSiteFlag flag = null;

        try {
            session = PersistenceManager.getSession();
            flag = (AmpSiteFlag) session.get(AmpSiteFlag.class, id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return flag;
    }

    public static byte[] getFlag(Long id) {
        Session session = null;
        byte flag[] = null;

        try {
            session = PersistenceManager.getSession();
            AmpSiteFlag tmp = (AmpSiteFlag) session.get(AmpSiteFlag.class, id);
            if (tmp != null) {
                flag = tmp.getFlag();
            }

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return flag;
    }

    public static byte[] getDefaultFlag() {
        Session session = null;
        byte flag[] = null;
        String qryStr = null;
        Query qry = null;

        try {
            qryStr = "select f from " + AmpSiteFlag.class.getName() + " f " +
            "where f.defaultFlag=true";
            session = PersistenceManager.getSession();
            qry = session.createQuery(qryStr);
            AmpSiteFlag sf = null;
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                sf = (AmpSiteFlag) itr.next();
            }
            if (sf != null) {
                flag = sf.getFlag();
            }

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return flag;
    }

    public static boolean defaultFlagExist() {
        Session session = null;
        boolean exist = false;
        String qryStr = null;
        Query qry = null;

        try {
            qryStr = "select count(*) from " + AmpSiteFlag.class.getName() + " f " +
            "where f.defaultFlag=true";
            session = PersistenceManager.getSession();
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            Integer num = null;
            if (itr.hasNext()) {
                num = (Integer) itr.next();
            }
            if (num.intValue() > 0) {
                exist = true;
            }

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return exist;
    }

    public static void setDefaultFlag(Long id) {
        Session session = null;
        String qryStr = null;
        Query qry = null;
        try {
            session = PersistenceManager.getSession();
            qryStr = "select s from " + AmpSiteFlag.class.getName() + " s " +
            "where s.defaultFlag=true";
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            AmpSiteFlag defFlag = null;
            if (itr.hasNext()) {
                defFlag = (AmpSiteFlag) itr.next();
            }
            AmpSiteFlag newDefFlag = (AmpSiteFlag) session.load(AmpSiteFlag.class, id);
            newDefFlag.setDefaultFlag(true);
            session.update(newDefFlag);
            if (defFlag != null) {
                defFlag.setDefaultFlag(false);
                session.update(defFlag);
            }
        }
        catch (Exception ex) {
            logger.error("Exception : " ,ex);
        }
    }

    public static AmpGlobalSettings getGlobalSetting(String globalSettingName) {
        
        AmpGlobalSettings ampGlobalSettings = null;
        List<AmpGlobalSettings> coll = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select a from " + AmpGlobalSettings.class.getName()
            + " a where (a.globalSettingsName=:globalSettingName) ";
            qry = session.createQuery(queryString);
            qry.setParameter("globalSettingName", globalSettingName, StringType.INSTANCE);
            coll = qry.list();
            if(coll.size()!=0)
                ampGlobalSettings = (AmpGlobalSettings) coll.iterator().next();
        }
        catch (Exception ex) {
            logger.error(ex, ex);
        }

        if (ampGlobalSettings == null)
            return null;
        return ampGlobalSettings;
    }
    

    public static void updateGlobalSetting(AmpGlobalSettings ampGlobalSettings) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.save(ampGlobalSettings);

            updateCachedValue(ampGlobalSettings);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    private static void updateCachedValue(AmpGlobalSettings ampGlobalSettings) {
        if (globalSettingsCache != null) {
            AmpGlobalSettings gs = globalSettingsCache.get(ampGlobalSettings.getGlobalSettingsName());
            if (gs != null) {
                gs.setGlobalSettingsValue(ampGlobalSettings.getGlobalSettingsValue());
            }
        }
    }
    
    public static String getGlobalSettingValue(String globalSettingName) {
        if (globalSettingsCache == null)
            buildGlobalSettingsCache(getGlobalSettings());
        Map<String, AmpGlobalSettings> settings = globalSettingsCache;
        AmpGlobalSettings value = settings.get(globalSettingName);
        if (value == null)
            return null;
        return value.getGlobalSettingsValue();
    }
    
    public static boolean getGlobalSettingValueBoolean(String globalSettingName) {
        String globalValue = getGlobalSettingValue(globalSettingName);
        return (globalValue != null && globalValue.equalsIgnoreCase("true"));
    }

    public static boolean isAmpOfflineEnabled() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.AMP_OFFLINE_ENABLED);
    }

    public static boolean showEditableExportFormats() {
        return TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER) != null
                || FeaturesUtil.isVisibleModule("Show Editable Export Formats");
    }

        /**
         *
         * @author dan
         * made for visibility module
         */
    public static Long getGlobalSettingValueLong(String globalSettingName) {
        String globalValue = getGlobalSettingValue(globalSettingName);
        return globalValue != null ? Long.parseLong(globalValue) : -1l;
    }
    
    public static Integer getGlobalSettingValueInteger(String globalSettingName) {
        String globalValue = getGlobalSettingValue(globalSettingName);
        return globalValue != null ? Integer.parseInt(globalValue) : -1;
    }

    public static Double getGlobalSettingDouble(String globalSettingName) {
        String globalValue = getGlobalSettingValue(globalSettingName);
        return globalValue != null ? Double.parseDouble(globalValue) : -1;
    }

    public static String[] getGlobalSettingsStringArray(String key) {
        String[] ret = null;
        ret = getGlobalSettingValue(key).split(";");
        return ret;
    }
    
    /*
     * edited by Govind G Dalwani
     */
    /*
     * to get all the Global settings
     */
    public static List<AmpGlobalSettings> getGlobalSettings() {
        List<AmpGlobalSettings> coll = null;
        Session session = null;
        String qryStr = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs ";
            qry = session.createQuery(qryStr);
            coll = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex, ex);
        }
        return coll;
    }

    /*
     * to get the country names
     */
    public static Collection getCountryNames() {
        Collection col = null;
        Session session = null;
        Query qry = null;
        String qryStr = null;
        try {
            session = PersistenceManager.getSession();
            qryStr = "select cn from " + Country.class.getName() +
            " cn order by cn.countryName";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    /*
     * to get the country ISO that is set as a default value...
     */
    public static Collection getDefaultCountryISO() {
        Collection col = null;
        Session session = null;
        Query qry = null;
        String qryStr = null;
        try {
            session = PersistenceManager.getSession();
            qryStr = "select gs from " + AmpGlobalSettings.class.getName() +
            " gs where gs.globalSettingsName = 'Default Country' ";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    /*
     * to get the country name from the Iso got
     */
    public static Collection<Country> getDefaultCountry(String ISO) {
        Collection<Country> col = null;
        Session session = null;
        Query qry = null;
        String qryStr = null;
        String a = "in the get country...";
        logger.info(a);
        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select cn from " + Country.class.getName() +
            " cn where cn.iso = '" + ISO + "'";
            qry = session.createQuery(qryStr);
            col = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return col;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPTemplatesVisibility() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpTemplatesVisibility.class.getName() +
            " f order by f.name asc";
            qry = session.createQuery(qryStr);
            col = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    public static Collection getAMPTemplatesVisibilityWithSession() {
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;
        Session hbsession=null;

        try {
            hbsession=PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpTemplatesVisibility.class.getName() +
            " f";
            qry = hbsession.createQuery(qryStr);
            col = qry.list();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

        }
        return col;
    }

    
    
    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPModulesVisibility() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpModulesVisibility.class.getName() +
            " f order by f.name asc";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return col;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPFeaturesVisibility() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpFeaturesVisibility.class.getName() +
            " f order by f.name asc";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return col;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection<AmpFieldsVisibility> getAMPFieldsVisibility() {
        Session session = null;
        Collection<AmpFieldsVisibility> col = new ArrayList<AmpFieldsVisibility>();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpFieldsVisibility.class.getName() +
            " f order by f.name asc";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return col;
    }
    
    /**
     * Returns the list of {@link AmpFieldsVisibility} with names in the specified list of fieldNames 
     * that are attached to the template with the given templateId 
     * @param fieldNames
     * @return a list of AmpFieldsVisibility
     */
    public static List<AmpFieldsVisibility> getAmpFieldsVisibility(Collection<String> fieldNames, Long templateId) {
        return getAmpObjectVisibility(AmpFieldsVisibility.class, fieldNames, templateId);
    }
    
    /**
     * Returns the list of {@link AmpFeaturesVisibility} with names in the specified list of featuresNames 
     * that are attached to the template with the given templateId
     * @param featuresNames
     * @param templateId
     * @return a list of AmpFeaturesVisibility
     */
    public static List<AmpFeaturesVisibility> getAmpFeaturesVisibility(Collection<String> featuresNames, Long templateId) {
        return getAmpObjectVisibility(AmpFeaturesVisibility.class, featuresNames, templateId);
    }
    
    /**
     * Returns the list of {@link AmpModulesVisibility} with names in the specified list of modulesNames 
     * that are attached to the template with the given templateId
     * @param modulesNames
     * @param templateId
     * @return a list of AmpModulesVisibility
     */
    public static List<AmpModulesVisibility> getAmpModulesVisibility(Collection<String> modulesNames, Long templateId) {
        return getAmpObjectVisibility(AmpModulesVisibility.class, modulesNames, templateId);
    }
    
    private static <T extends AmpObjectVisibility> List<T> getAmpObjectVisibility(Class<T> clazz, Collection<String> names, Long templateId) {
        String joinBy = "items";
        
        if (clazz.isAssignableFrom(AmpFieldsVisibility.class))
            joinBy = "fields";
        else if (clazz.isAssignableFrom(AmpFeaturesVisibility.class))
            joinBy = "features";
        
        if (names == null || names.isEmpty()) {
            return new ArrayList<>();
        }

        String qryStr = "select aov from " 
                + AmpTemplatesVisibility.class.getName() + " as templ"
                + " join templ." + joinBy + " aov"
                + " where templ.id=:templateId"
                + " and aov.name in (:names)"
                + " order by aov.name asc";
        Query qry = PersistenceManager.getSession().createQuery(qryStr);
        qry.setParameter("templateId", templateId);
        qry.setParameterList("names", names);
        return qry.list();
    }
    
    /**
     * Verifies if specified AmpObjectVisibility object is visible or not
     * @param object - AmpObjectVisibility to check
     * @return true if it is visible
     */
    public static <T extends AmpObjectVisibility> boolean isVisible(T object, Long templateId) {
        if (templateId == null) {
            templateId = FeaturesUtil.getCurrentTemplateId();
        }

        AmpTemplatesVisibility ampTreeVisibility = FeaturesUtil.getTemplateVisibility(templateId);
        return object.isVisibleTemplateObj(ampTreeVisibility);
    }

    /**
     * gets the currently-relevant FM template ID.
     * if running inside a team, returns the FM template relevant for the team. Else, returns the global FM
     * @return
     */
    public static long getCurrentTemplateId() {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm != null && tm.getVisibilityTemplateId() > 0) {
            return tm.getVisibilityTemplateId();
        }
        //if outside the workspace, then provide the default one
        return FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE);
    }
    
    /**
     * Detects current visibility template: used by the current workspace or default one if outside the workspace
     * @return AmpTemplatesVisibility
     */
    public static AmpTemplatesVisibility getCurrentTemplate() {
        long tId = getCurrentTemplateId();
        return getTemplateVisibility(tId);
    }

    /**
     * Returns the default AMP template
     * @return
     */
    public static AmpTemplatesVisibility getDefaultAmpTemplateVisibility() {
        // get the default amp template
        return getTemplateVisibility(getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE));
    }
    
    public static boolean existTemplateVisibility(String templateName,Long templateId) {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpTemplatesVisibility.class.getName() + " f where f.name =:tempName ";
            if(templateId!=null){
                qryStr+=" and f.id!="+templateId;
            }
            qry = session.createQuery(qryStr);
            qry.setParameter("tempName", templateName);
            col = qry.list();
            if (col == null)
                return false;
            if (col.size() == 0)
                return false;
            if (col.size() > 0)
                return true;
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return true;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPModules() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select f from " + AmpModulesVisibility.class.getName() +
            " f order by f.name asc";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return col;
    }
    
    /**
     * @return names of the root FM groups 
     */
    public static Set<String> getMainModulesNames() {
        return new HashSet<String>(PersistenceManager.getSession().createQuery(
            "select f.name from " + AmpModulesVisibility.class.getName() +
            " f where f.parent is null").list());
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void insertTemplate(String templateName, Session session) {
        Transaction tx = null;

        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
            ampTemplate.setName(templateName);
            session.save(ampTemplate);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return;
    }
    
    

    /**
     * @author dan
     */
    public static Collection getTemplateModules(Long id) {
        Session session = null;
        AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
        try {
            session = PersistenceManager.getRequestDBSession();
            ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
                    id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }    
        return ft.getItems();
    }

    /**
     * @author dan
     */
    public static String getTemplateNameVisibility(Long id) {
        return getTemplateVisibility(id).getName();
    }


    /**
     * @author dan
     */
    public static AmpTemplatesVisibility getTemplateNoSession(Long id) {
        Session session = null;
        AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
        try {
            session = PersistenceManager.getSession();
            ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
                    id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ft;
    }


    /**
     * @author dan
     * @param session
     * @throws HibernateException
     */
    public static AmpTemplatesVisibility getTemplateVisibility(Long id) {
        AmpTemplatesVisibility ft = PersistenceManager.getRequestDBSession().load(AmpTemplatesVisibility.class, id);
        TreeSet<AmpTemplatesVisibility> mySet = new TreeSet<AmpTemplatesVisibility>(FeaturesUtil.ALPHA_ORDER);
        mySet.addAll(PersistenceManager.getRequestDBSession().createQuery("from " + AmpModulesVisibility.class.getName()).list());
        ft.setAllItems(mySet);
        return ft;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void updateModulesTemplate(Collection modules, Long templateId,
            String templateName) {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpTemplatesVisibility ampTemplate;
            ampTemplate = (AmpTemplatesVisibility) session.load(
                    AmpTemplatesVisibility.class, templateId);
            ampTemplate.setItems(null);
            session.saveOrUpdate(ampTemplate);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpTemplatesVisibility ampTemplate;
            ampTemplate = (AmpTemplatesVisibility) session.load(
                    AmpTemplatesVisibility.class, templateId);
            ampTemplate.setName(templateName);
            for (Iterator it = modules.iterator(); it.hasNext(); ) {
                AmpModulesVisibility ampModule = (AmpModulesVisibility) it.next();
                ampTemplate.getItems().add(ampModule);
            }
            session.saveOrUpdate(ampTemplate);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.error("Exception ;;;; " + ex.getMessage());
        }   
        return;
    }

    /**
     * @author dan
     */
    public static boolean deleteTemplateVisibility(Long id) {
        Session hbsession=null;
        try {
            hbsession=PersistenceManager.getRequestDBSession();
            AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
            ft = (AmpTemplatesVisibility) hbsession.load(AmpTemplatesVisibility.class,id);

            for (AmpFieldsVisibility f:ft.getFields()) {
                f.getTemplates().remove(ft);
            }
            for (AmpFeaturesVisibility f:ft.getFeatures()) {
                f.getTemplates().remove(ft);
            }
            for (Iterator it = ft.getItems().iterator(); it.hasNext();) {
                AmpModulesVisibility f = (AmpModulesVisibility) it.next();
                f.getTemplates().remove(ft);
            }
            ft.clearFields();
            ft.getFeatures().clear();
            ft.getItems().clear();
            hbsession.delete(ft);
            
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static boolean deleteFieldVisibility(Long id, Session session) {

        try {
            AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(
                    AmpFieldsVisibility.class, id);
            AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
            parent.getItems().remove(field);
            Iterator i = field.getTemplates().iterator();
            while (i.hasNext()) {
                AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                element.removeField(field);
            }
            session.delete(field);

        }
        catch (HibernateException e) {
            // TODO check that the error is correctly handled
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static boolean deleteFeatureVisibility(Long id, Session session) {
        try {
            AmpFeaturesVisibility feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class, id);
            
            AmpObjectVisibility parent = feature.getParent();
            parent.getItems().remove(feature);

            Iterator<AmpObjectVisibility> j = feature.getItems().iterator();
            while (j.hasNext()) {
                AmpFieldsVisibility field = (AmpFieldsVisibility) j.next();
                for (AmpTemplatesVisibility element:feature.getTemplates()) {
                    element.removeField(field);
                }
                for (AmpTemplatesVisibility element:field.getTemplates()) {
                    element.removeField(field);
                }
            }
            for(AmpTemplatesVisibility element:feature.getTemplates()) {
                element.getFeatures().remove(feature);
            }
            session.delete(feature);
        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static boolean deleteModuleVisibility(Long id, Session session) {
        try {
            AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
            
            Iterator k = module.getItems().iterator();
            while (k.hasNext()) {
                AmpFeaturesVisibility feature = (AmpFeaturesVisibility) k.next();

                Iterator j = feature.getItems().iterator();
                while (j.hasNext()) {
                    AmpFieldsVisibility field = (AmpFieldsVisibility) j.next();
                    Iterator i = feature.getTemplates().iterator();
                    while (i.hasNext()) {
                        AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                        element.removeField(field);
                    }
                    i = field.getTemplates().iterator();
                    while (i.hasNext()) {
                        AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                        element.removeField(field);
                    }
                }
                Iterator i = feature.getTemplates().iterator();
                while (i.hasNext()) {
                    AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                    element.getFeatures().remove(feature);
                }
            }
            k = module.getItems().iterator();
            module.getItems().clear();
            k = module.getTemplates().iterator();
            while (k.hasNext()) {
                AmpTemplatesVisibility element = (AmpTemplatesVisibility) k.next();
                element.getItems().remove(module);
            }
            
            AmpModulesVisibility parentModule = ((AmpModulesVisibility)module.getParent());
            if (parentModule != null) {
                parentModule.getSubmodules().remove(module);
            }
            session.delete(module);
        }  catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * @author dan
     */
    public static AmpModulesVisibility getModuleVisibility(String moduleName) {

        Session session = null;
        Query q = null;
        Collection c = null;
        AmpModulesVisibility id = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select a from " + AmpModulesVisibility.class.getName()
            + " a where (a.name=:moduleName) ";
            q = session.createQuery(queryString);
            q.setParameter("moduleName", moduleName, StringType.INSTANCE);
            c = q.list();
            if(c.size()!=0)
                id=(AmpModulesVisibility) c.iterator().next();

        }
        catch (Exception ex) {
            logger.error("ERROR amp modules visibility" + moduleName , ex);
        }
        return id;
    }

    /**
     * @author dan
     */
    public static AmpFieldsVisibility getFieldVisibility(String fieldName) {

        Session session = null;
        Query q = null;
        Collection c = null;
        AmpFieldsVisibility id = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select a from " + AmpFieldsVisibility.class.getName()
            + " a where (a.name=:fieldName) ";
            q = session.createQuery(queryString);
            q.setParameter("fieldName", fieldName, StringType.INSTANCE);
            c = q.list();
            if(c.size()!=0)
                id=(AmpFieldsVisibility) c.iterator().next();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return id;
    }

    /**
     * @author dan
     */
    public static AmpFeaturesVisibility getFeatureVisibility(String featureName) {

        Session session = null;
        Query q = null;
        Collection c = null;
        AmpFeaturesVisibility id = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select a from " + AmpFeaturesVisibility.class.getName()
            + " a where (a.name=:featureName) ";
            q = session.createQuery(queryString);
            q.setParameter("featureName", featureName, StringType.INSTANCE);
            c = q.list();
            if(c.size()!=0)
                id=(AmpFeaturesVisibility) c.iterator().next();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return id;
    }




    /**
     * @author dan
     */
    public static Collection getModuleFeatures(Long id) {
        Session session = null;
        AmpModulesVisibility ft = new AmpModulesVisibility();
        try {
            session = PersistenceManager.getSession();
            ft = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ft.getItems();
    }

    /**
     * @author dan
     */
    public static String getModuleNameVisibility(Long id) {
        Session session = null;
        AmpModulesVisibility ft = new AmpModulesVisibility();
        String ret = null;//It might return null value if the module was removed but it still having a permission associated.
        try {
            session = PersistenceManager.getSession();
            ft = (AmpModulesVisibility) session.get(AmpModulesVisibility.class, id);
            if (ft!=null)
                ret = ft.getName();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ret;
    }

    /**
     * @author dan
     */
    public static Collection getFeaturesOfModules(Long id) {
        Session session = null;
        AmpModulesVisibility ft = new AmpModulesVisibility();
        try {
            session = PersistenceManager.getSession();
            ft = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return ft.getItems();
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void updateFeaturesModule(Collection modules, Long templateId,
            String templateName) {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpModulesVisibility ampModule;
            ampModule = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,
                    templateId);
            boolean found = false;
            for (Iterator it = ampModule.getItems().iterator(); it.hasNext(); ) {
                AmpFeaturesVisibility fDb = (AmpFeaturesVisibility) it.next();
                found = false;
                for (Iterator jt = modules.iterator(); jt.hasNext(); ) {
                    AmpFeaturesVisibility fRqst = (AmpFeaturesVisibility) jt.next();
                    if (fRqst.getId().compareTo(fDb.getId()) == 0) {
                        found = true;
                        break;
                    }
                }
            }
            session.saveOrUpdate(ampModule);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static void updateAmpTreeVisibility(Collection modules,
            Collection features,
            Collection fields, Long templateId) {
        Session session = null;
        AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
        try {
            session = PersistenceManager.getSession();
            ampTemplate = (AmpTemplatesVisibility) session.load(
                    AmpTemplatesVisibility.class, templateId);
            ampTemplate.getItems().retainAll(modules);
            ampTemplate.getItems().addAll(modules);
            ampTemplate.getFeatures().retainAll(features);
            ampTemplate.getFeatures().addAll(features);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    /**
     *
     * @author dan
     * @param session
     *
     * @return
     * @throws HibernateException
     */
    public static void updateAmpModulesTreeVisibility(Collection modules,
            Long templateId, Session session) throws HibernateException {
        AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
        ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
                templateId);
        
        ampTemplate.getOrCreateItems().retainAll(modules);
        ampTemplate.getOrCreateItems().addAll(modules);
        return;
    }

    /**
     *
     * @author dan
     * @param session
     *
     * @return
     * @throws HibernateException
     */
    public static void updateAmpTemplateNameTreeVisibility(String templateName,
            Long templateId, Session session) throws HibernateException {
        AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
        ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
                templateId);
        ampTemplate.setName(templateName);
        return;
    }

    /**
     *
     * @author dan
     *
     * @return
     * @throws HibernateException
     */
    public static void updateAmpFeaturesTreeVisibility(Collection<AmpFeaturesVisibility> features, Long templateId, Session session) throws HibernateException {
        AmpTemplatesVisibility ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, templateId);
        if (ampTemplate.getFeatures() != null) {
            ampTemplate.getFeatures().retainAll(features);
            ampTemplate.getFeatures().addAll(features);
        } else{
            ampTemplate.setFeatures(new TreeSet<AmpFeaturesVisibility>());
            ampTemplate.getFeatures().addAll(features);
        }
    }

    /**
     *
     * @author dan
     * @param session
     *
     * @return
     * @throws HibernateException
     */
    public static void updateAmpFieldsTreeVisibility(Collection fields,
            Long templateId, Session session) throws HibernateException {
        Transaction tx = null;
        AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
        ampTemplate = session.load(AmpTemplatesVisibility.class,
                templateId);
        if (ampTemplate.getFields()!=null){
            ampTemplate.getFields().retainAll(fields);
            ampTemplate.getFields().addAll(fields);
            ampTemplate.invalidateCache();
        }else{
            ampTemplate.setFields(new TreeSet<>());
            ampTemplate.getFields().addAll(fields);
            ampTemplate.invalidateCache();
        }
        return;
    }

    /**
     * @author dan
     * @param templateId - id of the template with which this field will be linked in case defaultVisible=on 
     * or (if defaultVisible==null and the GS "new fields visibility" is "on")
     * @param featureId
     * @param fieldName
     * @param hasLevel
     * @param defaultVisible overrides the setting "new fields visibility" in GS
     * @throws DgException
     */
    public static void insertFieldWithFeatureVisibility(Long templateId,
            Long featureId, String fieldName, String hasLevel, Boolean defaultVisible) throws DgException{
        Session session = null;
        AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
        AmpFieldsVisibility field = new AmpFieldsVisibility();
        AmpTemplatesVisibility template = null;
        try {
            session = PersistenceManager.getSession();
            if(session.isDirty()) {

            }
            feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class,featureId);
            field.setParent(feature);
            field.setName(fieldName);
            if(hasLevel!=null && "no".compareTo(hasLevel)==0)
                field.setHasLevel(false);
            else field.setHasLevel(false);
            session.save(field);
            boolean makeVisible = false;
            if ( defaultVisible == null ) {
                String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
                if (gsValue != null && gsValue.equalsIgnoreCase("on")){
                    makeVisible     = true;
                }
            }
            else
                makeVisible     = defaultVisible;
            
            if  ( makeVisible ){
                template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, templateId);
                template.addField(field);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new DgException(ex);
        }
        return;
    }

    /**
     * @author dan
     */
    public static void insertFeatureWithModuleVisibility(Long templateId, Long moduleId, String featureName, String hasLevel) {
        Session session = null;
        AmpModulesVisibility module = new AmpModulesVisibility();
        AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
        AmpTemplatesVisibility template = null;
        try {
            session = PersistenceManager.getSession();
            module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,moduleId);
            feature.setParent(module);
            feature.setName(featureName);
            if(hasLevel!=null && "no".compareTo(hasLevel)==0)
                feature.setHasLevel(false);
            else feature.setHasLevel(true);
                        
            module.getOrCreateItems().add(feature);
            
            session.update(module);
            session.save(feature);
            
            String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
            if (gsValue != null && gsValue.equalsIgnoreCase("on")){
                template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,templateId);
                template.getFeatures().add(feature);
                session.update(template);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return;
    }

    /**
     * @author dan
     */
    public static void insertModuleVisibility(Long templateId, String moduleName, String hasLevel) {
        Session session = null;
        AmpModulesVisibility module = new AmpModulesVisibility();
        AmpTemplatesVisibility template = null;
        try {
            session = PersistenceManager.getSession();
            module.setName(moduleName);
            if(hasLevel!=null && "no".compareTo(hasLevel)==0)
                module.setHasLevel(false);
            else module.setHasLevel(true);
            session.save(module);
            String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
            if (gsValue != null && gsValue.equalsIgnoreCase("on")){
                template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,templateId);
                template.getItems().add(module);
                session.update(template);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    public static void insertModuleVisibility(Long templateId, Long parentId, String moduleName, String hasLevel) {
        Session session = null;
        AmpModulesVisibility module = new AmpModulesVisibility();
        AmpTemplatesVisibility template = null;
        try {
            session = PersistenceManager.getSession();
            AmpModulesVisibility parent = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, parentId);
            
            module.setName(moduleName);
            module.setParent(parent);
            if(hasLevel!=null && "no".compareTo(hasLevel)==0)
                module.setHasLevel(false);
            else module.setHasLevel(true);
            
            if(parent.getSubmodules()==null) parent.setSubmodules(new HashSet());
            parent.getSubmodules().add(module);
            
            session.update(parent);
            session.save(module);
            
            String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
            if (gsValue != null && gsValue.equalsIgnoreCase("on")){
                template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
                        templateId);
                template.getItems().add(module);
                session.update(template);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }

    /**
     * @author dan
     */
    public static void updateModuleVisibility(Long id, String moduleParentName) {
        Session session = null;
        AmpModulesVisibility module ;//= new AmpModulesVisibility();
        AmpModulesVisibility moduleParent;
        try {
            session = PersistenceManager.getSession();
            module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,id);
            moduleParent = getModuleVisibility(moduleParentName);
            module.setParent(moduleParent);
            session.save(module);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return;
    }


    /**
     * @author dan
     */
    public static void updateFieldWithFeatureVisibility(Long featureId,
            String fieldName) {
        Session session = null;
        Query qry;      
        String qryStr;
        try {
            session = PersistenceManager.getSession();
            AmpFeaturesVisibility feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class, featureId);
            qryStr = "select f from " + AmpFieldsVisibility.class.getName() + " f  where f.name = :fieldName"; ;
            qry = session.createQuery(qryStr);
            qry.setString("fieldName", fieldName);
            AmpFieldsVisibility field = (AmpFieldsVisibility) qry.uniqueResult();
            if (field != null){             
                feature.getOrCreateItems().add(field);
                field.setParent(feature);
                session.saveOrUpdate(field);
            }
        }
        catch (Exception ex) {
            logger.error(ex, ex);
        }
        return;
    }

    /**
     * @author dan
     */
    public static void updateFeatureWithModuleVisibility(Long moduleId,
            String featureName) {
        Session session = null;
        Query qry;
        String qryStr;
        try {

            session = PersistenceManager.getSession();
            AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,moduleId);
            qryStr = "select f from " + AmpFeaturesVisibility.class.getName() + " f  where f.name = :featureName";
            qry = session.createQuery(qryStr).setString("featureName", featureName);
            AmpFeaturesVisibility feature = (AmpFeaturesVisibility) qry.uniqueResult();
            if (feature != null){               
                module.getOrCreateItems().add(feature);
                feature.setParent(module);
                session.saveOrUpdate(feature);
            }
        }
        catch (Exception ex) {
            logger.error(ex, ex);
        }
        return;
    }

    /**
     * @author dan
     */
    public static AmpTemplatesVisibility getTemplateById(Long id) {
        Session session = null;
        AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
        try {
            session = PersistenceManager.getSession();
            ft = (AmpTemplatesVisibility) session.get(AmpTemplatesVisibility.class,
                    id);
            List list = session.createQuery("from " +
                    AmpModulesVisibility.class.getName() ). list();

            TreeSet<AmpModulesVisibility> mySet=new TreeSet<AmpModulesVisibility>(FeaturesUtil.ALPHA_ORDER);
            mySet.addAll(list);
            ft.setAllItems(mySet);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        
        return ft;
    }

    /**
     * @author dan
     */
    public static List getAllFieldsId() {
        Session session = null;
        ArrayList <Long> result=new ArrayList();

        try {
            session = PersistenceManager.getSession();
            List list = session.createQuery("from " + AmpFieldsVisibility.class.getName() ). list();
            for(Iterator it=list.iterator();it.hasNext();)
            {
                AmpFieldsVisibility f=(AmpFieldsVisibility) it.next();
                result.add(f.getId());
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            // TODO Check correct exception handling

        }

        return result;
    }

    /**
     * @author dan
     */
    public static List getAllModulesId() {
        Session session = null;
        ArrayList <Long> result=new ArrayList();

        try {
            session = PersistenceManager.getSession();
            List list = session.createQuery("from " + AmpModulesVisibility.class.getName() ). list();
            for(Iterator it=list.iterator();it.hasNext();)
            {
                AmpModulesVisibility f=(AmpModulesVisibility) it.next();
                result.add(f.getId());
            }
        } catch (HibernateException e) {
            // TODO Check for correct error handling
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @author dan
     */
    public static List getAllFeaturesId() {
        Session session = null;
        ArrayList <Long> result=new ArrayList();

        try {
            session = PersistenceManager.getSession();
            List list = session.createQuery("from " + AmpFeaturesVisibility.class.getName() ). list();
            for(Iterator it=list.iterator();it.hasNext();)
            {
                AmpFeaturesVisibility f=(AmpFeaturesVisibility) it.next();
                result.add(f.getId());
            }
        } catch (HibernateException e) {
            // TODO Check for correct error handling
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @author dan
     * 
     */

    public static void cleanUpFM(ArrayList allFieldsId) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            for (Iterator it = allFieldsId.iterator(); it.hasNext();) {
                Long idf = (Long) it.next();
                AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(AmpFieldsVisibility.class, idf);
                AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
                parent.getItems().remove(field);
                Iterator i = field.getTemplates().iterator();
                while (i.hasNext()) {
                    AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                    element.removeField(field);
                }
                session.delete(field);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * @author dan
     * setting the parent to null
     */
    public static void deleteOneModule(Long id) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
            AmpModulesVisibility parent = (AmpModulesVisibility) module.getParent();
            Iterator i = module.getTemplates().iterator();
            i = module.getTemplates().iterator();
            while (i.hasNext()) {
                AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                if(element.getAllItems()!=null)  element.getAllItems().remove(module);
                if(element.getItems()!=null) element.getItems().remove(module);
            }
            if(module.getParent()!=null)
            {
                parent.getSubmodules().remove(module);
                session.delete(module);
            }
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * @author dan
     * delete one module from db
     */
    public static void deleteModule(Long id) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
            session.delete(module);

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }




    /**
     * @author dan
     * 
     */
    public static void deleteOneFeature(Long id) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();

            AmpFeaturesVisibility feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class, id);
            AmpObjectVisibility parent = (AmpObjectVisibility) feature.getParent();
            parent.getItems().remove(feature);
            Iterator i = feature.getTemplates().iterator();
            i = feature.getTemplates().iterator();
            while (i.hasNext()) {
                AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                element.getFeatures().remove(feature);
            }
            session.delete(feature);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * @author dan
     */

    public static void deleteOneField(Long id) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(AmpFieldsVisibility.class, id);
            AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
            parent.getItems().remove(field);
            Iterator i = field.getTemplates().iterator();
            while (i.hasNext()) {
                AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
                element.removeField(field);
            }
            session.delete(field);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }


    /**
     * @author dan
     */
    public static AmpIndicatorRiskRatings getFilter(String ratingName) {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;
        AmpIndicatorRiskRatings airr = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + AmpIndicatorRiskRatings.class.getName() +
            " f" +
            " where f.ratingName = '" + ratingName + "'";
            qry = session.createQuery(qryStr);
            col = qry.list();
            airr = (AmpIndicatorRiskRatings) col.iterator().next();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return airr;
    }

    /**
     *
     * @author dan
     *
     * @return
     */
    public static Collection getAMPColumnsOrder() {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select f from " + AmpColumnsOrder.class.getName() +
            " f order by f.indexOrder asc";
            qry = session.createQuery(qryStr);
            col = qry.list();

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return col;
    }

    public static String getDefaultCountryIso()
    {
        String qryStr = "select gs.globalSettingsValue from " + AmpGlobalSettings.class.getName() +
                    " gs where gs.globalSettingsName = 'Default Country' ";
        Query qry = PersistenceManager.getSession().createQuery(qryStr); 
        String defaultCountryIso = (String) qry.uniqueResult();
        return defaultCountryIso;
    }

    public static String makeProperString(String theString) throws java.io.IOException{
        if (theString.startsWith("/"))
            theString = theString.substring(theString.lastIndexOf("/") + 1, theString.length());

        java.io.StringReader in = new java.io.StringReader(theString.toLowerCase());
        boolean precededBySpace = true;
        StringBuffer properCase = new StringBuffer();    
        while(true) {      
            int i = in.read();
            if (i == -1)  break;      
            char c = (char)i;
            if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/' || c == '\\' || c == ',') {
                properCase.append(c);
                precededBySpace = true;
            } else {
                if (precededBySpace) { 
                    properCase.append(Character.toUpperCase(c));
                } else { 
                    properCase.append(c); 
                }
                precededBySpace = false;
            }
        }
        return properCase.toString();    

    }
    
    public static final Comparator ALPHA_ORDER = new AmpTreeVisibilityAlphaOrderComparator();
    public static final Comparator ALPHA_AMP_TREE_ORDER = new AmpTreeVisibilityAlphaTreeOrderComparator();

    public static Boolean isShowComponentFundingByYear() {
        String componentFundingByYearStr = FeaturesUtil
        .getGlobalSettingValue(Constants.GLOBAL_SHOW_COMPONENT_FUNDING_BY_YEAR);
        if (componentFundingByYearStr != null && "On".equals(componentFundingByYearStr))
            return true;
        return false;
    }

    public static void switchLogicInstance() {
        String countryISO = null;
        Collection col = FeaturesUtil.getDefaultCountryISO();
        Iterator itr = col.iterator();
        while (itr.hasNext()) {
            AmpGlobalSettings ampG = (AmpGlobalSettings) itr.next();
            countryISO = ampG.getGlobalSettingsValue();
        }
        if (countryISO != null) {
            if (countryISO.equalsIgnoreCase("BO")) {
                Logic.switchLogic(Logic.BOLIVIA_FACTORY);
            } else {
                Logic.switchLogic(Logic.DEFAULT_FACTORY);
            }
        } else {
            Logic.switchLogic(Logic.DEFAULT_FACTORY);
        }
    }

    /**
     * return feature if it is visible or NULL
     * @param featureName
     * @param moduleName 
     * @param defTemplId
     * @return feature
     * 
     */
    public static AmpFeaturesVisibility getFeatureByName(String featureName, String moduleName,Long defTemplId){
        AmpFeaturesVisibility feature=null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select fv from " +AmpModulesVisibility.class.getName() +
            " mv inner join mv.items fv "+
            " inner join fv.templates tmpl" +
            " where (mv.name=:moduleName) and (fv.name=:featureName)" +
            " and (tmpl.id=:defTemplId)";
            Query qry = session.createQuery(queryString);
            qry.setString("featureName", featureName);
            qry.setString("moduleName", moduleName);
            qry.setLong("defTemplId", defTemplId);
            if (qry.list() != null && qry.list().size() > 0) {
                feature = (AmpFeaturesVisibility) qry.uniqueResult();
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return feature;

    }   
    /**
     * return module if it is visible or NULL
     * @param moduleName
     * @param parentModuleName
     * @param defTemplId
     * @return module  
     * 
     */


    public static AmpModulesVisibility getModuleByName(String moduleName, String parentModuleName,Long defTemplId){
        AmpModulesVisibility module=null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select mv from " + AmpModulesVisibility.class.getName()+ " mv ";
            if (parentModuleName != null) {
                queryString += " inner join mv.parent parent ";
            }
            queryString += " inner join mv.templates tmpl " +
            " where (mv.name=:moduleName) " +
            " and (tmpl.id=:defTemplId)";
            if (parentModuleName != null) {
                queryString += " and (parent.name=:parentModuleName) ";
            }
            Query qry = session.createQuery(queryString);
            qry.setString("moduleName", moduleName);
            if (parentModuleName != null) {
                qry.setString("parentModuleName", parentModuleName);
            }
            qry.setLong("defTemplId", defTemplId);
            if (qry.list() != null && qry.list().size() > 0) {
                module = (AmpModulesVisibility) qry.uniqueResult();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return module;

    }
    
    /**
     * updates descriptions for AmpObjectVisibility descendants
     * @param clazz
     * @param id
     * @param description
     * @throws DgException
     */
    public static void upadateObjectVisibility(Class<? extends AmpObjectVisibility> clazz, Long id,String description) throws DgException{
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpObjectVisibility objectVisibility =(AmpObjectVisibility) session.load(clazz, id);
            objectVisibility.setDescription(description);
            session.update(objectVisibility);
        }
        catch (Exception e) {
            throw new DgException("Cannot update Object Visibility!",e);
        }
    }

    public static AmpComponentType getDefaultComponentType() {
        String defaultComponentTypeIdStr = getGlobalSettingValue(GlobalSettingsConstants.COMPONENT_TYPE);
        return ComponentsUtil.getComponentTypeById(Long.parseLong(defaultComponentTypeIdStr));
    }

    public static boolean isVisibleSectors(String type, ServletContext ampContext,HttpSession session){
        AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(ampContext, session);

        AmpFieldsVisibility fieldToTest=ampTreeVisibility.getFieldByNameFromRoot(type+" Sector");
        if(fieldToTest!=null)
            return fieldToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
        return false;
    }
    
    

    /**
     * for testcases, since the implementation is lame and does not allow one to supply a dummy FM instance
     */
    public static HashMap<String, Boolean> overriddenFields = new HashMap<>();

    public static boolean isVisibleField(String fieldName){
        if (overriddenFields.containsKey(fieldName))
            return overriddenFields.get(fieldName);
        
        ServletContext  ampContext= TLSUtils.getRequest().getSession().getServletContext();
        HttpSession session = TLSUtils.getRequest().getSession();
        AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(ampContext, session);
        AmpFieldsVisibility fieldToTest=ampTreeVisibility.getFieldByNameFromRoot(fieldName);
        if(fieldToTest!=null)
            return fieldToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
        
        return false;
    }

    public static boolean isVisibleFeature(String featureName) {
        return isVisibleFeature(null, featureName) ;
    }

    public static boolean isVisibleFeature(String moduleName, String featureName) {
        HttpSession session = TLSUtils.getRequest().getSession();
        AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(session.getServletContext(), session);
        AmpFeaturesVisibility featureToTest;
        if (moduleName != null) {
            featureToTest = ampTreeVisibility.getFeatureByNameFromModule(moduleName, featureName);
        } else {
            featureToTest = ampTreeVisibility.getFeatureByNameFromRoot(featureName);
        }

        if (featureToTest != null)
            return featureToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
        return false;
    }

    public static boolean isVisibleModule(String moduleName){
        AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(TLSUtils.getRequest().getServletContext(), TLSUtils.getRequest().getSession());
        AmpModulesVisibility moduleToTest = ampTreeVisibility.getModuleByNameFromRoot(moduleName);
        
        if (moduleToTest != null)
            return moduleToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
        
        return false;
    }
    
    /**
     * @return AmpTreeVisibility used by the current context
     */
    public static AmpTreeVisibility getCurrentAmpTreeVisibility() {
        return getAmpTreeVisibility(TLSUtils.getRequest().getServletContext(), TLSUtils.getRequest().getSession()); 
        
    }
    
    /**
     * gets the visibility tree relevant for this session. Does that by trying to get the tree of the session; if none exists, then falls back to the global one
     * @param ampContext
     * @param session
     * @return
     */
    public static AmpTreeVisibility getAmpTreeVisibility(ServletContext ampContext, HttpSession session) {
        if (session != null && session.getAttribute(AMP_TREE_VISIBILITY_ATTR) != null) {
            AmpTreeVisibility currentOnSession = (AmpTreeVisibility) session.getAttribute(AMP_TREE_VISIBILITY_ATTR);
            // if there is an object in session we should check if its valid, if
            // not we should go and fetch it again
            // from database
            ConcurrentHashMap<Long, Date> contextModificationDateMap = (ConcurrentHashMap<Long, Date>) ampContext.getAttribute("templateVisibilityChangeDate");;
            // we should save the time of last actualization
            Date sessionModificationDate = (Date) session.getAttribute("ampTreeVisibilityModificationDate");
            if (contextModificationDateMap.get(currentOnSession.getRoot().getId()).after(sessionModificationDate)) {
                AmpTemplatesVisibility currentTemplate = FeaturesUtil.getTemplateVisibility(currentOnSession.getRoot().getId());
                currentOnSession.buildAmpTreeVisibility(currentTemplate);
                session.setAttribute(AMP_TREE_VISIBILITY_ATTR, currentOnSession);
                // we store the new date where the visibility has been modified
                // for the current session
                session.setAttribute("ampTreeVisibilityModificationDate", new Date());
            }

            return currentOnSession;
        } else {
            return (AmpTreeVisibility) ampContext.getAttribute(AMP_TREE_VISIBILITY_ATTR);
        }
    }

    public static void setAmpTreeVisibility(ServletContext ampContext, HttpSession session, AmpTreeVisibility ampTreeVisibility) {
        Date modificationDate = new Date();
        ConcurrentHashMap<Long, Date> ampVisibilityModification = null;
        //we should save the time of last actualization 
        ampVisibilityModification = (ConcurrentHashMap<Long,Date>) ampContext.getAttribute("templateVisibilityChangeDate");
        if (ampVisibilityModification == null) {
            ampVisibilityModification = new ConcurrentHashMap<Long, Date>();
        }

        //we set the modification date for the corresponding template
        ampVisibilityModification.put(ampTreeVisibility.getRoot().getId(), modificationDate);
        ampContext.setAttribute("templateVisibilityChangeDate", ampVisibilityModification);
        
        //we only save on context the default template so we don't mess the administrative section
        if (FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE).equals(ampTreeVisibility.getRoot().getId())) {
            ampContext.setAttribute(AMP_TREE_VISIBILITY_ATTR, ampTreeVisibility);           
        }
        
        if (session != null) {
            session.setAttribute(AMP_TREE_VISIBILITY_ATTR, ampTreeVisibility);
            //after setting the session visibility tree we set the date for that session
            session.setAttribute("ampTreeVisibilityModificationDate", modificationDate);
        }

    }
}