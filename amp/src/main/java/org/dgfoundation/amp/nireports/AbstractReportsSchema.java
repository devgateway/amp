package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.amp.SubDimensions;
import org.dgfoundation.amp.nireports.behaviours.MeasureDividingBehaviour;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.schema.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * an abstract {@link NiReportsSchema} which contains utility methods for defining a schema in a coder-friendly way while ensuring reasonable safeguards for correctness.
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractReportsSchema implements NiReportsSchema {
    protected Map<String, NiReportColumn<?>> columns = new HashMap<>();
    protected Map<String, NiReportMeasure<?>> measures = new HashMap<>();
    
    @Override
    public Map<String, NiReportColumn<? extends Cell>> getColumns() {
        return Collections.unmodifiableMap(columns);
    }
    
    @Override
    public Map<String, NiReportMeasure<?>> getMeasures() {
        return Collections.unmodifiableMap(measures);
    }
    
    /**
     * adds a column definition to {@link #columns}, checking that no other column with the same name has been previously defined
     * @param col the column to add to the schema
     * @return
     */
    public AbstractReportsSchema addColumn(NiReportColumn<?> col) {
        failIf(columns.containsKey(col.name), "double definition of column with name " + col.name);
        failIf(col.getBehaviour() == null, "no behaviour specified for column with name " + col.name);
        columns.put(col.name, col);
        return this;
    }
    
    /**
     * adds a measure definition to {@link #measures}, checking that no other measure with the same name has been previously defined
     * @param meas
     * @return
     */
    public AbstractReportsSchema addMeasure(NiReportMeasure<?> meas) {
        failIf(measures.containsKey(meas.name), "double definition of measure with name " + meas.name);
        failIf(meas.getBehaviour() == null, "no behaviour specified for measure with name " + meas.name);
        measures.put(meas.name, meas);
        return this;
    }
    
    /**
     * constructs a {@link NiCombinationContextTransactionMeasure} measure and then adds it to the schema by using {@link #addMeasure(NiReportMeasure)}  
     * @param compMeasureName the name of the measure to construct
     * @param description the description of the measure to construct
     * @param behaviour the behaviour of the measure to construct
     * @param def an even-length array. Each pair is a (measureName, {@link Number}) tuple. The <i>measureName</i> should reference an already-defined measure of type {@link NiTransactionContextMeasure}.
     * @see NiCombinationContextTransactionMeasure for information on the measure built and inserted by this function
     * @return
     */
    public AbstractReportsSchema addDerivedLinearFilterMeasure(String compMeasureName, String description, Behaviour<?> behaviour, Object...def) {
        failIf(def.length % 2 != 0, "you should supply an even number of arguments");
        @SuppressWarnings("rawtypes")
        Map<NiTransactionContextMeasure, BigDecimal> defMap = parseContextFilterMap(String.format("while defining measure %s", compMeasureName), def);
        return addMeasure(new NiCombinationContextTransactionMeasure(compMeasureName, defMap, behaviour, description));
    }

    /**
     * constructs a {@link NiLinearCombinationTransactionMeasure} measure and then adds it to the schema by using {@link #addMeasure(NiReportMeasure)}
     * @param compMeasureName the name of the measure to construct
     * @param description the description of the measure to construct
     * @param behaviour the behaviour of the measure to construct
     * @param ignoreFilters whether the constructed measure should be based off filtered or unfiltered funding cells. See {@link NiPredicateTransactionMeasure#ignoreFilters}
     * @param stripCoords whether to strip coordinates off the generated cells
     * @param def def an even-length array. Each pair is a (measureName, {@link Number}) tuple. The <i>measureName</i> should reference an already-defined measure of type {@link NiTransactionMeasure}.
     * @return
     */
    public AbstractReportsSchema addLinearFilterMeasure(String compMeasureName, String description, Behaviour<?> behaviour, 
            boolean ignoreFilters, boolean stripCoords, Object...def) {
        failIf(def.length % 2 != 0, "you should supply an even number of arguments");
        Map<NiTransactionMeasure, BigDecimal> defMap = parseMap(String.format("while defining measure %s", compMeasureName), def);
        return addMeasure(new NiLinearCombinationTransactionMeasure(compMeasureName, defMap, behaviour, ignoreFilters, stripCoords, description));
    }

    /**
     * <p>Use this method to add computed measures for which scaling does not apply.</p>
     * See also {@link AbstractReportsSchema#addFormulaComputedMeasure(String, String, NiFormula, boolean, boolean)}.
     */
    public AbstractReportsSchema addFormulaComputedMeasure(String compMeasureName, String description, NiFormula formula, boolean average) {
        return addFormulaComputedMeasure(compMeasureName, description, formula, average, false);
    }
    
    public AbstractReportsSchema addFormulaComputedMeasure(String compMeasureName, String description, NiFormula formula, boolean average, TimeRange timeRange) {
        return addFormulaComputedMeasure(compMeasureName, description, formula, average, false, timeRange);
    }
    
    /**
     * constructs a measure based off a formula and then adds it to the schema by using {@link #addMeasure(NiReportMeasure)}. The measure would be of one of {@link NiFormulaicMeasure} or {@link NiFormulaicAverageMeasure}
     * @param compMeasureName the name of the measure to construct
     * @param description the description of the measure to construct
     * @param formula the formula to drive the cells
     * @param average whether to create a {@link NiFormulaicAverageMeasure} (e.g. populate trail cells based off the average of a formula across cells) or a {@link NiFormulaicMeasure} (e.g. populate body and trail cells based off the value of the formula)
     * @param isScalableByUnits are cells created by this measure to be scaled or not
     * @return
     */
    public AbstractReportsSchema addFormulaComputedMeasure(String compMeasureName, String description, NiFormula formula, boolean average, boolean isScalableByUnits, TimeRange timeRange) {
        Map<String, NiReportMeasure<CategAmountCell>> depMeas = new HashMap<>();
        for(String measName:formula.getDependencies()) {
            NiReportMeasure<CategAmountCell> meas = (NiReportMeasure) measures.get(measName);
            failIf(meas == null, () -> String.format("measure <%s> defined as dependency of measure <%s> does not exist", measName, compMeasureName));
            depMeas.put(measName, meas);
        }
        NiReportMeasure<CategAmountCell> res;
        if (average)
            res = new NiFormulaicAverageMeasure(compMeasureName, description, depMeas, formula, true, isScalableByUnits, timeRange);
        else
            res = new NiFormulaicMeasure(compMeasureName, description, depMeas, formula, isScalableByUnits, timeRange);
        return addMeasure(res);
        //return addMeasure(meas)
    }
    
    public AbstractReportsSchema addFormulaComputedMeasure(String compMeasureName, String description, NiFormula formula, boolean average, boolean isScalableByUnits) {
        return addFormulaComputedMeasure(compMeasureName, description, formula, average, isScalableByUnits, TimeRange.NONE);
    }
    
    /**
     * parses an even-length array of ({@link NiTransactionContextMeasure}, {@link Number}) elements into a Map from measure to BigDecimal. Normally used for building linear-combination measures
     * @param errPrefix the prefix of the error message in the exception to the generated in case of any error
     * @param def the even-length array
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map<NiTransactionContextMeasure, BigDecimal> parseContextFilterMap(String errPrefix, Object...def) {
        Map<NiTransactionContextMeasure, BigDecimal> res = new IdentityHashMap<>();
        for(int i = 0; i < def.length / 2; i++) {
            String measureName = (String) def[i * 2];
            Number factor = (Number) def[i * 2 + 1];
            
            NiReportMeasure meas = (NiTransactionContextMeasure) getMeasures().get(measureName);
            failIf(meas == null, () -> String.format("%s: measure %s not found in the schema", errPrefix, measureName));
            res.put((NiTransactionContextMeasure) meas, toBigDecimal(factor));
        }
        return res;
    }
    
    /**
     * parses an even-length array of ({@link NiTransactionMeasure}, {@link Number}) elements into a Map from measure to BigDecimal. Normally used for building linear-combination measures
     * @param errPrefix the prefix of the error message in the exception to the generated in case of any error
     * @param def the even-length array
     * @return
     */
    public Map<NiTransactionMeasure, BigDecimal> parseMap(String errPrefix, Object...def) {
        Map<NiTransactionMeasure, BigDecimal> res = new IdentityHashMap<>();
        for(int i = 0; i < def.length / 2; i++) {
            String measureName = (String) def[i * 2];
            Number factor = (Number) def[i * 2 + 1];
            
            NiTransactionMeasure meas = (NiTransactionMeasure) getMeasures().get(measureName);
            failIf(meas == null, () -> String.format("%s: measure %s not found in the schema", errPrefix, measureName));
            res.put(meas, toBigDecimal(factor));
        }
        return res;
    }
    
    /**
     * builds a numerical column behaviour which is trivial in all regards except that it divides its output by the per-report total of a measure
     * @param tr the time range of the generated behaviour
     * @param measureName the measure by whose total the results should be divided. It is up to the schema to ensure that this measure has been previously fetched
     * @return
     */
    public TrivialMeasureBehaviour byMeasureDividingBehaviour(TimeRange tr, String measureName) {
        return new MeasureDividingBehaviour(tr, TrivialMeasureBehaviour.buildMeasureTotalDivider(measureName), false);
    }
    
    /**
     * builds a single-entry map
     * @param k
     * @param v
     * @return
     */
    public static Map<String, Boolean> singletonMap(String k, Boolean v) {
        Map<String, Boolean> res = new HashMap<>();
        res.put(k, v);
        return res;
    }
    
    /**
     * converts a numeric value into BigDecimal
     * @param n
     * @return
     */
    public static BigDecimal toBigDecimal(Number n) {
        if (n instanceof BigDecimal)
            return ((BigDecimal) n);
        
        if (n instanceof Integer || n instanceof Long)
            return BigDecimal.valueOf(n.longValue());
        
        if (n instanceof Double || n instanceof Float)
            return BigDecimal.valueOf(n.doubleValue());
        
        throw new RuntimeException(String.format("cannot convert instances of class %s to BigDecimal", n.getClass().getName()));
    }

    @Override
    public SubDimensions getSubDimensions() {
        return null;
    }
}
