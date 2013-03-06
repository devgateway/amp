/**
 * 
 */
package org.digijava.module.dataExchange.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.sdm.dbentity.Sdm;

/**
 * @author dan
 *
 */
public class CreateSourceForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1898016793971504413L;
	
	private String name;
	private String source;
	private String approvalStatus;
	private String uniqueIdentifier;
	private String importStrategy;
	private Long teamId;
	
	private String[] languages;
	private List<KeyValue> importStrategyValues;
	private List<KeyValue> sourceValues;
	private List<KeyValue> approvalStatusValues;
	private Collection<AmpTeam> teamValues;
	private AmpColumnEntry activityTree = null;
	
	private FormFile uploadedFile;
	private String[] selectedLanguages;
	
	//private String[] options;
	private String[] selectedOptions;
	
	private String frequency;
	private String dayOfWeek;
	private String time;
	
	private Long sourceId;
	
	//holds attached files	
	private Sdm sdmDocument;
	
	private List<ComponentFM> listComponents;
	
	private String [] moduleId;
	private String [] moduleImport;
	private String [] moduleAOW;
	
	public class ComponentFM{
		private long id;
		private String name;
		private String iatiName;
		private boolean state;
		private boolean importComp;
		private boolean autoOverwrite;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isState() {
			return state;
		}
		public void setState(boolean state) {
			this.state = state;
		}
		public boolean isImportComp() {
			return importComp;
		}
		public void setImportComp(boolean importComp) {
			this.importComp = importComp;
		}
		public boolean isAutoOverwrite() {
			return autoOverwrite;
		}
		public void setAutoOverwrite(boolean autoOverwrite) {
			this.autoOverwrite = autoOverwrite;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getIatiName() {
			return iatiName;
		}
		public void setIatiName(String iatiName) {
			this.iatiName = iatiName;
		}
	}	

	public String[] getSelectedLanguages() {
		return selectedLanguages;
	}

	public void setSelectedLanguages(String[] selectedLanguages) {
		this.selectedLanguages = selectedLanguages;
	}

	public FormFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}


	public String[] getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(String[] selectedOptions) {
		this.selectedOptions = selectedOptions;
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
	 * @return the importStrategyValues
	 */
	public List<KeyValue> getImportStrategyValues() {
		return importStrategyValues;
	}

	/**
	 * @param importStrategyValues the importStrategyValues to set
	 */
	public void setImportStrategyValues(List<KeyValue> importStrategyValues) {
		this.importStrategyValues = importStrategyValues;
	}

	/**
	 * @return the sourceValues
	 */
	public List<KeyValue> getSourceValues() {
		return sourceValues;
	}

	/**
	 * @param sourceValues the sourceValues to set
	 */
	public void setSourceValues(List<KeyValue> sourceValues) {
		this.sourceValues = sourceValues;
	}

	/**
	 * @return the activityTree
	 */
	public AmpColumnEntry getActivityTree() {
		return activityTree;
	}

	/**
	 * @param activityTree the activityTree to set
	 */
	public void setActivityTree(AmpColumnEntry activityTree) {
		this.activityTree = activityTree;
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
	 * @return the teamId
	 */
	public Long getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}


	/**
	 * @return the teamValues
	 */
	public Collection<AmpTeam> getTeamValues() {
		return teamValues;
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
	 * @param teamValues the teamValues to set
	 */
	public void setTeamValues(Collection<AmpTeam> teamValues) {
		this.teamValues = teamValues;
	}

	/**
	 * @return the approvalStatusValues
	 */
	public List<KeyValue> getApprovalStatusValues() {
		return approvalStatusValues;
	}

	/**
	 * @param approvalStatusValues the approvalStatusValues to set
	 */
	public void setApprovalStatusValues(List<KeyValue> approvalStatusValues) {
		this.approvalStatusValues = approvalStatusValues;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Sdm getSdmDocument() {
		return sdmDocument;
	}

	public void setSdmDocument(Sdm sdmDocument) {
		this.sdmDocument = sdmDocument;
	}


	public List<ComponentFM> getListComponents() {
		return listComponents;
	}

	public void setListComponents(List<ComponentFM> listComponents) {
		this.listComponents = listComponents;
	}

	public String [] getModuleId() {
		return moduleId;
	}

	public void setModuleId(String [] moduleId) {
		this.moduleId = moduleId;
	}

	public String [] getModuleImport() {
		return moduleImport;
	}

	public void setModuleImport(String [] moduleImport) {
		this.moduleImport = moduleImport;
	}

	public String [] getModuleAOW() {
		return moduleAOW;
	}

	public void setModuleAOW(String [] moduleAOW) {
		this.moduleAOW = moduleAOW;
	}
	
}
