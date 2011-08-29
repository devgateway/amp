package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibilityAlphaOrderComparator;
import org.dgfoundation.amp.visibility.AmpTreeVisibilityAlphaTreeOrderComparator;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
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
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Flag;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.logic.Logic;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author medea
 *
 */
public class FeaturesUtil {

	private static Logger logger = Logger.getLogger(FeaturesUtil.class);

	private static Collection globalSettingsCache = null;

	private ServletContext ampContext = null;

	public static String errorLog="";
	
	public static void logGlobalSettingsCache() {
		String log = "";
		Iterator iter = globalSettingsCache.iterator();
		while (iter.hasNext()) {
			AmpGlobalSettings ampGlobalSetting = (AmpGlobalSettings) iter.next();
			log = log + ampGlobalSetting.getGlobalSettingsName() + ":" +
			ampGlobalSetting.getGlobalSettingsValue() + ";";
		}
		logger.info("GlobalSettingsCache is -> " + log);
	}

	public static synchronized Collection getGlobalSettingsCache() {
		return globalSettingsCache;
	}

	public static synchronized void setGlobalSettingsCache(Collection
			globalSettings) {
		globalSettingsCache = globalSettings;
	}

	public static boolean isDefault(Long templateId) {
		String s = FeaturesUtil.getGlobalSettingValue("Feature Template");
		if (s != null)
			if (templateId.compareTo(new Long(Long.parseLong(s))) == 0)
				return true;
		return false;
	}
	
	public static Double applyThousandsForVisibility(Double amount) {
		if(amount==null) return null;
		return amount*("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))?0.001:1);
	}

	public static Double applyThousandsForEntry(Double amount) {
		if(amount==null) return null;
		return amount*("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))?1000:1);
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}

	/**
	 * Used to get the features which are currently active for AMP
	 * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
	 */
	public static Collection getActiveFeatures() {
		FeatureTemplates template = getTemplate(getGlobalSettingValue(
		"Feature Template"));
		return getTemplateFeatures(template.getTemplateId());
		/*	Session session = null;
     Collection col = new ArrayList();
     String qryStr = null;
     Query qry = null;

     try {
      session = PersistenceManager.getSession();
      qryStr = "select f from " + AmpFeature.class.getName() + " f" +
        " where f.active = true";
      qry = session.createQuery(qryStr);
      col = qry.list();

     } catch (Exception ex) {
      logger.error("Exception : " + ex.getMessage());
     } finally {
      if (session != null) {
       try {
        PersistenceManager.releaseSession(session);
       } catch (Exception rsf) {
        logger.error("Release session failed :" + rsf.getMessage());
       }
      }
     }
     return col;
		 */

	}
	
	public static FeatureTemplates getActiveTemplate(){
		FeatureTemplates template = getTemplate(getGlobalSettingValue("Feature Template"));
		return template;
	}

	public static AmpFeature toggleFeature(Integer featureId) {
		Session session = null;
		Transaction tx = null;
		AmpFeature feature = null;

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			feature = (AmpFeature) session.load(AmpFeature.class,
					featureId);
			feature.setActive(!feature.isActive());
			session.update(feature);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Transaction rollback failed :" + rbf.getMessage());
				}
			}
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			FeatureTemplates ampTemplate = new FeatureTemplates();
			ampTemplate.setFeatureTemplateName(template);
			ampTemplate.setFeatures(new HashSet());
			for (Iterator it = features.iterator(); it.hasNext(); ) {
				AmpFeature ampFeature = (AmpFeature) it.next();
				ampTemplate.getFeatures().add(ampFeature);
			}
			session.save(ampTemplate);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			FeatureTemplates ampTemplate = new FeatureTemplates();
			ampTemplate = (FeatureTemplates) session.load(FeatureTemplates.class,
					templateId);
			ampTemplate.setFeatureTemplateName(templateName);
			////System.out.println(ampTemplate.getFeatureTemplateName());
			ampTemplate.setFeatures(new HashSet());
			//ampTemplate.getFeatures().addAll(features);
			for (Iterator it = features.iterator(); it.hasNext(); ) {
				AmpFeature ampFeature = (AmpFeature) it.next();
				//boolean found=false;
				//for(Iterator jt=ampTemplate.getFeatures().iterator();jt.hasNext();)
				//{
				//AmpFeature ampFeature2=(AmpFeature)jt.next();
				//if(ampFeature.getName().compareTo(ampFeature2.getName())==0) {found=true;break;}
				//}
				//if(!found)
				ampTemplate.getFeatures().add(ampFeature);
			}
			session.saveOrUpdate(ampTemplate);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}

		return col;
	}


	public static String getCurrentCountryName() {
		Session session = null;
		String qryStr = null;
		String countryName = null;
		Query qry = null;

		try {
			String defaultCountry = getGlobalSettingValue("Default Country");
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return countryName;
	}

	public static Collection<Country> getAllDgCountries() {
		Session session = null;
		Collection<Country> col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select c from " + Country.class.getName() +
			" c  "; //order by c.countryName asc";
			qry = session.createQuery(qryStr);
			col = qry.list();
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return col;
	}

	public static void deleteThumbnail(int placeholder) {
 	 	Session session = null;
 	 	Transaction tx = null;
 	 	AmpHomeThumbnail thumbnail = getAmpHomeThumbnail(placeholder);
 	 	try {
			session = PersistenceManager.getSession();
//beginTransaction();
			session.delete(thumbnail);
			//tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : " + e.getMessage());
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
 	 		q.setParameter("placeholder", placeholder, Hibernate.INTEGER);
 	 		c = q.list();
 	 		if(c.size()!=0)
 	 			thumbnail=(AmpHomeThumbnail) c.iterator().next();
 	 	} catch (Exception ex) {
 	 		logger.error("Exception : " + ex.getMessage());
 	 	} finally {
 	 		if (session != null) {
 	 			try {
 	 				PersistenceManager.releaseSession(session);
 	 			} catch (Exception rsf) {
 	 				logger.error("Release session failed :" + rsf.getMessage());
 	 			}
 	 		}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}

		return exist;
	}

	public static void setDefaultFlag(Long id) {
		Session session = null;
		Transaction tx = null;
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
//beginTransaction();
			newDefFlag.setDefaultFlag(true);
			session.update(newDefFlag);
			if (defFlag != null) {
				defFlag.setDefaultFlag(false);
				session.update(defFlag);
			}
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}

	public static String getGlobalSettingValue(String globalSettingName) {
		Collection settings = null;
		settings = getGlobalSettingsCache();
		if (settings == null) {
			settings = getGlobalSettings();
			setGlobalSettingsCache(settings);
		}

		Iterator i = settings.iterator();
		AmpGlobalSettings element = null;
		while (i.hasNext()) {
			element = (AmpGlobalSettings) i.next();
			// TODO would it be better if we add 'key' field in db for this?
			if (element.getGlobalSettingsName().equals(globalSettingName))
				return element.getGlobalSettingsValue();
		}
		return null;
	}
	
	public static boolean getGlobalSettingValueBoolean(String globalSettingName) {
		String globalValue = getGlobalSettingValue(globalSettingName);
		return (globalValue != null && globalValue.equalsIgnoreCase("true"));
	}

	/**
	 *
	 * @author dan
	 * made for visibility module
	 */
	public static Long getGlobalSettingValueLong(String globalSettingName) {
		String globalValue = getGlobalSettingValue(globalSettingName);
		if (globalValue != null)
			return new Long(Long.parseLong(globalValue));
		return new Long( -1);
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
	public static Collection getGlobalSettings() {
		Collection coll = null;
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();
			qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs ";
			qry = session.createQuery(qryStr);
			coll = qry.list();
			//tx.commit();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
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
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() +
			" cn order by cn.countryName";
			qry = session.createQuery(qryStr);
			col = qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select gs from " + AmpGlobalSettings.class.getName() +
			" gs where gs.globalSettingsName = 'Default Country' ";
			qry = session.createQuery(qryStr);
			col = qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;
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
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				}
				catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
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
			logger.error("Exception ::: " + ex.getMessage());
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
			logger.error("Exception ..... " + ex.getMessage());
			//ex.printStackTrace();
		}/*
		finally{
			try {
				PersistenceManager.releaseSession(hbsession);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
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
			logger.error("Exception : " + ex.getMessage());
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
			logger.error("Exception : " + ex.getMessage());
		}    
		return col;
	}

	/**
	 *
	 * @author dan
	 *
	 * @return
	 */
	public static Collection getAMPFieldsVisibility() {
		Session session = null;
		Collection col = new ArrayList();
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
			logger.error("Exception : " + ex.getMessage());
		}    
		return col;
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
			logger.error("Exception : " + ex.getMessage());
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
			logger.error("Exception : " + ex.getMessage());
		}    
		return col;
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
			logger.error("Exception : " + ex.getMessage());
		}    
		return;
	}
	
	public static Long insertreturnTemplate(String templateName, Session session) {
		Transaction tx = null;
		Long id =null;
		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
			ampTemplate.setName(templateName);
			id = (Long)session.save(ampTemplate);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}    
		return id;
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
			logger.error("Exception : " + ex.getMessage());
		}    
		return ft.getItems();
	}

	/**
	 * @author dan
	 */
	public static String getTemplateNameVisibility(Long id) {
		Session session = null;
		AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
		try {
			session = PersistenceManager.getRequestDBSession();
			ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
					id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}    
		return ft.getName();
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ft;
	}


	/**
	 * @author dan
	 * @param session
	 * @throws HibernateException
	 */
	public static AmpTemplatesVisibility getTemplateVisibility(Long id,	Session session) throws HibernateException {
		AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
		ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, id);
		List list = session.createQuery("from " + AmpModulesVisibility.class.getName()).list();
		TreeSet mySet=new TreeSet(FeaturesUtil.ALPHA_ORDER);
		mySet.addAll(list);
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
		Transaction tx = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTemplatesVisibility ampTemplate;
//beginTransaction();
			ampTemplate = (AmpTemplatesVisibility) session.load(
					AmpTemplatesVisibility.class, templateId);
			ampTemplate.setItems(null);
			session.saveOrUpdate(ampTemplate);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception :::: " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTemplatesVisibility ampTemplate;
//beginTransaction();
			ampTemplate = (AmpTemplatesVisibility) session.load(
					AmpTemplatesVisibility.class, templateId);
			ampTemplate.setName(templateName);
			//ampTemplate.setVisible("true");
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
		Transaction tx = null;
		Session hbsession=null;
		try {
			hbsession=PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpTemplatesVisibility ft = new AmpTemplatesVisibility();
			ft = (AmpTemplatesVisibility) hbsession.load(AmpTemplatesVisibility.class,id);
//			ft.setItems(null);
//			ft.setFeatures(null);
//			ft.setFields(null);
			for (Iterator it = ft.getFields().iterator(); it.hasNext();) {
				AmpFieldsVisibility f = (AmpFieldsVisibility) it.next();
				f.getTemplates().remove(ft);
			}
			for (Iterator it = ft.getFeatures().iterator(); it.hasNext();) {
				AmpFeaturesVisibility f = (AmpFeaturesVisibility) it.next();
				f.getTemplates().remove(ft);
			}
			for (Iterator it = ft.getItems().iterator(); it.hasNext();) {
				AmpModulesVisibility f = (AmpModulesVisibility) it.next();
				f.getTemplates().remove(ft);
			}
			ft.getFields().clear();
			ft.getFeatures().clear();
			ft.getItems().clear();
			
			hbsession.delete(ft);
			
			//tx.commit();
//session.flush();
		}
		catch (Exception ex) {
			logger.error("Exception ; " + ex.getMessage());
			try {
				tx.rollback();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}/*
		finally{
			try {
				PersistenceManager.releaseSession(hbsession);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		*/
		return true;
	}

	/**
	 * @author dan
	 */
	public static boolean deleteFieldVisibility(Long id, Session session) {

		try {
			//Session s=PersistenceManager.getSession();
//beginTransaction();
			AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(
					AmpFieldsVisibility.class, id);
			AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
			parent.getItems().remove(field);
			Iterator i = field.getTemplates().iterator();
			while (i.hasNext()) {
				AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
				element.getFields().remove(field);
			}
			session.delete(field);
			//tx.commit();

		}
		catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @author dan
	 */
	public static boolean deleteFeatureVisibility(Long id, Session session) {

		try {
			//Session s=PersistenceManager.getSession();
//beginTransaction();
			AmpFeaturesVisibility feature = (AmpFeaturesVisibility) session.load(
					AmpFeaturesVisibility.class, id);
			AmpObjectVisibility parent = (AmpObjectVisibility) feature.getParent();
			parent.getItems().remove(feature);
			Iterator i = feature.getTemplates().iterator();
			//feature.getItems().clear();

			Iterator j = feature.getItems().iterator();
			while (j.hasNext()) {
				AmpFieldsVisibility field = (AmpFieldsVisibility) j.next();
				i = feature.getTemplates().iterator();
				while (i.hasNext()) {
					AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
					element.getFields().remove(field);
				}
				i = field.getTemplates().iterator();
				while (i.hasNext()) {
					AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
					element.getFields().remove(field);
				}
			}
			i = feature.getTemplates().iterator();
			while (i.hasNext()) {
				AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
				element.getFeatures().remove(feature);
			}
			session.delete(feature);
			//tx.commit();

			//s.close();

		}
		catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @author dan
	 */
	public static boolean deleteModuleVisibility(Long id, Session session) {
		Transaction tx = null;
		try {
//beginTransaction();
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
						element.getFields().remove(field);
					}
					i = field.getTemplates().iterator();
					while (i.hasNext()) {
						AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
						element.getFields().remove(field);
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
			//tx.commit();
		} catch (ObjectNotFoundException e) {
		} catch (HibernateException e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
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
			q.setParameter("moduleName", moduleName, Hibernate.STRING);
			c = q.list();
			if(c.size()!=0)
				id=(AmpModulesVisibility) c.iterator().next();

		}
		catch (Exception ex) {
			logger.error("ERROR amp modules visibility" + moduleName);
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			q.setParameter("fieldName", fieldName, Hibernate.STRING);
			c = q.list();
			if(c.size()!=0)
				id=(AmpFieldsVisibility) c.iterator().next();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			q.setParameter("featureName", featureName, Hibernate.STRING);
			c = q.list();
			if(c.size()!=0)
				id=(AmpFeaturesVisibility) c.iterator().next();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ft.getItems();
	}

	/**
	 * @author dan
	 */
	public static String getModuleNameVisibility(Long id) {
		Session session = null;
		AmpModulesVisibility ft = new AmpModulesVisibility();
		try {
			session = PersistenceManager.getSession();
			ft = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : ", ex);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
		}
		return ft.getName();
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
			logger.error("Exception : ", ex);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
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
		Transaction tx = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpModulesVisibility ampModule;
//beginTransaction();
			ampModule = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,
					templateId);
			//ampModule.setItems(null);
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
				//if(found) fDb.setVisible("true");
				//else fDb.setVisible("false");
			}
			session.saveOrUpdate(ampModule);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception :::: " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;
		AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			ampTemplate = (AmpTemplatesVisibility) session.load(
					AmpTemplatesVisibility.class, templateId);
			ampTemplate.getItems().retainAll(modules);
			ampTemplate.getItems().addAll(modules);
			ampTemplate.getFeatures().retainAll(features);
			ampTemplate.getFeatures().addAll(features);
			//ampTemplate.getFields().retainAll(fields);
			//ampTemplate.getFields().addAll(fields);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx = null;
		AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
//beginTransaction();
		ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
				templateId);
		if (ampTemplate.getItems()!=null){
			ampTemplate.getItems().retainAll(modules);
			ampTemplate.getItems().addAll(modules);
		}else{
			ampTemplate.setItems(new TreeSet<AmpModulesVisibility>());
			ampTemplate.getItems().addAll(modules);
		}
		//tx.commit();
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
		Transaction tx = null;
		AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
//beginTransaction();
		ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
				templateId);
		ampTemplate.setName(templateName);
		//tx.commit();
		return;
	}

	/**
	 *
	 * @author dan
	 *
	 * @return
	 * @throws SQLException
	 * @throws HibernateException
	 */
	public static void updateAmpFeaturesTreeVisibility(Collection features,
			Long templateId, Session session) throws HibernateException, SQLException {
		Transaction tx = null;
		AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
//beginTransaction();
		ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
				templateId);
		if (ampTemplate.getFeatures()!=null){
			ampTemplate.getFeatures().retainAll(features);
			ampTemplate.getFeatures().addAll(features);
		}else{
			ampTemplate.setFeatures(new TreeSet<AmpFeaturesVisibility>());
			ampTemplate.getFeatures().addAll(features);
		}
		
		//tx.commit();
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
	public static void updateAmpFieldsTreeVisibility(Collection fields,
			Long templateId, Session session) throws HibernateException {
		Transaction tx = null;
		AmpTemplatesVisibility ampTemplate = new AmpTemplatesVisibility();
//beginTransaction();
		ampTemplate = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
				templateId);
		if (ampTemplate.getFields()!=null){
			ampTemplate.getFields().retainAll(fields);
			ampTemplate.getFields().addAll(fields);
		}else{
			ampTemplate.setFields(new TreeSet<AmpFieldsVisibility>());
			ampTemplate.getFields().addAll(fields);
		}
		//tx.commit();
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
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			if(session.isDirty()) {
				//System.out.println("field:::: dirtyyyyyyyyyyyyyyyyyyyyyyy");
//session.flush();
			}
//beginTransaction();
			feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class,
					featureId);
			field.setParent(feature);
			field.setName(fieldName);
			if(hasLevel!=null && "no".compareTo(hasLevel)==0)
				field.setHasLevel(false);
			else field.setHasLevel(false);
			session.save(field);
			//tx.commit();
			
			
			boolean makeVisible	= false;
			if ( defaultVisible == null ) {
				String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
				if (gsValue != null && gsValue.equalsIgnoreCase("on")){
					makeVisible		= true;
				}
			}
			else
				makeVisible 	= defaultVisible;
			
       		
       		if  ( makeVisible ){
//beginTransaction();
    			template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, templateId);
    			template.getFields().add(field);
    			//tx.commit();
       		}
			
			//session.saveOrUpdate(template);
			////tx.commit();

		}
		catch (Exception ex) {
			try {
				tx.rollback();
			}
			catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			}
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
			throw new DgException(ex);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return;
	}

	/**
	 * @author dan
	 */
	public static void insertFeatureWithModuleVisibility(Long templateId,
			Long moduleId, String featureName, String hasLevel) throws DgException {
		Session session = null;
		AmpModulesVisibility module = new AmpModulesVisibility();
		AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
		AmpTemplatesVisibility template = null;
		Transaction tx;
		try {
			session = PersistenceManager.getSession();

//beginTransaction();
			module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,
					moduleId);
			feature.setParent(module);
			feature.setName(featureName);
			if(hasLevel!=null && "no".compareTo(hasLevel)==0)
				feature.setHasLevel(false);
			else feature.setHasLevel(true);
			
			module.getItems().add(feature);
			
			session.update(module);
			session.save(feature);
			//tx.commit();
			
       		String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
       		if (gsValue != null && gsValue.equalsIgnoreCase("on")){
//beginTransaction();
				template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
						templateId);
				template.getFeatures().add(feature);
				session.update(template);
				//tx.commit();
       		}
			//session.saveOrUpdate(template);
			////tx.commit();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
			throw new DgException(ex);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx;
		try {
			session = PersistenceManager.getSession();

//beginTransaction();
			module.setName(moduleName);
			if(hasLevel!=null && "no".compareTo(hasLevel)==0)
				module.setHasLevel(false);
			else module.setHasLevel(true);
			session.save(module);
			//tx.commit();
			
			String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
       		if (gsValue != null && gsValue.equalsIgnoreCase("on")){
//beginTransaction();
				template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
						templateId);
				template.getItems().add(module);
				session.update(template);
				//tx.commit();
       		}
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return;
	}

	public static void insertModuleVisibility(Long templateId, Long parentId, String moduleName, String hasLevel) {
		Session session = null;
		AmpModulesVisibility module = new AmpModulesVisibility();
		AmpTemplatesVisibility template = null;
		Transaction tx;
		try {
			session = PersistenceManager.getSession();

//beginTransaction();
			AmpModulesVisibility parent = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, parentId);
			
			module.setName(moduleName);
			module.setParent(parent);
			if(hasLevel!=null && "no".compareTo(hasLevel)==0)
				module.setHasLevel(false);
			else module.setHasLevel(true);
			
			parent.getSubmodules().add(module);
			
			session.update(parent);
			session.save(module);
			//tx.commit();
			
			String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
       		if (gsValue != null && gsValue.equalsIgnoreCase("on")){
//beginTransaction();
				template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
						templateId);
				template.getItems().add(module);
				session.update(template);
				//tx.commit();
       		}
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
		Transaction tx;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,id);
			moduleParent = getModuleVisibility(moduleParentName);
			module.setParent(moduleParent);
			session.save(module);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return;
	}


	/**
	 * @author dan
	 */
	public static void updateFieldWithFeatureVisibility(Long featureId,
			String fieldName) {
		Session session = null;
		AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
		AmpFieldsVisibility field = new AmpFieldsVisibility();
		Query qry;
		Collection col = new ArrayList();
		String qryStr;
		Transaction tx;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			feature = (AmpFeaturesVisibility) session.load(AmpFeaturesVisibility.class,
					featureId);
			qryStr = "select f from " + AmpFieldsVisibility.class.getName() + " f"
			+ " where f.name = '" + fieldName + "'"; ;
			qry = session.createQuery(qryStr);
			col = qry.list();
			field = (AmpFieldsVisibility) col.iterator().next();
			feature.getItems().add(field);
			field.setParent(feature);
			session.saveOrUpdate(field);
			//tx.commit();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return;
	}

	/**
	 * @author dan
	 */
	public static void updateFeatureWithModuleVisibility(Long moduleId,
			String featureName) {
		Session session = null;
		AmpModulesVisibility module = new AmpModulesVisibility();
		AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
		Query qry;
		Collection col = new ArrayList();
		String qryStr;
		Transaction tx;
		try {

			session = PersistenceManager.getSession();
//beginTransaction();
			module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class,
					moduleId);
			qryStr = "select f from " + AmpFeaturesVisibility.class.getName() + " f"
			+ " where f.name = '" + featureName + "'";
			qry = session.createQuery(qryStr);
			col = qry.list();
			feature = (AmpFeaturesVisibility) col.iterator().next();
			module.getItems().add(feature);
			feature.setParent(module);
			session.saveOrUpdate(feature);
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class,
					id);
			List list = session.createQuery("from " +
					AmpModulesVisibility.class.getName() ). list();

			TreeSet mySet=new TreeSet(FeaturesUtil.ALPHA_ORDER);
			mySet.addAll(list);
			ft.setAllItems(mySet);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ft;
	}

	/**
	 * @author dan
	 */
	public static List getAllFieldsId() {
		Session session = null;
		Transaction tx=null;
		ArrayList <Long> result=new ArrayList();

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			List list = session.createQuery("from " + AmpFieldsVisibility.class.getName() ). list();
			for(Iterator it=list.iterator();it.hasNext();)
			{
				AmpFieldsVisibility f=(AmpFieldsVisibility) it.next();
				result.add(f.getId());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * @author dan
	 */
	public static List getAllModulesId() {
		Session session = null;
		Transaction tx=null;
		ArrayList <Long> result=new ArrayList();

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			List list = session.createQuery("from " + AmpModulesVisibility.class.getName() ). list();
			for(Iterator it=list.iterator();it.hasNext();)
			{
				AmpModulesVisibility f=(AmpModulesVisibility) it.next();
				result.add(f.getId());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * @author dan
	 */
	public static List getAllFeaturesId() {
		Session session = null;
		Transaction tx=null;
		ArrayList <Long> result=new ArrayList();

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			List list = session.createQuery("from " + AmpFeaturesVisibility.class.getName() ). list();
			for(Iterator it=list.iterator();it.hasNext();)
			{
				AmpFeaturesVisibility f=(AmpFeaturesVisibility) it.next();
				result.add(f.getId());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * @author dan
	 * 
	 */

	public static void cleanUpFM(ArrayList allFieldsId) {
		Session session = null;

		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			for (Iterator it = allFieldsId.iterator(); it.hasNext();) {
				Long idf = (Long) it.next();
				AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(AmpFieldsVisibility.class, idf);
				AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
				parent.getItems().remove(field);
				Iterator i = field.getTemplates().iterator();
				while (i.hasNext()) {
					AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
					element.getFields().remove(field);
				}
				session.delete(field);
			}
//session.flush();
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception ::: " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}

	/**
	 * @author dan
	 * setting the parent to null
	 */
	public static void deleteOneModule(Long id) {
		Session session = null;

		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
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
//session.flush();
			}
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception ... " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}

	/**
	 * @author dan
	 * delete one module from db
	 */
	public static void deleteModule(Long id) {
		Session session = null;

		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, id);
			session.delete(module);
//session.flush();
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception ... " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}




	/**
	 * @author dan
	 * 
	 */
	public static void deleteOneFeature(Long id) {
		Session session = null;

		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();


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

//session.flush();
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception :: " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}

	/**
	 * @author dan
	 */

	public static void deleteOneField(Long id) {
		Session session = null;

		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			AmpFieldsVisibility field = (AmpFieldsVisibility) session.load(AmpFieldsVisibility.class, id);
			AmpFeaturesVisibility parent = (AmpFeaturesVisibility) field.getParent();
			parent.getItems().remove(field);
			Iterator i = field.getTemplates().iterator();
			while (i.hasNext()) {
				AmpTemplatesVisibility element = (AmpTemplatesVisibility) i.next();
				element.getFields().remove(field);
			}
			session.delete(field);
			//	 if(session.contains(field)) //System.out.println("o daaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: " + field.getName());
//session.flush();
			//tx.commit();
		}
		catch (Exception ex) {
			logger.error("Exception ::: " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}

	public static String getDefaultCountryIso() throws Exception {
		String defaultCountryIso = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select gs.globalSettingsValue from " +
			AmpGlobalSettings.class.getName() +
			" gs where gs.globalSettingsName = 'Default Country' ";
			qry = session.createQuery(qryStr);
			Object defIso = qry.uniqueResult();
			if(defIso!=null){
				defaultCountryIso = (String) defIso;
			}
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			throw ex;
		}
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
/*
	public static final Comparator ALPHA_ORDER = new Comparator()
	{
		public int compare(Object a, Object b)
		{
			AmpObjectVisibility pairA = (AmpObjectVisibility)a;
			AmpObjectVisibility pairB = (AmpObjectVisibility)b;

			return pairA.getName().compareTo(pairB.getName());
		}
	};

	public static final Comparator ALPHA_AMP_TREE_ORDER = new Comparator()
	{
		public int compare(Object a, Object b)
		{
			AmpTreeVisibility pairA = (AmpTreeVisibility)a;
			AmpTreeVisibility pairB = (AmpTreeVisibility)b;

			return pairA.getRoot().getName().compareTo(pairB.getRoot().getName());
		}
	};
*/
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
			logger.error(e);
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
			logger.error(e);
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

		Transaction tx=null;
		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpObjectVisibility objectVisibility =(AmpObjectVisibility) session.load(clazz, id);
			objectVisibility.setDescription(description);
			session.update(objectVisibility);
			//tx.commit();
		}
		catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Object Visibility update!",e1);
				}
			}
			throw new DgException("Cannot update Object Visibility!",e);
		}
		
	}

	public static AmpComponentType getDefaultComponentType() {
		String defaultComponentTypeIdStr = getGlobalSettingValue("Default Component Type");
		return ComponentsUtil.getComponentTypeById(Long.parseLong(defaultComponentTypeIdStr));
	}

	public static boolean isVisibleSectors(String type, ServletContext ampContext){
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpFieldsVisibility fieldToTest=ampTreeVisibility.getFieldByNameFromRoot(type+" Sector");
		if(fieldToTest!=null)
			return fieldToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
		return false;
	}

	public static boolean isVisibleField(String fieldName, ServletContext ampContext){
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpFieldsVisibility fieldToTest=ampTreeVisibility.getFieldByNameFromRoot(fieldName);
		if(fieldToTest!=null)
			return fieldToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
		return false;
	}
	
	public static boolean isVisibleFeature(String featureName, ServletContext ampContext){
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpFeaturesVisibility featureToTest=ampTreeVisibility.getFeatureByNameFromRoot(featureName);
		if(featureToTest!=null)
			return featureToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
		return false;
	}

	public static boolean isVisibleModule(String moduleName, ServletContext ampContext){
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpModulesVisibility moduleToTest=ampTreeVisibility.getModuleByNameFromRoot(moduleName);
		if(moduleToTest!=null)
			return moduleToTest.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
		return false;
	}
}
