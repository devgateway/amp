package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

public class AmpReportPhysicalPerformance implements Serializable
{
	
	private Long ampPpId ;
	private String title ;
	private String description ;
	private Long ampActivityId;
	
			
	/**
	 * @return
	 */
	public Long getAmpPpId() {
		return ampPpId;
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
	 * @param long1
	 */
	public void setAmpPpId(Long long1) {
		ampPpId = long1;
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


	public void setAmpActivityId(Long a ) 
	{
		this.ampActivityId = a ;
	}
		
	public Long getAmpActivityId() 
	{
		return ampActivityId;
		}

}	
