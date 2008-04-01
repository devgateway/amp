package org.digijava.module.aim.form ;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class QuarterlyInfoForm extends DetailedInfoForm
{
	private Collection quarterlyInfo;
	private int fiscalYrGrp;
	private int fiscalQtrGrp;
	private String clicked;
	private Collection discrepancies;
	private Collection fiscalYears;
	private long fiscalCalId;
        private String totalPlanned ;

    public String getTotalActual() {
        return totalActual;
    }

    public void setTotalActual(String totalActual) {
        this.totalActual = totalActual;
    }

    public String getTotalPlanned() {
        return totalPlanned;
    }

    public void setTotalPlanned(String totalPlanned) {
        this.totalPlanned = totalPlanned;
    }
	private String totalActual ;
	
	public Collection getDiscrepancies() {
		return discrepancies;
	}
	
	public void setDiscrepancies(Collection discrepancies) {
		this.discrepancies = discrepancies;
	}
		
	/**
	 * @return
	 */
	public Collection getQuarterlyInfo() {
		return quarterlyInfo;
	}
	/**
	 * @param collection
	 */
	public void setQuarterlyInfo(Collection collection) {
		quarterlyInfo = collection;
	}
	
	public ActionErrors validate(ActionMapping actionMapping,
									 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}	
	/**
	 * @return
	 */
	public int getFiscalQtrGrp() {
		return fiscalQtrGrp;
	}

	/**
	 * @return
	 */
	public int getFiscalYrGrp() {
		return fiscalYrGrp;
	}

	/**
	 * @param i
	 */
	public void setFiscalQtrGrp(int i) {
		fiscalQtrGrp = i;
	}

	/**
	 * @param i
	 */
	public void setFiscalYrGrp(int i) {
		fiscalYrGrp = i;
	}
	
	/**
	 * @return
	 */
	public String getClicked() {
		return clicked;
	}

	/**
	 * @param string
	 */
	public void setClicked(String string) {
		clicked = string;
	}

	public long getFiscalCalId() {	
		return fiscalCalId;
	}

	/**
	 * @param i
	 */
	public void setFiscalCalId(long i) {
		fiscalCalId = i;
	}

	public void setFiscalYears(Collection c)
	{
		fiscalYears=c;
	}

	public Collection getFiscalYears()
	{
		return fiscalYears;
	}

}
	
	
