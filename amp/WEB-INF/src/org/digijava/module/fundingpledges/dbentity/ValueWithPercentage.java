package org.digijava.module.fundingpledges.dbentity;

public interface ValueWithPercentage {
	public Long getDbId();
	public Long getInternalId();
	public String getName();
	
	public Long getRootId();
	public String getRootName();

	public String getHierarchicalName();
	public Float getPercentage();	
}
