package org.digijava.module.aim.helper;

public class FilterParams {
    
    private Long ampFundingId;
    private int transactionType;
    private String currencyCode;
    private Long fiscalCalId;
    private int fromYear;
    private int toYear;
        private int adjustmentType;

        public int getAdjustmentType() {
            return adjustmentType;
        }

        public void setAdjustmentType(int adjustmentType) {
            this.adjustmentType = adjustmentType;
        }

    /**
     * @return
     */
    public Long getAmpFundingId() {
        return ampFundingId;
    }

    /**
     * @return
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @return
     */
    public Long getFiscalCalId() {
        return fiscalCalId;
    }

    /**
     * @return
     */
    public int getFromYear() {
        return fromYear;
    }


    /**
     * @return
     */
    public int getToYear() {
        return toYear;
    }

    /**
     * @return
     */
    public int getTransactionType() {
        return transactionType;
    }

    /**
     * @param long1
     */
    public void setAmpFundingId(Long long1) {
        ampFundingId = long1;
    }

    /**
     * @param string
     */
    public void setCurrencyCode(String string) {
        currencyCode = string;
    }

    /**
     * @param long1
     */
    public void setFiscalCalId(Long long1) {
        fiscalCalId = long1;
    }

    /**
     * @param i
     */
    public void setFromYear(int i) {
        fromYear = i;
    }

    /**
     * @param i
     */
    public void setToYear(int i) {
        toYear = i;
    }

    /**
     * @param i
     */
    public void setTransactionType(int i) {
        transactionType = i;
    }

    public boolean copy(FilterParams fp)    {
        
        setAmpFundingId(fp.getAmpFundingId());
        setCurrencyCode(fp.getCurrencyCode());
        setFiscalCalId(fp.getFiscalCalId());
        setFromYear(fp.getFromYear());
        setToYear(fp.getToYear());
        setTransactionType(fp.getTransactionType());
        return true;
    }   
    
}
