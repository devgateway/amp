package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.contentrepository.action.DocumentManager;

/**
 * @author jose
 *
 */
public class Funding implements Serializable 
{
	private static Logger logger		= Logger.getLogger(Funding.class);
	
    private long fundingId;
	//private AmpTermsAssist ampTermsAssist;
    private AmpCategoryValue typeOfAssistance;
	private String orgFundingId;
	private String signatureDate;
	//private AmpModality modality;
	private AmpCategoryValue financingInstrument;
	private Collection fundingDetails;	// Collection of Funding Details
	private Collection<MTEFProjection> mtefProjections;
   	private String currentFunding;
   	private String propStartDate;
   	private String propCloseDate;
   	private String actStartDate;
   	private String actCloseDate;
   	private String reportingDate;
   	private String conditions;
	
   		
	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}

	public String getOrgFundingId() {
		return orgFundingId;
	}
	
	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}
	
	public String getSignatureDate() {
		return signatureDate;
	}
	
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
    public Collection getFundingDetails() {
        return fundingDetails;
    }
    public void setFundingDetails(Collection fundingDetails) {
        this.fundingDetails = fundingDetails;
    }
    public long getFundingId() {
        return fundingId;
    }
    public void setFundingId(long fundingId) {
        this.fundingId = fundingId;
    }
 
    public String getCurrentFunding() {
        return currentFunding;
    }
    
    public void setCurrentFunding(String currentFunding) {
        this.currentFunding = currentFunding;
    }
	/**
	 * @return Returns the actCloseDate.
	 */
	public String getActCloseDate() {
		return actCloseDate;
	}
	/**
	 * @param actCloseDate The actCloseDate to set.
	 */
	public void setActCloseDate(String actCloseDate) {
		this.actCloseDate = actCloseDate;
	}
	/**
	 * @return Returns the actStartDate.
	 */
	public String getActStartDate() {
		return actStartDate;
	}
	/**
	 * @param actStartDate The actStartDate to set.
	 */
	public void setActStartDate(String actStartDate) {
		this.actStartDate = actStartDate;
	}
	/**
	 * @return Returns the propCloseDate.
	 */
	public String getPropCloseDate() {
		return propCloseDate;
	}
	/**
	 * @param propCloseDate The propCloseDate to set.
	 */
	public void setPropCloseDate(String propCloseDate) {
		this.propCloseDate = propCloseDate;
	}
	/**
	 * @return Returns the propStartDate.
	 */
	public String getPropStartDate() {
		return propStartDate;
	}
	/**
	 * @param propStartDate The propStartDate to set.
	 */
	public void setPropStartDate(String propStartDate) {
		this.propStartDate = propStartDate;
	}
	/**
	 * @return Returns the conditions.
	 */
	public String getConditions() {
		return conditions;
	}
	/**
	 * @param conditions The conditions to set.
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}
	
		
	public AmpCategoryValue getFinancingInstrument() {
		return financingInstrument;
	}

	public void setFinancingInstrument(AmpCategoryValue financingInstrument) {
		this.financingInstrument = financingInstrument;
	}

	public boolean equals(Object e) {
		if (e instanceof Funding) {
			Funding tmp = (Funding) e;
			return fundingId == tmp.fundingId;
		}
		logger.error( "Received an object of class " + e.getClass().getName() + " instead of class Funding");
		throw new ClassCastException();
	}

	public Collection<MTEFProjection> getMtefProjections() {
		return mtefProjections;
	}

	public void setMtefProjections(Collection<MTEFProjection> mtefProjections) {
		this.mtefProjections = mtefProjections;
	}
}
