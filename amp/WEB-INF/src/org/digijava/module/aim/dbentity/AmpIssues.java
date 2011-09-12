package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.util.Output;

public class AmpIssues  implements Serializable, Versionable, Cloneable
{

	private Long ampIssueId ;
	private String name ;
	private AmpActivityVersion activity;
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
	
	public AmpActivityVersion getActivity() {
		return activity;
	}
	public void setActivity(AmpActivityVersion activity) {
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
	public boolean equals(Object arg) {
		if (arg instanceof AmpIssues) {
			AmpIssues issue = (AmpIssues) arg;
			return issue.getAmpIssueId().equals(ampIssueId);
		}
		throw new ClassCastException();
	}
	 */
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpIssues aux = (AmpIssues) obj;
		String original = this.name != null ? this.name : "";
		String copy = aux.name != null ? aux.name : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { " Name:&nbsp;" }, new Object[] { this.name != null ? this.name
						: "Empty Name" }));
		if (this.issueDate != null) {
			out.getOutputs().add(new Output(null, new String[] { " Date:&nbsp;" }, new Object[] { this.issueDate }));
		}
		// TODO add measures.
		return out;
	}

	@Override
	public Object getValue() {
		// TODO add measures.
		return this.issueDate != null ? this.issueDate : "";
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpIssues aux = (AmpIssues) clone();
		aux.activity = newActivity;
		aux.ampIssueId = null;
		if (aux.measures != null && aux.measures.size() > 0) {
			Set<AmpMeasure> setMeasures = new HashSet<AmpMeasure>();
			Iterator<AmpMeasure> i = aux.measures.iterator();
			while (i.hasNext()) {
				AmpMeasure newMeasure = (AmpMeasure) i.next().clone();
				newMeasure.setAmpMeasureId(null);
				newMeasure.setIssue(aux);
				if (newMeasure.getActors() != null && newMeasure.getActors().size() > 0) {
					Set<AmpActor> setActors = new HashSet<AmpActor>();
					Iterator<AmpActor> j = newMeasure.getActors().iterator();
					while (j.hasNext()) {
						AmpActor newActor = (AmpActor) j.next().clone();
						newActor.setAmpActorId(null);
						newActor.setMeasure(newMeasure);
						setActors.add(newActor);
					}
					newMeasure.setActors(setActors);
				} else {
					newMeasure.setActors(null);
				}
				setMeasures.add(newMeasure);
			}
			aux.measures = setMeasures;
		} else {
			aux.measures = null;
		}
		return aux;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}