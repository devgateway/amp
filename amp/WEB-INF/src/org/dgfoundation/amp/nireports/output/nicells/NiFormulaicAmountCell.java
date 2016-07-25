package org.dgfoundation.amp.nireports.output.nicells;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;

/**
 * a class which instantiates a NiAmountCell based on formulas
 * @author Dolghier Constantin
 *
 */
public class NiFormulaicAmountCell extends NiAmountCell {
	public final Map<String, BigDecimal> values;
	
	public final static BigDecimal UNDEFINED = new BigDecimal(0);
	public final static BigDecimal PLUS_INFINITY = new BigDecimal(0);
	public final static BigDecimal MINUS_INFINITY = new BigDecimal(0);
	
	public final static NiFormulaicAmountCell FORMULAIC_ZERO = new NiFormulaicAmountCell(Collections.emptyMap(), ZERO.amount, ZERO.precisionSetting); 
	
	public NiFormulaicAmountCell(Map<String, BigDecimal> values, BigDecimal amount, NiPrecisionSetting precision) {
		super(amount, precision);
		this.values = Collections.unmodifiableMap(values);
	}
	
	/**
	 * returns true iff a value not one of NiReports' special undefined values
	 * @param a
	 * @return
	 */
	public static boolean isDefined(BigDecimal a) {
		return a != null && a != UNDEFINED && a != PLUS_INFINITY && a != MINUS_INFINITY;
	}
}
