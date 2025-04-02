package org.dgfoundation.amp.nireports.amp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.ForecastExecutionRateBehaviour;
import org.dgfoundation.amp.nireports.amp.MtefColumn;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiFormulaicMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;

/**
 * the forecast execution rate measure
 * @author Dolghier Constantin
 *
 */
public class ForecastExecutionRateMeasure extends NiReportMeasure<CategAmountCell> {
    
    public ForecastExecutionRateMeasure(String measName) {
        super(measName, ForecastExecutionRateBehaviour.instance, AmpReportsSchema.measureDescriptions.get(measName));
    }
    

    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
        AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
        
        List<CategAmountCell> res = new ArrayList<>();
        res.addAll(
            engine.fetchedMeasures.get(MeasureConstants.ACTUAL_DISBURSEMENTS).getLinearData().stream().map(
                z -> stampCell(z, MeasureConstants.ACTUAL_DISBURSEMENTS)
            ).collect(Collectors.toList()));
        
        engine.timer.run("fetch_mtef", () -> {
            fetchAll(res, engine, "pipe", schema.pipelineMtefColumns);
            fetchAll(res, engine, "proj", schema.projectionMtefColumns);
        });
        return res;
    }

    protected void fetchAll(List<CategAmountCell> res, NiReportsEngine engine, String prefix, Map<Integer, MtefColumn> cols) {
        cols.forEach((year, col) -> {
            String stamp = String.format("%s%d", prefix, year);
            engine.timer.run(col.name, () -> 
                res.addAll(col.fetch(engine).stream().map(z -> z.withMeta(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE, stamp)).collect(Collectors.toList())));
        });
    }
        
    protected CategAmountCell stampCell(NiCell niCell, String tagValue) {
        return ((CategAmountCell) niCell.getCell()).withMeta(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE, tagValue);
    }
    
    @Override
    public Map<String, Boolean> getPrecursorMeasures() {
        return AmpReportsSchema.singletonMap(MeasureConstants.ACTUAL_DISBURSEMENTS, false);
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
