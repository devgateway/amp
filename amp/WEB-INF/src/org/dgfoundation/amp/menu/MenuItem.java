/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a menu item
 * 
 * @author Nadejda Mandrescu
 */
public class MenuItem {
	/** menu name */
	// this is needed until we have another way of mapping a menu entry to FM configuration, than constants
	public final String name;
	/** menu title to display */
	public final String title;
	/** menu tooltip */
	public final String tooltip;
	/** menu URL */
	public final String url;
	/** any specific flags */
	public final String flags;
	/** whether the action should open a new popup window */
	public final boolean isPopup;
	/** whether the action should open a new tab */
	public final boolean isTab;
	/** whether the action should be a post and not a get */
	public final boolean isPost;
	
	private List<MenuItem> children;
	private MenuItem parent;
	
	/**
	 * 
	 * @param name
	 * @param title
	 * @param tooltip
	 * @param flags
	 */
	public MenuItem(String name, String title, String tooltip, String url, String flags) {
		this.name = name;
		this.title = title;
		this.tooltip = tooltip;
		this.url = url;
		this.flags = flags;
		this.isPopup = flags != null && flags.contains(MenuFlags.OPEN_POPUP);
		this.isTab = flags != null && flags.contains(MenuFlags.OPEN_TAB);
		this.isPost = flags != null && flags.contains(MenuFlags.POST_REQUEST);
	}
	
	public MenuItem(MenuItem other) {
		this(other.name, other.title, other.tooltip, other.url, other.flags);
		this.parent = other.parent;
		this.children = new ArrayList<MenuItem>(other.getChildren());
	}
	
	/**
	 * Configures menu sub-items
	 * @param children
	 */
	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}
	
	/** Appends a new menu sub-item */
	public void appendChild(MenuItem item) {
		getChildren().add(item);
	}
	
	/** 
	 * @return list of sub-items
	 */
	public List<MenuItem> getChildren() {
		if (children == null) {
			children = new ArrayList<MenuItem>();
		}
		return children;
	}

	/**
	 * @return the parent
	 */
	public MenuItem getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}
}
