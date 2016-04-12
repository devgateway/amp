package org.dgfoundation.amp.nireports;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

/**
 * a NiFilters converter which does the job (through subclasses, if necessary) in reasonable filtering schemas
 * @author Dolghier Constantin
 *
 */
public abstract class BasicFiltersConverter {
	
	protected final Map<ReportElement, List<FilterRule>> rawRules;
	protected final ReportSpecification spec;
	protected final NiReportsEngine engine;
	protected final NiReportsSchema schema;
	protected final Map<NiDimensionUsage, List<Predicate<NiDimension.Coordinate>>> predicates = new HashMap<>();
	protected final Set<String> mandatoryHiers = new TreeSet<>();
	protected Predicate<Long> activityIdsPredicate;
	
	public BasicFiltersConverter(NiReportsEngine engine) {
		this.engine = engine;
		this.spec = engine.spec;
		this.schema = engine.schema;
		this.rawRules = (spec.getFilters() != null && spec.getFilters().getFilterRules() != null)
			? Collections.unmodifiableMap(spec.getFilters().getFilterRules())
			: Collections.emptyMap();
	}
	
	public PassiveNiFilters buildNiFilters(Function<NiReportsEngine, Set<Long>> activityIdsSrc) {
		rawRules.forEach((repElem, rules) -> {
			if (rules != null && !rules.isEmpty())
				processElement(repElem, rules);
		});
		return new PassiveNiFilters(engine, predicates, new LinkedHashSet<>(mandatoryHiers), activityIdsSrc, activityIdsPredicate);
	}
	
	protected void processElement(ReportElement repElem, List<FilterRule> rules) {
		if (repElem.type == ElementType.ENTITY) {
			if (repElem.entity instanceof ReportColumn)
				processColumnElement(repElem.entity.getEntityName(), rules);
			else
				throw new RuntimeException(String.format("entity type %s not supported: %s", repElem.entity.getClass().getName(), repElem.entity.toString()));
		} else
			processMiscElement(repElem, rules);
	}
		
	protected void processColumnElement(String columnName, List<FilterRule> rules) {
		// filtering by column
		NiReportColumn<?> col = schema.getColumns().get(columnName);
		if (col == null) {
			engine.addReportWarning(new ReportWarning(String.format("not filtering by unimplemented column %s", columnName)));
			return;
		}
		if (col.levelColumn == null || !col.levelColumn.isPresent()) {
			engine.addReportWarning(new ReportWarning(String.format("not filtering by non-level-column column %s", columnName)));
			return;
		}
		Set<Long> positiveIds = FilterRule.mergeIdRules(rules.stream().filter(rule -> rule.valuesInclusive).collect(toList()));
		Set<Long> negativeIds = FilterRule.mergeIdRules(rules.stream().filter(rule -> !rule.valuesInclusive).collect(toList()));
		notifySupportedColumn(columnName, positiveIds, negativeIds);
		addRulesIfPresent(col.levelColumn.get(), true, positiveIds);
		addRulesIfPresent(col.levelColumn.get(), false, negativeIds);
	}
	
	protected void notifySupportedColumn(String columnName, Set<Long> positiveIds, Set<Long> negativeIds) {
		this.mandatoryHiers.add(columnName);
	}
	
	protected void addRulesIfPresent(LevelColumn lc, boolean positive, Set<Long> ids) {
		if (ids == null)
			return;
		
		Predicate<Coordinate> predicate = FilterRule.maybeNegated(engine.buildAcceptor(lc.dimensionUsage, ids.stream().map(z -> new Coordinate(lc.level, z)).collect(toList())), positive);
		predicates.computeIfAbsent(lc.dimensionUsage, ignored -> new ArrayList<>()).add(predicate);
	}
	
	protected abstract void processMiscElement(ReportElement repElem, List<FilterRule> rules);
}
