/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mihai
 *
 */
public class EUActivityContribution implements Serializable, Comparable {
	private static final long serialVersionUID = -7752194026135595434L;
	
	private Long id;
	private Double amount;
	private AmpCurrency amountCurrency;
	private EUActivity euActivity;
	private AmpOrganisation donor;
	private AmpTermsAssist financingType;
	private AmpModality financingInstrument;
	private Date transactionDate;
	
	public EUActivity getEuActivity() {
		return euActivity;
	}

	public void setEuActivity(EUActivity euActivity) {
		this.euActivity = euActivity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public AmpModality getFinancingInstrument() {
		return financingInstrument;
	}

	public void setFinancingInstrument(AmpModality financingInstrument) {
		this.financingInstrument = financingInstrument;
	}

	public AmpTermsAssist getFinancingType() {
		return financingType;
	}

	public void setFinancingType(AmpTermsAssist financingType) {
		this.financingType = financingType;
	}

	public int compareTo(Object arg0) {
		return this.getId().compareTo(((EUActivityContribution)arg0).getId());
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

}
