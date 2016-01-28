package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Optional;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.digijava.module.aim.helper.TeamMember;

public class GeneratedReport {
	
	/**
	 * the specification which lead to this report being generated
	 */
	public final ReportSpecification spec;
	
	/**
	 * the time it took to generate this report, in milliseconds
	 */
	public final int generationTime;
	
	/**
	 * the timestamp of the moment this report was generated
	 */
	public final long generationMoment = System.currentTimeMillis();
	
	/**
	 * the user who has requsted this report to be generated.
	 * TODO: to specify what should stay here for the public user (null OR a hardcoded constant?)
	 */
	public final TeamMember requestingUser;
	
	public final ReportArea reportContents;
	
	/**
	 * Top report headers list. Each header may have sub-headers stored as direct children 
	 */
	public final List<ReportOutputColumn> rootHeaders;
	/**
	 * Ordered list of leaf headers of the report. Each leaf header can have a ancestors identified via parentColumn
	 * TODO: not sure if headers are useful via root or leaf
	 */
	public final List<ReportOutputColumn> leafHeaders;
	
	public final List<List<HeaderCell>> generatedHeaders;
	
	/**
	 * might be null, but not putting an {@link Optional} here because we have lots of old frontend code 
	 */
	@JsonIgnore
	public final RunNode timings;
	
	public GeneratedReport(ReportSpecification spec, int generationTime, TeamMember requestingUser, 
			ReportArea reportContents, List<ReportOutputColumn> rootHeaders, List<ReportOutputColumn> leafHeaders, 
			List<List<HeaderCell>> generatedHeaders, RunNode timings) {
		this.spec = spec;
		this.generationTime = generationTime;
		this.requestingUser = requestingUser;
		this.reportContents = reportContents;
		this.rootHeaders = rootHeaders;
		this.leafHeaders = leafHeaders;
		this.timings = timings;
		this.generatedHeaders = generatedHeaders;
	}
	
}

	