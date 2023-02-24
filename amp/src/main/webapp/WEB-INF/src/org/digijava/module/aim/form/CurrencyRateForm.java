/*
 * CurrencyRateForm.java
 * Created : 30-Apr-2005
 */
package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

public class CurrencyRateForm
    extends ActionForm {
  private Collection currencyCodes;
  private String filterByDateFrom;
  private String filterByDateTo;
  private Collection pages;
  private Integer currentPage;
  private Collection currencyRates;
  private Collection allRates;
  private String filterByCurrCode;
  private int numResultsPerPage;
  private String view;
  private String prevFromDate = "";
  private String prevToDate = "";
  private boolean filtered;

  private String updateCRateDate;
  private String updateCRateCode;
  private String updateCRateAmount;
  private Long updateCRateId;

  private String doAction;

  private Long selectedRates[];
  private String ratesFile;

  private boolean reset;

  private FormFile currRateFile;
  private Boolean clean;
  private List<LabelValueBean> timePeriods;
  private int timePeriod;
  private String lastRateUpdate;

  
  /**
   * @return Returns the allRates.
   */
  public Collection getAllRates() {
    return allRates;
  }

  /**
   * @param allRates The allRates to set.
   */
  public void setAllRates(Collection allRates) {
    this.allRates = allRates;
  }

  /**
   * @return Returns the currencyCodes.
   */
  public Collection getCurrencyCodes() {
    return currencyCodes;
  }

  /**
   * @param currencyCodes The currencyCodes to set.
   */
  public void setCurrencyCodes(Collection currencyCodes) {
    this.currencyCodes = currencyCodes;
  }

  /**
   * @return Returns the currencyRates.
   */
  public Collection getCurrencyRates() {
    return currencyRates;
  }

  /**
   * @param currencyRates The currencyRates to set.
   */
  public void setCurrencyRates(Collection currencyRates) {
    this.currencyRates = currencyRates;
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
   * @return Returns the pages.
   */
  public Collection getPages() {
    return pages;
  }

  /**
   * @param pages The pages to set.
   */
  public void setPages(Collection pages) {
    this.pages = pages;
  }

  /**
   * @return Returns the filterByCurrCode.
   */
  public String getFilterByCurrCode() {
    return filterByCurrCode;
  }

  /**
   * @param filterByCurrCode The filterByCurrCode to set.
   */
  public void setFilterByCurrCode(String filterByCurrCode) {
    this.filterByCurrCode = filterByCurrCode;
  }

  /**
   * @return Returns the numResultsPerPage.
   */
  public int getNumResultsPerPage() {
    return numResultsPerPage;
  }

  /**
   * @param numResultsPerPage The numResultsPerPage to set.
   */
  public void setNumResultsPerPage(int numResultsPerPage) {
    this.numResultsPerPage = numResultsPerPage;
  }

  /**
   * @return Returns the view.
   */
  public String getView() {
    return view;
  }

  /**
   * @param view The view to set.
   */
  public void setView(String view) {
    this.view = view;
  }

  /**
   * @return Returns the filtered.
   */
  public boolean isFiltered() {
    return filtered;
  }

  /**
   * @param filtered The filtered to set.
   */
  public void setFiltered(boolean filtered) {
    this.filtered = filtered;
  }

  /**
   * @return Returns the filterByDateFrom.
   */
  public String getFilterByDateFrom() {
    return filterByDateFrom;
  }

  /**
   * @param filterByDateFrom The filterByDateFrom to set.
   */
  public void setFilterByDateFrom(String filterByDateFrom) {
    this.filterByDateFrom = filterByDateFrom;
  }

  /**
   * @return Returns the filterByDateTo.
   */
  public String getFilterByDateTo() {
    return filterByDateTo;
  }

  /**
   * @param filterByDateTo The filterByDateTo to set.
   */
  public void setFilterByDateTo(String filterByDateTo) {
    this.filterByDateTo = filterByDateTo;
  }

  /**
   * @return Returns the updateCRateAmount.
   */
  public String getUpdateCRateAmount() {
    return updateCRateAmount;
  }

  /**
   * @param updateCRateAmount The updateCRateAmount to set.
   */
  public void setUpdateCRateAmount(String updateCRateAmount) {
    this.updateCRateAmount = updateCRateAmount;
  }

  /**
   * @return Returns the updateCRateCode.
   */
  public String getUpdateCRateCode() {
    return updateCRateCode;
  }

  /**
   * @param updateCRateCode The updateCRateCode to set.
   */
  public void setUpdateCRateCode(String updateCRateCode) {
    this.updateCRateCode = updateCRateCode;
  }

  /**
   * @return Returns the updateCRateDate.
   */
  public String getUpdateCRateDate() {
    return updateCRateDate;
  }

  /**
   * @param updateCRateDate The updateCRateDate to set.
   */
  public void setUpdateCRateDate(String updateCRateDate) {
    this.updateCRateDate = updateCRateDate;
  }

  /**
   * @return Returns the doAction.
   */
  public String getDoAction() {
    return doAction;
  }

  /**
   * @param doAction The doAction to set.
   */
  public void setDoAction(String doAction) {
    this.doAction = doAction;
  }

  /**
   * @return Returns the reset.
   */
  public boolean isReset() {
    return reset;
  }

  /**
   * @param reset The reset to set.
   */
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    if (reset) {
      updateCRateAmount = null;
      updateCRateCode = null;
      updateCRateDate = null;
      updateCRateId = null;
    }
  }

  /**
   * @return Returns the updateCRateId.
   */
  public Long getUpdateCRateId() {
    return updateCRateId;
  }

  /**
   * @param updateCRateId The updateCRateId to set.
   */
  public void setUpdateCRateId(Long updateCRateId) {
    this.updateCRateId = updateCRateId;
  }

  /**
   * @return Returns the prevFromDate.
   */
  public String getPrevFromDate() {
    return prevFromDate;
  }

  /**
   * @param prevFromDate The prevFromDate to set.
   */
  public void setPrevFromDate(String prevFromDate) {
    this.prevFromDate = prevFromDate;
  }

  /**
   * @return Returns the prevToDate.
   */
  public String getPrevToDate() {
    return prevToDate;
  }

  /**
   * @param prevToDate The prevToDate to set.
   */
  public void setPrevToDate(String prevToDate) {
    this.prevToDate = prevToDate;
  }

  /**
   * @return Returns the selectedRates.
   */
  public Long[] getSelectedRates() {
    return selectedRates;
  }

  /**
   * @param selectedRates The selectedRates to set.
   */
  public void setSelectedRates(Long[] selectedRates) {
    this.selectedRates = selectedRates;
  }

  /**
   * @return Returns the ratesFile.
   */
  public String getRatesFile() {
    return ratesFile;
  }

  /**
   * @param ratesFile The ratesFile to set.
   */
  public void setRatesFile(String ratesFile) {
    this.ratesFile = ratesFile;
  }

  /**
   * @return Returns the currRateFile.
   */
  public FormFile getCurrRateFile() {
    return currRateFile;
  }

  public Boolean isClean() {
    return clean;
  }

  /**
   * @param currRateFile The currRateFile to set.
   */
  public void setCurrRateFile(FormFile currRateFile) {
    this.currRateFile = currRateFile;
  }

  public void setClean(Boolean clean) {
    this.clean = clean;
  }

public List<LabelValueBean> getTimePeriods() {
    return timePeriods;
}

public void setTimePeriods(List<LabelValueBean> timePeriods) {
    this.timePeriods = timePeriods;
}

public int getTimePeriod() {
    return timePeriod;
}

public void setTimePeriod(int timePeriod) {
    this.timePeriod = timePeriod;
}

public String getLastRateUpdate() {
    return lastRateUpdate;
}

public void setLastRateUpdate(String lastRateUpdate) {
    this.lastRateUpdate = lastRateUpdate;
}
}
