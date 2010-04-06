/*
 * Issues.java
 * Created : 07-Sep-2005
 */

package org.digijava.module.aim.helper;

import java.util.ArrayList;

public class Issues {
	private Long id;
	private String name;
	private String issueDate;
	private String nameTrimmed ;
	private ArrayList<Measures> measures;
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the measures.
	 */
	public ArrayList<Measures> getMeasures() {
		return measures;
	}
	/**
	 * @param measures The measures to set.
	 */
	public void setMeasures(ArrayList<Measures> measures) {
		this.measures = measures;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		if (arg instanceof Issues) {
			Issues issue = (Issues) arg;
			return issue.getId().equals(id);
		}
		throw new ClassCastException();
	}
	public String getNameTrimmed() {
		return name.replace(" ", "");
	}
	public void setNameTrimmed(String nameTrimmed) {
		this.nameTrimmed = nameTrimmed;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	
}