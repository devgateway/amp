/**
 * AmpExternalMapping.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.dbentity;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 8, 2006
 * Generic class that maps internal ids with external ids. All ids are
 * generalised as StringS. 
 */
public class AmpExternalMapping {

	public static final String PROJECT_ID="PROJECT_ID";
	public static final String DONOR_ID="DONOR_ID";
	public static final String REGION_ID="REGION_ID";
	public static final String COMPONENT_ID="COMPONENT_ID";
	
	public static final String SOURCE_FB="FB";
	
	private String ampId;
	private String externalId;
	private String objectType;
	private String externalSource;
	
	private Long id;
	
	
	
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
	 * @return Returns the ampId.
	 */
	public String getAmpId() {
		return ampId;
	}



	/**
	 * @param ampId The ampId to set.
	 */
	public void setAmpId(String ampId) {
		this.ampId = ampId;
	}



	/**
	 * @return Returns the externalId.
	 */
	public String getExternalId() {
		return externalId;
	}



	/**
	 * @param externalId The externalId to set.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}



	/**
	 * @return Returns the objectType.
	 */
	public String getObjectType() {
		return objectType;
	}



	/**
	 * @param objectType The objectType to set.
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	/**
	 * 
	 */
	public AmpExternalMapping() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return Returns the externalSource.
	 */
	public String getExternalSource() {
		return externalSource;
	}



	/**
	 * @param externalSource The externalSource to set.
	 */
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}

}
