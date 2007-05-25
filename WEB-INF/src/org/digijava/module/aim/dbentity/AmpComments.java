/*
 * AmpComments.java
 */

package org.digijava.module.aim.dbentity;

import java.util.Date;
import java.io.Serializable;

public class AmpComments implements Serializable {
	
	private Long ampCommentId;
	private AmpActivity ampActivityId;
	private AmpField ampFieldId;
	private AmpTeamMember memberId;
	private Date commentDate;
	private String comment;
	
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
	 * @return Returns the ampCommentId.
	 */
	public Long getAmpCommentId() {
		return ampCommentId;
	}
	/**
	 * @param ampCommentId The ampCommentId to set.
	 */
	public void setAmpCommentId(Long ampCommentId) {
		this.ampCommentId = ampCommentId;
	}
	/**
	 * @return Returns the ampFieldId.
	 */
	public AmpField getAmpFieldId() {
		return ampFieldId;
	}
	/**
	 * @param ampFieldId The ampFieldId to set.
	 */
	public void setAmpFieldId(AmpField ampFieldId) {
		this.ampFieldId = ampFieldId;
	}
	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return Returns the commentDate.
	 */
	public Date getCommentDate() {
		return commentDate;
	}
	/**
	 * @param commentDate The commentDate to set.
	 */
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	/**
	 * @return Returns the memberId.
	 */
	public AmpTeamMember getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId The memberId to set.
	 */
	public void setMemberId(AmpTeamMember memberId) {
		this.memberId = memberId;
	}
}
