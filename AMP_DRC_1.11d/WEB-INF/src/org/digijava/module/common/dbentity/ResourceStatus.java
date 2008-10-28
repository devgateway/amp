
package org.digijava.module.common.dbentity;


public class ResourceStatus extends StringPersistentEnum {

	
	public static final ResourceStatus PUBLISHED = new ResourceStatus("Published","pu");
	public static final ResourceStatus PENDING = new ResourceStatus("Pending","pe");
	public static final ResourceStatus REJECTED = new ResourceStatus("Rejected", "re");
	public static final ResourceStatus ARCHIVED = new ResourceStatus("Archived","ar");
	public static final ResourceStatus DELETED = new ResourceStatus("Deleted","dl");
	public static final ResourceStatus REVOKED = new ResourceStatus("Revoked","rev");
	
	
	public ResourceStatus(){
		
	}
	
	public ResourceStatus(String name, String persistentValue) {
		super(name, persistentValue);
	}
	
	public static ResourceStatus getStatus(String persistentValue) {
		return (ResourceStatus) getEnum(ResourceStatus.class, persistentValue);
	}
}
