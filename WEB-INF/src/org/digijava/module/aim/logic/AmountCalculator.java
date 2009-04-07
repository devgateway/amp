package org.digijava.module.aim.logic;

import java.math.BigDecimal;
import java.util.Set;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface AmountCalculator {

	BigDecimal calculateAmount(Set<CategAmountCell> mergedCells);

}
