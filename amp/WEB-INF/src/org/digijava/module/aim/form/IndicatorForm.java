package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.helper.ActivitySector;



public class IndicatorForm extends ActionForm implements Serializable
{
	private Long indId = null;
	private Long activityId;
	private String indicatorName = null;
	private String indicatorDesc = null;
	private String indicatorCode = null;
	private String searchKey = null;
	private boolean defaultFlag;
	private Long selectedIndicator = null;
	private Collection searchReturn = null;
	private Collection indicators = null;
	private Collection nondefaultindicators = null;	
	private Collection indicatorValues = null;
	private Collection meIndActList = null;
	private String sameIndicatorName = null;
	private String sameIndicatorCode = null;
	private boolean errorFlag;
	private String event;
	private Long selectedIndicators[];
	private Long selIndicators[];
	private String searchkey = null;
	private String addswitch = null;
	private boolean noSearchResult = false;
	
	private Collection <AmpSector> allSectors;
	private Collection <AmpSector> parentSectors; //<---- this field should be deleted
	private Collection <ActivitySector> selectedSectorsForInd;
	private Collection <AmpSectorScheme> sectorSchemes;	
	private Long selActivitySector[];
	private Long sectorScheme;
	private Long sector;
	private String creationDate;
	private String sectorName;
	private String action ;
	private boolean showAddInd; //show or hide add Indicator fields on add Indicator page
	
	private char ascendingInd;
	
	
	public void resetsector(){
    	this.sector = new Long(-1);    	
    	this.sectorScheme = new Long(-1);
    	this.parentSectors = null;  
    	this.showAddInd=false;
    }
	
	public String getSameIndicatorCode() {
		return sameIndicatorCode;
	}

	public void setSameIndicatorCode(String sameIndicatorCode) {
		this.sameIndicatorCode = sameIndicatorCode;
	}	
	
	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public Long getSectorScheme() {
		return sectorScheme;
	}

	public void setSectorScheme(Long sectorScheme) {
		this.sectorScheme = sectorScheme;
	}

	public String getSameIndicatorName() {
		return sameIndicatorName;
	}

	public void setSameIndicatorName(String sameIndicatorName) {
		this.sameIndicatorName = sameIndicatorName;
	}

	public Collection getIndicators() {
		return indicators;
	}

	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
	}

	public boolean getDefaultFlag() {
		return defaultFlag;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public Collection getIndicatorValues() {
		return indicatorValues;
	}

	public void setIndicatorValues(Collection indicatorValues) {
		this.indicatorValues = indicatorValues;
	}

	public Collection getMeIndActList() {
		return meIndActList;
	}

	public void setMeIndActList(Collection meIndActList) {
		this.meIndActList = meIndActList;
	}
	
	
	
	public Collection<AmpSector> getParentSectors() {
		return parentSectors;
	}

	public void setParentSectors(Collection<AmpSector> parentSectors) {
		this.parentSectors = parentSectors;
	}

	public Collection<ActivitySector> getSelectedSectorsForInd() {
		return selectedSectorsForInd;
	}

	public void setSelectedSectorsForInd(Collection<ActivitySector> selectedSectorsForInd) {
		this.selectedSectorsForInd = selectedSectorsForInd;
	}

	public Collection<AmpSectorScheme> getSectorSchemes() {
		return sectorSchemes;
	}

	public void setSectorSchemes(Collection<AmpSectorScheme> sectorScheme) {
		this.sectorSchemes = sectorScheme;
	}

	public Long getSelectedIndicator() {
		return selectedIndicator;
	}

	public void setSelectedIndicator(Long selectedIndicator) {
		this.selectedIndicator = selectedIndicator;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Collection getSearchReturn() {
		return searchReturn;
	}

	public void setSearchReturn(Collection searchReturn) {
		this.searchReturn = searchReturn;
	}

	public Long getIndId() {
		return indId;
	}

	public void setIndId(Long indId) {
		this.indId = indId;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return Returns the nondefaultindicators.
	 */
	public Collection getNondefaultindicators() {
		return nondefaultindicators;
	}

	/**
	 * @param nondefaultindicators The nondefaultindicators to set.
	 */
	public void setNondefaultindicators(Collection nondefaultindicators) {
		this.nondefaultindicators = nondefaultindicators;
	}

	/**
	 * @return Returns the searchkey.
	 */
	public String getSearchkey() {
		return searchkey;
	}

	/**
	 * @param searchkey The searchkey to set.
	 */
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	/**
	 * @return Returns the selectedIndicators.
	 */
	public Long[] getSelectedIndicators() {
		return selectedIndicators;
	}

	/**
	 * @param selectedIndicators The selectedIndicators to set.
	 */
	public void setSelectedIndicators(Long[] selectedIndicators) {
		this.selectedIndicators = selectedIndicators;
	}

	/**
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return Returns the addswitch.
	 */
	public String getAddswitch() {
		return addswitch;
	}

	/**
	 * @param addswitch The addswitch to set.
	 */
	public void setAddswitch(String addswitch) {
		this.addswitch = addswitch;
	}

	/**
	 * @return Returns the selIndicators.
	 */
	public Long[] getSelIndicators() {
		return selIndicators;
	}

	/**
	 * @param selIndicators The selIndicators to set.
	 */
	public void setSelIndicators(Long[] selIndicators) {
		this.selIndicators = selIndicators;
	}

	/**
	 * @return Returns the noSearchResult.
	 */
	public boolean getNoSearchResult() {
		return noSearchResult;
	}

	/**
	 * @param noSearchResult The noSearchResult to set.
	 */
	public void setNoSearchResult(boolean noSearchResult) {
		this.noSearchResult = noSearchResult;
	}

	/**
	 * @return Returns the ascendingInd.
	 */
	public char getAscendingInd() {
		return ascendingInd;
	}

	/**
	 * @param ascendingInd The ascendingInd to set.
	 */
	public void setAscendingInd(char ascendingInd) {
		this.ascendingInd = ascendingInd;
	}

	/**
	 * @return Returns the defaultFlag.
	 */
	public boolean isDefaultFlag() {
		return defaultFlag;
}
	/**
	 * @param defaultFlag The defaultFlag to set.
	 */
	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		defaultFlag = false;
	}

	public Collection<AmpSector> getAllSectors() {
		return allSectors;
	}

	public void setAllSectors(Collection<AmpSector> allSectors) {
		this.allSectors = allSectors;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long[] getSelActivitySector() {
		return selActivitySector;
	}

	public void setSelActivitySector(Long[] selActivitySector) {
		this.selActivitySector = selActivitySector;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isShowAddInd() {
		return showAddInd;
	}

	public void setShowAddInd(boolean showAddInd) {
		this.showAddInd = showAddInd;
	}
}
