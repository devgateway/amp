package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsGenerator implements ReportExecutor {
	
	public final NiReportsSchema schema;
	public final CurrencyConvertor currencyConvertor;
	
	public NiReportsGenerator(NiReportsSchema schema, CurrencyConvertor currencyConvertor) {
		this.schema = schema;
		this.currencyConvertor = currencyConvertor;
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification report) {
		NiFilters filters = schema.getFiltersConverter().apply(report.getFilters());
		NiReportContext context = new NiReportContext(filters, currencyConvertor);
		return null;
	}
}
