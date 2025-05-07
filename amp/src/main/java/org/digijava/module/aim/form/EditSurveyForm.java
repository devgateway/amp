/*
 * Created on 9/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;
import java.util.List;

public class EditSurveyForm extends ActionForm {

    private List indicators = null;      // holds collection of Indicator helper objects
    private Collection pages = null;     // total number of survey pages
    private Integer currentPage = null;
    private Integer offset = null;       // starting record for iteartion over indicator collection on each page

    private String fundingOrg = null;    // acronym of funding organisation
    private String deliveryDonor = null;

    private Long ampSurveyId = null;

    private Long ampActivityId;
    private String tabIndex;
    private Boolean reset = null;

    /**
     * @return Returns the indicator.
     */
    public List getIndicators() {
        return indicators;
    }
    /**
     * @param indicator The indicator to set.
     */
    public void setIndicators(List indicators) {
        this.indicators = indicators;
    }
    /**
     * @return Returns the page.
     */
    public Integer getOffset() {
        return offset;
    }
    /**
     * @param page The page to set.
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
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
     * @return Returns the surveyId.
     */
    public Long getAmpSurveyId() {
        return ampSurveyId;
    }
    /**
     * @param surveyId The surveyId to set.
     */
    public void setAmpSurveyId(Long ampSurveyId) {
        this.ampSurveyId = ampSurveyId;
    }
    /**
     * @return Returns the fundingOrg.
     */
    public String getFundingOrg() {
        return fundingOrg;
    }
    /**
     * @param fundingOrg The fundingOrg to set.
     */
    public void setFundingOrg(String fundingOrg) {
        this.fundingOrg = fundingOrg;
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
     * @return Returns the ampActivityId.
     */
    public Long getAmpActivityId() {
        return ampActivityId;
    }
    /**
     * @param ampActivityId The ampActivityId to set.
     */
    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }
    /**
     * @return Returns the tabIndex.
     */
    public String getTabIndex() {
        return tabIndex;
    }
    /**
     * @param tabIndex The tabIndex to set.
     */
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }
    /**
     * @return Returns the reset.
     */
    public Boolean getReset() {
        return reset;
    }

    public String getDeliveryDonor() {
        return deliveryDonor;
    }

    /**
     * @param reset The reset to set.
     */
    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    public void setDeliveryDonor(String deliveryDonor) {
        this.deliveryDonor = deliveryDonor;
    }

}
