package org.digijava.module.aim.helper ;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class FinancingBreakdown
{
	private long ampFundingId ;
	private String financingId ;
	private String donor ;
	private String totalCommitted ;
	private String totalDisbursed ;
	private String unDisbursed ;
	private String totalExpended ;
	private String unExpended ;
    private String totalDisbOrdered;

	/* Added for Tanzania */
	private String totalProjection;

	private String actualStartDate ;
	private String actualCompletionDate ;
	private String signatureDate; // added by Priyajith
	private String goeId;
	private AmpOrganisation organisation;

	/**
	 * @return
	 */
	public String getActualCompletionDate() {
		return actualCompletionDate;
	}

	/**
	 * @return
	 */
	public String getActualStartDate() {
		return actualStartDate;
	}

	/**
	 * @return
	 */
	public String getDonor() {
		return donor;
	}

	/**
	 * @return
	 */
	public String getFinancingId() {
		return financingId;
	}

	/**
	 * @return
	 */
	public String getTotalCommitted() {
		return totalCommitted;
	}

	/**
	 * @return
	 */
	public String getTotalDisbursed() {
		return totalDisbursed;
	}

	public String getUnDisbursed() {
		return unDisbursed;
	}

	/**
	 * @return
	 */
	public String getTotalExpended() {
		return totalExpended;
	}

	public String getUnExpended() {
		return unExpended;
	}

	/**
	 * @param string
	 */
	public void setActualCompletionDate(String string) {
		actualCompletionDate = string;
	}

	/**
	 * @param string
	 */
	public void setActualStartDate(String string) {
		actualStartDate = string;
	}

	/**
	 * @param string
	 */
	public void setDonor(String string) {
		donor = string;
	}

	/**
	 * @param string
	 */
	public void setFinancingId(String string) {
		financingId = string;
	}

	/**
	 * @param string
	 */
	public void setTotalCommitted(String string) {
		totalCommitted = string;
	}

	/**
	 * @param string
	 */
	public void setTotalDisbursed(String string) {
		totalDisbursed = string;
	}

	public void setUnDisbursed(String string) {
		unDisbursed = string;
	}

	/**
	 * @param string
	 */
	public void setTotalExpended(String string) {
		totalExpended = string;
	}

	public void setUnExpended(String string) {
		unExpended = string;
	}

	/**
	 * @return
	 */
	public long getAmpFundingId() {
		return ampFundingId;
	}

	/**
	 * @param l
	 */
	public void setAmpFundingId(long l) {
		ampFundingId = l;
	}

	/**
	 * @return
	 */
	public String getGoeId() {
		return goeId;
	}

	/**
	 * @param string
	 */
	public void setGoeId(String string) {
		goeId = string;
	}

	/**
	 * @return Returns the signatureDate.
	 */
	public String getSignatureDate() {
		return signatureDate;
	}
	/**
	 * @param signatureDate The signatureDate to set.
	 */
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}

	public AmpOrganisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(AmpOrganisation organisation) {
		this.organisation = organisation;
	}

	public String getTotalProjection() {
		return totalProjection;
	}

        public String getTotalDisbOrdered() {
                return totalDisbOrdered;
        }

        public void setTotalProjection(String totalProjection) {
		this.totalProjection = totalProjection;
	}

        public void setTotalDisbOrdered(String totalDisbOrdered) {
                this.totalDisbOrdered = totalDisbOrdered;
        }
}
