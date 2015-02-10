/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

/**
 * Stores one menu entry details
 * 
 * @author Nadejda Mandrescu
 */
public class AmpMenuEntry implements Serializable {
	private Long id;
	private AmpMenuEntry parent;
	private String name;
	private String title;
	private String tooltip;
	private String url;
	private boolean publicView;
	private boolean adminView;
	private boolean teamView;
	private String flags;
	private int position = 0;
	private Set<AmpMenuEntry> items;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the parent
	 */
	public AmpMenuEntry getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AmpMenuEntry parent) {
		this.parent = parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	/**
	 * @return the publicView
	 */
	public boolean isPublicView() {
		return publicView;
	}

	/**
	 * @param publicView the publicView to set
	 */
	public void setPublicView(boolean publicView) {
		this.publicView = publicView;
	}

	/**
	 * @return the adminView
	 */
	public boolean isAdminView() {
		return adminView;
	}

	/**
	 * @param adminView the adminView to set
	 */
	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}

	/**
	 * @return the teamView
	 */
	public boolean isTeamView() {
		return teamView;
	}

	/**
	 * @param teamView the teamView to set
	 */
	public void setTeamView(boolean teamView) {
		this.teamView = teamView;
	}

	/**
	 * @return the flags
	 */
	public String getFlags() {
		return flags;
	}
	
	/**
	 * @param flags the flags to set
	 */
	public void setFlags(String flags) {
		this.flags = flags;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the items
	 */
	public Set<AmpMenuEntry> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Set<AmpMenuEntry> items) {
		this.items = items;
	}
	
}
