/**
 * @author Mauricio Coria
 * @version
 */

package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;


public class SelectLocationForm extends ActionForm{
	private String keyword;
	private int tempNumResults;
	private int numResults;
	private int currentPage;
	private Collection<Integer> pages;
	private Collection cols;
	private Collection pagedCol;
	
	private Long selLocs[] = null; // location selected from step 2 page to
	private Collection searchLocs = null; // list of searched locations.
	private Long searchedLocs[] = null; // locations selected by user to be
	private String fill; // which among countries,region,zone and woreda
	// need
	// to
	// be loaded with data. ie if the value

	// of fill is 'region', load all region data beloning to a particluar
	// country selected
	private Integer impLevelValue; // Implementation Level value
	private String impCountry; // Implementation country
	
	private TreeMap<Integer, Collection<KeyValue>> locationByLayers;
	
	private TreeMap<Integer, Long> selectedLayers ;
	
	private Long parentLocId;
	
	private Long [] userSelectedLocs;
	
	@Deprecated
	private Long impRegion; // Implementation region
	@Deprecated
	private Long impMultiRegion[]; // Implementation region

	@Deprecated
	private Long impZone; // Implementation zone
	@Deprecated
	private Long impMultiZone[]; // Implementation zone

	@Deprecated
	private Long impWoreda; // Implementation woreda
	@Deprecated
	private Long impMultiWoreda[];
	
	@Deprecated
	private Collection<Country> countries;
	@Deprecated
	private Collection<AmpRegion> regions;
	@Deprecated
	private Collection<AmpZone> zones;
	@Deprecated
	private Collection<AmpWoreda> woredas;

	private String country;
	private Long levelId = null;
	private Long implemLocationLevel = null;
	private Collection<org.digijava.module.aim.helper.Location> selectedLocs = null;
	private boolean defaultCountryIsSet;
	private int pagesSize;
	private boolean reseted;
	
	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	public Long getImplemLocationLevel() {
		return implemLocationLevel;
	}

	public void setImplemLocationLevel(Long implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
	}


	public Collection<org.digijava.module.aim.helper.Location> getSelectedLocs() {
		return selectedLocs;
	}

	public void setSelectedLocs(Collection<org.digijava.module.aim.helper.Location> selectedLocs) {
		this.selectedLocs = selectedLocs;
	}

	public Long[] getSelLocs() {
		return selLocs;
	}

	public void setSelLocs(Long[] selLocs) {
		this.selLocs = selLocs;
	}

	public Collection getSearchLocs() {
		return searchLocs;
	}

	public void setSearchLocs(Collection searchLocs) {
		this.searchLocs = searchLocs;
	}

	public Long[] getSearchedLocs() {
		return searchedLocs;
	}

	public void setSearchedLocs(Long[] searchedLocs) {
		this.searchedLocs = searchedLocs;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public Integer getImpLevelValue() {
		return impLevelValue;
	}

	public void setImpLevelValue(Integer impLevelValue) {
		this.impLevelValue = impLevelValue;
	}

	public String getImpCountry() {
		return impCountry;
	}

	public void setImpCountry(String impCountry) {
		this.impCountry = impCountry;
	}

	public Long getImpRegion() {
		return impRegion;
	}

	public void setImpRegion(Long impRegion) {
		this.impRegion = impRegion;
	}

	public Long[] getImpMultiRegion() {
		return impMultiRegion;
	}

	public void setImpMultiRegion(Long[] impMultiRegion) {
		this.impMultiRegion = impMultiRegion;
	}

	public Long getImpZone() {
		return impZone;
	}

	public void setImpZone(Long impZone) {
		this.impZone = impZone;
	}

	public Long[] getImpMultiZone() {
		return impMultiZone;
	}

	public void setImpMultiZone(Long[] impMultiZone) {
		this.impMultiZone = impMultiZone;
	}

	public Long getImpWoreda() {
		return impWoreda;
	}

	public void setImpWoreda(Long impWoreda) {
		this.impWoreda = impWoreda;
	}

	public Long[] getImpMultiWoreda() {
		return impMultiWoreda;
	}

	public void setImpMultiWoreda(Long[] impMultiWoreda) {
		this.impMultiWoreda = impMultiWoreda;
	}

	

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public Collection<AmpRegion> getRegions() {
		return regions;
	}

	public void setRegions(Collection<AmpRegion> regions) {
		this.regions = regions;
	}

	public Collection<AmpZone> getZones() {
		return zones;
	}

	public void setZones(Collection<AmpZone> zones) {
		this.zones = zones;
	}

	public Collection<AmpWoreda> getWoredas() {
		return woredas;
	}

	public void setWoredas(Collection<AmpWoreda> woredas) {
		this.woredas = woredas;
	}

	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isDefaultCountryIsSet() {
		return defaultCountryIsSet;
	}

	public void setDefaultCountryIsSet(boolean defaultCountryIsSet) {
		this.defaultCountryIsSet = defaultCountryIsSet;
	}

	public int getTempNumResults() {
		return tempNumResults;
	}

	public void setTempNumResults(int tempNumResults) {
		this.tempNumResults = tempNumResults;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public Collection getCols() {
		return cols;
	}

	public void setCols(Collection cols) {
		this.cols = cols;
	}

	public Collection getPagedCol() {
		return pagedCol;
	}

	public void setPagedCol(Collection pagedCol) {
		this.pagedCol = pagedCol;
	}

	public Collection getPages() {
		return pages;
	}

	public void setPages(Collection pages) {
		this.pages = pages;
		if (pages != null) {
			this.pagesSize = pages.size();
		}
	}

	
	public TreeMap<Integer, Collection<KeyValue>> getLocationByLayers() {
		if ( locationByLayers == null )
			locationByLayers	= new TreeMap<Integer, Collection<KeyValue>>();
		return locationByLayers;
	}
	

	public TreeMap<Integer, Long> getSelectedLayers() {
		if (selectedLayers == null)
			selectedLayers		= new TreeMap<Integer, Long>();
		return selectedLayers;
	}
	
	public void setSelectedLayer(String key, Object value) {
		selectedLayers.put(Integer.parseInt(key), (Long)value);
	}
	public Long getSelectedLayer(String key) {
		return selectedLayers.get( Integer.parseInt(key) );
	}

	public Long getParentLocId() {
		return parentLocId;
	}

	public void setParentLocId(Long parentLocId) {
		this.parentLocId = parentLocId;
	}

	public Long[] getUserSelectedLocs() {
		return userSelectedLocs;
	}

	public void setUserSelectedLocs(Long[] userSelectedLocs) {
		this.userSelectedLocs = userSelectedLocs;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if(!isReseted()){
			fill = "country";
			impLevelValue = new Integer(0);
			impCountry = "";
			impRegion = new Long(-1);
			impZone = new Long(-1);
			impWoreda = new Long(-1);
			regions = null;
			zones = null;
			woredas = null;
			setReseted(true);
		}
	}

	public void setReseted(boolean reseted) {
		this.reseted = reseted;
	}

	public boolean isReseted() {
		return reseted;
	}
}