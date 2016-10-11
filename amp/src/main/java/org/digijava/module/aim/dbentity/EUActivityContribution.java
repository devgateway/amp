/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * @author mihai
 *
 */
public class EUActivityContribution implements Serializable, Comparable, Cloneable {
	private static final long serialVersionUID = -7752194026135595434L;
	
	private Long id;
	private Double amount;
	private AmpCurrency amountCurrency;
	private EUActivity euActivity;
	private AmpOrganisation donor;
	//private AmpTermsAssist financingType;
	private AmpCategoryValue financingTypeCategVal;
	//private AmpModality financingInstrument;
	private AmpCategoryValue financingInstr;
	private Date transactionDate;
	
	public EUActivity getEuActivity() {
		return euActivity;
	}

	public void setEuActivity(EUActivity euActivity) {
		this.euActivity = euActivity;
	}

	public Double getAmount() {
		return FeaturesUtil.applyThousandsForVisibility(amount);
	}

	public void setAmount(Double amount) {
		this.amount = FeaturesUtil.applyThousandsForEntry(amount);
	}

	public AmpCurrency getAmountCurrency() {
		return amountCurrency;
	}

	public void setAmountCurrency(AmpCurrency amountCurrency) {
		this.amountCurrency = amountCurrency;
	}

	/**
	 * 
	 */
	public EUActivityContribution() {
		super();
		id=null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmpOrganisation getDonor() {
		return donor;
	}

	public void setDonor(AmpOrganisation donor) {
		this.donor = donor;
	}


/*	public AmpTermsAssist getFinancingType() {
		return financingType;
	}

	public void setFinancingType(AmpTermsAssist financingType) {
		this.financingType = financingType;
	}*/
	
	

	public int compareTo(Object arg0) {
		return this.getId().compareTo(((EUActivityContribution)arg0).getId());
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public AmpCategoryValue getFinancingInstr() {
		return financingInstr;
	}

	public void setFinancingInstr(AmpCategoryValue financingInstr) {
		this.financingInstr = financingInstr;
	}

	public AmpCategoryValue getFinancingTypeCategVal() {
		return financingTypeCategVal;
	}

	public void setFinancingTypeCategVal(AmpCategoryValue financingTypeCategVal) {
		this.financingTypeCategVal = financingTypeCategVal;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
