package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class OrgManagerForm
    extends ActionForm {

  //private Collection organisation;
  //private Collection pages;

  private Long ampOrgTypeId;
  private Long oldAmpOrgTypeId;
  private String orgType;
  private String oldOrgType;

private String keyword;
  private int numResults;
  private int tempNumResults;
  private Collection orgTypes = null;
  private Integer currentPage;
  private String currentAlpha;
  private boolean startAlphaFlag;
  private Collection cols = null;
  private Collection<AmpOrganisation> pagedCol = null;
  private Collection colsAlpha = null;
  private Collection pages = null;
  private boolean orgSelReset;
  private boolean reset;
  private boolean orgPopupReset;
  private boolean viewAll;
  private String alpha;
  private Boolean added;
  private int pagesToShow;
  private int pagesSize;
  private int offset;
  private String sortBy;
  private boolean adminSide;

  private String[] digitPages;
  private String[] alphaPages = null;

    public boolean isAdminSide() {
        return adminSide;
    }

    public void setAdminSide(boolean adminSide) {
        this.adminSide = adminSide;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public OrgManagerForm() {
        reset = false;
        orgPopupReset = true;
        numResults = 0;
        tempNumResults = 0;
        //ampOrgTypeId = new Long(-1);
    }

  public void reset(ActionMapping mapping, HttpServletRequest request) {

    if (reset) {
      numResults = 0;
      cols = null;
      colsAlpha = null;
      orgTypes = null;
      pages = null;
      alphaPages = null;
      reset = false;
      currentPage = new Integer(0);
      currentAlpha = null;
     
    }

    if (orgSelReset) {
      pagedCol = null;
      keyword = null;
      setOrgType("");
      setAmpOrgTypeId(null);
      setOldAmpOrgTypeId(null);
      setTempNumResults(10);
    }
  }

  /**
   * @return Returns the alphaPages.
   */
  public String[] getAlphaPages() {
    return alphaPages;
  }

  /**
   * @param alphaPages The alphaPages to set.
   */
  public void setAlphaPages(String[] alphaPages) {
    this.alphaPages = alphaPages;
  }

  /**
   * @return Returns the ampOrgTypeId.
   */
  public Long getAmpOrgTypeId() {
    return ampOrgTypeId;
  }

  /**
   * @param ampOrgTypeId The ampOrgTypeId to set.
   */
  public void setAmpOrgTypeId(Long ampOrgTypeId) {
    this.ampOrgTypeId = ampOrgTypeId;
  }

  /**
   * @return Returns the colsAlpha.
   */
  public Collection getColsAlpha() {
    return colsAlpha;
  }

  /**
   * @param colsAlpha The colsAlpha to set.
   */
  public void setColsAlpha(Collection colsAlpha) {
    this.colsAlpha = colsAlpha;
  }

  /**
   * @return Returns the currentAlpha.
   */
  public String getCurrentAlpha() {
    return currentAlpha;
  }
  
  /**
   * 
   * @param Alpha
   * @return
   */
  public int getAlphaPosition(String Alpha){
      int alphaPosition=1;
      for (int i = 0; i < alphaPages.length; i++) {
        if(alphaPages[i].equalsIgnoreCase(alpha)){
            alphaPosition = i;
            break;
        }
      }
      return alphaPosition;
  }

  /**
   * @param currentAlpha The currentAlpha to set.
   */
  public void setCurrentAlpha(String currentAlpha) {
    this.currentAlpha = currentAlpha;
  }

  /**
   * @return Returns the currentPage.
   */
  public Integer getCurrentPage() {
    return currentPage;
  }

  /**
   * @param currentPage The currentPage to set.
   */
  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * @return Returns the keyword.
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * @param keyword The keyword to set.
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  /**
   * @return Returns the orgType.
   */
  public String getOrgType() {
    return orgType;
  }

  /**
   * @param orgType The orgType to set.
   */
  public void setOrgType(String orgType) {
    this.orgType = orgType;
  }

  /**
   * @return Returns the orgTypes.
   */
  public Collection getOrgTypes() {
    return orgTypes;
  }

  /**
   * @param orgTypes The orgTypes to set.
   */
  public void setOrgTypes(Collection orgTypes) {
    this.orgTypes = orgTypes;
  }

  /**
   * @return Returns the pagedCol.
   */
  public Collection<AmpOrganisation> getPagedCol() {
    return pagedCol;
  }

  /**
   * @param pagedCol The pagedCol to set.
   */
  public void setPagedCol(Collection<AmpOrganisation> pagedCol) {
    this.pagedCol = pagedCol;
  }

  /**
   * @return Returns the pages.
   */
  public Collection getPages() {
    return pages;
  }

  public void setPages(Collection pages) {
      this.pages = pages;
      if(pages!=null)
      {    
          this.pagesSize=pages.size();
      }
  }
  
  public int getPagesSize() {
    return pagesSize;
}

public void setPagesSize(int pagesSize) {
    this.pagesSize = pagesSize;
}

  /**
   * @return Returns the startAlphaFlag.
   */
  public boolean isStartAlphaFlag() {
    return startAlphaFlag;
  }

  /**
   * @param startAlphaFlag The startAlphaFlag to set.
   */
  public void setStartAlphaFlag(boolean startAlphaFlag) {
    this.startAlphaFlag = startAlphaFlag;
  }

  /**
   * @return Returns the numResults.
   */
  public int getNumResults() {
    return numResults;
  }

  /**
   * @param numResults The numResults to set.
   */
  public void setNumResults(int numResults) {
    this.numResults = numResults;
  }

  /**
   * @return Returns the tempNumResults.
   */
  public int getTempNumResults() {
    return tempNumResults;
  }

  /**
   * @param tempNumResults The tempNumResults to set.
   */
  public void setTempNumResults(int tempNumResults) {
    this.tempNumResults = tempNumResults;
  }

  /**
   * @return Returns the cols.
   */
  public Collection getCols() {
    return cols;
  }

  /**
   * @param cols The cols to set.
   */
  public void setCols(Collection cols) {
    this.cols = cols;
  }

  /**
   * @return Returns the orgSelReset.
   */
  public boolean isOrgSelReset() {
    return orgSelReset;
  }

  /**
   * @param orgSelReset The orgSelReset to set.
   */
  public void setOrgSelReset(boolean orgSelReset) {
    this.orgSelReset = orgSelReset;
  }

  /**
   * @return Returns the orgPopupReset.
   */
  public boolean isOrgPopupReset() {
    return orgPopupReset;
  }

  /**
   * @param orgPopupReset The orgPopupReset to set.
   */
  public void setOrgPopupReset(boolean orgPopupReset) {
    this.orgPopupReset = orgPopupReset;
  }

  /**
   * @return Returns the reset.
   */
  public boolean isReset() {
    return reset;
  }

  public boolean isViewAll() {
    return viewAll;
  }

  public String getAlpha() {
    return alpha;
  }

  public Boolean getAdded() {
    return added;
  }

  /**
   * @param reset The reset to set.
   */
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  public void setViewAll(boolean viewAll) {
    this.viewAll = viewAll;
  }

  public void setAlpha(String alpha) {
    this.alpha = alpha;
  }

  public void setAdded(Boolean added) {
    this.added = added;
  }

public int getPagesToShow() {
    return pagesToShow;
}

public void setPagesToShow(int pagesToShow) {
    this.pagesToShow = pagesToShow;
}

public int getOffset() {
    int value;
    if (getCurrentPage()> (this.getPagesToShow()/2)){
        value = (this.getCurrentPage() - (this.getPagesToShow()/2))-1;
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
public String getOldOrgType() {
    return oldOrgType;
}

public void setOldOrgType(String oldOrgType) {
    this.oldOrgType = oldOrgType;
}
public Long getOldAmpOrgTypeId() {
    return oldAmpOrgTypeId;
}

public void setOldAmpOrgTypeId(Long oldAmpOrgTypeId) {
    this.oldAmpOrgTypeId = oldAmpOrgTypeId;
}

public void setDigitPages(String[] digitPages) {
    this.digitPages = digitPages;
}

public String[] getDigitPages() {
    return digitPages;
}
}
