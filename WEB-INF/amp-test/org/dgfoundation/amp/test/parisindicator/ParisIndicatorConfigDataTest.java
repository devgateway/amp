package org.dgfoundation.amp.test.parisindicator;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestionType;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.parisindicator.util.PIConstants;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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
	 * Check the PI tables.
	 * 
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testNeededDataAvailability() throws HibernateException, SQLException {

		Collection<AmpAhsurveyIndicator> ahSurveyIndicators = DbUtil.getAllAhSurveyIndicators();
		assertFalse("ERROR: The table 'amp_ahsurvey_indicator' cant be empty.", ahSurveyIndicators.size() == 0);

		String queryString = "from " + AmpAhsurveyQuestion.class.getName();
		Query qry = session.createQuery(queryString);
		assertTrue("ERROR: The table 'amp_ahsurvey_question' does not have " + PIConstants.NUMBER_OF_SURVEY_QUESTIONS
				+ " questions.", qry.list().size() == PIConstants.NUMBER_OF_SURVEY_QUESTIONS);

		queryString = "from " + AmpAhsurveyQuestionType.class.getName();
		qry = session.createQuery(queryString);
		assertFalse("ERROR: The table 'amp_ahsurvey_question_type' cant be empty.", qry.list().size() == 0);

		queryString = "from " + AmpAhsurveyResponse.class.getName();
		qry = session.createQuery(queryString);
		assertFalse("ERROR: The table 'amp_ahsurvey' does not have the right number of records.", ((float) qry.list()
				.size() / PIConstants.NUMBER_OF_SURVEY_QUESTIONS) == 1);

		queryString = "from " + AmpAhsurveyIndicatorCalcFormula.class.getName();
		qry = session.createQuery(queryString);
		Collection<AmpAhsurveyIndicatorCalcFormula> formulas = session.createQuery(queryString).list();
		Iterator<AmpAhsurveyIndicatorCalcFormula> iterFormulas = formulas.iterator();
		while (iterFormulas.hasNext()) {
			AmpAhsurveyIndicatorCalcFormula auxFormula = iterFormulas.next();
			try {
				String.valueOf(AmpMath.calcExp(auxFormula.getCalcFormula()));
			} catch (Exception e) {
				assertNotNull("ERROR: Wrong formula '" + auxFormula.getCalcFormula() + "'", e);
			}
		}

		Collection<AmpAhsurvey> commonData = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Criteria criteria = session.createCriteria(AmpAhsurvey.class);
			criteria.createAlias("pointOfDeliveryDonor", "podd1");
			criteria.createAlias("pointOfDeliveryDonor.orgTypeId", "podd2");
			criteria.add(Restrictions.not(Restrictions.in("podd2.orgTypeCode", new String[] {
					PIConstants.ORG_GRP_MULTILATERAL, PIConstants.ORG_GRP_BILATERAL })));
			commonData = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue("WARNING: The table 'amp_ahsurvey' has records for PoDD not 'BILATERAL' or 'MULTILATERAL'. ",
				commonData.size() == 0);
	}
}