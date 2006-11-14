package org.digijava.module.aim.form;
import java.util.*;

import org.apache.struts.action.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ProposedProjCost extends ActionForm{
    private Long actID;
    private String usID;
    private Long fundingID;
    private Long currencyID;
    private String currencyCode;
    private Double funAmount;
    private String funDate;

    public Double getFunAmount() {
        return funAmount;
    }

    public String getFunDate() {
        return funDate;
    }

    public Long getFundingID() {
        return fundingID;
    }

    public Long getActID() {
        return actID;
    }

    public String getUsID() {
        return usID;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Long getCurrencyID() {
        return currencyID;
    }

    public void setFundingID(Long fundingID) {
        this.fundingID = fundingID;
    }

    public void setFunDate(String funDate) {
        this.funDate = funDate;
    }

    public void setFunAmount(Double funAmount) {
        this.funAmount = funAmount;
    }

    public void setActID(Long actID) {
        this.actID = actID;
    }

    public void setUsID(String usID) {
        this.usID = usID;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setCurrencyID(Long currencyID) {
        this.currencyID = currencyID;
    }

}
