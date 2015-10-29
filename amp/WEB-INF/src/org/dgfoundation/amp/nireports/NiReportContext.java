package org.dgfoundation.amp.nireports;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportContext {
	public final NiFilters filters;
	public final CurrencyConvertor currencyConvertor;
	public final Map<String, CellColumn> fetchedColumns = new LinkedHashMap<>();
	public final Map<String, CellColumn> fetchedMeasures = new LinkedHashMap<>();
	public List<CategAmountCell> funding;
	
	public NiReportContext(NiFilters filters, CurrencyConvertor currencyConvertor) {
		this.filters = filters;
		this.currencyConvertor = currencyConvertor;
	}
}
