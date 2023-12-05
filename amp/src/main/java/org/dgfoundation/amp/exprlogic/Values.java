package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.dyn.DynamicColumnsUtil;
import org.digijava.module.aim.dbentity.AmpColumns;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.dgfoundation.amp.exprlogic.TokenRepository.tokens;

public class Values extends HashMap<String, BigDecimal> {

    private Long ownerId;
    private static final long serialVersionUID = 1L;
    Set<Values> countValues = new HashSet<Values>();
    
    private List<AmpColumns> mtefCols   ;
    //private List<AmpMeasures> mtefMeas    ;

    public Values(Long ownerID) {
        this.ownerId = ownerID;
        this.mtefCols = DynamicColumnsUtil.getAllMtefColumns();
    }
    
    public Values() {
        this(null);
    }

    
    public void addValue(String key, BigDecimal value)
    {
        if (value == null)
            return;

        BigDecimal oldValue = this.get(key);
        BigDecimal newValue = oldValue == null ? value : oldValue.add(value);
        this.put(key, newValue);
    }

    
    public void addValue(String key, double value) {
        addValue(key, new BigDecimal(value));
    }

    
    public void incrementValue(String key) {
        addValue(key, 1d);
    }

    public void importValues(Viewable v)
    {
        if (!(v instanceof ColumnReportData))
            return;
        ColumnReportData crd = (ColumnReportData) v;
        this.put(ArConstants.TOTAL_ACTUAL_COMMITMENT, 
                new BigDecimal(crd.retrieveSubReportDataValue(ArConstants.TOTAL_ACTUAL_COMMITMENT)));
        this.put(ArConstants.TOTAL_ACTUAL_DISBURSEMENT, 
                new BigDecimal(crd.retrieveSubReportDataValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT)));
        this.addValue(ArConstants.TOTAL_PLANNED_COMMITMENT, 
                new BigDecimal(crd.retrieveSubReportDataValue(ArConstants.TOTAL_PLANNED_COMMITMENT)));
        this.addValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT, 
                new BigDecimal(crd.retrieveSubReportDataValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT)));
    }
    
    public static long nr_CTV_called = 0;
    public static long cumulative_CTV_sizes = 0;
    
    /**
     * 
     * @param values
     */
    public void collectTrailVariables(Values values)
    {
        nr_CTV_called ++;

        // keep the original cell values for futures uses

        BigDecimal ac = values.get(ArConstants.ACTUAL_COMMITMENT);
        BigDecimal ad = values.get(ArConstants.ACTUAL_DISBURSEMENT);
        BigDecimal pc = values.get(ArConstants.PLANNED_COMMITMENT);
        BigDecimal pd = values.get(ArConstants.PLANNED_DISBURSEMENT);

        this.addValue(ArConstants.ACTUAL_COMMITMENT, ac);
        this.addValue(ArConstants.ACTUAL_DISBURSEMENT, ad);
        this.addValue(ArConstants.PLANNED_COMMITMENT, ad);
        this.addValue(ArConstants.PLANNED_DISBURSEMENT, pd);
        this.addValue(ArConstants.TOTAL_ACTUAL_COMMITMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_PLANNED_COMMITMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.ACTUAL_COMMITMENT_FILTERED, values.get(ArConstants.ACTUAL_COMMITMENT_FILTERED));
        this.addValue(ArConstants.ACTUAL_DISBURSEMENT_FILTERED, values.get(ArConstants.ACTUAL_DISBURSEMENT_FILTERED));
        this.addValue(ArConstants.PLANNED_COMMITMENT_FILTERED, values.get(ArConstants.PLANNED_COMMITMENT_FILTERED));
        this.addValue(ArConstants.PLANNED_DISBURSEMENT_FILTERED, values.get(ArConstants.PLANNED_DISBURSEMENT_FILTERED));
        this.addValue(ArConstants.PROPOSED_COST, values.get(ArConstants.PROPOSED_COST));
        this.addValue(ArConstants.TOTAL_COMMITMENTS, values.get(ArConstants.TOTAL_COMMITMENTS));
        
        this.addValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH, values.get(ArConstants.TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH));
        this.addValue(ArConstants.TOTAL_PRIOR_ACTUAL_DISBURSEMENT, values.get(ArConstants.TOTAL_PRIOR_ACTUAL_DISBURSEMENT));
        this.addValue(ArConstants.PLEDGED_TOTAL, values.get(ArConstants.PLEDGED_TOTAL));
        
//      this.addValue(ArConstants.MTEF_COLUMN, values.get(ArConstants.MTEF_COLUMN) );
        if ( this.mtefCols != null )
            for (AmpColumns col: this.mtefCols ) {
                String mtefColName  = col.getColumnName();
                this.addValue(mtefColName, values.get(mtefColName) );
            }
//      if ( this.mtefMeas != null )
//          for (AmpMeasures meas: this.mtefMeas ) {
//              String mtefMeasName = meas.getMeasureName();
//              this.addValue(mtefMeasName, values.get(mtefMeasName) );
//          }

        this.setIfGreaterThan(ArConstants.MAX_ACTUAL_COMMITMENT, ac);
        this.setIfGreaterThan(ArConstants.MAX_ACTUAL_DISBURSEMENT, ad);
        this.setIfGreaterThan(ArConstants.MAX_PLANNED_DISBURSEMENT, pd);
        this.setIfGreaterThan(ArConstants.MAX_PLANNED_COMMITMENT, pc);
        this.setIfLessThan(ArConstants.MIN_ACTUAL_COMMITMENT, ac);
        this.setIfLessThan(ArConstants.MIN_ACTUAL_DISBURSEMENT, ad);
        this.setIfLessThan(ArConstants.MIN_PLANNED_COMMITMENT, pc);
        this.setIfLessThan(ArConstants.MIN_PLANNED_DISBURSEMENT, pd);

        cumulative_CTV_sizes += this.size();
        
        // make a list of counted variables to eliminante duplicate counting
        Values countValues = new Values(values.ownerId);
        countValues.put(ArConstants.ACTUAL_COMMITMENT_COUNT, values.get(ArConstants.ACTUAL_COMMITMENT_COUNT));
        countValues.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, values.get(ArConstants.ACTUAL_DISBURSEMENT_COUNT));
        countValues.put(ArConstants.PLANNED_COMMITMENT_COUNT, values.get(ArConstants.PLANNED_COMMITMENT_COUNT));
        countValues.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, values.get(ArConstants.PLANNED_DISBURSEMENT_COUNT));
        this.countValues.add(countValues);
    }

    /**
     * 
     * @param cell
     */
    public void collectCellVariables(CategAmountCell cell) {

        this.addValue(ArConstants.TOTAL_COMMITMENTS, tokens.get(TokenNames.TOTAL_COMMITMENTS).evaluate(cell));

        this.addValue(ArConstants.ACTUAL_COMMITMENT, tokens.get(TokenNames.ACTUAL_COMMITMENTS).evaluate(cell));
        this.addValue(ArConstants.ACTUAL_DISBURSEMENT, tokens.get(TokenNames.ACTUAL_DISBURSEMENT).evaluate(cell));

        this.addValue(ArConstants.PLANNED_COMMITMENT, tokens.get(TokenNames.PLANED_COMMITMENTS).evaluate(cell));
        this.addValue(ArConstants.PLANNED_DISBURSEMENT, tokens.get(TokenNames.PLANED_DISBURSEMENT).evaluate(cell));

        this.addValue(ArConstants.TOTAL_ACTUAL_COMMITMENT, tokens.get(TokenNames.ACTUAL_COMMITMENTS).evaluate(cell));
        this.addValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT, tokens.get(TokenNames.ACTUAL_DISBURSEMENT).evaluate(cell));

        this.addValue(ArConstants.TOTAL_PLANNED_COMMITMENT, tokens.get(TokenNames.PLANED_COMMITMENTS).evaluate(cell));
        this.addValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT, tokens.get(TokenNames.PLANED_DISBURSEMENT).evaluate(cell));
        //this.addValue(ArConstants.ACTUAL_PLEDGE_COMMITMENT, TokenRepository.buildPledgesCommitmentsLogicalToken().evaluateOriginalvalue(cell));
        
        // no filtered, affected by %
        this.addValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR, tokens.get(TokenNames.SELECTED_YEAR_PLANNED_DISBURSEMENTS).evaluate(cell));
        this.addValue(ArConstants.CUMULATED_DISBURSEMENT_SELECTED_YEAR, tokens.get(TokenNames.CUMULATED_DISBURSEMENTS).evaluate(cell));
        this.addValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH, tokens.get(TokenNames.CLOSED_MONTH_ACTUAL_DISBURSEMENTS).evaluate(cell));
        this.addValue(ArConstants.TOTAL_PRIOR_ACTUAL_DISBURSEMENT, tokens.get(TokenNames.PRIOR_ACTUAL_DISBURSEMENTS).evaluate(cell));

        this.addValue(ArConstants.PLEDGED_TOTAL, tokens.get(TokenNames.PLEDGES_TOTAL).evaluate(cell));
        this.addValue(ArConstants.TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT, tokens.get(TokenNames.TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT).evaluate(cell));
        
        if (cell.isShow()) {
            this.addValue(ArConstants.ACTUAL_COMMITMENT_FILTERED, tokens.get(TokenNames.ACTUAL_COMMITMENTS).evaluate(cell));
            this.addValue(ArConstants.ACTUAL_DISBURSEMENT_FILTERED, tokens.get(TokenNames.ACTUAL_DISBURSEMENT).evaluate(cell));
            this.addValue(ArConstants.PLANNED_COMMITMENT_FILTERED, tokens.get(TokenNames.PLANED_COMMITMENTS).evaluate(cell));
            this.addValue(ArConstants.PLANNED_DISBURSEMENT_FILTERED, tokens.get(TokenNames.PLANED_DISBURSEMENT).evaluate(cell));
        }

        if (cell.getMetaValueString(ArConstants.ADJUSTMENT_TYPE) != null) {
            if (cell.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.ACTUAL)) {
//              if (cell.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
//                  this.incrementValue(ArConstants.ACTUAL_COMMITMENT_COUNT);
//              }
                if (cell.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT) && cell.isNonDirectedTransaction(ArConstants.ACTUAL_COMMITMENTS)) {
                    this.incrementValue(ArConstants.ACTUAL_COMMITMENT_COUNT);
                }               
                if (cell.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT) && cell.isNonDirectedTransaction(ArConstants.ACTUAL_DISBURSEMENTS)) {
                    this.incrementValue(ArConstants.ACTUAL_DISBURSEMENT_COUNT);
                }
            }

            if (cell.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.PLANNED)) {
                if (cell.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
                    this.incrementValue(ArConstants.PLANNED_COMMITMENT_COUNT);
                }
                if (cell.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
                    this.incrementValue(ArConstants.PLANNED_DISBURSEMENT_COUNT);
                }
            }
        } else if (cell.existsMetaString(ArConstants.PROPOSED_COST)) {
            this.addValue(ArConstants.PROPOSED_COST, tokens.get(TokenNames.UNCOMMITTED).evaluate(cell)); // what? why was "buildUncommitted()" here?
        }
        
        //this.addValue(ArConstants.MTEF_COLUMN, TokenRepository.buildMtefColumnToken().evaluate(cell));
        if (this.mtefCols != null ) {
            for (AmpColumns col: this.mtefCols ) {
                String mtefColName = col.getAliasName();
                String yearStr = mtefColName.substring(mtefColName.length() - 4, mtefColName.length());
                Integer year = Integer.parseInt(yearStr);
                double evalResult = TokenRepository.buildMtefColumnToken(col.getColumnName(), year).evaluate(cell);
                
                this.addValue(col.getColumnName(), evalResult );
            }
        }
//      if ( this.mtefMeas != null )
//          for (AmpMeasures meas: this.mtefMeas ) {
//              String mtefExprName = meas.getExpression();
//              mtefExprName        = mtefExprName.replace("Measure ", "");
//              String yearStr      = mtefExprName.substring(mtefExprName.length()-4, mtefExprName.length() );
//              Integer year        = Integer.parseInt(yearStr)-1;
//              double evalResult   = TokenRepository.buildMtefColumnToken(mtefExprName, year).evaluate(cell);
//              this.addValue(meas.getMeasureName(), evalResult );
//          }

    }

    /**
     * 
     * @param values
     */
    public void mergeTrailValues(Values values) {

        this.countValues.addAll(values.countValues);
        setIfGreaterThan(ArConstants.MAX_ACTUAL_COMMITMENT, values.get(ArConstants.MAX_ACTUAL_COMMITMENT));
        setIfGreaterThan(ArConstants.MAX_ACTUAL_DISBURSEMENT, values.get(ArConstants.MAX_ACTUAL_DISBURSEMENT));
        setIfLessThan(ArConstants.MIN_ACTUAL_COMMITMENT, values.get(ArConstants.MIN_ACTUAL_COMMITMENT));
        setIfLessThan(ArConstants.MIN_ACTUAL_DISBURSEMENT, values.get(ArConstants.MIN_ACTUAL_DISBURSEMENT));
        this.addValue(ArConstants.ACTUAL_COMMITMENT, values.get(ArConstants.ACTUAL_COMMITMENT));
        this.addValue(ArConstants.ACTUAL_DISBURSEMENT, values.get(ArConstants.ACTUAL_DISBURSEMENT));
        this.addValue(ArConstants.PLANNED_COMMITMENT, values.get(ArConstants.PLANNED_COMMITMENT));
        this.addValue(ArConstants.PLANNED_DISBURSEMENT, values.get(ArConstants.PLANNED_DISBURSEMENT));
        this.addValue(ArConstants.TOTAL_COMMITMENTS, values.get(ArConstants.TOTAL_COMMITMENTS));
        this.addValue(ArConstants.TOTAL_ACTUAL_COMMITMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_ACTUAL_DISBURSEMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_PLANNED_COMMITMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.TOTAL_PLANNED_DISBURSEMENT, values.get(ArConstants.TOTAL_ACTUAL_COMMITMENT));
        this.addValue(ArConstants.ACTUAL_COMMITMENT_FILTERED, values.get(ArConstants.ACTUAL_COMMITMENT_FILTERED));
        this.addValue(ArConstants.ACTUAL_DISBURSEMENT_FILTERED, values.get(ArConstants.ACTUAL_DISBURSEMENT_FILTERED));
        this.addValue(ArConstants.PLANNED_COMMITMENT_FILTERED, values.get(ArConstants.PLANNED_COMMITMENT_FILTERED));
        this.addValue(ArConstants.PLANNED_DISBURSEMENT_FILTERED, values.get(ArConstants.PLANNED_DISBURSEMENT_FILTERED));
        this.addValue(ArConstants.PROPOSED_COST, values.get(ArConstants.PROPOSED_COST));

        if ( this.mtefCols != null )
            for (AmpColumns col: this.mtefCols ) {
                String mtefColName  = col.getColumnName();
                this.addValue(mtefColName, values.get(mtefColName) );
            }
        
//      if ( this.mtefMeas != null )
//          for (AmpMeasures meas: this.mtefMeas ) {
//              String mtefMeasName = meas.getMeasureName();
//              this.addValue(mtefMeasName, values.get(mtefMeasName) );
//          }
        
    }

    /**
     * 
     * @param key
     * @param value
     */
    private void setIfGreaterThan(String key, BigDecimal value) {
        if (value != null && value.doubleValue() != 0d) {
            if (this.containsKey(key)) {
                BigDecimal thisVal = this.get(key);
                if (thisVal.compareTo(value) < 0) {
                    this.put(key, value);
                }
            } else {
                this.put(key, value);
            }
        }
    }

    /**
     * 
     * @param key
     * @param value
     */
    private void setIfLessThan(String key, BigDecimal value) {
        if (value != null && value.doubleValue() != 0d) {
            if (value.doubleValue() != 0d) {
            }
            if (this.containsKey(key)) {
                BigDecimal thisVal = this.get(key);
                if (thisVal.compareTo(value) > 0) {
                    this.put(key, value);
                }
            } else {
                this.put(key, value);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        Values src = (Values) o;
        if (this.ownerId != null && src.ownerId != null) {
            return (this.ownerId == src.ownerId);
        }
        return false;
    }

    public void prepareCountValues() {
        if (countValues.size() > 0) {
            BigDecimal ac = new BigDecimal(0);
            BigDecimal ad = new BigDecimal(0);
            BigDecimal pc = new BigDecimal(0);
            BigDecimal pd = new BigDecimal(0);

            for (Values val : countValues) {
                if ((val.get(ArConstants.ACTUAL_COMMITMENT_COUNT) != null)) {
                    ac = ac.add(val.get(ArConstants.ACTUAL_COMMITMENT_COUNT));
                }
                if ((val.get(ArConstants.ACTUAL_DISBURSEMENT_COUNT) != null)) {
                    ad = ad.add(val.get(ArConstants.ACTUAL_DISBURSEMENT_COUNT));
                }

                if ((val.get(ArConstants.PLANNED_COMMITMENT_COUNT) != null)) {
                    pc = pc.add(val.get(ArConstants.PLANNED_COMMITMENT_COUNT));
                }

                if ((val.get(ArConstants.PLANNED_DISBURSEMENT_COUNT) != null)) {
                    pd = pd.add(val.get(ArConstants.PLANNED_DISBURSEMENT_COUNT));
                }
            }

            this.put(ArConstants.ACTUAL_COMMITMENT_COUNT, ac);
            this.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, ad);
            this.put(ArConstants.PLANNED_COMMITMENT_COUNT, pc);
            this.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, pd);
        }
    }

}
