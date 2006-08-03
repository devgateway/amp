/**
 * Viewable.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;


/**
 * Class describing a viewable behaviour. Viewable objects always have
 * a viewer name for each view modes (types).
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 *
 */
public abstract class Viewable implements Cloneable {

	/**
	 * returns the viewer name for the specified view type. 
	 * @param viewType the viewer type
	 * @return the viewer name
	 */
	public String getViewerName(String viewType) {
		return null;
	}
	
	/**
	 * Returns the full name of the viewer (along with the path)
	 * @param viewType the view type for which the viewer is requested
	 * @return the full viewer path
	 */
	public String getViewerPath(String viewType) {
		String className=this.getClass().getName();
		int idx=className.lastIndexOf('.');
		for (int i = 0; i < ArConstants.prefixes.length ; i++ )
			if (ArConstants.prefixes[i].getCategory().equals(viewType)) return (String) ArConstants.prefixes[i].getValue() +
			className.substring(idx+1,className.length()) + (String) ArConstants.suffixes[i].getValue();
		return null;
	}	


	public String getViewerPath() {
		return getViewerPath(getCurrentView());
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return Returns the currentView.
	 */
	public abstract String getCurrentView();

	
}
