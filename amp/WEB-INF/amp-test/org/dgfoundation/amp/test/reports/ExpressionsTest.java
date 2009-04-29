package org.dgfoundation.amp.test.reports;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;

public class ExpressionsTest extends TestCase {

	public void testOverageProjects() {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		// Current Date - Proposed Completion
		long now = (new Date()).getTime();
		long nowminus10days = now - (10 * 1000 * 60 * 60 * 24);

		values.put(ArConstants.PROPOSED_COMPLETION_DATE_VALUE, new BigDecimal(nowminus10days));
		values.put(ArConstants.CURRENT_DATE_VALUE, new BigDecimal(now));

		assertEquals(new BigDecimal(10), MathExpressionRepository.get(MathExpressionRepository.OVERAGE_PROJECT_KEY).result(values));
	}

	// Current Date - Actual Start Date
	public void testAgeOfProject() {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		// Current Date - Proposed Completion
		long now = (new Date()).getTime();
		long nowminus10days = now - (10 * 1000 * 60 * 60 * 24);

		values.put(ArConstants.ACTUAL_START_DATE_VALUE, new BigDecimal(nowminus10days));
		values.put(ArConstants.CURRENT_DATE_VALUE, new BigDecimal(now));

		assertEquals(new BigDecimal(10), MathExpressionRepository.get(MathExpressionRepository.AGE_OF_PROJECT_KEY).result(values));
	}

	// ((Actual Disbursements - planned Disbursements)/planned disbursements) X
	// 100
	public void testPredictabilityOfFunding() {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		values.put(ArConstants.ACTUAL_DISBURSEMENT, new BigDecimal(100000));
		values.put(ArConstants.PLANNED_DISBURSEMENT, new BigDecimal(500000));

		assertEquals(-80L, MathExpressionRepository.get(MathExpressionRepository.PREDICTABILITY_OF_FUNDING_KEY).result(values).longValue());
	}

	public void testAverageSizeofProjects() {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		values.put(ArConstants.TOTAL_COMMITMENTS, new BigDecimal(100000));
		values.put(ArConstants.COUNT_PROJECTS, new BigDecimal(10));
		// Total commitments/Count Of Activities
		assertEquals(10000L, MathExpressionRepository.get(MathExpressionRepository.AVERAGE_SIZE_OF_PROJECT_KEY).result(values).longValue());
	}

	public void testActualCommitmentsVariance() {
		HashMap<String	, BigDecimal> values = new HashMap<String, BigDecimal>();

		values.put(ArConstants.MAX_ACTUAL_COMMITMENT, new BigDecimal(100000));
		values.put(ArConstants.MIN_ACTUAL_COMMITMENT, new BigDecimal(50000));
		// Total commitments/Count Of Activities
		assertEquals(50000L, MathExpressionRepository.get(MathExpressionRepository.VARIANCE_ACTUAL_COMMITMENTS_KEY).result(values).longValue());
	}

	public void testActualDisbursmentVariance() {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		values.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, new BigDecimal(100000));
		values.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, new BigDecimal(50000));
		// Total commitments/Count Of Activities
		assertEquals(50000L, MathExpressionRepository.get(MathExpressionRepository.VARIANCE_ACTUAL_DISBURSEMENTS_KEY).result(values).longValue());
	}
}
