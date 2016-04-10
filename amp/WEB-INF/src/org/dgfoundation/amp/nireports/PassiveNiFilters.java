package org.dgfoundation.amp.nireports;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static org.dgfoundation.amp.algo.AmpCollections.remap;

/**
 * the NiReports faucet of filtering 
 * @author Dolghier Constantin
 *
 */
public class PassiveNiFilters implements NiFilters {
			
	protected final Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> filteringCoordinates;
	protected final Set<String> mandatoryHierarchies;
	//protected final BiFunction<NiFilters, NiReportsEngine, Set<Long>> activityIdsComputer;
	protected final Function<NiReportsEngine, Set<Long>> activityIdsComputer;
	protected final ConcurrentHashMap<NiReportsEngine, Set<Long>> activityIds = new ConcurrentHashMap<>();
	
	protected PassiveNiFilters(Map<NiDimensionUsage, List<Predicate<Coordinate>>> predicates, LinkedHashSet<String> mandatoryHierarchies, Function<NiReportsEngine, Set<Long>> activityIdsComputer) {
		Objects.requireNonNull(activityIdsComputer);
		this.filteringCoordinates = unmodifiableMap(remap(predicates, AmpCollections::mergePredicates, null));
		this.mandatoryHierarchies = unmodifiableSet(mandatoryHierarchies);
		this.activityIdsComputer = activityIdsComputer;
	}
	
	@Override
	public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters(NiReportsEngine engine) {
		return filteringCoordinates;
	}
	
	@Override
	public Set<String> getMandatoryHierarchies() {
		return mandatoryHierarchies;
	}
	
	@Override
	public Set<Long> getActivityIds(NiReportsEngine engine) {
		return activityIds.computeIfAbsent(engine, activityIdsComputer);
	}
}
