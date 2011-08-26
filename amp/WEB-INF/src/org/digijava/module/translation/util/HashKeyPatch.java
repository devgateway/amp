package org.digijava.module.translation.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.entity.PatcherMessageGroup;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Patches of translations, sets body hash code as key of translations.
 * @author Irakli Kobiashvili
 *
 */
public class HashKeyPatch {

	private static Logger logger = Logger.getLogger(HashKeyPatch.class);
	public static final String TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME = "Translation hashcode patch";
	
	public static void patchTranslationsIfNecessary(){
		Transaction tx = null;
		Session session = null;
		try {
			//check if we already ran this patch.
			//String gsv = FeaturesUtil.getGlobalSettingValue(TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME);
			String gsv = getGlobalSettingValueBypassCache(TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME);
			if (gsv == null){
				session = PersistenceManager.getSession();
				//patch translations
				patch(session);
				
				String formatedDate = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss").format(new Date());
				
				//mark db patched in global settings. 
//beginTransaction();
				AmpGlobalSettings ags = new AmpGlobalSettings();
				ags.setGlobalSettingsDescription("Show if translation keys have been patched to hash codes");
				ags.setGlobalSettingsName(TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME);
				ags.setGlobalSettingsPossibleValues("");
				ags.setGlobalSettingsValue("Ran normally at "+formatedDate);
				session.save(ags);
				//tx.commit();
			}else{
				logger.info("No translation hash code patch is necessary");
			}
		} catch (Exception e) {
			logger.error("Cannot patch translations!",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e2) {
					logger.error("cannot rolback translation hash code patcher",e2);
				}
			}
		}finally{
			if (session!=null){
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e3) {
					logger.error("cannot close translastion hash code pather db session",e3);
				}
			}
		}
	}
	
	/**
	 * Executes patch
	 * @param session
	 */
	private static void patch(Session session){
		Transaction tx = null;
		try {
			logger.info("Loading all translations from database...");
			Collection<Message> allMessages = PersistenceManager.loadAll(Message.class, session);
			if (allMessages!= null && allMessages.size() > 0){
				logger.info("Grouping translations with same key...");
				Collection<PatcherMessageGroup> messageGroups = 
					TrnUtil.groupByKey(allMessages, new TrnUtil.PatcherMessageGroupFactory());
					//createPachedGroups(allMessages);
				logger.info("Filtering duplicates...");
				Collection<PatcherMessageGroup> groups = TrnUtil.removeDuplicateHashCodes(messageGroups);
				logger.info("Patching and saving new translations... this may take some time");
				saveToDatabase(groups);
				logger.info("patching translations keys with hashcodes finished.");
			}
		} catch (Exception e) {
			System.err.println(e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error("Cannot rollback traslation patch actions"+e1);
				}
			}
		}
	}

	/**
	 * Saves all patched groups in db. 
	 * @param messageGroups
	 * @throws WorkerException
	 */
	private static void saveToDatabase(Collection<PatcherMessageGroup> messageGroups){
		int c = 0;
		long cm = 0;
		if (messageGroups != null){
			for (PatcherMessageGroup messageGroup : messageGroups) {
				for (Message patchedMessage : messageGroup.patcheAll()) {
					try {
						TranslatorWorker.getInstance().save(patchedMessage);
					} catch (Exception e) {
						logger.error(e);
					}
				}
				if(++c == 7000){
					cm += c;
					logger.info("Please wait, patch is still working. Processed "+cm+" messages...");
					c=0;
				}
			}
		}
	}

	
	/**
	 * Loads globals setting bypassing global settings cache to see changes done by autopacher.
	 * TODO This may be moved to {@link FeaturesUtil}
	 * TODO global settings cache may have some reload/refresh or cache update feature.
	 * @param key
	 * @return
	 */
	private static String getGlobalSettingValueBypassCache(String key){
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			String oql = "from "+AmpGlobalSettings.class.getName()+" as gs where gs.globalSettingsName=:gsName";
			Query query = session.createQuery(oql);
			query.setString("gsName", key);
			AmpGlobalSettings gs = (AmpGlobalSettings) query.uniqueResult();
			if (null == gs) return null;
			return gs.getGlobalSettingsValue();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != session){
				try {
					PersistenceManager.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
