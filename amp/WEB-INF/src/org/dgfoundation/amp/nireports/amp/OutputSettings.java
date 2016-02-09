/**
 * 
 */
package org.dgfoundation.amp.nireports.amp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportWarning;

/**
 * Stores custom output settings and information
 * 
 * @author Nadejda Mandrescu
 */
public class OutputSettings {
	
	protected boolean isProvideEntityIds = false;
	protected Optional<Set<String>> provideIdsOnly = Optional.empty();
	// TODO: may not need this option?
	protected Optional<Set<String>> provideIdsValues = Optional.empty();
	protected Optional<Set<ReportWarning>> reportWarnings = Optional.ofNullable(null);
	
	public OutputSettings() {
	}
	
	/**
	 * Configures if entity ID has to be provided at the first entity level result 
	 * @param isProvideEntityIds
	 */
	public void setProvideEntityIds(boolean isProvideEntityIds) {
		this.isProvideEntityIds = true;
	}
	
	public boolean isProvideEntityIds() {
		return isProvideEntityIds;
	}
	
	public void addEntityToProvideIds(String columnName) {
		if (!provideIdsOnly.isPresent()) {
			provideIdsOnly = Optional.of(new HashSet<String>());
		}
		provideIdsOnly.get().add(columnName);
	}
	
	/**
	 * @return entities for which to provide only ids
	 */
	public Set<String> getProvideIdsOnly() {
		return provideIdsOnly.orElse(Collections.emptySet());
	}
	
	public void addEntityToProvideIdsAndValues(String columnName) {
		if (!provideIdsValues.isPresent()) {
			provideIdsValues = Optional.of(new HashSet<String>());
		}
		provideIdsValues.get().add(columnName);
	}
	
	/**
	 * @return entities for which to provide both ids and values
	 */
	public Set<String> getProvideIdsAndValues() {
		return provideIdsValues.orElse(Collections.emptySet());
	}

	/**
	 * @return the reportWarnings
	 */
	public Optional<Set<ReportWarning>> getReportWarnings() {
		return reportWarnings;
	}

	/**
	 * @param reportWarnings the reportWarnings to set
	 */
	public void setReportWarnings(Set<ReportWarning> reportWarnings) {
		this.reportWarnings = Optional.ofNullable(reportWarnings);
	}
}
