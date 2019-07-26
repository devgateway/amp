package org.dgfoundation.amp.exprlogic;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dyn.DynamicColumnsUtil;
import org.digijava.module.aim.dbentity.AmpColumns;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class MathExpressionRepository {
    private static Logger logger = Logger.getLogger(MathExpressionRepository.class);

    public static final String OVERAGE_PROJECT = "overageProjects";

    public static final String AGE_OF_PROJECT = "ageOfProject";
    
    public static final String CALCULATED_PROJECT_LIFE = "calculatedProjectLife";

    public static final String PREDICTABILITY_OF_FUNDING = "predictabilityOfFunding";

    public static final String AVERAGE_SIZE_OF_PROJECT = "averageSizeofProjects";

    public static final String VARIANCE_ACTUAL_COMMITMENTS = "actualCommitmentsVariance";

    public static final String VARIANCE_ACTUAL_DISBURSEMENTS = "actualDisbursmentVariance";

    public static final String CUMULATIVE_COMMITMENT = "cumulativeCommitment";

    public static final String CUMULATIVE_DISBURSEMENT = "cumulativeDisbursement";

    public static final String CUMULATIVE_EXECUTION_RATE = "cumulativeExecutionRate";

    public static final String PROJECT_PERIOD = "projectPeriod";

    public static final String OVERAGE = "overage";

    public static final String PERCENTAGE_DISBURSEMENT = "percentageDisbursements";

    public static final String AVERAGE_SIZE_DISBURSEMENT = "averageSizeofDisbursements";

    public static final String AVERAGE_DISBURSEMENT_RATE = "averageDisbursementRate";

    public static final String PROJECT_AGE_RATIO = "projectAgeRatio";

    public static final String COUNT_ACTUAL_COMMITMENT = "countsActualCommitments";

    public static final String COUNT_ACTUAL_DISBURSEMENT = "countActualDisbursment";

    public static final String COUNT_PLANNED_COMMITMENT = "countsPlannedCommitments";

    public static final String COUNT_PLANNED_DISBURSEMENT = "countPlannedDisbursment";

    public static final String UNDISBURSED_CUMULATIVE_BALANCE = "undisbursedCumulativeBalance";

    public static final String UNCOMMITED_CUMULATIVE_BALANCE = "uncommitedCumulativeBalance";

    public static final String UNDISBURSED_BALANCE = "undisbursedBalance";

    public static final String UNCOMMITED_BALANCE = "uncommitedBalance";

    public static final String NUMBER_OF_PROJECTS = "numberOfProjects";

    public static final String COSTING_GRAND_TOTAL = "grandTotalCost";

    public static final String EXECUTION_RATE = "executionRate";

    /** NIGER COLUMNS */

    public static final String PRIOR_ACTUAL_DISBURSEMENT = "priorActualDisbursements";

    public static final String CURRENT_MONTH_DISBURSEMENT = "currentMonthDisbursements";

    public static final String CUMULATED_DISBURSEMENT = "cumulatedDisbursements";

    public static final String CONSUMPTION_RATE = "consumptionRate";

    public static final String SELECTED_YEAR_PLANNED_DISBURSEMENT = "selectedYearPlannedDisbursement";

    /***
     * Pledge Columns
     */
    public static final String PERCENTAGE_OF_DISBURSEMENT= "percentageOfDisbursement";

    public static final String PERCENTAGE_OF_TOTAL_COMMITMENTS = "percentageOfTotalCommitments";

    public static final String DISBURSEMENT_RATION = "disbursmentRatio";

    private static Hashtable<String, MathExpression> expresions = new Hashtable<String, MathExpression>();

    /**
     * Create all expressions
     */

    static {
        // Build all expression
        buildOverageProjects();
        buildAgeOfProject();
        buildCalculatedProjectLife();
        buildPredictabilityOfFunding();
        buildAverageSizeofProjects();
        buildActualDisbursementVariance();
        buildActualCommitmentsVariance();
        buildCumulativeCommitment();
        buildCumulativeDisbursement();
        buildCumulativeExecutionRate();
        buildProjectPeriod();
        buildOverage();
        buildPercentageDisbursement();
        buildCountActualCommitments();
        buildCountActualDisbursement();
        buildCountPlannedCommitments();
        buildCountPlannedDisbursement();
        buildAverageSizeofDisbursements();

        buildProjectAgeRatio();
        buildUndisbursedCumulativeBalance();
        buildUncommitedCumulativeBalance();
        buildNumberOfProject();
        buildMtefColumn();
        buildCostingGrandTotal();
        buildExecutionRate();
        buildAverageDisbursementRate();

        buildUncommitedBalance();
        buildUndisbursedBalance();

        /** Niger Columns **/
        buildPriorActualDisbursements();
        buildCurrentMonthDisbursements();
        buildCumulatedDisbursements();
        buildConsumptionRate();
        buildSelectdYearOfPlannedDisbursements();
        
        buildPercentageOfDisbursement();
        buildPercentageOfTotalCommitments();

        buildDisbursementRatio();//AMP-15581
    }

    private static void buildDisbursementRatio() {
        try {
            //MathExpression m1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.PLANNED_DISBURSEMENT, ArConstants.TOTAL_ACTUAL_DISBURSEMENT);
            MathExpression m1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT_FILTERED, ArConstants.TOTAL_ACTUAL_DISBURSEMENT);
            MathExpression m2 = new MathExpression(MathExpression.Operation.MULTIPLY, m1, new BigDecimal(100));
            expresions.put(DISBURSEMENT_RATION, m2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * buildPledgesPercentageOfDisbursement (Total disbursed by year/quarter/month)/(TOTAL_ACTUAL_DISBURSEMENT)*100 
     */
    private static void buildPercentageOfDisbursement() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT_FILTERED, ArConstants.TOTAL_ACTUAL_DISBURSEMENT);
            MathExpression m2 = new MathExpression(MathExpression.Operation.MULTIPLY, m1, new BigDecimal(100));
            expresions.put(PERCENTAGE_OF_DISBURSEMENT, m2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * buildPercentageOfTotalCommitments (Total disbursed by year/quarter/month)/(TOTAL_ACTUAL_DISBURSEMENT)*100 
     */
    private static void buildPercentageOfTotalCommitments() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_COMMITMENT_FILTERED, new BigDecimal(100));
            MathExpression m2 = new MathExpression(MathExpression.Operation.DIVIDE_ROUND_TWO_DECIMALS, m1, ArConstants.TOTAL_ACTUAL_COMMITMENT);
            expresions.put(PERCENTAGE_OF_TOTAL_COMMITMENTS, m2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }       
    
    /**
     * buildOverageProjects Current Date - Proposed Completion
     */
    private static void buildOverageProjects() {
        try {
            // subtract PROPOSED_COMPLETION_DATE_VALUE to CURRENT_DATE_VALUE
            MathExpression oper = new MathExpression(MathExpression.Operation.DATE_MONTH_DIFF, ArConstants.CURRENT_DATE_VALUE, ArConstants.PROPOSED_COMPLETION_DATE_VALUE);
            expresions.put(OVERAGE_PROJECT, oper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
            logger.error(e.getMessage(), e);
        }

    }
    
    /**
     * calculatedProjectLife (Difference between Planned Start Date AND Original Completion Date) 
     */
    private static void buildCalculatedProjectLife() {
        try {
            MathExpression oper = new MathExpression(MathExpression.Operation.DATE_DAY_DIFF, ArConstants.ORIGINAL_COMPLETION_DATE_VALUE, ArConstants.PROPOSED_START_DATE_VALUE);
            expresions.put(CALCULATED_PROJECT_LIFE, oper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }           
    }

    /**
     * buildPredictabilityOfFunding ((Actual Disbursements - planned
     * Disbursements)/planned disbursements) X 100
     */
    private static void buildPredictabilityOfFunding() {
        try {
            MathExpression substractActualPlanned = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.PLANNED_DISBURSEMENT, ArConstants.ACTUAL_DISBURSEMENT);
            MathExpression divideOper1ByPLanned = new MathExpression(MathExpression.Operation.DIVIDE, substractActualPlanned, ArConstants.PLANNED_DISBURSEMENT);
            MathExpression multiResultPannedBy100 = new MathExpression(MathExpression.Operation.MULTIPLY, divideOper1ByPLanned, new BigDecimal(100));
            expresions.put(PREDICTABILITY_OF_FUNDING, multiResultPannedBy100);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
            logger.error(e.getMessage(), e);
        }
    }

    // variances
    /**
     * Actual Commitments Variance MAX_ACTUAL_COMMITMENT - MIN_ACTUAL_COMMITMENT
     */
    private static void buildActualCommitmentsVariance() {
        try {
            MathExpression variance = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.MAX_ACTUAL_COMMITMENT, ArConstants.MIN_ACTUAL_COMMITMENT);
            expresions.put(VARIANCE_ACTUAL_COMMITMENTS, variance);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Actual Disbursement Variance MAX_ACTUAL_DISBURSEMENT -
     * MIN_ACTUAL_DISBURSEMENT
     */
    private static void buildActualDisbursementVariance() {
        try {
            MathExpression variance = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.MAX_ACTUAL_DISBURSEMENT, ArConstants.MIN_ACTUAL_DISBURSEMENT);
            expresions.put(VARIANCE_ACTUAL_DISBURSEMENTS, variance);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Cumulative Commitment Sum of Actual Commitment only affected by the
     * hierarchy percentage and filters percentage
     */
    private static void buildCumulativeCommitment() {
        try {
            MathExpression variance = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_COMMITMENT, new BigDecimal(1));
            expresions.put(CUMULATIVE_COMMITMENT, variance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Cumulative Disbursement Sum of Actual Disbursement only affected by the
     * hierarchy percentage and filters percentage
     */
    private static void buildCumulativeDisbursement() {
        try {
            MathExpression variance = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_DISBURSEMENT, new BigDecimal(1));
            expresions.put(CUMULATIVE_DISBURSEMENT, variance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Cumulative Execution Rate = (Cumulative Disbursement / Cumulative
     * Commitment) * 100
     */
    private static void buildCumulativeExecutionRate() {
        try {
            MathExpression divideDisbursementByCommitment = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT, ArConstants.ACTUAL_COMMITMENT);
            MathExpression multiplyBy100 = new MathExpression(MathExpression.Operation.MULTIPLY, divideDisbursementByCommitment, new BigDecimal(100));
            expresions.put(CUMULATIVE_EXECUTION_RATE, multiplyBy100);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Project Period (months) = Proposed Completion Date - Actual Start date
     */
    private static void buildProjectPeriod() {
        try {
            MathExpression dateDiff = new MathExpression(MathExpression.Operation.DATE_MONTH_DIFF, ArConstants.PROPOSED_COMPLETION_DATE_VALUE, ArConstants.ACTUAL_START_DATE_VALUE);
            expresions.put(PROJECT_PERIOD, dateDiff);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Overage (months) = Age of project - Project period
     */
    private static void buildOverage() {
        try {
            MathExpression subsract = new MathExpression(MathExpression.Operation.SUBTRACT, expresions.get(AGE_OF_PROJECT), expresions.get(PROJECT_PERIOD));
            expresions.put(OVERAGE, subsract);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Percentage Of Total Disbursements (Actual Disbursement affected by all
     * filters / Actual Disbursement o affected by percentages
     */
    private static void buildPercentageDisbursement() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT_FILTERED, ArConstants.TOTAL_ACTUAL_DISBURSEMENT);
            MathExpression x2 = new MathExpression(MathExpression.Operation.MULTIPLY, x1, new BigDecimal(100d));
            expresions.put(PERCENTAGE_DISBURSEMENT, x2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Count Actual Commitments Number of Actual Commitments for the current
     * activity
     */
    private static void buildCountActualCommitments() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_COMMITMENT_COUNT, new BigDecimal(1));
            expresions.put(COUNT_ACTUAL_COMMITMENT, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Count Planned Commitments Number of Planned Commitment for the current
     * activity
     */
    private static void buildCountPlannedCommitments() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.PLANNED_COMMITMENT_COUNT, new BigDecimal(1));

            expresions.put(COUNT_PLANNED_COMMITMENT, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Count Planned Disbursement Number of Planned Disbursement for the current
     * activity
     */
    private static void buildCountPlannedDisbursement() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.PLANNED_DISBURSEMENT_COUNT, new BigDecimal(1));

            expresions.put(COUNT_PLANNED_DISBURSEMENT, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Count Actual Disbursement Number of Actual Disbursement for the current
     * activity
     */
    private static void buildCountActualDisbursement() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.ACTUAL_DISBURSEMENT_COUNT, new BigDecimal(1));

            expresions.put(COUNT_ACTUAL_DISBURSEMENT, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Average Size of Disbursements Cumulative Disbursements / Count Actual
     * Disbursement
     */
    private static void buildAverageSizeofDisbursements() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT, ArConstants.ACTUAL_DISBURSEMENT_COUNT);
            expresions.put(AVERAGE_SIZE_DISBURSEMENT, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Project Age Ratio = Age of project / Project Period
     */
    private static void buildProjectAgeRatio() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.DIVIDE, expresions.get(AGE_OF_PROJECT), expresions.get(PROJECT_PERIOD));
            expresions.put(PROJECT_AGE_RATIO, x1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Average Disbursement Cumulative Disb/Cumulative Commit * 100 / Number of
     * Activities (filtered)
     */
    private static void buildAverageDisbursementRate() {
        try {
            MathExpression x1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT, ArConstants.ACTUAL_COMMITMENT);
            MathExpression x2 = new MathExpression(MathExpression.Operation.MULTIPLY, x1, new BigDecimal(100));
            MathExpression x3 = new MathExpression(MathExpression.Operation.DIVIDE, x2, ArConstants.COUNT_PROJECTS);
            expresions.put(AVERAGE_DISBURSEMENT_RATE, x3);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Undisbursed Cumulative Balance = Cumulative Commitment - Cumulative
     * Disbursement
     */
    private static void buildUndisbursedCumulativeBalance() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.ACTUAL_COMMITMENT, ArConstants.ACTUAL_DISBURSEMENT);
            expresions.put(UNDISBURSED_CUMULATIVE_BALANCE, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Proposed project cost - Cummalative Commitments
     */
    private static void buildUncommitedCumulativeBalance() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.PROPOSED_COST, ArConstants.ACTUAL_COMMITMENT);
            expresions.put(UNCOMMITED_CUMULATIVE_BALANCE, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Undisbursed Balance = Actual Commitments (depending on filter) - Actual
     * Disbursements (dependent on filter).
     * 
     */
    private static void buildUndisbursedBalance() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.ACTUAL_COMMITMENT_FILTERED, ArConstants.ACTUAL_DISBURSEMENT_FILTERED);
            expresions.put(UNDISBURSED_BALANCE, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Uncommitted Balance = Proposed project cost - Actual Commitments
     * (dependent on the filter)
     * 
     */
    private static void buildUncommitedBalance() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.SUBTRACT, ArConstants.PROPOSED_COST, ArConstants.ACTUAL_COMMITMENT_FILTERED);
            expresions.put(UNCOMMITED_BALANCE, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Number Of Projects = Count Of Activities under the current hierarchy
     */
    private static void buildNumberOfProject() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.COUNT_PROJECTS, new BigDecimal(1));
            expresions.put(NUMBER_OF_PROJECTS, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Costing Grand Total value
     */
    private static void buildCostingGrandTotal() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.COSTING_GRAND_TOTAL, new BigDecimal(1));
            expresions.put(COSTING_GRAND_TOTAL, m1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
        
    /**
     * Costing MTEF column
     */
    public static void buildMtefColumn() {
        try {
            List<AmpColumns> mtefCols = DynamicColumnsUtil.getAllMtefColumns();
            if ( mtefCols != null  ) {
                for (AmpColumns col: mtefCols ) {
                    String colName      = col.getColumnName();
                    MathExpression m1   = new MathExpression(MathExpression.Operation.MULTIPLY, colName, new BigDecimal(1));
                    expresions.put(colName, m1);
                }
            }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * (ACTUAL_DISBURSEMENT / PLANNED_DISBURSEMENT_FILTERED) * 100
     */
    private static void buildExecutionRate() {
        try {
            MathExpression m1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.ACTUAL_DISBURSEMENT, ArConstants.PLANNED_DISBURSEMENT_FILTERED);
            MathExpression m2 = new MathExpression(MathExpression.Operation.MULTIPLY, m1, new BigDecimal(100));
            expresions.put(EXECUTION_RATE, m2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Sum of Actual Disbursements of the current year (Not including the
     * current month)
     */
    private static void buildPriorActualDisbursements() {
        try {
            MathExpression m = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.TOTAL_PRIOR_ACTUAL_DISBURSEMENT, new BigDecimal(1));
            expresions.put(PRIOR_ACTUAL_DISBURSEMENT, m);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Sum of Actual Disbursements of the current month
     */
    private static void buildCurrentMonthDisbursements() {
        try {
            MathExpression m = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH, new BigDecimal(1));
            expresions.put(CURRENT_MONTH_DISBURSEMENT, m);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Cumulated Disbursements: Prior Actual Disbursements + Current Month
     * Disbursements
     */
    private static void buildCumulatedDisbursements() {
        try {

            MathExpression m = new MathExpression(MathExpression.Operation.ADD, ArConstants.TOTAL_PRIOR_ACTUAL_DISBURSEMENT, ArConstants.TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH);
            expresions.put(CUMULATED_DISBURSEMENT, m);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     *Consumption Rate (Cumulated Disbursements of Selected year / Selected Year of Planned Disbursements) * 100
     */
    private static void buildConsumptionRate() {
        try {

            MathExpression m1 = new MathExpression(MathExpression.Operation.DIVIDE, ArConstants.CUMULATED_DISBURSEMENT_SELECTED_YEAR, ArConstants.TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR);
            MathExpression m2 = new MathExpression(MathExpression.Operation.MULTIPLY, m1, new BigDecimal(100));
            expresions.put(CONSUMPTION_RATE, m2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Current Year Planned Disbursements
     */
    private static void buildSelectdYearOfPlannedDisbursements() {
        try {

            MathExpression m = new MathExpression(MathExpression.Operation.MULTIPLY, ArConstants.TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR, new BigDecimal(1));
            expresions.put(SELECTED_YEAR_PLANNED_DISBURSEMENT, m);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * Get The expression by Key
     * 
     * @param key
     * @return
     */
    public static MathExpression get(String key) {
        if (expresions.get(key) == null) {
            logger.error("Invalid Expression Key :" + key);
        }
        return expresions.get(key);
    }
}
