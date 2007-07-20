package org.digijava.module.aim.util;

import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.exception.AimException;

public class NpdSettingsUtil {
	private static Logger logger = Logger.getLogger(NpdSettingsUtil.class);

	public static void updateSettings(NpdSettings settings) throws AimException {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(settings);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", ex);
				}
			}
			throw new AimException("Can't update settings", e);
		}
	}

	public static NpdSettings getCurrentSettings(Long teamId)
			throws AimException {
		Session session = null;
		NpdSettings npdSettings = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class, teamId);
			if (ampTeam.getNpdSettings() == null) {
				npdSettings = new NpdSettings();
				npdSettings.setAngle(new Integer(0));
				npdSettings.setWidth(new Integer(ChartUtil.CHART_WIDTH));
				npdSettings.setHeight(new Integer(ChartUtil.CHART_HEIGHT));
				npdSettings.setTeam(ampTeam);
				updateSettings(npdSettings);
			} else {
				npdSettings = ampTeam.getNpdSettings();
			}
		} catch (ObjectNotFoundException ex) {
			logger.error("Unable to load team");
			throw new AimException("Team can't be found", ex);
		} catch (Exception e) {
			logger.error("Unable to load NpdSettings");
			throw new AimException("Can't rollback", e);
		}
		return npdSettings;
	}

}
