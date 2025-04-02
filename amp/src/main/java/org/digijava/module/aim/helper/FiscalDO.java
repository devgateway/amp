package org.digijava.module.aim.helper ;

public class FiscalDO
{
    int fiscalYear ;
    int fiscalQuarter ; 
    
    
    /**
     * @return
     */
    public int getFiscalQuarter() {
        return fiscalQuarter;
    }

    /**
     * @return
     */
    public int getFiscalYear() {
        return fiscalYear;
    }

    /**
     * @param i
     */
    public void setFiscalQuarter(int i) {
        fiscalQuarter = i;
    }

    /**
     * @param i
     */
    public void setFiscalYear(int i) {
        fiscalYear = i;
    }
    
}
