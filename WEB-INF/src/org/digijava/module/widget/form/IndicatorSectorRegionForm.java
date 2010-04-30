package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;

/**
 *
 * @author medea
 */
public class IndicatorSectorRegionForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private List<IndicatorSector> indSectList;
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
    private int selectedPage;
    private long pages;
    //used for search
    private String keyWord;

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
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
    
}
