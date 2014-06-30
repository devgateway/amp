package org.dgfoundation.amp.newreports;

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
	
	public GeneratedReport(ReportSpecification spec, int generationTime, TeamMember requestingUser, ReportArea reportContents) {
		this.spec = spec;
		this.generationTime = generationTime;
		this.requestingUser = requestingUser;
		this.reportContents = reportContents;
	}
}
