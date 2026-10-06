package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.FormulaAverageBehaviour;
import org.dgfoundation.amp.nireports.behaviours.FormulaicAmountBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;

import java.util.*;
import java.util.stream.Collectors;

/**
 * a measure whose behaviour is fully defined by a formula and flags governing its totals strategy; for trail cells it outputs the average value throughout the activities; for individual rows it displays the ER
 * closely related to {@link NiFormulaicMeasure} 
 * @author Dolghier Constantin
 *
 */
public class NiFormulaicAverageMeasure extends NiReportMeasure<CategAmountCell> {

    /**
     * the measures this measure's formula depends on, indexed by the respective tag
     */
    public final Map<String, NiReportMeasure<CategAmountCell>> depMeas;
    public final boolean displayIndividualValues;
    
    public NiFormulaicAverageMeasure(String measName, String description, Map<String, NiReportMeasure<CategAmountCell>> depMeas, NiFormula formula, boolean displayIndividualValues, boolean isScalableByUnits) {
        this(measName, description, depMeas, formula, displayIndividualValues, isScalableByUnits, TimeRange.NONE);
    }
    
    public NiFormulaicAverageMeasure(String measName, String description, Map<String, NiReportMeasure<CategAmountCell>> depMeas, NiFormula formula, boolean displayIndividualValues, boolean isScalableByUnits, TimeRange timeRange) {
        super(measName, buildBehaviour(formula, displayIndividualValues, isScalableByUnits, timeRange), description);
        this.depMeas = Collections.unmodifiableMap(new HashMap<>(depMeas));
        this.displayIndividualValues = displayIndividualValues;
    }
    
    static FormulaAverageBehaviour buildBehaviour(NiFormula formula, boolean displayIndividualValues, boolean isScalableByUnits, TimeRange timeRange) {
        return new FormulaAverageBehaviour(timeRange, 
            formula.getDependencies().stream().collect(Collectors.toMap(z -> z, z -> FormulaicAmountBehaviour::REDUCE_SUM)),
            displayIndividualValues,
            formula,
            isScalableByUnits);
    }

    /**
     * fetches the cells using the fetcher of each of the dependencies. Each fetched cell is tagged (using {@link CategAmountCell#withMeta(String, Object)}) with its configured tag.
     * Then a list contained all the cells fetched by all the dependencies is returned
     */
    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
        List<CategAmountCell> res = new ArrayList<>();
        for(String tag:depMeas.keySet()) {
            List<CategAmountCell> umCells = depMeas.get(tag).fetch(engine).stream().map(z -> z.withMeta(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE, tag)).collect(Collectors.toList());
            res.addAll(umCells);
        }
        return res;
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
