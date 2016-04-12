package org.dgfoundation.amp.nireports;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.newreports.FilterRule;
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
	protected final Set<String> mandatoryHierarchies;
	//protected final BiFunction<NiFilters, NiReportsEngine, Set<Long>> activityIdsComputer;
	protected final Function<NiReportsEngine, Set<Long>> activityIdsComputer;
	protected final Memoizer<Set<Long>> workspaceFilter;
	//protected final ConcurrentHashMap<NiReportsEngine, Set<Long>> activityIds = new ConcurrentHashMap<>();
	
	protected PassiveNiFilters(NiReportsEngine engine, Map<NiDimensionUsage, List<Predicate<Coordinate>>> predicates, LinkedHashSet<String> mandatoryHierarchies, Function<NiReportsEngine, Set<Long>> activityIdsComputer) {
		Objects.requireNonNull(activityIdsComputer);
		Objects.requireNonNull(engine);
		this.engine = engine;
		this.filteringCoordinates = unmodifiableMap(remap(predicates, AmpCollections::mergePredicates, null));
		this.mandatoryHierarchies = unmodifiableSet(mandatoryHierarchies);
		this.activityIdsComputer = activityIdsComputer;
		this.workspaceFilter = new Memoizer<>(() -> this.activityIdsComputer.apply(this.engine));
	}
	
	@Override
	public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters() {
		return filteringCoordinates;
	}
	
	@Override
	public Set<String> getMandatoryHierarchies() {
		return mandatoryHierarchies;
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
		
		if (!col.levelColumn.isPresent())
			return false;
		
		return getProcessedFilters().keySet().contains(col.levelColumn.get().dimensionUsage);
	}
	
}
