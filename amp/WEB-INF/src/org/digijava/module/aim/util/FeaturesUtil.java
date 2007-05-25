package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.FeatureManager;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.helper.Flag;

public class FeaturesUtil {

	private static Logger logger = Logger.getLogger(FeaturesUtil.class);

	private static Collection globalSettingsCache=null;
	
	private ServletContext ampContext = null;
	
	public static void logGlobalSettingsCache() {
		String log		= "";
		Iterator iter	= globalSettingsCache.iterator();
		while( iter.hasNext() ) {
			AmpGlobalSettings ampGlobalSetting	= (AmpGlobalSettings) iter.next();
			log		= log + ampGlobalSetting.getGlobalSettingsName() + ":" + ampGlobalSetting.getGlobalSettingsValue() + ";";
		}
		logger.info ("GlobalSettingsCache is -> " + log);
	}
	
	public static synchronized Collection getGlobalSettingsCache() {
		return globalSettingsCache;
	}
	
	public static synchronized void setGlobalSettingsCache(Collection globalSettings) {
		globalSettingsCache=globalSettings;
	}
	
	public static boolean isDefault(Long templateId)
	{
		String s=FeaturesUtil.getGlobalSettingValue("Feature Template");
		if(s!=null)
			if(templateId.compareTo(new Long(Long.parseLong(s)))==0) return true;
		return false;
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
	}
	
	/**
	 * Used to get the features which are currently active for AMP
	 * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
	 */
	public static Collection getActiveFeatures() {
		FeatureTemplates template=getTemplate(getGlobalSettingValue("Feature Template"));
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

	public static AmpFeature toggleFeature(Integer featureId) {
		Session session = null;
		Transaction tx = null;
		AmpFeature feature = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			feature = (AmpFeature) session.load(AmpFeature.class,
					featureId);
			feature.setActive(!feature.isActive());
			session.update(feature);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Transaction rollback failed :" + rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
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
					" where f.featureTemplateName = '"+templateName+"'";
			qry = session.createQuery(qryStr);
			col = qry.list();
			if(col==null) return false;
			if(col.size()==0) return false;
			if(col.size()>0) return true;
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
					" where f.templateId = '"+templateId+"'";
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
		if(!col.isEmpty())
		{
			Iterator it=col.iterator();
		
			FeatureTemplates x=(FeatureTemplates) it.next();
			return x;
		}
		else return null;
	}

	
	/**
	 * @author dan
	 */
	public static boolean deleteTemplate(Long id) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx=session.beginTransaction();
			FeatureTemplates ft=new FeatureTemplates();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
			session.delete(ft);
			tx.commit();
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
		return true;
	}


	/**
	 * @author dan
	 */
	public static Collection getTemplateFeatures(Long id) {
		Session session = null;
		FeatureTemplates ft=new FeatureTemplates();
		try {
			session = PersistenceManager.getSession();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
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
		return ft.getFeatures();
	}
	
	/**
	 * @author dan
	 */
	public static String getTemplateName(Long id) {
		Session session = null;
		FeatureTemplates ft=new FeatureTemplates();
		try {
			session = PersistenceManager.getSession();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
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
		return ft.getFeatureTemplateName();
	}
	
	/**
	 * 
	 * @author dan
	 *
	 * @return
	 */
	public static void insertTemplateFeatures(Collection features, String template) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			FeatureTemplates ampTemplate=new FeatureTemplates();
			ampTemplate.setFeatureTemplateName(template);
			ampTemplate.setFeatures(new HashSet());
			for(Iterator it=features.iterator();it.hasNext();)
				{	
					AmpFeature ampFeature=(AmpFeature)it.next();
					ampTemplate.getFeatures().add(ampFeature);
				}
			session.save(ampTemplate);
			tx.commit();
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

		return ;
	}

	/**
	 * 
	 * @author dan
	 *
	 * @return
	 */
	public static void updateTemplateFeatures(Collection features, Long templateId, String templateName) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			FeatureTemplates ampTemplate=new FeatureTemplates();
			ampTemplate=(FeatureTemplates)session.load(FeatureTemplates.class,templateId);
			ampTemplate.setFeatureTemplateName(templateName);
		    System.out.println(ampTemplate.getFeatureTemplateName());
			ampTemplate.setFeatures(new HashSet());
			//ampTemplate.getFeatures().addAll(features);
			for(Iterator it=features.iterator();it.hasNext();)
				{	
					AmpFeature ampFeature=(AmpFeature)it.next();
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
			tx.commit();
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

		return ;
	}

	
	
	public static Collection getAllCountries() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c order by c.countryName";
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
	}

	public static Collection getAllCountryFlags() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		String params = "";

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f.countryId,f.defaultFlag from " + AmpSiteFlag.class.getName() + " f";
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
				qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c" +
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
	}

	public static AmpSiteFlag getAmpSiteFlag(Long id) {
		Session session = null;
		AmpSiteFlag flag = null;

		try {
			session = PersistenceManager.getSession();
			flag = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
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

		return flag;
	}

	public static byte[] getFlag(Long id) {
		Session session = null;
		byte flag[] = null;

		try {
			session = PersistenceManager.getSession();
			AmpSiteFlag tmp = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
			if (tmp != null) {
				flag = tmp.getFlag();
			}

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
			AmpSiteFlag newDefFlag = (AmpSiteFlag) session.load(AmpSiteFlag.class,id);
			tx = session.beginTransaction();
			newDefFlag.setDefaultFlag(true);
			session.update(newDefFlag);
			if (defFlag != null) {
				defFlag.setDefaultFlag(false);
				session.update(defFlag);
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	
	
	public static String getGlobalSettingValue(String globalSettingName) {
		Collection settings=null;
		settings=getGlobalSettingsCache();
		if(settings==null) {
			settings=getGlobalSettings();
			setGlobalSettingsCache(settings);
		}
		
		Iterator i=settings.iterator();
		while (i.hasNext()) {
			AmpGlobalSettings element = (AmpGlobalSettings) i.next();
			if(element.getGlobalSettingsName().equals(globalSettingName)) return element.getGlobalSettingsValue();
		}
		return null;
	}
	
	/*
	 * edited by Govind G Dalwani
	 */
	/*
	 * to get all the Global settings
	 */
	public static Collection getGlobalSettings()
	{
		Collection coll = null;
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try{
				session = PersistenceManager.getSession();
				qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs " ;
				qry = session.createQuery(qryStr);
				coll=qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return coll;
	}
	/*
	 * to get the country names
	 */
	public static Collection getCountryNames()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn order by cn.countryName" ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	}
	/*
	 * to get the country ISO that is set as a default value...
	 */
	public static Collection getDefaultCountryISO()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs where gs.globalSettingsName = 'Default Country' " ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	}
	/*
	 * to get the country name from the Iso got
	 */
	public static Collection getDefaultCountry(String ISO)
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		String a ="in the get country...";
		logger.info(a);
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn where cn.iso = '"+ ISO +"'" ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	}
	
		
	
}
