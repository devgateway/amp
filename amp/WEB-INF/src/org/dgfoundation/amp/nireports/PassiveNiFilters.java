package org.dgfoundation.amp.nireports;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static org.dgfoundation.amp.algo.AmpCollections.remap;

/**
 * the NiReports faucet of filtering
 * @author Dolghier Constantin
 *
 */
public class PassiveNiFilters implements NiFilters {
		
	protected final NiReportsEngine engine;
	protected final Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> filteringCoordinates;
	protected final Map<String, Predicate<Cell>> cellPredicates;
	protected final Set<String> filteringColumns;
	protected final Function<NiReportsEngine, Set<Long>> activityIdsComputer;
	protected final Memoizer<Set<Long>> workspaceFilter;
	protected final Predicate<Long> activityIdsPredicate;
	protected final Set<String> mandatoryHiers;
	
	public PassiveNiFilters(NiReportsEngine engine, Map<NiDimensionUsage, List<Predicate<Coordinate>>> filteringCoordinates, Map<String, List<Predicate<Cell>>> cellPredicates, Set<String> filteringColumns, Set<String> mandatoryHiers, Function<NiReportsEngine, Set<Long>> activityIdsComputer, Predicate<Long> activityIdsPredicate) {
		Objects.requireNonNull(activityIdsComputer);
		Objects.requireNonNull(engine);
		this.engine = engine;
		this.filteringCoordinates = unmodifiableMap(remap(filteringCoordinates, AmpCollections::mergePredicates, null));
		this.cellPredicates = unmodifiableMap(remap(cellPredicates, AmpCollections::mergePredicates, null));
		this.filteringColumns = unmodifiableSet(filteringColumns);
		this.activityIdsComputer = activityIdsComputer;
		this.activityIdsPredicate = Optional.ofNullable(activityIdsPredicate).orElse(ignored -> true);
		this.workspaceFilter = new Memoizer<Set<Long>>(() -> this.activityIdsComputer.apply(this.engine).stream().filter(this.activityIdsPredicate).collect(Collectors.toSet()));
		this.mandatoryHiers = unmodifiableSet(mandatoryHiers);
	}
	
	@Override
	public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters() {
		return filteringCoordinates;
	}
	
	@Override
	public Map<String, Predicate<Cell>> getCellPredicates() {
		return this.cellPredicates;
	}
	
	@Override
	public Set<String> getFilteringColumns() {
		return filteringColumns;
	}

	@Override
	public Set<Long> getWorkspaceActivityIds() {
		return workspaceFilter.get();
	}

	@Override
	public Set<Long> getFilteredActivityIds() {
		Set<Long> curResult = new HashSet<>(getWorkspaceActivityIds());
		for(String fetchedColumn:engine.fetchedColumns.keySet().stream().filter(this::isFilteringColumn).collect(Collectors.toList())) { //TODO: maybe refactor this to plainly iterate over #getProcessedFilters (after we have some testcases)
			Set<Long> colIds = engine.fetchedColumns.get(fetchedColumn).data.keySet();
			curResult.retainAll(colIds);
		}
		return curResult;
	}
	
	/**
	 * returns true IFF this column should be used for filtering entityIDs
	 * @param colName
	 * @return
	 */
	protected boolean isFilteringColumn(String colName) {
		NiReportColumn<?> col = engine.schema.getColumns().get(colName);
		
		if (col == null)
			return false;
		 
		if (cellPredicates.containsKey(colName))
			return true;
		
		if (!col.levelColumn.isPresent())
			return false;
		
		return getProcessedFilters().keySet().contains(col.levelColumn.get().dimensionUsage);
	}

	@Override
	public Set<String> getMandatoryHiers() {
		return this.mandatoryHiers;
	}
	
}
