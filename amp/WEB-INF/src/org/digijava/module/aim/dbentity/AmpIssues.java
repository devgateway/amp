package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class AmpIssues  implements Serializable
{

	private Long ampIssueId ;
	private String name ;
	private AmpActivity activity;
	private Set measures;
	private Date issueDate;
 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmpIssueId() {
		return ampIssueId;
	}
	public void setAmpIssueId(Long ampIssueId) {
		this.ampIssueId = ampIssueId;
	}
	
	public AmpActivity getActivity() {
		return activity;
	}
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}

	public Set getMeasures() {
		return measures;
	}
	public void setMeasures(Set measures) {
		this.measures = measures;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		if (arg instanceof AmpIssues) {
			AmpIssues issue = (AmpIssues) arg;
			return issue.getAmpIssueId().equals(ampIssueId);
		}
		throw new ClassCastException();
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	

}
