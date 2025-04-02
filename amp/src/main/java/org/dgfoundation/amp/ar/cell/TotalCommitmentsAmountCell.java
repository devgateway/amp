/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.logic.AmountCalculator;
import org.digijava.module.aim.logic.Logic;

/**
 * @author mihai
 */
public class TotalCommitmentsAmountCell extends AmountCell {


    public TotalCommitmentsAmountCell() {
        super();
    }

    public TotalCommitmentsAmountCell(AmountCell ac) {
        super(ac.getOwnerId());
        this.mergedCells = ac.getMergedCells();
    }

    /**
         * @param id
         */
    public TotalCommitmentsAmountCell(Long id) {
        super(id);
    }

    /**
         * Ovverider of the normal behaviour of AmountCell.getAmount. This will take into consideration only undisbursed
         * related merged cells and will also make the required calculations
         * 
         * @return Returns the amount.
         */
    public double getAmount() {
        if (id != null)
            return convert();
        return Logic.getInstance().getCommitmentCalculator().calculateAmount((Set)mergedCells);
    }

    @Override
    public AmountCell merge(Cell c) {
        AmountCell ac = (AmountCell) super.merge(c);        
        TotalCommitmentsAmountCell uac = new TotalCommitmentsAmountCell(ac);
        return uac;
    }

    @Override
    public AmountCell newInstance() {
        return new TotalCommitmentsAmountCell();
    }

}
