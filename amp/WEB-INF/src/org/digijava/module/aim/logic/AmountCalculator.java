package org.digijava.module.aim.logic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

import java.util.Set;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface AmountCalculator {

    double calculateAmount(Set<CategAmountCell> mergedCells);

}
