package org.digijava.module.aim.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.GPISetup;
import org.hibernate.Session;

public class GPISetupUtil {

	public static AmpMeasures getMeasure(Long id) {
		AmpMeasures measure = null;
		try {
			Session session = null;
			session = PersistenceManager.getRequestDBSession();
			measure = (AmpMeasures) session.get(AmpMeasures.class, id);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return measure;
	}

	public static GPISetup getSetup() {
		GPISetup setup = null;
		try {
			Session session = null;
			session = PersistenceManager.getRequestDBSession();
			setup = (GPISetup) session.createCriteria(GPISetup.class).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return setup;
	}

	public static void saveGPISetup(GPISetup setup) throws Exception {
		Session session = null;
		session = PersistenceManager.getRequestDBSession();
		session.saveOrUpdate(setup);
	}
	
	public static void saveDescription(Long id, String description) {
		try {
			Session session = null;
			session = PersistenceManager.getRequestDBSession();
			AmpGPISurveyIndicator indicator = (AmpGPISurveyIndicator) session.load(AmpGPISurveyIndicator.class, id);
			indicator.setDescription(description);
			session.saveOrUpdate(indicator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
