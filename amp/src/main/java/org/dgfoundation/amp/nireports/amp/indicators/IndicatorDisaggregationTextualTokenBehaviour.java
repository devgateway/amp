package org.dgfoundation.amp.nireports.amp.indicators;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;

import java.util.Map;
import java.util.function.BiFunction;

import static org.dgfoundation.amp.ar.ColumnConstants.NATIONAL_PLAN_OBJECTIVE;

public class IndicatorDisaggregationTextualTokenBehaviour extends IndicatorTextualTokenBehaviour {

    private static Logger log = Logger.getLogger(IndicatorDisaggregationTextualTokenBehaviour.class);

    public IndicatorDisaggregationTextualTokenBehaviour(BiFunction<NiReportsEngine, Cell, String> formatter, NiDimension.NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        super(formatter, indicatorDimensionUsage, removeDuplicates);
    }

    public static IndicatorDisaggregationTextualTokenBehaviour forText(NiDimension.NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        return new IndicatorDisaggregationTextualTokenBehaviour((engine, cell) -> ((TextCell) cell).text, indicatorDimensionUsage, removeDuplicates);
    }

    @Override
    public Cell filterCell(Map<NiDimension.NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell, Cell splitCell, boolean isTransactionLevelHierarchy) {
        /*long oldCellNPOId = -1;
        for (Map.Entry<NiDimension.NiDimensionUsage, NiDimension.Coordinate> e : oldCell.getCoordinates().entrySet()) {
            if (e.getKey().toString().equals(PROGRAM_TYPE)) {
                oldCellNPOId = e.getValue().id;
                break;
            }
        }
        if (splitCell.entityId == oldCellNPOId) {
            return super.filterCell(acceptors, oldCell, splitCell, isTransactionLevelHierarchy);
        } else {*/
        boolean continueFiltering = false;
        for (Map.Entry<NiDimension.NiDimensionUsage, NiDimension.Coordinate> e : splitCell.getCoordinates().entrySet()) {
            if (!e.getKey().instanceName.equals(NATIONAL_PLAN_OBJECTIVE)) {
                continueFiltering = true;
                break;
            }
        }
        if (continueFiltering) {
            // TODO: evaluate if this code helps to display Donor Groups hierarchies + funding columns.
            Cell cell = super.filterCell(acceptors, oldCell, splitCell, isTransactionLevelHierarchy);
            if (cell != null) {
                return cell;
            }

            // TODO: this change allowed to display outcomes and outputs (new columns), check if it has side effects.
            // return super.filterCell(acceptors, oldCell, splitCell, false);
            return oldCell;
        }
        //log.warn("Ignoring cell with id " + splitCell.entityId + " because it is not part of the same NPO as the old cell");
        //return super.filterCell(acceptors, oldCell, splitCell, isTransactionLevelHierarchy);
        //return null;
        return oldCell;
        //}
    }
}
