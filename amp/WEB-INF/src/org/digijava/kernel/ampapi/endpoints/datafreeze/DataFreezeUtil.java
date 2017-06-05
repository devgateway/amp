package org.digijava.kernel.ampapi.endpoints.datafreeze;


import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.hibernate.Session;
import org.apache.log4j.Logger;

public class DataFreezeUtil {
	private static Logger logger = Logger.getLogger(DataFreezeUtil.class);
	public static AmpDataFreezeSettings getDataFreezeEventById(Long id) {
		return (AmpDataFreezeSettings) PersistenceManager.getSession().get(AmpDataFreezeSettings.class, id);
	}
	
	public static void saveDataFreezeEvent(AmpDataFreezeSettings dataFreezeEvent) {
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.saveOrUpdate(dataFreezeEvent);
		} catch (Exception e) {
			logger.error("Exception from saveDataFreezeEvent: " + e.getMessage());
		}
	}
	
	public static void deleteDataFreezeEvent(Long id) {
		AmpDataFreezeSettings dataFreezeEvent = getDataFreezeEventById(id);
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.delete(dataFreezeEvent);
		} catch (Exception e) {
			logger.error("Exception from deleteDataFreezeEvent: " + e.getMessage());
		}
	}


}
