package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class SelectSectorForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 4442909317530257299L;
    private Long sector;
    private Long subsectorLevel1;
    private Long subsectorLevel2;
    private Long sectorScheme;
    private Collection sectorSchemes;
    private Collection parentSectors;
    private Collection childSectorsLevel1;
    private Collection childSectorsLevel2;
    private boolean sectorReset;
    private Collection orderedFundingOrganizations;
    
    
    private int numResults;
    private int tempNumResults;
    private String keyword;
    private Integer currentPage;
    
    private Collection cols = null;
    private Collection pagedCol;
    private Collection pages;

    private Collection searchedSectors = null; // list of searched Sectors.
    private boolean someError;
    private Long selSectors[] = null; // sectors selected by user to be added in activity after searching
    private Long configId;
    private String configName;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

        public Long getConfigId() {
            return configId;
        }

        public void setConfigId(Long configId) {
            this.configId = configId;
        }

    public Collection getSearchedSectors() {
        return searchedSectors;
    }

    public void setSearchedSectors(Collection searchedSectors) {
        this.searchedSectors = searchedSectors;
    }

    public Long[] getSelSectors() {
        return selSectors;
    }

    public void setSelSectors(Long[] selSectors) {
        this.selSectors = selSectors;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getTempNumResults() {
        return tempNumResults;
    }

    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (sectorReset) {
            sector = new Long(-1);
            subsectorLevel1 = new Long(-1);
            subsectorLevel2 = new Long(-1);
            sectorScheme = new Long(-1);
            parentSectors = null;
            childSectorsLevel1 = null;
            childSectorsLevel2 = null;
            cols=null;
            selSectors=null;
            keyword ="";
        }
    }

    public Collection getChildSectorsLevel1() {
        return childSectorsLevel1;
    }

    public void setChildSectorsLevel1(Collection childSectorsLevel1) {
        this.childSectorsLevel1 = childSectorsLevel1;
    }

    public Collection getChildSectorsLevel2() {
        return childSectorsLevel2;
    }

    public void setChildSectorsLevel2(Collection childSectorsLevel2) {
        this.childSectorsLevel2 = childSectorsLevel2;
    }

    public Collection getOrderedFundingOrganizations() {
        return orderedFundingOrganizations;
    }

    public void setOrderedFundingOrganizations(
            Collection orderedFundingOrganizations) {
        this.orderedFundingOrganizations = orderedFundingOrganizations;
    }

    public Collection getParentSectors() {
        return parentSectors;
    }

    public void setParentSectors(Collection parentSectors) {
        this.parentSectors = parentSectors;
    }

    public Long getSector() {
        return sector;
    }

    public void setSector(Long sector) {
        this.sector = sector;
    }

    public boolean isSectorReset() {
        return sectorReset;
    }

    public void setSectorReset(boolean sectorReset) {
        this.sectorReset = sectorReset;
    }

    public Long getSectorScheme() {
        return sectorScheme;
    }

    public void setSectorScheme(Long sectorScheme) {
        this.sectorScheme = sectorScheme;
    }

    public Collection getSectorSchemes() {
        return sectorSchemes;
    }

    public void setSectorSchemes(Collection sectorSchemes) {
        this.sectorSchemes = sectorSchemes;
    }


    public Long getSubsectorLevel1() {
        return subsectorLevel1;
    }

    public void setSubsectorLevel1(Long subsectorLevel1) {
        this.subsectorLevel1 = subsectorLevel1;
    }

    public Long getSubsectorLevel2() {
        return subsectorLevel2;
    }

    public void setSubsectorLevel2(Long subsectorLevel2) {
        this.subsectorLevel2 = subsectorLevel2;
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
    }
    public boolean isSomeError() {
        return someError;
    }

    public void setSomeError(boolean someError) {
        this.someError = someError;
    }
    
}
