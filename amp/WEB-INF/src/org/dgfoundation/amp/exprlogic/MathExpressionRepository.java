package org.dgfoundation.amp.exprlogic;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class MathExpressionRepository {
	private static Logger logger = Logger.getLogger(MathExpressionRepository.class);
	public static final String OVERAGE_PROJECT = "overageProjects";
	public static final String AGE_OF_PROJECT = "ageOfProject";
	public static final String PREDICTABILITY_OF_FUNDING = "predictabilityOfFunding";
	public static final String AVERAGE_SIZE_OF_PROJECT = "averageSizeofProjects";
	public static final String VARIANCE_ACTUAL_COMMITMENTS = "actualCommitmentsVariance";
	public static final String VARIANCE_ACTUAL_DISBURSEMENTS = "actualDisbursmentVariance";
	public static final String CUMULATIVE_COMMITMENT = "cumulativeCommitment";
	public static final String CUMULATIVE_DISBURSMENT = "cumulativeDisbursement";
	public static final String EXECUTION_RATE = "buildExecutionRate";
	public static final String PROJECT_PERIOD = "projectPeriod";
	public static final String OVERAGE = "overage";
	
	private static Hashtable<String, MathExpression> expresions = new Hashtable<String, MathExpression>();

	/**
	 * Create all expressions
	 */

	static {
		// Build all expression
		buildOverageProjects();
		buildAgeOfProject();
		buildPredictabilityOfFunding();
		buildAverageSizeofProjects();
		buildActualDisbursementVariance();
		buildActualCommitmentsVariance();
		buildCumulativeCommitment();
		buildCumulativeDisbursment();
		buildExecutionRate();
		buildProjectPeriod();
		buildOverage();
	}

	/**
	 * buildOverageProjects Current Date - Proposed Completion
	 */
	private static void buildOverageProjects() {
		try {
			// subtract PROPOSED_COMPLETION_DATE_VALUE to CURRENT_DATE_VALUE
			MathExpression oper = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.CURRENT_DATE_VALUE, ArConstants.PROPOSED_COMPLETION_DATE_VALUE);
			// get the result in days
			MathExpression oper2 = new MathExpression(MathExpression.Operation.DIVIDE_ROUND_DOWN, oper, new BigDecimal(1000 * 60 * 60 * 24));
			expresions.put(OVERAGE_PROJECT, oper2);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * buildAgeOfProject Current Date - Actual Start Date
	 */
	private static void buildAgeOfProject() {
		try {
			MathExpression oper = new MathExpression(MathExpression.Operation.DATE_MONTH_DIFF, ArConstants.CURRENT_DATE_VALUE, ArConstants.ACTUAL_START_DATE_VALUE);
		 expresions.put(AGE_OF_PROJECT, oper);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * buildPredictabilityOfFunding ((Actual Disbursements - planned
	 * Disbursements)/planned disbursements) X 100
	 */
	private static void buildPredictabilityOfFunding() {
		try {
			MathExpression substractActualPlanned = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.ACTUAL_DISBURSEMENT, ArConstants.PLANNED_DISBURSEMENT);
			MathExpression divideOper1ByPLanned = new MathExpression(MathExpression.Operation.DIVIDE, substractActualPlanned, ArConstants.PLANNED_DISBURSEMENT);

			MathExpression multiResultPannedBy100 = new MathExpression(MathExpression.Operation.MULTIPLY, divideOper1ByPLanned, new BigDecimal(100));

			expresions.put(PREDICTABILITY_OF_FUNDING, multiResultPannedBy100);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * AverageSizeofProjects Total commitments/Count Of Activities
	 */
	private static void buildAverageSizeofProjects() {
		try {
			MathExpression divide = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.TOTAL_COMMITMENTS, ArConstants.COUNT_PROJECTS);
			expresions.put(AVERAGE_SIZE_OF_PROJECT, divide);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// variances

	private static void buildActualCommitmentsVariance() {
		try {
			MathExpression variance = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.MAX_ACTUAL_COMMITMENT, ArConstants.MIN_ACTUAL_COMMITMENT);
			expresions.put(VARIANCE_ACTUAL_COMMITMENTS, variance);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void buildActualDisbursementVariance() {
		try {
			MathExpression variance = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.MAX_ACTUAL_DISBURSEMENT, ArConstants.MIN_ACTUAL_DISBURSEMENT);
			expresions.put(VARIANCE_ACTUAL_DISBURSEMENTS, variance);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void buildCumulativeCommitment() {
		try {
			MathExpression variance = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_COMMITMENT, new BigDecimal(1));
			expresions.put(CUMULATIVE_COMMITMENT, variance);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void buildCumulativeDisbursment() {
		try {
			MathExpression variance = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_DISBURSEMENT, new BigDecimal(1));
			expresions.put(CUMULATIVE_DISBURSMENT, variance);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	 // Execution rate =	(Cumulative Disbursement/ Cumulative Commitment)*100 
	private static void  buildExecutionRate() {
		try {
			MathExpression divideDisbursementByCommitment=new MathExpression(MathExpression.Operation.DIVIDE,ArConstants.ACTUAL_DISBURSEMENT,ArConstants.ACTUAL_COMMITMENT);
			MathExpression multiplyBy100=new MathExpression(MathExpression.Operation.MULTIPLY,divideDisbursementByCommitment,new BigDecimal(100));
			expresions.put(EXECUTION_RATE, multiplyBy100);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * Project Period (months) = Proposed Completion Date - Actual Start date 
	 */
	private static void  buildProjectPeriod() {
		try {
			MathExpression dateDiff=new MathExpression(MathExpression.Operation.DATE_MONTH_DIFF, ArConstants.PROPOSED_COMPLETION_DATE_VALUE,ArConstants.ACTUAL_START_DATE_VALUE);
			expresions.put(PROJECT_PERIOD, dateDiff);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Overage (months) = Age of project - Project period
	 */
	private static void  buildOverage() {
		try {
			MathExpression subsract=new MathExpression(MathExpression.Operation.SUBTRACT,expresions.get(AGE_OF_PROJECT) ,expresions.get(PROJECT_PERIOD));
			expresions.put(OVERAGE, subsract);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	
	/**
	 * Get The expression by Key
	 * 
	 * @param key
	 * @return
	 */
	public static MathExpression get(String key) {
		return expresions.get(key);
	}
}
