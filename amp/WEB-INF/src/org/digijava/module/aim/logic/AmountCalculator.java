package org.digijava.module.aim.logic;

import java.util.Set;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public interface AmountCalculator {

    double calculateAmount(Set<CategAmountCell> mergedCells);

}
