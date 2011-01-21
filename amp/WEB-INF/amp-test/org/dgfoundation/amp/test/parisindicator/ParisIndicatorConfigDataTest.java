package org.dgfoundation.amp.test.parisindicator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestionType;
import org.digijava.module.aim.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import junit.framework.TestCase;

public class ParisIndicatorConfigDataTest extends TestCase {

	private Session session = null;

	protected void setUp() throws Exception {
		Configuration.initConfig();
		session = PersistenceManager.getSession();
	}

	protected void tearDown() throws Exception {
		session.close();
	}

	/**
	 * Tests if the PI tables have records, otherwise the PI reports will fail.
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testNeededDataAvailability() throws HibernateException,
			SQLException {
		
		Collection<AmpAhsurveyIndicator> ahSurveyIndicators = DbUtil.getAllAhSurveyIndicators();
		assertFalse("ERROR: The table 'amp_ahsurvey_indicator' cant be empty.", ahSurveyIndicators.size() == 0);

		String queryString = "from " + AmpAhsurveyQuestion.class.getName();
		Query qry = session.createQuery(queryString);
		assertFalse("ERROR: The table 'amp_ahsurvey_question' cant be empty.", qry.list().size() == 0);
		
		queryString = "from " + AmpAhsurveyQuestionType.class.getName();
		qry = session.createQuery(queryString);
		assertFalse("ERROR: The table 'amp_ahsurvey_question_type' cant be empty.", qry.list().size() == 0);
	}
}