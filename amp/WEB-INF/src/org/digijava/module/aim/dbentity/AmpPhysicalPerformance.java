package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Date;

public class AmpPhysicalPerformance implements Serializable
{
	
	private Long ampPpId ;
	private String type ;
	private String title ;
	private String description ;
	private Date reportingDate ;
	private AmpOrganisation reportingOrgId;
	private AmpActivity ampActivityId;
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

	public void setAmpActivityId(AmpActivity a ) 
	{
		this.ampActivityId = a ;
	}
		
	public AmpActivity getAmpActivityId() 
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
}	
