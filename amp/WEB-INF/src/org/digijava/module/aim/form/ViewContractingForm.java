/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

import java.util.List;

/**
 *
 * @author medea
 */
public class ViewContractingForm extends ActionForm {
    

    private List contracts;
    
    private Long ampActivityId;
    private String tabIndex;
    private String currCode;
    private List<AmpFundingDetail> fundingDetailsLinked;
    
    public List<AmpFundingDetail> getFundingDetailsLinked() {
        return fundingDetailsLinked;
    }
    public void setFundingDetailsLinked(List<AmpFundingDetail> fundingDetailsLinked) {
        this.fundingDetailsLinked = fundingDetailsLinked;
    }
    public String getCurrCode() {
        return currCode;
    }
    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }
    /**
     * @return Returns the contracts.
     */
    public List getContracts() {
        return contracts;
    }
    /**
     * @param contracts The contracts to set.
     */
    public void setContracts(List contracts) {
        this.contracts = contracts;
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
}
