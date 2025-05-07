package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * the behaviour of an entity which filter the cells by a value
 * @author Viorel Chihai
 *
 */
public class FilteredMeasureBehaviour extends TrivialMeasureBehaviour {
    
    MetaCategory cat;
    Long filteredValue;
    
    public FilteredMeasureBehaviour(MetaCategory cat, Long filteredValue) {
        super();
        Objects.nonNull(filteredValue);
        Objects.nonNull(cat);
        this.cat = cat;
        this.filteredValue = filteredValue;
    }
    
    @Override
    public NiAmountCell doHorizontalReduce(List<NiCell> cells) {
        List<NiCell> filteredCells = cells.stream()
                .filter(cell -> cell.getCell().getMetaInfo().getMetaInfo(cat.category) != null)
                .filter(byMetaInfoValue())
                .collect(Collectors.toList());
        
        if (filteredCells.isEmpty()) {
            return getZeroCell();
        }
        
        return super.doHorizontalReduce(filteredCells);
    }

    /**
     * @return
     */
    private Predicate<? super NiCell> byMetaInfoValue() {
        return cell -> filteredValue.equals(cell.getCell().getMetaInfo().getMetaInfo(cat.category).getValue());
    }
    
}
