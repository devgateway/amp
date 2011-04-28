/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity;

import java.util.Date;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
public class AmpActivityVersion extends AmpActivityFields{
	
	/**
	 * 
	 * NOTE:
	 *    All new fields should be added in {@link AmpActivityFields}
	 *    
	 */
	
	public AmpActivityVersion() {
	}

	public AmpActivityVersion(Long ampActivityId, String name, Date updatedDate, AmpTeamMember updateBy, String ampid) {
		this.ampActivityId=ampActivityId;
		this.name=name;
		//this.budget=budget;
		this.updatedDate=updatedDate;
		this.updatedBy=updateBy;
		this.ampId=ampid;
	}

	public AmpActivityVersion(Long ampActivityId, String name, String ampid) {
		this.ampActivityId=ampActivityId;
		this.name=name;
		this.ampId=ampid;
	}
}