package org.dgfoundation.amp.nireports.output.nicells;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * a {@link NiAmountCell} subclass with some extra possibilities 
 * <ul>
 * <li>each cell holds a list of named values (see {@link #values})</li>
 * <li>holding <strong>undefined</strong> values, which is one of {@link #UNDEFINED}, {@link #PLUS_INFINITY}, {@link #MINUS_INFINITY}</li>
 * <li>cells are formatted potentially differently (see {@link CellVisitor#visit(NiFormulaicAmountCell, CellColumn)})</li>
 * </ul>
 * 
 * @author Dolghier Constantin
 *
 */
public class NiFormulaicAmountCell extends NiAmountCell {
    public final Map<String, BigDecimal> values;
    
    public final static BigDecimal UNDEFINED = new BigDecimal(0);
    public final static BigDecimal PLUS_INFINITY = new BigDecimal(0);
    public final static BigDecimal MINUS_INFINITY = new BigDecimal(0);
    
    public final static NiFormulaicAmountCell FORMULAIC_ZERO = new NiFormulaicAmountCell(Collections.emptyMap(), ZERO.amount, ZERO.precisionSetting, false);

    public NiFormulaicAmountCell(Map<String, BigDecimal> values, BigDecimal amount, NiPrecisionSetting precision, boolean isScalableByUnits) {
        super(amount, precision, isScalableByUnits);
        this.values = Collections.unmodifiableMap(values);
    }
    
    public boolean isDefined() {
        return isDefined(this.amount);
    }
    
    @Override
    public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
        return visitor.visit(this, niCellColumn);
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
