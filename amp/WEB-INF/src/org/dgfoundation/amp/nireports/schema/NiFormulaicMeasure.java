package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.FormulaicAmountBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;

/**
 * a measure whose behaviour is fully defined by a formula and flags governing its totals strategy
 * //TODO: work in progress now
 * @author Dolghier Constantin
 *
 */
public class NiFormulaicMeasure extends NiReportMeasure<CategAmountCell> {

	public NiFormulaicMeasure(String measName, String description, Map<String, NiReportMeasure<CategAmountCell>> depMeas, NiFormula formula) {
		super(measName, buildBehaviour(formula), description);
	}
	
	static FormulaicAmountBehaviour buildBehaviour(NiFormula formula) {
		return null;
	}
	
	@Override
	public Set<String> getPrecursorMeasures() {
		return null;
	}

	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		// TODO Auto-generated method stub
		return null;
	}
}
