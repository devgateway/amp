package org.dgfoundation.amp.nireports.amp.indicators;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DoubleCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.dgfoundation.amp.algo.AmpCollections.any;
import static org.dgfoundation.amp.ar.ColumnConstants.NATIONAL_PLAN_OBJECTIVE;
import static org.dgfoundation.amp.nireports.amp.AmpReportsSchema.ANY_PROGRAM_TYPE;

/**
 * This behaviour matches {@link TextualTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorTextualTokenBehaviour extends TextualTokenBehaviour {

    private static Logger log = Logger.getLogger(IndicatorTextualTokenBehaviour.class);

    private final BiFunction<NiReportsEngine, Cell, String> formatter;
    private final NiDimensionUsage indicatorDimensionUsage;
    private final boolean removeDuplicates;
    private final IndicatorCellComparator indicatorCellComparator;
    private final static String PROGRAM_TYPE = "orgs." + ANY_PROGRAM_TYPE;

    public static IndicatorTextualTokenBehaviour forText(NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        return new IndicatorTextualTokenBehaviour((engine, cell) -> ((TextCell) cell).text, indicatorDimensionUsage, removeDuplicates);
    }

    public static IndicatorTextualTokenBehaviour forDouble(NiDimensionUsage indicatorDimensionUsage) {
        return new IndicatorTextualTokenBehaviour((engine, cell) -> formatDouble(engine, ((DoubleCell) cell).value), indicatorDimensionUsage, false);
    }

    protected IndicatorTextualTokenBehaviour(BiFunction<NiReportsEngine, Cell, String> formatter,
                                             NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        this.formatter = formatter;
        this.indicatorDimensionUsage = indicatorDimensionUsage;
        this.removeDuplicates = removeDuplicates;
        indicatorCellComparator = new IndicatorCellComparator(indicatorDimensionUsage);
    }

    public static Behaviour<?> getInstance() {
        return instance;
    }

    @Override
    public NiTextCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        StringJoiner reducedText = new StringJoiner(", ");
        Map<Long, String> entityIdsValues = new HashMap<>();
        cells.stream().filter(cellFilter()).sorted(indicatorCellComparator).forEach(niCell -> {
            Cell cell = niCell.getCell();
            String text = formatter.apply(context, cell);
            reducedText.add(text);
            entityIdsValues.put(cell.entityId, text);
        });
        return new NiTextCell(reducedText.toString(), any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }

    private Predicate<NiCell> cellFilter() {
        if (removeDuplicates) {
            return distinctByIndicator();
        } else {
            return c -> true;
        }
    }

    private Predicate<NiCell> distinctByIndicator() {
        return AmpCollections.distinctByKey(c -> c.getCell().coordinates.get(indicatorDimensionUsage));
    }

    private static String formatDouble(NiReportsEngine engine, Double value) {
        if (value == null) {
            return "";
        }
        return ReportsUtil.getDecimalFormatOrDefault(engine.spec).format(value);
    }

    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return true;
    }

    @Override
    public Cell filterCell(Map<NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell, Cell splitCell, boolean isTransactionLevelHierarchy) {
        long oldCellNPOId = -1;
        for (Map.Entry<NiDimensionUsage, NiDimension.Coordinate> e : oldCell.getCoordinates().entrySet()) {
            if (e.getKey().toString().equals(PROGRAM_TYPE)) {
                oldCellNPOId = e.getValue().id;
                break;
            }
        }
        if (splitCell.entityId == oldCellNPOId) {
            return super.filterCell(acceptors, oldCell, splitCell, isTransactionLevelHierarchy);
        } else {
            boolean continueFiltering = false;
            for (Map.Entry<NiDimensionUsage, NiDimension.Coordinate> e : splitCell.getCoordinates().entrySet()) {
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
            System.out.println("Ignoring cell with id " + splitCell.entityId + " because it is not part of the same NPO as the old cell");
            // return super.filterCell(acceptors, oldCell, splitCell, isTransactionLevelHierarchy);
            return null;
        }
    }
}
