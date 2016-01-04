/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Stores a report configuration by implementing {@link ReportSpecification} and defines all data required to generate a report. 
 * this is the Mondrian-specific implementation (full of hacks)
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportSpec implements ReportSpecification {
	
	/**
	 * the encapsulated MondrianReportSpecificationImpl
	 */
	private final ReportSpecificationImpl emb;
	protected boolean usesFundingFlows = false;
	
	/**
	 * this can only be reset and not set
	 */
	protected boolean calculateRowTotals = true;
	protected String projectTitleColumn = ColumnConstants.PROJECT_TITLE;
	protected Set<ReportColumn> dummyColumns = new LinkedHashSet<ReportColumn>();
	
	/**
	 * Mondrian-specific hack - not part of the API, thus public
	 */
	public Map<String, SortedSet<Integer>> allowedYearsPerMeasure = new HashMap<>();
  
	public MondrianReportSpec(ReportSpecificationImpl emb) {
		this.emb = emb;
	}
	
	public Set<ReportColumn> getDummyColumns() {
		return Collections.unmodifiableSet(dummyColumns);
	}
	
	public void addColumn(ReportColumn column, boolean dummy) {
		emb.addColumn(column);
		if (dummy) {
			this.dummyColumns.add(column);
		}
	}

	public String getReportName() {
		return emb.getReportName();
	}
	
	public Set<ReportColumn> getHierarchies() {
		return emb.getHierarchies();
	}
	
	public Set<ReportColumn> getColumns() {
		return emb.getColumns();
	}

	
	/**
	 * remove the dummyColumns from the columns list; also clear {@link #dummyColumns}
	 */
	public void removeDummyColumns() {
		Iterator<ReportColumn> columnsIter = emb.getColumns().iterator();
		while(columnsIter.hasNext()) {
			ReportColumn col = columnsIter.next();
			if (dummyColumns.contains(col)) columnsIter.remove();
		}
		dummyColumns.clear();
	}
	   
	public boolean getCalculateRowTotals() {
		return this.calculateRowTotals;
	}
	
	protected void resetCalculateRowTotals() {
		this.calculateRowTotals = false;
	}
	
	/**
	 * 1. checks that every column specified in "hierarchies" is also present in "columns"
	 * 2. brings the columns specified as hierarchies to front
	 * 
	 * Somehow hacky, but faster to code than redoing the whole rest of the code to support input in any order. Sorry, Nadia :=)  
	 * 
	 * Nadia's note: I'm glad that you made up your mind to not change the rest of the code :P
	 */
	public void reorderColumnsByHierarchies() {
		//if (getHierarchies() == null) return; // nothing to do
		LinkedHashSet<ReportColumn> newCols = new LinkedHashSet<>();
		for(ReportColumn hier:emb.getHierarchies()) {
			if (!emb.getColumns().contains(hier))
				throw new RuntimeException("column specified as hierarchy, but not as column: " + hier);
			newCols.add(hier);
		}
		for(ReportColumn col:emb.getColumns()) {
			if (!newCols.contains(col))
				newCols.add(col);
		}
		emb.setColumns(newCols);
		
		/**
		 * ugly workaround for AMP-18558 when the report has no hierarchies - a saiku bug which is easier to workaround than fix
		 */
		if (emb.getColumns().isEmpty() && emb.getHierarchies().isEmpty()) {
			ReportColumn constantDummyColumn = new ReportColumn(ColumnConstants.CONSTANT);
			emb.getColumns().add(constantDummyColumn);
			emb.getHierarchies().add(constantDummyColumn);
			this.resetCalculateRowTotals();
			/* we must not reconfigure column totals to be calculated 
			setCalculateColumnTotals(true);
			*/
		}
	}
	
	protected void addIfMtef(AmpReportColumn arc, SortedSet<Integer> years, String prefix) {
		Integer yearNr = arc.getColumn().getMtefYear(prefix);
		if (yearNr != null) {
			yearNr = Math.min(Math.max(yearNr, 1970), 2050); // clamp between 1970 and 2050 - this is what our Mondrian reports implementation supports (for now)
			years.add(yearNr); 
		}
	}
	
	protected void addMeasureIfMandatedByColumn(ReportSpecificationImpl spec, SortedSet<Integer> years, ElementType elementType, String measureToAdd, boolean addMeasureAtBeginning) {
		List<FilterRule> rules = MtefConverter.instance.buildRulesFor(years);
		if (rules == null)
			return;
		
		// we have to add MTEF info to filters -> ensure that a filters instance exists
		if (spec.getFilters() == null)
			spec.setFilters(new MondrianReportFilters());

		spec.getFilters().getFilterRules().put(new ReportElement(elementType), rules);
		ReportMeasure measure = new ReportMeasure(measureToAdd);
		if (!spec.getMeasures().contains(measure)) {
//			if (addMeasureAtBeginning)
//				spec.getMeasures().add(0, measure);
//			else
				spec.getMeasures().add(measure);
		}
	}
	
	/**
	 * scans the report for MTEF columns and converts them to "mtef" measure reference + filter entries <br />
	 * this function is thread-safe, because it has no off-stack state <br />
	 * dies if the input is fishy - this is done on purpose
	 */
	public void convertMtefs(AmpReports report) {
		SortedSet<Integer> mtefYears = new TreeSet<>(), realMtefYears = new TreeSet<>(), pipelineMtefYears = new TreeSet<>(), projectionMtefYears = new TreeSet<>();
		
		for(AmpReportColumn arc:report.getColumns()) {
			addIfMtef(arc, mtefYears, "mtef");
			addIfMtef(arc, realMtefYears, "realmtef");
			addIfMtef(arc, pipelineMtefYears, "pipelinemtef");
			addIfMtef(arc, projectionMtefYears, "projectionmtef");
		}
						
		addMeasureIfMandatedByColumn(emb, mtefYears, ElementType.MTEF_DATE, MeasureConstants.MTEF_PROJECTIONS, true);
		addMeasureIfMandatedByColumn(emb, realMtefYears, ElementType.REAL_MTEF_DATE, MeasureConstants.REAL_MTEFS, false);
		addMeasureIfMandatedByColumn(emb, pipelineMtefYears, ElementType.PIPELINE_MTEF_DATE, MeasureConstants.PIPELINE_MTEF_PROJECTIONS, false);
		addMeasureIfMandatedByColumn(emb, projectionMtefYears, ElementType.PROJECTION_MTEF_DATE, MeasureConstants.PROJECTION_MTEF_PROJECTIONS, false);
		
		allowedYearsPerMeasure.put("MTEF", mtefYears);
		allowedYearsPerMeasure.put(MeasureConstants.REAL_MTEFS, realMtefYears);
		allowedYearsPerMeasure.put(MeasureConstants.PIPELINE_MTEF_PROJECTIONS, pipelineMtefYears);
		allowedYearsPerMeasure.put(MeasureConstants.PROJECTION_MTEF_PROJECTIONS, projectionMtefYears);
	}
	
	/**
	 * Configures default & mandatory behavior
	 * @param spec - report specification
	 */
	public void configureDefaults() {
		if (emb.getSettings() == null) {
			emb.setSettings(MondrianReportUtils.getCurrentUserDefaultSettings());
		}
	}
	
	public boolean getUsesFundingFlows() {
		return usesFundingFlows;
	}

	void setUsesFundingFlows(boolean usesFundingFlows) {
		this.usesFundingFlows = usesFundingFlows;
	}

	public String getProjectTitleColumn() {
		return projectTitleColumn;
	}

	public void setProjectTitleColumn(String projectTitleColumn) {
		this.projectTitleColumn = projectTitleColumn;
	}
	
	/**
	 * @return true if totals per each measure column must be calculated
	 */
	public boolean isCalculateColumnTotals() {
		return !GroupingCriteria.GROUPING_TOTALS_ONLY.equals(emb.getGroupingCriteria());
	}


	@Override
	public int getReportType() {
		return emb.getReportType();
	}

	@Override
	public Set<ReportMeasure> getMeasures() {
		return emb.getMeasures();
	}

	@Override
	public ReportFilters getFilters() {
		return emb.getFilters();
	}

	@Override
	public ReportSettings getSettings() {
		return emb.getSettings();
	}

	@Override
	public List<SortingInfo> getSorters() {
		return emb.getSorters();
	}

	@Override
	public GroupingCriteria getGroupingCriteria() {
		return emb.getGroupingCriteria();
	}

	@Override
	public boolean isDisplayEmptyFundingColumns() {
		return emb.isDisplayEmptyFundingColumns();
	}

	@Override
	public boolean isDisplayEmptyFundingRows() {
		return emb.isDisplayEmptyFundingRows();
	}

	@Override
	public boolean isEmptyOutputForUnspecifiedData() {
		return emb.isEmptyOutputForUnspecifiedData();
	}

	@Override
	public boolean isPopulateReportHeadersIfEmpty() {
		return emb.isPopulateReportHeadersIfEmpty();
	}

	@Override
	public boolean isAlsoShowPledges() {
		return emb.isAlsoShowPledges();
	}
}
