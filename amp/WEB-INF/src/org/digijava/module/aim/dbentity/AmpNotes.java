package org.digijava.module.aim.dbentity ;


public class AmpNotes
{
		
	private Long ampNotesId ;
	private String name ;
	private String type; 
	private String description ;
	private String language ;
	private String version ;
	private AmpActivityVersion ampActivityId;
	

	/**
	 * @return
	 */
	public Long getAmpNotesId() {
		return ampNotesId;
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
	public String getLanguage() {
		return language;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}


	/**
	 * @param long1
	 */
	public void setAmpNotesId(Long long1) {
		ampNotesId = long1;
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
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}
	public void setAmpActivityId(AmpActivityVersion a ) 
	{
		this.ampActivityId = a ;
	}
		
	public AmpActivityVersion getAmpActivityId() 
	{
		return ampActivityId;
		}

}
