package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Arrays;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

public class NiReportsEngineForTesting extends NiReportsEngine {
	
	public final static ReportSpecification EMPTY_REPORT_SPEC = ReportSpecificationImpl.buildFor("dummy", new ArrayList<>(), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY);

	final ExceptionConsumer<NiReportsEngine> runnable;
	
	public NiReportsEngineForTesting(NiReportsSchema schema, CurrencyConvertor currencyConvertor, ExceptionConsumer<NiReportsEngine> runnable) {
		super(schema, currencyConvertor, EMPTY_REPORT_SPEC);
		this.runnable = runnable;
	}
	
	@Override
	protected void runReport() {
		try {runnable.accept(this);}
		catch(Exception e) {throw AlgoUtils.translateException(e);}
	}
}
