package org.dgfoundation.amp.nireports.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.FormulaicAmountBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;

/**
 * a measure whose behaviour is fully defined by a formula and flags governing its totals strategy
 * @author Dolghier Constantin
 *
 */
public class NiFormulaicMeasure extends NiReportMeasure<CategAmountCell> {

    /**
     * the category key for the metaset of the CategAmountCell instances passes through the {@link #fetch(NiReportsEngine)} method of this class
     */
    public static final String METAINFO_KEY_UNDERLYING_MEASURE = "underlying_measure";

    /**
     * the measures this measure's formula depends on, indexed by the respective tag
     */
    public final Map<String, NiReportMeasure<CategAmountCell>> depMeas;
    
    public NiFormulaicMeasure(String measName, String description, Map<String, NiReportMeasure<CategAmountCell>> depMeas, NiFormula formula, boolean isScalableByUnits) {
        this(measName, description, depMeas, formula, isScalableByUnits, TimeRange.NONE);
    }
    
    public NiFormulaicMeasure(String measName, String description, Map<String, NiReportMeasure<CategAmountCell>> depMeas, NiFormula formula, boolean isScalableByUnits, TimeRange timeRange) {
        super(measName, buildBehaviour(formula, isScalableByUnits, timeRange), description);
        this.depMeas = Collections.unmodifiableMap(new HashMap<>(depMeas));
    }
    
    static FormulaicAmountBehaviour buildBehaviour(NiFormula formula, boolean isScalableByUnits, TimeRange timeRange) {
        return new FormulaicAmountBehaviour(timeRange, 
            formula.getDependencies().stream().collect(Collectors.toMap(z -> z, z -> FormulaicAmountBehaviour::REDUCE_SUM)),
            null,
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
            List<CategAmountCell> umCells = depMeas.get(tag).fetch(engine).stream().map(z -> z.withMeta(METAINFO_KEY_UNDERLYING_MEASURE, tag)).collect(Collectors.toList());
            res.addAll(umCells);
        }
        return res;
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
