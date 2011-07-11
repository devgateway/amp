package org.digijava.module.aim.util;

public final class Crumb {

	/**
	 * Constructor takes all the attributs for a crumb object.
	 * 
	 * @param title
	 *            - Description of the bread crumb page
	 * @param link
	 *            - The link to the page definition or jsp for the crumb
	 */
	public Crumb(String title, String link) {
		setTitle(title);
		setLink(link);
	}

	/**
	 * Empty Constructor set all attributes to null
	 */
	public Crumb() {
		setTitle(null);
		setLink(null);
	}

	/**
	 * Sets the link of the crumb
	 * 
	 * @param link
	 *            Absolute link for the page
	 * 
	 * @see #link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Returns link to the page for the crumb.
	 * 
	 * @return link Absolute link for page
	 * 
	 * @see #link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets title for an crumb
	 * 
	 * @param title
	 *            The title of the crumb
	 * 
	 * @see #title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns title of the crumb
	 * 
	 * @return title String title the crumb page
	 * 
	 * @see #title
	 */
	public String getTitle() {
		return title;
	}

	
	/** Path to the page containing the crumb */
	private String link;

	/** Title of the item */
	private String title;

	@Override
	public boolean equals(Object obj) {
		Crumb tmp = (Crumb) obj;
		if (tmp.getTitle().equals(this.getTitle())) {
			return true;
		} else if (tmp.getLink().equals(this.getLink())) {
			return true;
		}
		return false;
	}
}
