/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;
import java.util.*;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.dataExchange.util.WrapperSourceSetting;
import org.digijava.module.dataExchange.util.XmlWrappable;
import org.digijava.module.dataExchange.util.XmlWrapper;
import org.digijava.module.sdm.dbentity.Sdm;
/**
 * 
 *
 */
public class DESourceSetting implements XmlWrappable{
	public final static String SOURCE_URL			= "URL";
	public final static String SOURCE_FILE			= "FILE";
	public final static String SOURCE_WEB_SERVICE	= "WEB_SERVICE";
	
	public final static String IMPORT_STRATEGY_NEW_PROJ					= "ADD NEW PROJECTS";
	public final static String IMPORT_STRATEGY_UPD_PROJ					= "UPDATE PROJECTS";
	public final static String IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ	= "ADD NEW PROJECTS AND UPDATE PROJECTS";
	
	public final static String[] DAYS 	= {"Monday","Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	
	
	private Long id;
	private String name;
	
	/**
	 * Decides if it is an import or export source
	 */
	private Boolean isImport;
	
	/**
	 * 
	 */
	private String source;
	
	private AmpTeam importWorkspace;
	private String approvalStatus;
	private List<String>  fields;
	private String uniqueIdentifier;
	private String uniqueIdentifierSeparator	= "|";
	
	private List exportFilters;
	private List<DELogPerExecution> logs;
	
	private String languageId;
	/**import option (dropdown):    
	                        1. add only new projects
	                        2. update only existing projects
	                        3. add new projects and update existing projects*/
	private String importStrategy;
	
	
	private String frequency;
	private String dayOfWeek;
	private String time;
	
	
	/**
	 * In case the source would be a URL
	 */
	private String url;
	
	private String language ; //non-db field
	private Sdm attachedFile ;
	
	private Sdm previousAttachedFile ;

    private Map<String, Boolean> importFlds;
    private Map<String, Boolean> updateFlds;
    private AmpCategoryValueLocations defaultLocation;

    private boolean regionalFundings;
    private boolean mergeDisbAndExp;

    public boolean isRegionalFundings() {
        return regionalFundings;
    }

    public void setRegionalFundings(boolean regionalFundings) {
        this.regionalFundings = regionalFundings;
    }

    public boolean isMergeDisbAndExp() {
        return mergeDisbAndExp;
    }

    public void setMergeDisbAndExp(boolean mergeDisbAndExp) {
        this.mergeDisbAndExp = mergeDisbAndExp;
    }

    public AmpCategoryValueLocations getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(AmpCategoryValueLocations defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    /**
	 * @return the isImport
	 */
	public Boolean getIsImport() {
		return isImport;
	}

	/**
	 * @param isImport the isImport to set
	 */
	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the importWorkspace
	 */
	public AmpTeam getImportWorkspace() {
		return importWorkspace;
	}

	/**
	 * @param importWorkspace the importWorkspace to set
	 */
	public void setImportWorkspace(AmpTeam importWorkspace) {
		this.importWorkspace = importWorkspace;
	}

	/**
	 * @return the approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return the uniqueIdentifier
	 */
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	/**
	 * @param uniqueIdentifier the uniqueIdentifier to set
	 */
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	/**
	 * @return the uniqueIdentifierSeparator
	 */
	public String getUniqueIdentifierSeparator() {
		return uniqueIdentifierSeparator;
	}

	/**
	 * @param uniqueIdentifierSeparator the uniqueIdentifierSeparator to set
	 */
	public void setUniqueIdentifierSeparator(String uniqueIdentifierSeparator) {
		this.uniqueIdentifierSeparator = uniqueIdentifierSeparator;
	}

	/**
	 * @return the exportFilters
	 */
	public List getExportFilters() {
		return exportFilters;
	}

	/**
	 * @param exportFilters the exportFilters to set
	 */
	public void setExportFilters(List exportFilters) {
		this.exportFilters = exportFilters;
	}

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * @return the importStrategy
	 */
	public String getImportStrategy() {
		return importStrategy;
	}

	/**
	 * @param importStrategy the importStrategy to set
	 */
	public void setImportStrategy(String importStrategy) {
		this.importStrategy = importStrategy;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the logs
	 */
	public List<DELogPerExecution> getLogs() {
		return logs;
	}

	/**
	 * @param logs the logs to set
	 */
	public void setLogs(List<DELogPerExecution> logs) {
		this.logs = logs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the dayOfWeek
	 */
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}



	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	@Override
	public XmlWrapper getXmlWrapperInstance() {
		return new WrapperSourceSetting(this);
	}

	public String getLanguage() {		
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Sdm getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(Sdm attachedFile) {
		this.attachedFile = attachedFile;
	}

	public Sdm getPreviousAttachedFile() {
		return previousAttachedFile;
	}

	public void setPreviousAttachedFile(Sdm previousAttachedFile) {
		this.previousAttachedFile = previousAttachedFile;
	}

    private void initSelectedFields() {
        importFlds = new HashMap<String, Boolean>();
        updateFlds = new HashMap<String, Boolean>();
        if (fields != null && !fields.isEmpty()) {
            for (String fld : fields) {
                StringTokenizer st = new StringTokenizer(fld, "|||");
                String name = st.nextToken();
                String imp = st.nextToken();
                String upd = st.nextToken();
                importFlds.put(name.trim(), new Boolean(imp));
                updateFlds.put(name.trim(), new Boolean(upd));
            }
        }

    }

    public boolean importEnabled(String fldName) {
        boolean retVal = false;
        if (importFlds == null) {
            initSelectedFields();
        }

        if (importFlds.containsKey(fldName)) {
            retVal = importFlds.get(fldName).booleanValue();
        }

        return retVal;
    }

    public boolean updateEnabled(String fldName) {
        boolean retVal = false;

        if (updateFlds == null) {
            initSelectedFields();
        }

        if (updateFlds.containsKey(fldName)) {
            retVal = updateFlds.get(fldName).booleanValue();
        }

        return retVal;

    }
	
}
