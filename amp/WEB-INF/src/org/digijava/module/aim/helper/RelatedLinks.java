/*
 * RelatedLinks.java
 * Created: 23-Nov-2005
 */

package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.CMSContentItem;

public class RelatedLinks {
	private AmpTeamMember member;
	private CMSContentItem relLink;
	private boolean showInHomePage;
	
	public RelatedLinks() {}

	/**
	 * @return Returns the member.
	 */
	public AmpTeamMember getMember() {
		return member;
	}

	/**
	 * @param member The member to set.
	 */
	public void setMember(AmpTeamMember member) {
		this.member = member;
	}

	/**
	 * @return Returns the relLink.
	 */
	public CMSContentItem getRelLink() {
		return relLink;
	}

	/**
	 * @param relLink The relLink to set.
	 */
	public void setRelLink(CMSContentItem relLink) {
		this.relLink = relLink;
	}

	/**
	 * @return Returns the showInHomePage.
	 */
	public boolean isShowInHomePage() {
		return showInHomePage;
	}

	/**
	 * @param showInHomePage The showInHomePage to set.
	 */
	public void setShowInHomePage(boolean showInHomePage) {
		this.showInHomePage = showInHomePage;
	}
	
}