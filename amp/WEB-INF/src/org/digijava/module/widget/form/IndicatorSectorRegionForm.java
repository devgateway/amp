package org.digijava.module.widget.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.gis.helper.IndicatorSectorWithSubgroup;

/**
 *
 * @author medea
 */
public class IndicatorSectorRegionForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private List<IndicatorSector> indSectList;
	private List<IndicatorSector> allIndSectList;
	private List<IndicatorSectorWithSubgroup> indSectsWithSubGroups;
    private List<AmpIndicator> indicators;
    private Long selIndicator;
    private List<AmpCategoryValueLocations> regions;
    private Long selRegionId;
    private AmpSector sector;
    private Long indSectId;
    private List<AmpIndicatorValue> values;
    private int deleteValIndex;
    private String indicatorName;
    private String sectorName;
    private String regionName;
    //pagination
    private Collection pages = null;
    private int selectedPage;
    private long pages1;
    private int offset;
	private int pagesSize;
	//alphabet
	private String[] alphaPages = null;
	private String alpha;
	private String currentAlpha;
    //used for search , filter
    private String keyWord;
    private Integer resultsPerPage;
	private String sortBy;
	private Collection  Sectors;
	private Long sectorId = new Long(-1);	
	private Long regionId=new Long(-1);	

	public List<IndicatorSectorWithSubgroup> getIndSectsWithSubGroups() {
		return indSectsWithSubGroups;
	}

	public void setIndSectsWithSubGroups(
			List<IndicatorSectorWithSubgroup> indSectsWithSubGroups) {
		this.indSectsWithSubGroups = indSectsWithSubGroups;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public Collection getSectors() {
		return Sectors;
	}

	public void setSectors(Collection sectors) {
		Sectors = sectors;
	}

	public Integer getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(Integer resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

    public String[] getAlphaPages() {
		return alphaPages;
	}

	public void setAlphaPages(String[] alphaPages) {
		this.alphaPages = alphaPages;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getCurrentAlpha() {
		return currentAlpha;
	}

	public void setCurrentAlpha(String currentAlpha) {
		this.currentAlpha = currentAlpha;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

    public Collection getPages() {
		return pages;
	}

	public void setPages(Collection pages) {
		this.pages = pages;
	}

	public long getPages1() {
		return pages1;
	}

	public void setPages1(long pages1) {
		this.pages1 = pages1;
	}

	public int getOffset() {
		int value;
		if (getSelectedPage()> (Constants.PAGES_TO_SHOW/2)){
			value = (this.getSelectedPage() - (Constants.PAGES_TO_SHOW/2))-1;
		}
		else {
			value = 0;
		}
		setOffset(value);
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPagesSize() {
		return pagesSize;
	}

	public void setPagesSize(int pagesSize) {
		this.pagesSize = pagesSize;
	}

	public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public int getDeleteValIndex() {
        return deleteValIndex;
    }

    public void setDeleteValIndex(int deleteValIndex) {
        this.deleteValIndex = deleteValIndex;
    }

    public List<AmpIndicatorValue> getValues() {
        return values;
    }

    public void setValues(List<AmpIndicatorValue> values) {
        this.values = values;
    }

    public Long getIndSectId() {
        return indSectId;
    }

    public void setIndSectId(Long indSectId) {
        this.indSectId = indSectId;
    }

    public Long getSelRegionId() {
        return selRegionId;
    }

    public void setSelRegionId(Long selRegionId) {
        this.selRegionId = selRegionId;
    }

    public List<AmpCategoryValueLocations> getRegions() {
        return regions;
    }

    public void setRegions(List<AmpCategoryValueLocations> regions) {
        this.regions = regions;
    }

    public Long getSelIndicator() {
        return selIndicator;
    }

    public void setSelIndicator(Long selIndicator) {
        this.selIndicator = selIndicator;
    }

    public List<AmpIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<AmpIndicator> indcators) {
        this.indicators = indcators;
    }

    public AmpSector getSector() {
        return sector;
    }

    public void setSector(AmpSector sector) {
        this.sector = sector;
    }

    public List<IndicatorSector> getIndSectList() {
        return indSectList;
    }

    public void setIndSectList(List<IndicatorSector> indSectList) {
        this.indSectList = indSectList;
    }

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<IndicatorSector> getAllIndSectList() {
		return allIndSectList;
	}

	public void setAllIndSectList(List<IndicatorSector> allIndSectList) {
		this.allIndSectList = allIndSectList;
	}
    
}
