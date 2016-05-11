package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiLinearCombinationTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractReportsSchema implements NiReportsSchema {
	protected Map<String, NiReportColumn<?>> columns = new HashMap<>();
	protected Map<String, NiReportMeasure<?>> measures = new HashMap<>();
	
	@Override
	public Map<String, NiReportColumn<?>> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	@Override
	public Map<String, NiReportMeasure<?>> getMeasures() {
		return Collections.unmodifiableMap(measures);
	}
		
	public AbstractReportsSchema addColumn(NiReportColumn<?> col) {
		failIf(columns.containsKey(col.name), "double definition of column with name " + col.name);
		failIf(col.getBehaviour() == null, "no behaviour specified for column with name " + col.name);
		columns.put(col.name, col);
		return this;
	}
	
	public AbstractReportsSchema addMeasure(NiReportMeasure<?> meas) {
		failIf(measures.containsKey(meas.name), "double definition of measure with name " + meas.name);
		failIf(meas.getBehaviour() == null, "no behaviour specified for measure with name " + meas.name);
		measures.put(meas.name, meas);
		return this;
	}

	/**
	 * accepts an array of (measureName, Number)
	 * @param def
	 * @return
	 */
	public AbstractReportsSchema addLinearFilterMeasure(String compMeasureName, String description, Behaviour<?> behaviour, Object...def) {
		failIf(def.length % 2 != 0, "you should supply an even number of arguments");
		Map<NiTransactionMeasure, BigDecimal> defMap = parseMap(String.format("while defining measure %s", compMeasureName), def);
		return addMeasure(new NiLinearCombinationTransactionMeasure(compMeasureName, defMap, behaviour, description));
	}
	
	public Map<NiTransactionMeasure, BigDecimal> parseMap(String errPrefix, Object...def) {
		Map<NiTransactionMeasure, BigDecimal> res = new HashMap<>();
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
}
