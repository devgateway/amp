package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.module.aim.util.Output;

public class AmpPhysicalPerformance implements Serializable, Versionable, Cloneable
{
	
	private Long ampPpId ;
	private String type ;
	private String title ;
	private String description ;
	private Date reportingDate ;
	private AmpOrganisation reportingOrgId;
	private AmpActivityVersion ampActivityId;
	private String language ;	
	private AmpComponent component;
	
	private String comments;
	
			
	/**
	 * @return
	 */
	public Long getAmpPpId() {
		return ampPpId;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

		/**
	 * @return
	 */
	public Date getReportingDate() {
		return reportingDate;
	}

	/**
	 * @return
	 */
	public AmpOrganisation getReportingOrgId() {
		return reportingOrgId;
	}


	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	


	/**
	 * @param long1
	 */
	public void setAmpPpId(Long long1) {
		ampPpId = long1;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	public void setTitle(String string) {
		title = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}


	/**
	 * @param string
	 */
	public void setReportingDate(Date date) {
		reportingDate = date;
	}

	
	/**
	 * @param string
	 */
	public void setReportingOrgId(AmpOrganisation org) {
		this.reportingOrgId = org;
	}

		/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	public void setAmpActivityId(AmpActivityVersion a ) 
	{
		this.ampActivityId = a ;
	}
		
	public AmpActivityVersion getAmpActivityId() 
	{
		return ampActivityId;
		}
	/**
	 * @return Returns the component.
	 */
	public AmpComponent getComponent() {
		return component;
	}
	/**
	 * @param component The component to set.
	 */
	public void setComponent(AmpComponent component) {
		this.component = component;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Override
	public int hashCode() {
		if (this.ampPpId == null)
			return 0;
		return ampPpId.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof AmpPhysicalPerformance)) throw new ClassCastException();
		if(this.ampPpId==null) return false;
		
		AmpPhysicalPerformance ampPhysicalPerformance = (AmpPhysicalPerformance) obj;
		return this.ampPpId.equals(ampPhysicalPerformance.ampPpId);
		
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpPhysicalPerformance aux = (AmpPhysicalPerformance) obj;
		if (this.title.equals(aux.getTitle())) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] { "Title:&nbsp;" }, new Object[] { this.title }));
		if (this.description != null && !this.description.trim().equals("")) {
			out.getOutputs()
					.add(new Output(null, new String[] { " - Description:&nbsp;" }, new Object[] { this.description }));
		}
		if (this.reportingDate != null) {
			out.getOutputs().add(
					new Output(null, new String[] { " - Reporting Date:&nbsp;" }, new Object[] { this.reportingDate }));
		}
		return out;
	}

	@Override
	public Object getValue() {
		return this.description;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpPhysicalPerformance aux = (AmpPhysicalPerformance) clone();
		aux.ampActivityId = newActivity;
		aux.ampPpId = null;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}