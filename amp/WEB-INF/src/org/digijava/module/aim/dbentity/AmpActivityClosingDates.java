package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.Output;

public class AmpActivityClosingDates implements Serializable, Versionable {
		
	private Long ampActivityClosingDateId;
	private AmpActivity ampActivityId;
	private Integer type;
	private String comments;
    private Date closingDate;
	/**
	 * @return Returns the ampActivityClosingDateId.
	 */
	public Long getAmpActivityClosingDateId() {
		return ampActivityClosingDateId;
	}
	/**
	 * @param ampActivityClosingDateId The ampActivityClosingDateId to set.
	 */
	public void setAmpActivityClosingDateId(Long ampActivityClosingDateId) {
		this.ampActivityClosingDateId = ampActivityClosingDateId;
	}
	/**
	 * @return Returns the ampActivityId.
	 */
	public AmpActivity getAmpActivityId() {
		return ampActivityId;
	}
	/**
	 * @param ampActivityId The ampActivityId to set.
	 */
	public void setAmpActivityId(AmpActivity ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	/**
	 * @return Returns the closingDate.
	 */
	public Date getClosingDate() {
		return closingDate;
	}
	/**
	 * @param closingDate The closingDate to set.
	 */
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	/**
	 * @return Returns the type.
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivityClosingDates aux = (AmpActivityClosingDates) obj;
		String original = this.closingDate != null ? this.closingDate.toString()
				: "" + "-" + this.comments != null ? this.comments : "" + "-" + this.type != null ? this.type
						.toString() : "";
		String other = aux.closingDate != null ? aux.closingDate.toString()
				: "" + "-" + aux.comments != null ? aux.comments : "" + "-" + aux.type != null ? aux.type.toString()
						: "";
		if (original.equals(other)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Output getOutput() {
		return new Output(null, new String[] { "" }, new Object[] { this.closingDate != null ? this.closingDate
				.toString() : "Empty Date" });
	}
	
	@Override
	public Object getValue() {
		return this.type.toString()+this.closingDate;
	}
}