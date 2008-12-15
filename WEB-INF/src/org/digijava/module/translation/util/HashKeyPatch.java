package org.digijava.module.translation.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.entity.PatchedMessageGroup;
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
			String gsv = FeaturesUtil.getGlobalSettingValue(TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME);
			if (gsv == null){
				session = PersistenceManager.getSession();
				//patch translations
				patch(session);
				
				String formatedDate = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss").format(new Date());
				
				//mark db patched in global settings. 
				tx = session.beginTransaction();
				AmpGlobalSettings ags = new AmpGlobalSettings();
				ags.setGlobalSettingsDescription("Show if translation keys have been patched to hash codes");
				ags.setGlobalSettingsName(TRN_HASH_PATCH_GLOBAL_SETTINGS_NAME);
				ags.setGlobalSettingsPossibleValues("");
				ags.setGlobalSettingsValue("Ran normally at "+formatedDate);
				session.save(ags);
				tx.commit();
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
	
	private static void patch(Session session){
		logger.info("Loading all translations from database...");
		Transaction tx = null;
		try {
			String oql = "from "+ Message.class.getName();
			Query query = session.createQuery(oql);
			@SuppressWarnings("unchecked")
			List<Message> allMessages = query.list();
			if (allMessages!= null && allMessages.size() > 0){
				logger.info("Grouping translations with same key...");
				Map<String, PatchedMessageGroup> keyGroupMap = new HashMap<String, PatchedMessageGroup>();
				for (Message message : allMessages) {
					PatchedMessageGroup group = keyGroupMap.get(message.getKey());
					if (group == null){
						group = new PatchedMessageGroup(message.getKey());
						keyGroupMap.put(message.getKey(), group);
					}
					//session.delete(message);
					group.addMessage(message);
				}
				logger.info("Filtering duplicates...");
				Collection<PatchedMessageGroup> patchedMessages = keyGroupMap.values();
				Map<String, PatchedMessageGroup> mapMessageGroup = new HashMap<String, PatchedMessageGroup>();
				for (PatchedMessageGroup messageGroup : patchedMessages) {
					mapMessageGroup.put(messageGroup.getHashKey(), messageGroup);
				}
				logger.info("Patching and saving new translations... this may take some time");
				int c = 0;
				long cm = 0;
				for (PatchedMessageGroup messageGroup : mapMessageGroup.values()) {
					for (Message patchedMessage : messageGroup.getPatchedMessages()) {
						TranslatorWorker.getInstance().save(patchedMessage);
					}
					if(++c == 7000){
						cm += c;
						logger.info("Please wait, patch is still working. Processed "+cm+" messages...");
						c=0;
					}
				}
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

	
}
